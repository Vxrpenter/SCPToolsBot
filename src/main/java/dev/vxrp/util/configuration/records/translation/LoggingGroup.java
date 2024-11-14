package dev.vxrp.util.configuration.records.translation;

public record LoggingGroup(String single_message_log_template, String create_log_template, String close_log_template, String database_log_template, String support_message_logging_action,
                           String support_ticket_create_logging_action, String support_ticket_close_logging_action, String notice_of_departure_create_logging_action,
                           String notice_of_departure_dismiss_logging_action, String notice_of_departure_close_logging_action) {
}
