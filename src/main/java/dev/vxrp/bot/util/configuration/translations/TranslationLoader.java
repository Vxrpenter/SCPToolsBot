package dev.vxrp.bot.util.configuration.translations;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
import dev.vxrp.bot.util.configuration.groups.ButtonGroup;
import dev.vxrp.bot.util.configuration.util.TRANSLATIONS;
import dev.vxrp.bot.util.configuration.LoadedConfigurations;
import dev.vxrp.bot.util.configuration.groups.NoticeOfDepartureGroup;
import dev.vxrp.bot.util.configuration.groups.SupportGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslationLoader {
    private final static Logger logger = LoggerFactory.getLogger(TranslationLoader.class);

    public TranslationLoader() {
        TranslationManager translationManager = ScpTools.getTranslationManager();

        SupportGroup supportGroup = new SupportGroup(
                translationManager.getString(TRANSLATIONS.SUPPORT.FIRST_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.FIRST_BODY),
                translationManager.getString(TRANSLATIONS.SUPPORT.SECOND_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.SECOND_BODY),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_FIRST_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_FIRST_PLACEHOLDER),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_SECOND_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.SUPPORT_SECOND_PLACEHOLDER),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_FIRST_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_FIRST_PLACEHOLDER),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_SECOND_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_SECOND_PLACEHOLDER),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_THIRD_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.UNBAN_THIRD_PLACEHOLDER),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_FIRST_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.MODAL.REASON_ACTION_FIRST_PLACEHOLDER),
                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_BODY),
                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_FOOTER),
                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.SUPPORT_CREATED),
                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_TITLE),
                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_BODY),
                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_FOOTER),
                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_CREATED),
                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_ACCEPTED),
                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_DISMISSED),
                translationManager.getString(TRANSLATIONS.SUPPORT.TICKET.UNBAN_MESSAGE_SENT)
        );

        NoticeOfDepartureGroup noticeOfDepartureGroup = new NoticeOfDepartureGroup(
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.FIRST_TITLE),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.FIRST_BODY),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.TILE),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.FIRST_TITLE),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.FIRST_PLACEHOLDER),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.SECOND_TITLE),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.MODAL.SECOND_PLACEHOLDER),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.TITLE),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.BODY),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.FOOTER),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.CREATED),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.MESSAGE_ACCEPTED),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.MESSAGE_DISMISSED),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.TICKET.MESSAGE_SENT),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.NOTICE.TITLE),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.NOTICE.BODY),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.NOTICE.FOOTER),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.ENDED),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.REVOKED),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.REVOKED_MESSAGE),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.ENDED_REPLACE),
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.DELETED_ENDED_REPLACE_MESSAGE));

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
        LoadedConfigurations.setButtonsMemoryLoad(buttonsGroup);
        logger.info("Loaded button translations");
        LoadedConfigurations.setSupportTranslationMemoryLoad(supportGroup);
        logger.info("Loaded support translations");
        LoadedConfigurations.setNoticeOfDepartureMemoryLoad(noticeOfDepartureGroup);
        logger.info("Loaded notice of departure translations");
    }
}
