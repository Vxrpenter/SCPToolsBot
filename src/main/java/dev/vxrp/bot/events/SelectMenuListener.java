package dev.vxrp.bot.events;
import dev.vxrp.bot.events.selectmenus.Regulars;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SelectMenuListener extends ListenerAdapter {
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        // Regulars
        if (event.getComponentId().equals("regulars_sync_group_select")) {
            Regulars.syncRegularsEnterInfo(event);
        }
    }
}
