package dev.vxrp.bot.events.modals;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.config.util.CONFIG;
import dev.vxrp.bot.config.util.TRANSLATIONS;
import dev.vxrp.bot.util.Enums.DCColor;
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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Support {
    public static void createSupportTicket(ModalInteractionEvent event, TranslationManager translationManager, Logger logger) {
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
                    event.reply(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_CREATED)
                            .replace("%channel%", "<#"+textChannel.getId()+">")).setEphemeral(true).queue();

                    List<String> roleIDs = ScpTools.getConfigManager().getStringList(CONFIG.SUPPORT_SETTINGS.ROLES_ACCESS_SUPPORT_TICKETS);
                    for (String id : roleIDs) {
                        textChannel.upsertPermissionOverride(
                                Objects.requireNonNull(guild.getRoleById(id)))
                                .grant(EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND))
                                .queue();
                    }
                    textChannel.sendMessageEmbeds(builderStatus(userName).build()).queue();
                    textChannel.sendMessageEmbeds( builder(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_TITLE).replace("%name%", name),
                            translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_BODY)
                                    .replace("%subject%", subject)
                                    .replace("%body%", body),
                            translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_FOOTER)
                                    .replace("%date%", date)
                                    .replace("%time%", time),
                            event.getGuild().getIconUrl(), event.getUser()).build())
                            .addActionRow(
                                    Button.danger("close_support_ticket:"+userID+":", "Close Ticket"),
                                    Button.primary("claim_support_ticket:"+userID+":", "Claim Ticket"),
                                    Button.secondary("settings_support_ticket", "Settings")
                            ).queue();
                });

        logger.info("Created new support ticket by user {} - under name {}", ColorTool.apply(DCColor.GREEN, userName), ColorTool.apply(DCColor.RED, name));
    }
    public static void createUnbanTicket(ModalInteractionEvent event, TranslationManager translationManager, Logger logger) {
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
                builderStatus(userName).build(),
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

    private static EmbedBuilder builder(String title, String body, String footerText, String IconURL, User user) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setThumbnail(user.getAvatarUrl());
        builder.setTitle(title);
        builder.setDescription(body);
        builder.setFooter(footerText, IconURL);
        return builder;
    }
    private static EmbedBuilder builderStatus(String username) {
        return new EmbedBuilder()
                .setDescription("""
                                    ```ansi
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Ticket Status: [2;32m[1;32mOpen[0m[2;32m[0m ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎\s
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Handler: [2;31m[1;31mNone[0m[2;31m[0m | Creator: [2;31m[1;31m[1;33m""" +username+"""
                                     [0m[1;31m[0m[2;31m[0m
                                    ```
                                    """);
    }

}