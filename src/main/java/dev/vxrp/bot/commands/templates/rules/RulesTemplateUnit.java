package dev.vxrp.bot.commands.templates.rules;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.util.configuration.util.CONFIG;
import dev.vxrp.bot.util.pastebin.PastebinUtil;
import dev.vxrp.bot.util.parser.CustomColorParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RulesTemplateUnit  {
    private static EmbedBuilder embed(Guild guild, TextChannel channel) {
        String rules;
        try {
            rules = PastebinUtil.getPasteBin("https://pastebin.com/raw/"+ScpTools.getConfigManager().getString(CONFIG.RULES.PASTEBIN));
        } catch (IOException e) {
            assert channel != null;
            channel.sendMessage("Failed to retrieve rules").queue();
            return null;
        }

        return new EmbedBuilder()
                .setDescription("```ansi\n"+ CustomColorParser.parse(rules) +"\n```")
                .setFooter(ScpTools.getConfigManager().getString(CONFIG.RULES.EMBED_FOOTER), guild.getIconUrl());
    }

    public static void pasteRules(ButtonInteractionEvent event) {
        event.getChannel().sendMessageEmbeds(Objects.requireNonNull(embed(event.getGuild(), event.getChannel().asTextChannel())).build()).queue();
    }
    public static void updateRules(Guild guild, String channelID,String messageId) {
        TextChannel channel = guild.getTextChannelById(channelID);
        assert channel != null;
        List<Message> mess = MessageHistory.getHistoryFromBeginning(channel).complete().getRetrievedHistory();

        for (Message m : mess) {
            if (!m.getId().equals(messageId)) {
                channel.editMessageEmbedsById(m.getId())
                        .setEmbeds(Objects.requireNonNull(embed(guild, channel)).build()).queue();
            }
        }
    }
}
