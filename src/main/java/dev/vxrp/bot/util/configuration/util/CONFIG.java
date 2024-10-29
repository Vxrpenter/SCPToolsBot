package dev.vxrp.bot.util.configuration.util;

public class CONFIG {
    public static final String TOKEN = "token";
    public static final String GUILD_ID = "guild_id";
    public static final String ACTIVITY_TYPE = "activity_type";
    public static final String ACTIVITY_CONTENT = "activity_content";
    public static final class LOGGING {
        public static final String DO_LOGGING = "logging.";
        public static final String TICKET_CHANNEL_ID = "logging.";
        public static final String NOTICE_OF_DEPARTURE_CHANNEL_ID = "logging.";
        public static final String DO_DATABASE_LOGGING = "logging.";
        public static final String DATABASE_CHANNEL_ID = "logging.";
    }
    public static final String COMMANDS = "commands";
    public static final class RULES {
        public static final String PASTEBIN = "rules.pastebin";
        public static final String EMBED_FOOTER = "rules.embed_footer";
    }
    public static final class COMMAND_SETTINGS {
        public static final class DEFAULT_PERMISSIONS {
            public static final String HELP = "command_settings.help.default_permissions";
            public static final String TEMPLATE = "command_settings.template.default_permissions";
        }
        public static final class DESCRIPTIONS {
            public static final String HELP = "command_settings.help.description";
            public static final String TEMPLATE = "command_settings.template.description";
        }
    }
    public static final class SUPPORT_SETTINGS {
        public static final String ROLES_ACCESS_SUPPORT_TICKETS = "support_settings.roles_access_support_tickets";
        public static final String ROLES_ACCESS_UNBAN_TICKETS = "support_settings.roles_access_unban_tickets";
        public static final String UNBAN_CHANNEL_ID = "support_settings.unban_channel_id";
    }
    public static final class NOTICE_OF_DEPARTURE {
        public static final String DESCISION_CHANNEL_ID = "notice_of_departure.decision_channel_id";
        public static final String NOTICE_CHANNEL_ID = "notice_of_departure.notice_channel_id";
        public static final String ROLES_ACCESS_NOTICES = "notice_of_departure.roles_access_notices";
        public static final String CHECK_ON_STARTUP = "notice_of_departure.check_on_startup";
        public static final String CHECK_TYPE = "notice_of_departure.check_type";
        public static final String CHECK_RATE = "notice_of_departure.check_rate";
    }
    public static final class CEDMOD {
        public static final String ACTIVE = "cedmod.active";
        public static final String INSTANCE_URL = "cedmod.instance_url";
        public static final String API_KEY = "cedmod.api_key";
        public static final String MASTER_BAN_LIST_ID = "cedmod.master_ban_list_id";
    }
}
