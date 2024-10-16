package dev.vxrp.bot.config.managers;

import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ConfigManager {
    public final Path configPath = Paths.get("./configs/config.yml");

    public ConfigManager(){
        File tokenFile = new File(configPath.toString());
        if (!tokenFile.exists()) {
            InputStream inputStream = getClass().getResourceAsStream("/configs/config.yml");
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
    public String getString(String key) {
        return getYamlConfig().getString(key);
    }
    public boolean getBoolean(String key) {
        return getYamlConfig().getBoolean(key);
    }
    public List<String> getStringList(String key) {
        return getYamlConfig().getStringList(key);
    }
    // Token
    public String getToken() {
        if (getYamlConfig().getString("token") == null) {
            throw new RuntimeException("Token is null");
        } else {
            return getYamlConfig().getString("token");
        }
    }
}
