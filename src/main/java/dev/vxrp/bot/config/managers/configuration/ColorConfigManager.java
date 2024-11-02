package dev.vxrp.bot.config.managers.configuration;

import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ColorConfigManager {
    public final Path configPath = Paths.get("./configs/colorconfig.yml");

    public ColorConfigManager(){
        File tokenFile = new File(configPath.toString());
        if (!tokenFile.exists()) {
            InputStream inputStream = getClass().getResourceAsStream("/configs/colorconfig.yml");
            try (FileOutputStream os = new FileOutputStream(tokenFile)) {
                assert inputStream != null;
                os.write(inputStream.readAllBytes());
                inputStream.close();
            } catch (IOException e) {
                e.setStackTrace(e.getStackTrace());
            }
        }
    }

    private YamlConfiguration getYamlConfig() {
        final File file = new File(configPath.toString());
        return YamlConfiguration.loadConfiguration(file);
    }

    public List getList(String list) {
        return getYamlConfig().getList(list);
    }
}
