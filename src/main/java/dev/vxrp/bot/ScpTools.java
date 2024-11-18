package dev.vxrp.bot;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import dev.vxrp.bot.commands.CommandManager;
import dev.vxrp.bot.config.managers.configuration.ColorConfigManager;
import dev.vxrp.bot.config.managers.configuration.ConfigManager;
import dev.vxrp.bot.config.managers.regulars.RegularsManager;
import dev.vxrp.bot.config.managers.translations.TranslationManager;
import dev.vxrp.bot.database.sqlite.SqliteManager;
import dev.vxrp.bot.events.ButtonListener;
import dev.vxrp.bot.events.MessageListener;
import dev.vxrp.bot.events.ModalListener;
import dev.vxrp.bot.events.SelectMenuListener;
import dev.vxrp.bot.runnables.CheckNoticeOfDeparture;
import dev.vxrp.bot.runnables.CheckPlaytime;
import dev.vxrp.util.Enums.DatabaseType;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.Enums.PredefinedDatabases;
import dev.vxrp.util.api.cedmod.CedModApi;
import dev.vxrp.util.api.github.GitHubApi;
import dev.vxrp.util.configuration.ConfigurationLoadManager;
import dev.vxrp.util.configuration.configs.ConfigLoader;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.configuration.translations.TranslationLoader;
import dev.vxrp.util.configuration.util.CONFIG;
import dev.vxrp.util.converter.PermissionListConverter;
import dev.vxrp.util.general.RepeatTask;
import dev.vxrp.util.logger.LoggerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    static ConfigurationLoadManager configurations;

    public static void main(String[] args) throws IOException, InterruptedException {
        GitHubApi.CheckForUpdatesByTags("https://api.github.com/repos/Vxrpenter/SCPToolsBot/git/refs/tags");

        configurations = new ConfigurationLoadManager();
        initializeConfigs();
        setLoggingLevel();
        loadConfigs();
        initializeRegulars();
        initializeCedModApi();

        ConfigGroup config = (ConfigGroup) configurations.getConfig(LoadIndex.CONFIG_GROUP);
        Activity.ActivityType activityType = Activity.ActivityType.valueOf(config.activity_type());
        String activityContent = config.activity_content();

        JDA api = JDABuilder.createDefault(configManager.getToken(), GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.of(activityType, activityContent))
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .addEventListeners(
                        new ButtonListener(),
                        new ModalListener(),
                        new MessageListener(),
                        new SelectMenuListener())
                .build();
        loggerManager = new LoggerManager(api);
        initializeSqlite();
        logger.info("Initialized Listeners");

        guild = api.awaitReady().getGuildById(configManager.getString("guild_id"));
        if (guild == null) {
            logger.error("GUILD ID NULL... SHUTTING DOWN");
            System.exit(1);
        }

        initializeCommands(api);
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

    private static void initializeSqlite() throws IOException {
        ConfigGroup config = (ConfigGroup) configurations.getConfig(LoadIndex.CONFIG_GROUP);
        if (Objects.equals(config.use_predefined_database_sets(), PredefinedDatabases.SQLITE.toString())) {
            Path path = Path.of(System.getProperty("user.dir"));
            File file = new File(path+"/sqlite/data.db");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            try {
                sqliteManager = new SqliteManager(path+"/sqlite/data.db");
            } catch (SQLException | InterruptedException e) {
                logger.error("Could not correctly set up Sqlite database {}", e.getMessage());
            }
        }
        if (Objects.equals(config.use_predefined_database_sets(), PredefinedDatabases.NONE.toString())) {
            String url = config.custom_url();
            DatabaseType type = DatabaseType.valueOf(config.custom_type());
            String username = config.custom_username();
            String password = config.custom_password();

            logger.warn("CUSTOM DATABASE - You have enabled an unfinished feature. For further process please deactivate the feature until it is supported");

            //Later database code goes here
        }
    }

    private static void loadConfigs() {

        try {
            new ConfigLoader(configurations);
        } catch (Exception e) {
            logger.error("Could not add config to load list {}", e.getMessage());
        }
        try {
            new TranslationLoader(configurations);
        } catch (Exception e) {
            logger.error("Could not add translation to load list {}", e.getMessage());
        }
        configurations.write();
    }

    private static void initializeRegulars() {
        try {
            regularsManager = new RegularsManager(Path.of(System.getProperty("user.dir")));
        } catch (IOException | NullPointerException e) {
            logger.error("Could not initialize regulars configs {}", e.getMessage());
        }
    }

    private static void initializeCedModApi() {
        ConfigGroup config = (ConfigGroup) configurations.getConfig(LoadIndex.CONFIG_GROUP);
        String instanceUrl = config.cedmod_instance_url();
        String apiKey = config.cedmod_api_key();
        cedModApi = new CedModApi(instanceUrl, apiKey);
    }

    private static void initializeConfigs() {
        List<String> folders = Arrays.asList("configs", "lang", "sqlite");
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
        ConfigGroup config = (ConfigGroup) configurations.getConfig(LoadIndex.CONFIG_GROUP);
        RepeatTask.repeatWithScheduledExecutorService(
                CheckNoticeOfDeparture.runNoticeOfDepartureCheck(api),
                config.notice_of_departure_check_rate(),
                TimeUnit.valueOf(config.notice_of_departure_check_type()));
        RepeatTask.repeatWithScheduledExecutorService(
                CheckPlaytime.runPlaytimeCheck(api),
                1, TimeUnit.HOURS
        );
    }

    private static void initializeCommands(JDA api) {
        ConfigGroup config = (ConfigGroup) configurations.getConfig(LoadIndex.CONFIG_GROUP);

        new CommandManager(api)
                .addCommand("help", config.command_settings_template_descriptions(), new PermissionListConverter(config.command_setting_help_default_permissions()).convert(), null)
                .addCommand("template", config.command_settings_template_descriptions(), new PermissionListConverter(config.command_settings_template_default_permissions()).convert(),
                        new OptionData(OptionType.STRING, "template", "What template are you referring to", true)
                                .addChoice("rules", "rules")
                                .addChoice("support", "support")
                                .addChoice("notice of departure", "notice_of_departure")
                                .addChoice("regulars", "regulars"))
                .build();
    }

    public static ConfigManager getConfigManager() {return configManager;}
    public static TranslationManager getTranslationManager() {return translationManager;}
    public static ColorConfigManager getColorConfigManager() {return colorConfigManager;}
    public static SqliteManager getSqliteManager() {return sqliteManager;}
    public static LoggerManager getLoggerManager() {return loggerManager;}
    public static RegularsManager getRegularsManager() {return regularsManager;}
    public static CedModApi getCedModApi() {return cedModApi;}
    public static Guild getGuild() {return guild;}
    public static ConfigurationLoadManager getConfigurations() {return configurations;}
}