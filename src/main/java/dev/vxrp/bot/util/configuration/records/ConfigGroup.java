package dev.vxrp.bot.util.configuration.records;

import java.util.List;

public record ConfigGroup(String token, boolean debug, boolean advanced_debug, String activity_type, String activity_content,
                          String rules_pastebin, String rules_embed_footer, boolean do_logging, String ticket_logging_channel_id, String ticket_backup_logging_channel_id,
                          String notice_of_departures_logging_channel_id, boolean do_database_logging, String database_logging_channel_id, List<String> commands,
                          List<String> command_setting_help_default_permissions, String command_settings_help_description, List<String> command_settings_template_default_permissions,
                          String command_settings_template_descriptions, List<String> support_settings_roles_access_support_tickets, List<String> support_settings_roles_access_unban_tickets,
                          String support_settings_unban_channel_id, String notice_of_departure_decision_channel_id, String notice_of_departure_notice_channel_id,
                          List<String> notice_of_departure_roles_access_notices, boolean notice_of_departure_check_on_startup, String notice_of_departure_check_type,
                          int notice_of_departure_check_rate, boolean cedmod_active, String cedmod_instance_url, String cedmod_api_key, String cedmod_master_banlist_id) { }