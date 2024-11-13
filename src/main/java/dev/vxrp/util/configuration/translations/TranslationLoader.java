package dev.vxrp.util.configuration.translations;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.translations.TranslationManager;
import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.configuration.records.translation.*;
import dev.vxrp.util.configuration.util.TRANSLATIONS;
import dev.vxrp.util.configuration.ConfigurationLoadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslationLoader {
    private final static Logger logger = LoggerFactory.getLogger(TranslationLoader.class);

    public TranslationLoader(ConfigurationLoadManager configurations) {
        TranslationManager translationManager = ScpTools.getTranslationManager();
        ConfigGroup configGroup = (ConfigGroup) configurations.getConfig(LoadIndex.CONFIG_GROUP);
        String translation = configGroup.load_translation();

        SupportGroup supportGroup = new SupportGroup(
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.FIRST_BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.SECOND_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.SECOND_BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.SUPPORT_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.SUPPORT_FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.SUPPORT_FIRST_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.SUPPORT_SECOND_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.SUPPORT_SECOND_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.UNBAN_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.UNBAN_FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.UNBAN_FIRST_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.UNBAN_SECOND_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.UNBAN_SECOND_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.UNBAN_THIRD_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.UNBAN_THIRD_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_FIRST_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.TICKET.SUPPORT_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.TICKET.SUPPORT_BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.TICKET.SUPPORT_FOOTER)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.TICKET.SUPPORT_CREATED)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.TICKET.UNBAN_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.TICKET.UNBAN_BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.TICKET.UNBAN_FOOTER)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.TICKET.UNBAN_CREATED)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_ACCEPTED)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_DISMISSED)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_SENT)));

        NoticeOfDepartureGroup noticeOfDepartureGroup = new NoticeOfDepartureGroup(
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.FIRST_BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.TILE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.FIRST_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.SECOND_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.SECOND_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.FOOTER)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.CREATED)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.MESSAGE_ACCEPTED)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.MESSAGE_DISMISSED)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.MESSAGE_SENT)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.NOTICE.TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.NOTICE.BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.NOTICE.FOOTER)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.ENDED)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.REVOKED)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.REVOKED_MESSAGE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.ENDED_REPLACE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.NOTICE_OF_DEPARTURE.DELETED_ENDED_REPLACE_MESSAGE)));

        RegularsGroup regularsGroup = new RegularsGroup(
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.REGULARS.FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.REGULARS.FIRST_BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.REGULARS.GROUP_TEMPLATE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.REGULARS.ROLE_TEMPLATE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.REGULARS.SECOND_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.REGULARS.SECOND_BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.REGULARS.SYNC_GROUP_SELECT_MESSAGE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.REGULARS.SYNC_SELECT_MESSAGE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.REGULARS.SYNC_REMOVE_CONFIRM_MESSAGE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.REGULARS.SYNC_REMOVED_MESSAGE))
        );

        LoggingGroup loggingGroup = new LoggingGroup(
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.LOGGING.SINGLE_MESSAGE_LOG_TEMPLATE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.LOGGING.CREATE_LOG_TEMPLATE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.LOGGING.CLOSE_LOG_TEMPLATE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.LOGGING.DATABASE_LOG_TEMPLATE)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.LOGGING.SUPPORT_MESSAGE_LOGGING_ACTION)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.LOGGING.SUPPORT_TICKET_CREATE_ACTION)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.LOGGING.SUPPORT_TICKET_CLOSE_ACTION)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.LOGGING.NOTICE_OF_DEPARTURE_CREATE_ACTION)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.LOGGING.NOTICE_OF_DEPARTURE_DISMISS_ACTION)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.LOGGING.NOTICE_OF_DEPARTURE_CLOSE_ACTION)));

        StatusbarGroup statusbarGroup = new StatusbarGroup(
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.STATUS_BAR.TICKET_STATUS)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.STATUS_BAR.NOTICE_OF_DEPARTURE_ACCEPTED)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.STATUS_BAR.NOTICE_OF_DEPARTURE_DISMISSED)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.STATUS_BAR.NOTICE_OF_DEPARTURE_ENDED)),
                ColorTool.useCustomColorCodes(translationManager.getString(translation, TRANSLATIONS.STATUS_BAR.NOTICE_OF_DEPARTURE_REVOKED))
        );

        ButtonGroup buttonsGroup = new ButtonGroup(
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.PASTE_RULES),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.UPDATE_RULES),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.CREATE_NEW_SUPPORT_TICKET),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.CLOSE_SUPPORT_TICKET),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.CLAIM_SUPPORT_TICKET),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.SETTINGS_SUPPORT_TICKET),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.CREATE_NEW_UNBAN_TICKET),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.ACCEPT_UNBAN_TICKET),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.DISMISS_UNBAN_TICKET),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.SETTINGS_UNBAN_TICKET),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.ACCEPT_NOTICE_OF_DEPARTURE_TICKET),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.DISMISS_NOTICE_OF_DEPARTURE_TICKET),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.REVOKE_NOTICE_OF_DEPARTURE),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.DELETE_NOTICE_OF_DEPARTURE),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.FILE_NOTICE_OF_DEPARTURE),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.SYNC_REGULARS),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.DEACTIVATE_SYNC_REGULARS),
                translationManager.getString(translation, TRANSLATIONS.BUTTONS.REMOVE_SYNC_REGULARS));

        configurations.addTranslations(supportGroup, noticeOfDepartureGroup, regularsGroup, loggingGroup, statusbarGroup, buttonsGroup);
    }
}
