package dev.vxrp.bot.events.modals;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.config.util.CONFIG;
import dev.vxrp.bot.config.util.TRANSLATIONS;
import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.builder.StatsBuilder;
import dev.vxrp.bot.util.colors.ColorTool;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Support {
    private static final TranslationManager translationManager = ScpTools.getTranslationManager();

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
                    event.reply(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_CREATED)
                            .replace("%channel%", "<#"+textChannel.getId()+">")).setEphemeral(true).queue();

                    List<String> roleIDs = ScpTools.getConfigManager().getStringList(CONFIG.SUPPORT_SETTINGS.ROLES_ACCESS_SUPPORT_TICKETS);
                    for (String id : roleIDs) {
                        textChannel.upsertPermissionOverride(
                                        Objects.requireNonNull(guild.getRoleById(id)))
                                .grant(EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND))
                                .queue();
                    }
                    textChannel.sendMessageEmbeds(StatsBuilder.buildStatus(userName).build()).queue();
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

    private static EmbedBuilder builder(String title, String body, String footerText, String IconURL, User user) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setThumbnail(user.getAvatarUrl());
        builder.setTitle(title);
        builder.setDescription(body);
        builder.setFooter(footerText, IconURL);
        return builder;
    }

}