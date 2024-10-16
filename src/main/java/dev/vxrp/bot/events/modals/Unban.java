package dev.vxrp.bot.events.modals;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.config.util.CONFIG;
import dev.vxrp.bot.config.util.TRANSLATIONS;
import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.builder.StatsBuilder;
import dev.vxrp.bot.util.cedmod.CedModApi;
import dev.vxrp.bot.util.colors.ColorTool;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Unban {
    private static final TranslationManager translationManager = ScpTools.getTranslationManager();

    public static void createUnbanTicket(ModalInteractionEvent event, Logger logger) {
        Member member = event.getMember();
        assert member != null;
        String userName = Objects.requireNonNull(member.getUser().getGlobalName());
        String userID = Objects.requireNonNull(member.getUser().getId());
        Guild guild = event.getGuild();
        if (guild == null) return;

        String steamID = Objects.requireNonNull(event.getValue("unban_steamID")).getAsString();
        String reason = Objects.requireNonNull(event.getValue("unban_ban_reason")).getAsString();
        String reasoning = Objects.requireNonNull(event.getValue("unban_reasoning")).getAsString();
        String name = "Request #" + ThreadLocalRandom.current().nextInt(1535, 75808);
        String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());

        TextChannel textChannel = event.getGuild().getTextChannelById(ScpTools.getConfigManager().getString(CONFIG.SUPPORT_SETTINGS.UNBAN_CHANNEL_ID));

        assert textChannel != null;
        event.reply(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_CREATED)
                .replace("%channel%", "<#"+textChannel.getId()+">")).setEphemeral(true).queue();

        List<String> roleIDs = ScpTools.getConfigManager().getStringList(CONFIG.SUPPORT_SETTINGS.ROLES_ACCESS_UNBAN_TICKETS);
        for (String id : roleIDs) {
            textChannel.upsertPermissionOverride(
                            Objects.requireNonNull(guild.getRoleById(id)))
                    .grant(EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND))
                    .queue();
        }
        textChannel.sendMessageEmbeds(
                        StatsBuilder.buildStatus(userName).build(),
                        builder(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_TITLE).replace("%name%", name),
                                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_BODY)
                                        .replace("%steamID%", steamID)
                                        .replace("%reason%", reason)
                                        .replace("%reasoning%", reasoning),
                                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_FOOTER)
                                        .replace("%date%", date)
                                        .replace("%time%", time),
                                event.getGuild().getIconUrl(), event.getUser()).build())
                .addActionRow(
                        Button.success("accept_support_ticket:"+userID+":"+steamID+":", "Accept Ticket"),
                        Button.danger("dismiss_unban_ticket:"+userID+":"+steamID+":", "Dismiss Ticket"),
                        Button.secondary("settings_unban_ticket", "Settings")
                ).queue();

        logger.info("Created new unban request by user {} - under name {}", ColorTool.apply(DCColor.GREEN, userName), ColorTool.apply(DCColor.RED, name));
    }

    public static void acceptUnban(ModalInteractionEvent event) throws IOException {
        String messageID = event.getModalId().split(":")[3];

        Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById(Objects.requireNonNull(event.getChannelId()))).deleteMessageById(messageID).queue();

        User bannedUser = Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getMemberById(event.getModalId().split(":")[1])).getUser();
        String steamID = event.getModalId().split(":")[2];
        String reason = Objects.requireNonNull(event.getValue("reason_action_reason")).getAsString();

        String instanceUrl = ScpTools.getConfigManager().getString(CONFIG.CEDMOD.INSTANCE_URL);
        String apiKey = ScpTools.getConfigManager().getString(CONFIG.CEDMOD.API_KEY);
        String banListID = ScpTools.getConfigManager().getString(CONFIG.CEDMOD.MASTER_BAN_LIST_ID);
        String banID = CedModApi.getBanId(instanceUrl, apiKey, banListID, steamID);

        bannedUser.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(
                StatsBuilder.buildAccepted(bannedUser.getGlobalName()).build(),
                        new EmbedBuilder()
                        .setDescription(ScpTools.getTranslationManager().getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_ACCEPTED)
                                .replace("%steamID%", steamID)
                                .replace("%reason%", reason))
                        .build()
                        )).queue();

        CedModApi.executeUnban(instanceUrl, apiKey, banID, reason);

        event.reply(ScpTools.getTranslationManager().getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_SENT)).setEphemeral(true).queue();
    }
    public static void dismissUnban(ModalInteractionEvent event) {
        String messageID = event.getModalId().split(":")[3];

        Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById(Objects.requireNonNull(event.getChannelId()))).deleteMessageById(messageID).queue();

        User bannedUser = Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getMemberById(event.getModalId().split(":")[1])).getUser();
        String steamID = event.getModalId().split(":")[2];
        String reason = Objects.requireNonNull(event.getValue("reason_action_reason")).getAsString();

        bannedUser.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(
                        StatsBuilder.buildDismissed(event.getUser().getGlobalName()).build(),
                        new EmbedBuilder()
                                .setDescription(ScpTools.getTranslationManager().getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_DISMISSED)
                                        .replace("%steamID%", steamID)
                                        .replace("%reason%", reason))
                                .build()
        )).queue();

        event.reply(ScpTools.getTranslationManager().getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_SENT)).setEphemeral(true).queue();
    }

    private static EmbedBuilder builder(String title, String body, String footerText, String IconURL, User user) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setThumbnail(user.getAvatarUrl());
        builder.setTitle(title);
        builder.setDescription(body);
        builder.setFooter(footerText, IconURL);
        return builder;
    }
}
