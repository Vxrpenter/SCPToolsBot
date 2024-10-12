package dev.vxrp.bot.support;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.Objects;

public class Support {
    public static void pasteSupportTemplate(Guild guild, String ChannelID) {
        Objects.requireNonNull(guild.getTextChannelById(ChannelID)).sendMessageEmbeds(new EmbedBuilder()
                .setColor(Color.decode("#5865F2"))
                .setTitle("Create Support Ticket")
                .setDescription("""
                        Bei jeglicher Art von Problem kannst du ein Support Ticket erstellen
                       
                        **Bearbeitungsdauer:**
                        Bis zu 24h bis dein Ticket bearbeitet wird
                       """)
                .setThumbnail(guild.getIconUrl())
                .setColor(Color.RED).build())
                .queue();
        Objects.requireNonNull(guild.getTextChannelById(ChannelID)).sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.decode("#5865F2"))
                        .setTitle("Unban Request")
                        .setDescription("""
                        Wurdest du ungerechterweise gebannt? Wenn ja, dann stell bitte einen Entbannungsantrag.

                        **Bearbeitungsdauer:**
                        Die Bearbeitung eines Entbannungsantrag kann bis zu einer Woche dauern

                        **Weitergegebene Informationen:**
                        Wir erhalten von dir Folgende Infos:
                        - Deine SteamID
                        - (Optional) Discord User
                        """)
                        .setColor(Color.RED).build())
                .addActionRow(
                        net.dv8tion.jda.api.interactions.components.buttons.Button.success("createNewTicket", "Create Support Ticket").withEmoji(Emoji.fromUnicode("📩")),
                        Button.link("https://forms.gle/vGiBBhnRddoVBqZu9", "File unban request").withEmoji(Emoji.fromUnicode("📩"))
                ).queue();
    }
}
