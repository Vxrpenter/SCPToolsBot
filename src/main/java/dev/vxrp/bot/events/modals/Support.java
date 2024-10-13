package dev.vxrp.bot.events.modals;

import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.config.util.TRANSLATIONS;
import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.colors.ColorTool;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Support {
    public static void createSupportTicket(ModalInteractionEvent event, TranslationManager translationManager, Logger logger) {
        EmbedBuilder builder = new EmbedBuilder();

        Member member = event.getMember();
        assert member != null;
        String userName = Objects.requireNonNull(member.getUser().getGlobalName());
        Guild guild = event.getGuild();
        if (guild == null) return;

        String subject = Objects.requireNonNull(event.getValue("support_subject")).getAsString();
        String body = Objects.requireNonNull(event.getValue("support_body")).getAsString();
        String name = "Ticket #" + ThreadLocalRandom.current().nextInt(1535, 75808);
        String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());

        builder.setThumbnail(member.getUser().getAvatarUrl());
        builder.setTitle(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_TITLE).replace("%user%", name));
        builder.setDescription(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_BODY)
                .replace("%subject%", subject)
                .replace("%user%", userName)
                .replace("%body%", body)
        );
        builder.setFooter("Erstellt am: "+date+" um "+time+"Uhr", event.getGuild().getIconUrl());


        guild.createTextChannel(name)
                .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), null)
                .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .setSlowmode(1)
                .queue(textChannel -> {
                    event.reply(":white_check_mark: Your ticket has been created and will be reviewed in the next 24h  - <#"+textChannel.getId()+">").setEphemeral(true).queue();
                    textChannel.sendMessageEmbeds(builder.build())
                            .addActionRow(
                                    Button.danger("closeticket", "Close Ticket")
                            ).queue();
                });
        logger.info("Created new support ticket by user "+ ColorTool.apply(DCColor.GREEN, userName)+" - under name "+ColorTool.apply(DCColor.RED, name));
    }
}
