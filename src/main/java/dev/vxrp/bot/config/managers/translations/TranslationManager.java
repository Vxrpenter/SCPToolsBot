package dev.vxrp.bot.config.managers.translations;

import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class TranslationManager {

    public TranslationManager(){
        List<String> files = Arrays.asList("de_de", "en_us");

        for (String file : files) {
            File translation = new File("lang/" + file+".yml");
            if (!translation.exists()) {
                InputStream inputStream = getClass().getResourceAsStream("/lang/" +file+".yml");
                try (FileOutputStream os = new FileOutputStream(translation)) {
                    assert inputStream != null;
                    os.write(inputStream.readAllBytes());
                    inputStream.close();
                } catch (IOException e) {
                    e.setStackTrace(e.getStackTrace());
                }
            }
        }
    }
    private YamlConfiguration getYamlConfig(String fileName) {
        final File file = new File(Path.of("lang/" +fileName+".yml").toString());
        return YamlConfiguration.loadConfiguration(file);
    }

    public String getString(String fileName, String key) {return getYamlConfig(fileName).getString(key);}
}
