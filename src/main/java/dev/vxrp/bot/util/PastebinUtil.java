package dev.vxrp.bot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class PastebinUtil {
    public static String getPasteBin(String link) throws IOException {
        URL url = new URL(link);
        return new BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
    }
}
