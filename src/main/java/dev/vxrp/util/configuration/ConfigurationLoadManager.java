package dev.vxrp.util.configuration;

import dev.vxrp.util.Enums.LoadIndex;
import dev.vxrp.util.configuration.records.configs.ConfigGroup;
import dev.vxrp.util.configuration.records.translation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ConfigurationLoadManager {
    private final static Logger logger = LoggerFactory.getLogger(ConfigurationLoadManager.class);

    public List<Record> configs = new ArrayList<>();
    public List<Record> translations = new ArrayList<>();
    public static HashSet<Record> configLoads = new HashSet<>();
    public static HashSet<Record> translationLoads = new HashSet<>();

    public void addConfigs(ConfigGroup configGroup) {
        configs.add(configGroup);
    }

    public void addTranslations(SupportGroup supportGroup, NoticeOfDepartureGroup noticeOfDepartureGroup, RegularsGroup regularsGroup, LoggingGroup loggingGroup, ButtonGroup buttonGroup) {
        translations.add(supportGroup);
        translationLoads.add(noticeOfDepartureGroup);
        translationLoads.add(regularsGroup);
        translationLoads.add(loggingGroup);
        translationLoads.add(buttonGroup);
    }

    public void write() {
        logger.warn("Writing listed configurations, this could take a while...");
        if (!configs.isEmpty()) {
            configLoads.addAll(configs);
        }
        if (!translations.isEmpty()) {
            translationLoads.addAll(translations);
        }
        logger.info("Wrote all listed configurations to memory");
    }

    public Record getConfig(LoadIndex loadIndex) {
        int index = returnIndex(loadIndex);

        int count = 0;
        for (Record record : configs) {
            if (count == index) {
                return record;
            }
                count++;
        }
        return null;
    }

    public Record getTranslation(LoadIndex loadIndex) {
        int index = returnIndex(loadIndex);

        int count = 0;
        for (Record record : translations) {
            if (count == index) {
                return record;
            }
            count++;
        }
        return null;
    }

    private int returnIndex(LoadIndex loadIndex) {
        return switch (loadIndex) {
            case CONFIG_GROUP -> 0;
            case SUPPORT_GROUP -> 0;
            case NOTICE_OF_DEPARTURE_GROUP -> 1;
            case REGULARS_GROUP -> 2;
            case LOGGING_GROUP -> 3;
            case BUTTON_GROUP -> 4;
        };
    }
}
