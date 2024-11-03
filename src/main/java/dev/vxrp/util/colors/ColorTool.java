package dev.vxrp.util.colors;

import dev.vxrp.util.Enums.DCColor;
import org.jetbrains.annotations.NotNull;

public class ColorTool {

    private static final String filler = "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E " +
            "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E " +
            "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E " +
            "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E " +
            "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E " +
            "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E " +
            "\u200E \u200E \u200E \u200E \u200E \u200E \u200E";
    public static final String DARK_GRAY = "\u001B[2;30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String GOLD = "\u001B[33m";
    public static final String LIGHT_BLUE = "\u001B[34m";
    public static final String PINK = "\u001B[35m";
    public static final String TEAL = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1;2m";
    public static final String UNDERLINE = "\u001B[4;2m";
    public static final String RESET = "\u001B[0m";


    @NotNull
    public static String apply(DCColor color, String text) {
        switch (color) {
            case DARK_GRAY -> {
                return "\u001B[2;30m"+text+"\u001B[0m";
            }
            case RED -> {
                return "\u001B[2;31m"+text+"\u001B[0m";
            }
            case GREEN -> {
                return "\u001B[2;32m"+text+"\u001B[0m";
            }
            case GOLD -> {
                return "\u001B[2;33m"+text+"\u001B[0m";
            }
            case LIGHT_BLUE -> {
                return "\u001B[2;34m"+text+"\u001B[0m";
            }
            case PINK -> {
                return "\u001B[2;35m"+text+"\u001B[0m";
            }
            case TEAL -> {
                return "\u001B[2;36m"+text+"\u001B[0m";
            }
            case WHITE -> {
                return "\u001B[2;37m"+text+"\u001B[0m";
            }
            case BOLD -> {
                return "\u001B[1;2m"+text+"\u001B[0m";
            }
            case UNDERLINE -> {
                return "\u001B[4;2m"+text+"\u001B[0m";
            }
        }
        return text;
    }
    @NotNull
    public static String useCustomColorCodes(String text) {
        return text
                .replace("&dark_gray&", "\u001B[2;30m")
                .replace("&red&", "\u001B[2;31m")
                .replace("&green&", "\u001B[2;32m")
                .replace("&gold&", "\u001B[2;33m")
                .replace("&light_blue&", "\u001B[2;34m")
                .replace("&pink&", "\u001B[2;35m")
                .replace("&teal&", "\u001B[2;36m")
                .replace("&white&", "\u001B[2;37m")
                .replace("&bold&", "\u001B[1;2m")
                .replace("&reset&", "\u001B[0m")
                .replace("&underline&", "\u001B[4;2m")
                .replace("%filler%", filler);

    }
    @NotNull
    public static String replaceCustomColorCode(String colorCode) {
        switch (colorCode) {
            case "&dark_gray&" -> {
                return "\u001B[2;30m";
            }
            case "&red&" -> {
                return "\u001B[2;31m";
            }
            case "&green&" -> {
                return "\u001B[2;32m";
            }
            case "&gold&" -> {
                return "\u001B[2;33m";
            }
            case "&light_blue&" -> {
                return "\u001B[2;34m";
            }
            case "&pink&" -> {
                return "\u001B[2;35m";
            }
            case "&teal&" -> {
                return "\u001B[2;36m";
            }
            case "&white&" -> {
                return "\u001B[2;37m";
            }
            case "&bold&" -> {
                return "\u001B[1;2m";
            }
            case "&reset&" -> {
                return "\u001B[0m";
            }
            case "&underline&" -> {
                return "\u001B[4;2m";
            }
        }
        return colorCode;
    }
}