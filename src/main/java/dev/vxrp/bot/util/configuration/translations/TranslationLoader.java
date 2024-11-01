package dev.vxrp.bot.util.configuration.translations;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.colors.ColorTool;
import dev.vxrp.bot.util.configuration.records.ButtonGroup;
import dev.vxrp.bot.util.configuration.records.LoggingGroup;
import dev.vxrp.bot.util.configuration.util.TRANSLATIONS;
import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.records.NoticeOfDepartureGroup;
import dev.vxrp.bot.util.configuration.records.SupportGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslationLoader {
    private final static Logger logger = LoggerFactory.getLogger(TranslationLoader.class);

    public TranslationLoader() {
        TranslationManager translationManager = ScpTools.getTranslationManager();

        SupportGroup supportGroup = new SupportGroup(
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.FIRST_BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.SECOND_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.SECOND_BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_FIRST_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_SECOND_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_SECOND_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_FIRST_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_SECOND_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_SECOND_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_THIRD_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_THIRD_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_FIRST_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_FOOTER)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_CREATED)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_FOOTER)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_CREATED)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_ACCEPTED)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_DISMISSED)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_SENT)));

        NoticeOfDepartureGroup noticeOfDepartureGroup = new NoticeOfDepartureGroup(
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.FIRST_BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.TILE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.FIRST_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.FIRST_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.SECOND_TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.SECOND_PLACEHOLDER)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.FOOTER)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.CREATED)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.MESSAGE_ACCEPTED)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.MESSAGE_DISMISSED)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.MESSAGE_SENT)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.NOTICE.TITLE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.NOTICE.BODY)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.NOTICE.FOOTER)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.ENDED)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.REVOKED)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.REVOKED_MESSAGE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.ENDED_REPLACE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.DELETED_ENDED_REPLACE_MESSAGE)));

        LoggingGroup loggingGroup = new LoggingGroup(
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.LOGGING.SINGLE_MESSAGE_LOG_TEMPLATE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.LOGGING.CREATE_LOG_TEMPLATE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.LOGGING.CLOSE_LOG_TEMPLATE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.LOGGING.DATABASE_LOG_TEMPLATE)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.LOGGING.SUPPORT_MESSAGE_LOGGING_ACTION)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.LOGGING.SUPPORT_TICKET_CREATE_ACTION)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.LOGGING.SUPPORT_TICKET_CLOSE_ACTION)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.LOGGING.NOTICE_OF_DEPARTURE_CREATE_ACTION)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.LOGGING.NOTICE_OF_DEPARTURE_DISMISS_ACTION)),
                ColorTool.useCustomColorCodes(translationManager.getString(TRANSLATIONS.LOGGING.NOTICE_OF_DEPARTURE_CLOSE_ACTION)));

        ButtonGroup buttonsGroup = new ButtonGroup(
                translationManager.getString(TRANSLATIONS.BUTTONS.PASTE_RULES),
                translationManager.getString(TRANSLATIONS.BUTTONS.UPDATE_RULES),
                translationManager.getString(TRANSLATIONS.BUTTONS.CREATE_NEW_SUPPORT_TICKET),
                translationManager.getString(TRANSLATIONS.BUTTONS.CLOSE_SUPPORT_TICKET),
                translationManager.getString(TRANSLATIONS.BUTTONS.CLAIM_SUPPORT_TICKET),
                translationManager.getString(TRANSLATIONS.BUTTONS.SETTINGS_SUPPORT_TICKET),
                translationManager.getString(TRANSLATIONS.BUTTONS.CREATE_NEW_UNBAN_TICKET),
                translationManager.getString(TRANSLATIONS.BUTTONS.ACCEPT_UNBAN_TICKET),
                translationManager.getString(TRANSLATIONS.BUTTONS.DISMISS_UNBAN_TICKET),
                translationManager.getString(TRANSLATIONS.BUTTONS.SETTINGS_UNBAN_TICKET),
                translationManager.getString(TRANSLATIONS.BUTTONS.ACCEPT_NOTICE_OF_DEPARTURE_TICKET),
                translationManager.getString(TRANSLATIONS.BUTTONS.DISMISS_NOTICE_OF_DEPARTURE_TICKET),
                translationManager.getString(TRANSLATIONS.BUTTONS.REVOKE_NOTICE_OF_DEPARTURE),
                translationManager.getString(TRANSLATIONS.BUTTONS.DELETE_NOTICE_OF_DEPARTURE),
                translationManager.getString(TRANSLATIONS.BUTTONS.FILE_NOTICE_OF_DEPARTURE));


        logger.warn("Loading translations, this could take some time...");
        for (var component : buttonsGroup.getClass().getRecordComponents()) {
            try {
                logger.trace("Added value to button translations - {}", component.getAccessor().invoke(buttonsGroup));
            } catch (Exception e) {debuggerErrorHandler(e);}
        }
        LoadedConfigurations.setButtonsMemoryLoad(buttonsGroup);
        logger.info("Loaded button translations");

        for (var component : supportGroup.getClass().getRecordComponents()) {
            try {
                logger.trace("Added value to support translations - {}", component.getAccessor().invoke(supportGroup));
            } catch (Exception e) {debuggerErrorHandler(e);}
        }
        LoadedConfigurations.setSupportTranslationMemoryLoad(supportGroup);
        logger.info("Loaded support translations");

        for (var component : loggingGroup.getClass().getRecordComponents()) {
            try {
                logger.trace("Added value to logging translations - {}", component.getAccessor().invoke(loggingGroup));
            } catch (Exception e) {debuggerErrorHandler(e);}
        }
        LoadedConfigurations.setLoggingMemoryLoad(loggingGroup);
        logger.info("Loaded logging translations");

        for (var component : noticeOfDepartureGroup.getClass().getRecordComponents()) {
            try {
                logger.trace("Added value to notice of departure translations - {}", component.getAccessor().invoke(noticeOfDepartureGroup));
            } catch (Exception e) {debuggerErrorHandler(e);}
        }
        LoadedConfigurations.setNoticeOfDepartureMemoryLoad(noticeOfDepartureGroup);
        logger.info("Loaded notice of departure translations");
    }

    private static void debuggerErrorHandler(Exception e) {
        logger.debug("{} Could not log the exact translation value (this error can be ignored) - Stacktrace {}", ColorTool.apply(DCColor.RED, "ERROR") ,e.getMessage());
    }
}
