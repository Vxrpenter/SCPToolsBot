package dev.vxrp.bot.util.configuration.translations;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.TranslationManager;
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
                translationManager.getString(TRANSLATIONS.NOTICE_OF_DEPARTURE.ENDED_REPLACE));

        LoadedConfigurations.setSupportTranslationMemoryLoad(supportGroup);
        LoadedConfigurations.setNoticeOfDepartureMemoryLoad(noticeOfDepartureGroup);
        logger.info("Loaded translations into memory");
    }
}
