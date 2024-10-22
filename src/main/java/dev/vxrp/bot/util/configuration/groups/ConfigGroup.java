package dev.vxrp.bot.util.configuration.groups;

import java.util.List;

public record ConfigGroup(String token, String guild_id, String activity_type, String activity_content,
                          String rules_pastebin, String rules_embed_footer, List<String> commands, List<String> command_setting_help_default_permissions,
                          String command_settings_help_description, List<String> command_settings_template_default_permissions,
                          String command_settings_template_descriptions, List<String> support_settings_roles_access_support_tickets,
                          List<String> support_settings_roles_access_unban_tickets, String support_settings_unban_channel_id,
                          String notice_of_departure_decision_channel_id, String notice_of_departure_notice_channel_id, boolean cedmod_active,
                          String cedmod_instance_url, String cedmod_api_key, String cedmod_cedmod) {
}
