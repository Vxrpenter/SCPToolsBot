package dev.vxrp.bot.commands.help;
import dev.vxrp.util.colors.ColorTool;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HelpCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("help")) return;

        event.replyEmbeds(pages().getFirst())
                .setActionRow(actionRow(0)).queue();
    }

    public static List<MessageEmbed> pages() {
        List<MessageEmbed> pages = new ArrayList<>();

        pages.add(new EmbedBuilder()
                .setColor(Color.BLACK)
                .setTitle("Scp Tools Help Page")
                .setDescription(ColorTool.useCustomColorCodes("""
                                > This is ScpTools. A bot for managing your SCP Secret Laboratory Server with community and staff with quality of life improvements and a custom support system. Translate everything to your liking and choose the settings that fit your server the best to get going.
                                &filler&
                                [⭐] If you need help with bot setup or you are running into issues then please refer to the documentation [here](https://github.com/Vxrpenter/SCPToolsBot/wiki).
                                
                                [❌] Is your issue not explained here? Then please create an issue on github [here](https://github.com/Vxrpenter/SCPToolsBot/issues)
                                
                                ### Short list of commands:
                                `/help` - for help
                                `/template` - for pasting the templates
                                - `rules` - secret lab rules
                                - `support` - support ticket system + unban tickets
                                - `notice of departure` - notice of departure panel for staff
                                - `regulars` - regular panel for users
                                - `adminpanel` - ❌ WIP
                                """))
                        .setFooter("⭐ Created with lots of passion by Vxrpenter ⭐", "https://avatars.githubusercontent.com/u/110356385?v=4")
                .build());

        pages.add(new EmbedBuilder()
                .setColor(Color.BLACK)
                .setTitle("Scp Tools Help Page - Rules")
                .setDescription(ColorTool.useCustomColorCodes("""
                        > ❌ CAUTION
                        > This feature is experimental and not that tested. Also the update feature is not working the moment
                        &filler&
                        Use `/template rules` to paste the rule menu.
                        
                        You can choose between updating the rules and pasting them. This then takes your Scp Secret Laboratory Server ruleset and translate it using a color parser automatically.
                        > define these color translations in the `colorconfig.yml`
                        """))
                .setFooter("⭐ Created with lots of passion by Vxrpenter ⭐", "https://avatars.githubusercontent.com/u/110356385?v=4")
                .build());

        pages.add(new EmbedBuilder()
                .setColor(Color.BLACK)
                .setTitle("Scp Tools Help Page - Support and Unban")
                .setDescription(ColorTool.useCustomColorCodes("""
                        Use `/template support` command to paste the support template.
                        &filler&
                        ### Options of the template:
                        - `Create Support Ticket` - Create a support ticket that the question or problem of the user can be processed in
                        - `File Unban Request` - Let's a user file an unban request for your SCP:SL Server. when accepting these requests the user will be automatically be unbanned from the server given they put in the correct steamID
                        """))
                .setFooter("⭐ Created with lots of passion by Vxrpenter ⭐", "https://avatars.githubusercontent.com/u/110356385?v=4")
                .build());

        pages.add(new EmbedBuilder()
                .setColor(Color.BLACK)
                .setTitle("Scp Tools Help Page - Notice of Departure")
                .setDescription(ColorTool.useCustomColorCodes("""
                        Use `/template notice of departure` to paste the notice of departure template
                        &filler&
                        This feature is to make users able to take time off without worrying about the date to much because the bot notifies them when they have to come on again
                        
                        When a user files a notice of departure it is send to a predefined channel for acceptance. When accepted is is then processed and added to the database.
                        > if accepted, declined, revoked or it ending normally the user is notified
                        """))
                .setFooter("⭐ Created with lots of passion by Vxrpenter ⭐", "https://avatars.githubusercontent.com/u/110356385?v=4")
                .build());

        pages.add(new EmbedBuilder()
                .setColor(Color.BLACK)
                .setTitle("Scp Tools Help Page - Regulars")
                .setDescription(ColorTool.useCustomColorCodes("""
                        Use `/template regulars` to paste the regular template.
                        &filler&
                        Offer your users a reward for playing a certain amount off time.
                        
                        Users can activate/deactivate the regular syncing which automatically caches their time from the server and gives them the roles they deserve for playing that amount of time
                        """))
                .setFooter("⭐ Created with lots of passion by Vxrpenter ⭐", "https://avatars.githubusercontent.com/u/110356385?v=4")
                .build());

        pages.add(new EmbedBuilder()
                .setColor(Color.BLACK)
                .setTitle("Scp Tools Help Page - Admin Panel")
                .setDescription(ColorTool.useCustomColorCodes("""
                        > ❌ WORK IN PROGRESS
                        > This feature has not been developed yet
                        &filler&
                        Nothing here yet
                        """))
                .setFooter("⭐ Created with lots of passion by Vxrpenter ⭐", "https://avatars.githubusercontent.com/u/110356385?v=4")
                .build());

        return pages;
    }

    public static Collection<? extends ItemComponent> actionRow(int page) {
        Collection<ItemComponent> rows = new ArrayList<>();
        if (page == 0 ) {
            rows.add(Button.danger("help_first_page", "|<").asDisabled());
            rows.add( Button.secondary("help_go_back:"+page, "<").asDisabled());
            rows.add( Button.link("https://github.com/Vxrpenter/SCPToolsBot/wiki", "📝 Documentation"));
            rows.add(Button.primary("help_go_forward:"+page, ">"));
            rows.add(Button.success("help_last_page:"+page, ">|"));
        }
        if (page == 5) {
            rows.add(Button.success("help_first_page", "|<"));
            rows.add( Button.primary("help_go_back:"+page, "<"));
            rows.add( Button.link("https://github.com/Vxrpenter/SCPToolsBot/wiki", "📝 Documentation"));
            rows.add(Button.secondary("help_go_forward:"+page, ">").asDisabled());
            rows.add(Button.danger("help_last_page:"+page, ">|").asDisabled());
        }
        if (page != 0 && page != 5) {
            rows.add(Button.success("help_first_page", "|<"));
            rows.add(Button.primary("help_go_back:"+page, "<"));
            rows.add( Button.link("https://github.com/Vxrpenter/SCPToolsBot/wiki", "📝 Documentation"));
            rows.add(Button.primary("help_go_forward:"+page, ">"));
            rows.add(Button.success("help_last_page:"+page, ">|"));
        }
        return rows;
    }
}
