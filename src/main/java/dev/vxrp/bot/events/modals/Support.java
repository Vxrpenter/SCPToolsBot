package dev.vxrp.bot.events.modals;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.Enums.TicketIdentifier;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.records.translation.ButtonGroup;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.builder.StatsBuilder;
import dev.vxrp.util.configuration.records.translation.LoggingGroup;
import dev.vxrp.util.configuration.records.translation.SupportGroup;
import dev.vxrp.util.logger.LoggerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;

import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Support {
    private static final SupportGroup translations = (SupportGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.SUPPORT_GROUP);
    private static final ConfigGroup configs = (ConfigGroup) ScpTools.getConfigurations().getConfig(LoadIndex.CONFIG_GROUP);
    private static final ButtonGroup buttons = (ButtonGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.BUTTON_GROUP);
    private static final LoggingGroup logging = (LoggingGroup) ScpTools.getConfigurations().getTranslation(LoadIndex.LOGGING_GROUP);

    public static void createSupportTicket(ModalInteractionEvent event, Logger logger) {
        Member member = event.getMember();
        assert member != null;
        String userName = Objects.requireNonNull(member.getUser().getGlobalName());
        String userID = Objects.requireNonNull(member.getUser().getId());
        Guild guild = event.getGuild();
        if (guild == null) return;

        String subject = Objects.requireNonNull(event.getValue("support_subject")).getAsString();
        String body = Objects.requireNonNull(event.getValue("support_body")).getAsString();
        String name = "Ticket #" + ThreadLocalRandom.current().nextInt(1535, 75808);
        String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());

        guild.createTextChannel(name)
                .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), null)
                .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .setSlowmode(1)
                .queue(textChannel -> {
                    event.reply(translations.ticket_support_created()
                            .replace("%channel%", "<#"+textChannel.getId()+">")).setEphemeral(true).queue();

                    List<String> roleIDs = configs.support_settings_roles_access_support_tickets();
                    for (String id : roleIDs) {
                        textChannel.upsertPermissionOverride(
                                        Objects.requireNonNull(guild.getRoleById(id)))
                                .grant(EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND))
                                .queue();
                    }
                    textChannel.sendMessageEmbeds(StatsBuilder.buildStatus(userName).build(),
                            builder(translations.ticket_support_title().replace("%name%", name),
                                    translations.ticket_support_body()
                                            .replace("%subject%", subject)
                                            .replace("%body%", body),
                                    translations.ticket_support_footer()
                                            .replace("%date%", date)
                                            .replace("%time%", time),
                                    event.getGuild().getIconUrl(), event.getUser()).build())
                            .addActionRow(
                                    Button.danger("close_support_ticket:"+userID+":", buttons.close_support_ticket()),
                                    Button.primary("claim_support_ticket:"+userID+":", buttons.claim_support_ticket()),
                                    Button.secondary("settings_support_ticket", buttons.settings_support_ticket())
                            ).queue();

                    try {
                        new LoggerManager(event.getJDA()).creationLog(event.getUser(),
                                logging.support_ticket_create_logging_action()
                                        .replace("%id%", textChannel.getId())
                                        .replace("%channel%", "<#"+textChannel.getId()+">")
                                        .replace("%user%", "<@"+userID+">")
                                        .replace("%type%", TicketIdentifier.SUPPORT.toString())
                                        .replace("%creator%", "<@"+userID+">")
                                        .replace("%handler%", "None")
                                        .replace("%date%", date),
                                configs.ticket_logging_channel_id(),
                                Color.GREEN);
                        ScpTools.getSqliteManager().getTicketsTableManager().addTicket(textChannel.getId(), TicketIdentifier.SUPPORT, date, userID, null);
                    } catch (SQLException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
        logger.info("Created new support ticket by user {} - under name {}", ColorTool.apply(DCColor.GREEN, userName), ColorTool.apply(DCColor.RED, name));
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