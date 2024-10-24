package dev.vxrp.bot.util.api.github;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import dev.vxrp.bot.util.Enums.DCColor;
import dev.vxrp.bot.util.colors.ColorTool;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;


public class GitHubApi {
    private static final Logger logger = LoggerFactory.getLogger(GitHubApi.class);
    private static final OkHttpClient client = new OkHttpClient();

    public static void CheckForUpdatesByTags(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        assert response.body() != null;
        JsonArray tagArray = JsonParser.parseString(response.body().string()).getAsJsonArray();

        String tag = tagArray.get(tagArray.size() -1).getAsJsonObject().get("ref").getAsString()
                .replace("refs/tags/v.", "");

        Properties properties = new Properties();

        try (InputStream versionPropertiesStream = GitHubApi.class.getResourceAsStream("/vxrp/dev/version.properties")) {
            if (versionPropertiesStream == null) {
                throw new IllegalStateException("Version properties file does not exist");
            }
            properties.load(new InputStreamReader(versionPropertiesStream, StandardCharsets.UTF_8));
        }

        if (!Objects.equals(properties.getProperty("version"), tag)) {
            logger.warn("A new version has been found, you can download it from {}", ColorTool.apply(DCColor.LIGHT_BLUE, "https://github.com/Vxrpenter/SCPToolsBot/releases/tag/v."+tag));
        }
    }
}
