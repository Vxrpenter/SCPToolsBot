package dev.vxrp.bot.util.configuration.groups;

public record ButtonGroup(String paste_rules, String update_rules, String create_new_support_ticket,
                          String close_support_ticket, String claim_support_ticket, String settings_support_ticket,
                          String create_new_unban_ticket, String accept_unban_ticket, String dismiss_unban_ticket,
                          String settings_unban_ticket, String accept_notice_of_departure_ticket, String dismiss_notice_of_departure_ticket,
                          String revoke_notice_of_departure, String delete_notice_of_departure, String file_notice_of_departure) {
}
