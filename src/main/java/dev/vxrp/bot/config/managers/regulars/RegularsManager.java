package dev.vxrp.bot.config.managers.regulars;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import dev.vxrp.util.configuration.LoadedConfigurations;
import dev.vxrp.util.records.Regular;
import dev.vxrp.util.records.RegularConfig;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class RegularsManager {
    private final Path path;
    public RegularsManager(Path path) throws IOException, NullPointerException {
        this.path = path;
        String folderPath = path+"/regulars/";
        new File(folderPath).mkdir();

        if (LoadedConfigurations.getConfigMemoryLoad().create_example_configuration()) {
            String examplePath = path+"/regulars/examples/";
            new File(examplePath).mkdirs();
            File config = new File(examplePath+"config.json");
            File manifest = new File(examplePath+"config.json");
            manifest.createNewFile();
            config.createNewFile();

            for (String array : Arrays.asList("config.json", "manifest.json")) {
                if (new File(examplePath+array).exists()) continue;
                try (FileOutputStream os = new FileOutputStream(examplePath+array)) {
                    InputStream in = getClass().getResourceAsStream("/regulars/example/"+array);
                    assert in != null;
                    os.write(in.readAllBytes());
                    in.close();
                }
            }
        }
    }

    public HashMap<String, List<JsonObject>> loadConfigurationObjects() throws IOException {
        HashMap<String, List<JsonObject>> configObjects = new HashMap<>();
        for (File file : folders()) {
            List<String> files = getStrings(file);
            List<JsonObject> objects = new ArrayList<>();

            for (String fileName : files) {
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new FileReader(file+"/"+fileName));
                objects.add(gson.fromJson(reader, JsonObject.class));
            }
            configObjects.put(file.getName(), objects);
        }
        return configObjects;
    }

    public List<Regular> getRegulars() throws IOException {
        List<Regular> regulars = new ArrayList<>();

        for (File file : folders()) {
            List<JsonObject> objects = loadConfigurationObjects().get(file.getName());
            JsonObject manifest = objects.get(1);
            JsonObject config = objects.get(0);

            Regular regular = new Regular(
                    manifest.get("name").getAsString(),
                    manifest.get("description").getAsString(),
                    manifest.get("custom_role").getAsJsonObject().get("use").getAsBoolean(),
                    manifest.get("custom_role").getAsJsonObject().get("id").getAsString(),
                    config);
            regulars.add(regular);
        }
        return regulars;
    }

    public List<RegularConfig> getConfig() throws IOException {
        List<RegularConfig> configs = new ArrayList<>();

        for (Regular regular : getRegulars()) {
            JsonArray config = regular.config().get("roles").getAsJsonArray();

            for (JsonElement object : config) {
                JsonObject jsonObject = object.getAsJsonObject();

                RegularConfig regularConfig = new RegularConfig(
                        jsonObject.get("id").getAsString(),
                        jsonObject.get("description").getAsString(),
                        jsonObject.get("playtime_requirements").getAsInt());
                configs.add(regularConfig);
            }
        }
        return configs;
    }

    public List<File> folders() {
        List<File> folders = new ArrayList<>();
        Collections.addAll(folders, Objects.requireNonNull(new File(path + "/regulars/").listFiles()));
        return folders;
    }

    @NotNull
    private static List<String> getStrings(File file) {
        File[] files = file.listFiles();
        List<String> names = new ArrayList<>();
        assert files != null;
        for (File currenFile : files) {
            names.add(currenFile.getName());
        }
        return names;
    }
}
