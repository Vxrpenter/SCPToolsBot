package dev.vxrp.bot.events.modals;

import dev.vxrp.bot.util.configuration.groups.ButtonsGroup;
import dev.vxrp.bot.util.configuration.groups.ConfigGroup;
import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.builder.StatsBuilder;
import dev.vxrp.bot.util.api.cedmod.CedModApi;
import dev.vxrp.bot.util.colors.ColorTool;
import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.groups.SupportGroup;
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
    private static final SupportGroup translations = LoadedConfigurations.getSupportTranslationMemoryLoad();
    private static final ConfigGroup configs = LoadedConfigurations.getConfigMemoryLoad();
    private static final ButtonsGroup buttons = LoadedConfigurations.getButtonsMemoryLoad();

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

        TextChannel textChannel = event.getGuild().getTextChannelById(configs.support_settings_unban_channel_id());

        assert textChannel != null;
        event.reply(ColorTool.useCustomColorCodes(translations.ticket_unban_created()
                .replace("%channel%", "<#"+textChannel.getId()+">"))).setEphemeral(true).queue();

        List<String> roleIDs = configs.support_settings_roles_access_unban_tickets();
        for (String id : roleIDs) {
            textChannel.upsertPermissionOverride(
                            Objects.requireNonNull(guild.getRoleById(id)))
                    .grant(EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND))
                    .queue();
        }
        textChannel.sendMessageEmbeds(
                        StatsBuilder.buildStatus(userName).build(),
                        builder(ColorTool.useCustomColorCodes(translations.ticket_unban_title().replace("%name%", name)),
                                ColorTool.useCustomColorCodes(translations.ticket_unban_body()
                                        .replace("%steamID%", steamID)
                                        .replace("%reason%", reason)
                                        .replace("%reasoning%", reasoning)),
                                ColorTool.useCustomColorCodes(translations.ticket_unban_footer()
                                        .replace("%date%", date)
                                        .replace("%time%", time)),
                                event.getGuild().getIconUrl(), event.getUser()).build())
                .addActionRow(
                        Button.success("accept_unban_ticket:"+userID+":"+steamID+":", buttons.accept_unban_ticket()),
                        Button.danger("dismiss_unban_ticket:"+userID+":"+steamID+":", buttons.dismiss_unban_ticket()),
                        Button.secondary("settings_unban_ticket", buttons.settings_unban_ticket())
                ).queue();

        logger.info("Created new unban request by user {} - under name {}", ColorTool.apply(DCColor.GREEN, userName), ColorTool.apply(DCColor.RED, name));
    }

    public static void acceptUnban(ModalInteractionEvent event) throws IOException {
        String messageID = event.getModalId().split(":")[3];

        Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById(Objects.requireNonNull(event.getChannelId()))).deleteMessageById(messageID).queue();

        User bannedUser = Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getMemberById(event.getModalId().split(":")[1])).getUser();
        String steamID = event.getModalId().split(":")[2];
        String reason = Objects.requireNonNull(event.getValue("reason_action_reason")).getAsString();

        String instanceUrl = configs.cedmod_instance_url();
        String apiKey = configs.cedmod_api_key();
        String banListID = configs.cedmod_master_banlist_id();
        String banID = CedModApi.getBanId(instanceUrl, apiKey, banListID, steamID);

        bannedUser.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(
                StatsBuilder.buildAccepted(bannedUser.getGlobalName()).build(),
                        new EmbedBuilder()
                        .setDescription(ColorTool.useCustomColorCodes(translations.ticket_unban_message_accepted()
                                .replace("%steamID%", steamID)
                                .replace("%reason%", reason)))
                        .build()
                        )).queue();

        CedModApi.executeUnban(instanceUrl, apiKey, banID, reason);

        event.reply(ColorTool.useCustomColorCodes(translations.ticket_unban_message_sent())).setEphemeral(true).queue();
    }
    public static void dismissUnban(ModalInteractionEvent event, User user) {
        String messageID = event.getModalId().split(":")[3];

        Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById(Objects.requireNonNull(event.getChannelId()))).deleteMessageById(messageID).queue();

        String steamID = event.getModalId().split(":")[2];
        String reason = Objects.requireNonNull(event.getValue("reason_action_reason")).getAsString();

        user.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(
                StatsBuilder.buildDismissed(event.getUser().getGlobalName()).build(),
                new EmbedBuilder()
                        .setDescription(ColorTool.useCustomColorCodes(translations.ticket_unban_message_dismissed()
                                .replace("%steamID%", steamID)
                                .replace("%reason%", reason)))
                        .build()
        )).queue();


        event.reply(ColorTool.useCustomColorCodes(translations.ticket_unban_message_sent())).setEphemeral(true).queue();
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
