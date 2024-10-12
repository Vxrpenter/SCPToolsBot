package dev.vxrp.bot.util.colors;

import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.util.Enmus.DCColor;
import dev.vxrp.bot.util.Enmus.SLColors;

import java.util.*;

public class ColorTool {


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

    private static DCColor translator(SLColors colors) {
        List objects = ScpTools.getColorTranslationManager().getList("Scp_Colors_List");
        Map<String, String> objecting = new HashMap<>();

        for (Object object : objects) {
            String object2 = object.toString().replace("{", "").replace("}", "");

            objecting.put(object2.split("=")[0], object2.split("=")[1]);
        }

        return DCColor.valueOf(objecting.get(colors.toString()));
    }
}