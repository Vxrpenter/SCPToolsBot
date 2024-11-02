package dev.vxrp.util.configuration;

import dev.vxrp.util.configuration.records.*;

import java.util.HashSet;
import java.util.Iterator;

public class LoadedConfigurations {
    public static HashSet<ConfigGroup> configMemoryLoad = new HashSet<>();
    public static HashSet<SupportGroup> supportTranslationMemoryLoad  = new HashSet<>();
    public static HashSet<NoticeOfDepartureGroup> noticeOfDepartureMemoryLoad  = new HashSet<>();
    public static HashSet<ButtonGroup> buttonsMemoryLoad  = new HashSet<>();
    public static HashSet<LoggingGroup> loggingButtonMemoryLoad  = new HashSet<>();

    public static ConfigGroup getConfigMemoryLoad() {
        Iterator<ConfigGroup> iterator = configMemoryLoad.iterator();
        ConfigGroup configGroup = null;
        while (iterator.hasNext()) {
            configGroup = iterator.next();
        }
        return configGroup;
    }

    public static void setConfigMemoryLoad(ConfigGroup configGroup) {
        LoadedConfigurations.configMemoryLoad.add(configGroup);
    }

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

    public static ButtonGroup getButtonMemoryLoad() {
        Iterator<ButtonGroup> iterator = buttonsMemoryLoad.iterator();
        ButtonGroup buttonsGroup = null;
        while (iterator.hasNext()) {
            buttonsGroup = iterator.next();
        }
        return buttonsGroup;
    }

    public static void setButtonsMemoryLoad(ButtonGroup buttonsMemoryLoad) {
        LoadedConfigurations.buttonsMemoryLoad.add(buttonsMemoryLoad);
    }

    public static LoggingGroup getLoggingMemoryLoad() {
        Iterator<LoggingGroup> iterator = loggingButtonMemoryLoad.iterator();
        LoggingGroup loggingGroup = null;
        while (iterator.hasNext()) {
            loggingGroup = iterator.next();
        }
        return loggingGroup;
    }

    public static void setLoggingMemoryLoad(LoggingGroup loggingMemoryLoad) {
        LoadedConfigurations.loggingButtonMemoryLoad.add(loggingMemoryLoad);
    }
}
