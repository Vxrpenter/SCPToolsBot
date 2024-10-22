package dev.vxrp.bot.util.colors;

import dev.vxrp.bot.util.Enums.DCColor;
import org.jetbrains.annotations.NotNull;

public class ColorTool {


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
        }
        return null;
    }
}