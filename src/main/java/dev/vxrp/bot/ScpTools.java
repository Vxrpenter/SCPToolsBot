package dev.vxrp.bot;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import dev.vxrp.bot.config.managers.regulars.RegularsManager;
import dev.vxrp.bot.database.sqlite.SqliteManager;
import dev.vxrp.bot.events.ButtonListener;
import dev.vxrp.bot.commands.CommandManager;
import dev.vxrp.bot.commands.help.HelpCommand;
import dev.vxrp.bot.commands.templates.TemplateCommand;
import dev.vxrp.bot.config.managers.configuration.ColorConfigManager;
import dev.vxrp.bot.config.managers.translations.TranslationManager;
import dev.vxrp.bot.events.MessageListener;
import dev.vxrp.bot.runnables.CheckNoticeOfDeparture;
import dev.vxrp.bot.runnables.CheckPlaytime;
import dev.vxrp.util.api.cedmod.CedModApi;
import dev.vxrp.util.api.github.GitHubApi;
import dev.vxrp.util.configuration.LoadedConfigurations;
import dev.vxrp.util.configuration.configs.ConfigLoader;
import dev.vxrp.util.configuration.records.ConfigGroup;
import dev.vxrp.util.configuration.util.CONFIG;
import dev.vxrp.bot.config.managers.configuration.ConfigManager;
import dev.vxrp.bot.events.ModalListener;
import dev.vxrp.util.configuration.translations.TranslationLoader;
import dev.vxrp.util.general.RepeatTask;
import dev.vxrp.util.logger.LoggerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ScpTools {
    private final static Logger logger = LoggerFactory.getLogger(ScpTools.class);
    static ConfigManager configManager;
    static TranslationManager translationManager;
    static ColorConfigManager colorConfigManager;
    static SqliteManager sqliteManager;
    static LoggerManager loggerManager;
    static RegularsManager regularsManager;
    static CedModApi cedModApi;
    static Guild guild;

    public static void main(String[] args) throws IOException, InterruptedException {
        GitHubApi.CheckForUpdatesByTags("https://api.github.com/repos/Vxrpenter/SCPToolsBot/git/refs/tags");

        initializeConfigs();
        setLoggingLevel();
        loadConfigs();
        initializeRegulars();
        initializeCedModApi();

        Activity.ActivityType activityType = Activity.ActivityType.valueOf(LoadedConfigurations.getConfigMemoryLoad().activity_type());
        String activityContent = LoadedConfigurations.getConfigMemoryLoad().activity_content();

        JDA api = JDABuilder.createDefault(configManager.getToken(), GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.of(activityType, activityContent))
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .addEventListeners(
                        new TemplateCommand(),
                        new HelpCommand(),
                        new ButtonListener(),
                        new ModalListener(),
                        new MessageListener())
                .build();
        loggerManager = new LoggerManager(api);
        initializeSqlite();
        logger.info("Initialized Listeners");

        new CommandManager(api);
        guild = api.awaitReady().getGuildById(configManager.getString("guild_id"));
        if (guild == null) {
            logger.error("GUILD ID NULL... SHUTTING DOWN");
            System.exit(1);
        }
        runCheckups(api);
    }

    private static void setLoggingLevel() {
        Level level = Level.INFO;
        if (configManager.getBoolean(CONFIG.DEBUG)) {level = Level.DEBUG;}
        if (configManager.getBoolean(CONFIG.ADVANCED_DEBUG)) {level = Level.TRACE;}

        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final ch.qos.logback.classic.Logger log = loggerContext.exists(org.slf4j.Logger.ROOT_LOGGER_NAME);
        log.setLevel(level);
    }

    private static void initializeSqlite() {
        Path path = Path.of(System.getProperty("user.dir"));
        File file = new File(path+"\\sqlite\\data.db");
        try {
            file.createNewFile();
            sqliteManager = new SqliteManager(path+"\\sqlite\\data.db");
            sqliteManager.getQueueManager();
        } catch (SQLException | IOException | InterruptedException e) {
            logger.error("Could not correctly set up Sqlite database {}", e.getMessage());
        }
    }

    private static void loadConfigs() {
        try {
            new TranslationLoader();
        } catch (Exception e) {
            logger.error("Could not load translation to memory {}", e.getMessage());
        }
        try {
            new ConfigLoader();
        } catch (Exception e) {
            logger.error("Could not load config to memory {}", e.getMessage());
        }
    }

    private static void initializeRegulars() {
        try {
            regularsManager = new RegularsManager(Path.of(System.getProperty("user.dir")));
        } catch (IOException | NullPointerException e) {
            logger.error("Could not initialize regulars configs {}", e.getMessage());
        }
    }

    private static void initializeCedModApi() throws IOException {
        String instanceUrl = LoadedConfigurations.getConfigMemoryLoad().cedmod_instance_url();
        String apiKey = LoadedConfigurations.getConfigMemoryLoad().cedmod_api_key();
        cedModApi = new CedModApi(instanceUrl, apiKey);
    }

    private static void initializeConfigs() {
        List<String> folders = Arrays.asList("configs", "translations", "sqlite");
        for (String folder : folders) {
            String folderPath = Paths.get(folder).toString();
            new File(folderPath).mkdirs();
            logger.info("Initializing folder {}", folderPath);
        }

        try {
            configManager = new ConfigManager();
        } catch (Exception e) {
            logger.error("Could not load configs from configManager : {}", e.getMessage());
            System.exit(1);
        }
        try {
            translationManager = new TranslationManager();
        } catch (Exception e) {
            logger.error("Could not load configs from translationManager : {}", e.getMessage());
            System.exit(1);
        }
        try {
            colorConfigManager = new ColorConfigManager();
        } catch (Exception e) {
            logger.error("Could not load configs from colorConfigManager : {}", e.getMessage());
            System.exit(1);
        }
    }

    private static void runCheckups(JDA api) {
        ConfigGroup config = LoadedConfigurations.getConfigMemoryLoad();
        RepeatTask.repeatWithScheduledExecutorService(
                CheckNoticeOfDeparture.runNoticeOfDepartureCheck(api),
                config.notice_of_departure_check_rate(),
                TimeUnit.valueOf(config.notice_of_departure_check_type()));
        RepeatTask.repeatWithScheduledExecutorService(
                CheckPlaytime.runPlaytimeCheck(api),
                1, TimeUnit.HOURS
        );
    }

    public static ConfigManager getConfigManager() {return configManager;}
    public static TranslationManager getTranslationManager() {return translationManager;}
    public static ColorConfigManager getColorConfigManager() {return colorConfigManager;}
    public static SqliteManager getSqliteManager() {return sqliteManager;}
    public static LoggerManager getLoggerManager() {return loggerManager;}
    public static RegularsManager getRegularsManager() {return regularsManager;}
    public static CedModApi getCedModApi() {return cedModApi;}
    public static Guild getGuild() {return guild;}
}