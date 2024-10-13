package dev.vxrp.bot.config.util;

public class CONFIG {
    public static final String ACTIVITY_TYPE = "activity_type";
    public static final String ACTIVITY_CONTENT = "activity_content";
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
        public static final String ROLES_ACCESS_TICKETS = "support_settings.roles_access_tickets";
    }
}
