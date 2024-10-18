package dev.vxrp.bot.util.configuration;

import dev.vxrp.bot.util.configuration.translations.groups.NoticeOfDepartureGroup;
import dev.vxrp.bot.util.configuration.translations.groups.SupportGroup;

import java.util.HashSet;
import java.util.Iterator;

public class LoadedConfigurations {
    public static HashSet<SupportGroup> supportTranslationMemoryLoad  = new HashSet<>();
    public static HashSet<NoticeOfDepartureGroup> noticeOfDepartureMemoryLoad  = new HashSet<>();

    public static SupportGroup getSupportTranslationMemoryLoad() {
        Iterator<SupportGroup> iterator = supportTranslationMemoryLoad.iterator();
        SupportGroup supportGroup = null;
        while (iterator.hasNext()) {
            supportGroup = iterator.next();
        }
        return supportGroup;
    }

    public static void setSupportTranslationMemoryLoad(SupportGroup supportMemoryLoad) {
        LoadedConfigurations.supportTranslationMemoryLoad.add(supportMemoryLoad);
    }

    public static NoticeOfDepartureGroup getNoticeOfDepartureMemoryLoad() {
        Iterator<NoticeOfDepartureGroup> iterator = noticeOfDepartureMemoryLoad.iterator();
        NoticeOfDepartureGroup noticeOfDepartureGroup = null;
        while (iterator.hasNext()) {
            noticeOfDepartureGroup = iterator.next();
        }
        return noticeOfDepartureGroup;
    }

    public static void setNoticeOfDepartureMemoryLoad(NoticeOfDepartureGroup noticeOfDepartureMemoryLoad) {
        LoadedConfigurations.noticeOfDepartureMemoryLoad.add(noticeOfDepartureMemoryLoad);
    }
}
