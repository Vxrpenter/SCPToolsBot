package dev.vxrp.bot.commands.help;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class HelpCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        event.replyEmbeds(new EmbedBuilder()
                .setColor(Color.BLACK)
                        .setDescription("""
                                ## Help Manuscript
                                
                                - /help for getting all help information
                                - /template to paste certain templates in channels manually
                                - /info to get the info that was entered into the config file
                                
                                **For more specific help please use the category buttons listed down below**
                                """)
                .build())
                .addActionRow(
                    Button.secondary("help.commands", "Commands").withEmoji(Emoji.fromUnicode("⚙️")),
                    Button.secondary("help.credit", "Credit").withEmoji(Emoji.fromUnicode("📫"))
                )
                .setEphemeral(true).queue();
    }
}
