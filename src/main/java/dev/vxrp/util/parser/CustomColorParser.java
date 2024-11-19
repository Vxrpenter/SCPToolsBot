package dev.vxrp.util.parser;

import dev.vxrp.util.Enums.DCColor_DEPRECATED;
import dev.vxrp.util.Enums.SLColors;
import dev.vxrp.util.colors.ColorTool;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomColorParser {
    // This parser is still a work in progress and also probably inefficient

    public static String parse(String input) {
        Map<String, String> tokens = new HashMap<>();

        List<Pattern> patternList = Arrays.asList(PINK_COLOR_TAG_PATTERN, RED_COLOR_TAG_PATTERN, BROWN_COLOR_TAG_PATTERN, SILVER_COLOR_TAG_PATTERN, LIGHT_GREEN_COLOR_TAG_PATTERN,
                CRIMSON_COLOR_TAG_PATTERN, CYAN_COLOR_TAG_PATTERN, AQUA_COLOR_TAG_PATTERN, DEEP_PINK_COLOR_TAG_PATTERN, TOMATO_COLOR_TAG_PATTERN, YELLOW_COLOR_TAG_PATTERN,
                MAGENTA_COLOR_TAG_PATTERN, BLUE_GREEN_COLOR_TAG_PATTERN, ORANGE_COLOR_TAG_PATTERN, LIME_COLOR_TAG_PATTERN, GREEN_COLOR_TAG_PATTERN, EMERALD_COLOR_TAG_PATTERN,
                CARMINE_COLOR_TAG_PATTERN, NICKEL_COLOR_TAG_PATTERN, MINT_COLOR_TAG_PATTERN, ARMY_GREEN_COLOR_TAG_PATTERN, PUMPKIN_COLOR_TAG_PATTERN);

        LinkedList<String> matcherList = new LinkedList<>();
        for (Pattern pattern : patternList) {
            Matcher matcher = pattern.matcher(input);
            //Loop through
            while (matcher.find()) {
                matcherList.add(matcher.group());

                String colorsTags = pattern.toString().replace("(?<=", "").replace(").+?(?=", matcher.group()).replace("|$)", "");
                tokens.put(matcher.group(), colorsTags);
            }
        }

        for (String obj : matcherList) {
            String finalValue = ColorTool.apply(translator(parseColorCodeToColor(tokens.get(obj))), obj);

            input = input.replace(tokens.get(obj), finalValue);
        }

        Map<String, String> linkList = new HashMap<>();
        List<String> linkTextsList = new ArrayList<>();
        Matcher matcher = LINK_TAG_PATTERN.matcher(input);
        while (matcher.find()) {
            Matcher linkTextMatcher = Pattern.compile("(?<=<link="+matcher.group()+">).+?(?=</link>|$)").matcher(input);
            while (linkTextMatcher.find()) {
                linkTextsList.add(linkTextMatcher.group());
                linkList.put(linkTextMatcher.group(), matcher.group());
            }
        }

        for (String obj : linkTextsList) {
            String link = linkList.get(obj);
            input = input.replace("<link="+link+">"+obj+"</link>", obj+"("+link+")");
        }

        input = input.replaceAll("</align>", "");
        input = input.replaceAll("<align=center>", "ㅤ");
        input = input.replaceAll("<size=.*?>", "");
        input = input.replaceAll("</size>", "");
        input = input.replaceAll("(?m)^[ \t]*\r?\n", "");
        input = input.replaceAll("ㅤ", "");
        input = "ㅤ"+input;
        return input.trim();
    }

    private static DCColor_DEPRECATED translator(SLColors colors) {
        List objects = Collections.emptyList();
        Map<String, String> objecting = new HashMap<>();

        for (Object object : objects) {
            String object2 = object.toString().replace("{", "").replace("}", "");

            objecting.put(object2.split("=")[0], object2.split("=")[1]);
        }

        return DCColor_DEPRECATED.valueOf(objecting.get(colors.toString()));
    }

    private static SLColors parseColorCodeToColor(String colorCode) {
        if (colorCode.contains("pink")) {
            return SLColors.PINK;
        }
        if (colorCode.contains("red")) {
            return SLColors.RED;
        }
        if (colorCode.contains("brown")) {
            return SLColors.BROWN;
        }
        if (colorCode.contains("silver")) {
            return SLColors.SILVER;
        }
        if (colorCode.contains("light_green")) {
            return SLColors.LIGHT_GREEN;
        }
        if (colorCode.contains("crimson")) {
            return SLColors.CRIMSON;
        }
        if (colorCode.contains("cyan")) {
            return SLColors.CYAN;
        }
        if (colorCode.contains("aqua")) {
            return SLColors.AQUA;
        }
        if (colorCode.contains("deep_pink")) {
            return SLColors.DEEP_PINK;
        }
        if (colorCode.contains("tomato")) {
            return SLColors.TOMATO;
        }
        if (colorCode.contains("yellow")) {
            return SLColors.YELLOW;
        }
        if (colorCode.contains("magenta")) {
            return SLColors.MAGENTA;
        }
        if (colorCode.contains("blue_green")) {
            return SLColors.BLUE_GREEN;
        }
        if (colorCode.contains("orange")) {
            return SLColors.ORANGE;
        }
        if (colorCode.contains("lime")) {
            return SLColors.LIME;
        }
        if (colorCode.contains("green")) {
            return SLColors.GREEN;
        }
        if (colorCode.contains("emerald")) {
            return SLColors.EMERALD;
        }
        if (colorCode.contains("carmine")) {
            return SLColors.CARMINE;
        }
        if (colorCode.contains("nickel")) {
            return SLColors.NICKEL;
        }
        if (colorCode.contains("mint")) {
            return SLColors.MINT;
        }
        if (colorCode.contains("army_green")) {
            return SLColors.ARMY_GREEN;
        }
        if (colorCode.contains("pumpkin")) {
            return SLColors.PUMPKIN;
        }
        return null;
    }

    private static final Pattern LINK_TAG_PATTERN = Pattern.compile("(?<=<link=).+?(?=>|$)");
    private static final Pattern PINK_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=pink>).+?(?=</color>|$)");
    private static final Pattern RED_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=red>).+?(?=</color>|$)");
    private static final Pattern BROWN_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=brown>).+?(?=</color>|$)");
    private static final Pattern SILVER_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=silver>).+?(?=</color>|$)");
    private static final Pattern LIGHT_GREEN_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=light_green>).+?(?=</color>|$)");
    private static final Pattern CRIMSON_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=crimson>).+?(?=</color>|$)");
    private static final Pattern CYAN_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=cyan>).+?(?=</color>|$)");
    private static final Pattern AQUA_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=aqua>).+?(?=</color>|$)");
    private static final Pattern DEEP_PINK_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=deep_pink>).+?(?=</color>|$)");
    private static final Pattern TOMATO_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=tomato>).+?(?=</color>|$)");
    private static final Pattern YELLOW_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=yellow>).+?(?=</color>|$)");
    private static final Pattern MAGENTA_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=magenta>).+?(?=</color>|$)");
    private static final Pattern BLUE_GREEN_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=blue_green>).+?(?=</color>|$)");
    private static final Pattern ORANGE_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=orange>).+?(?=</color>|$)");
    private static final Pattern LIME_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=lime>).+?(?=</color>|$)");
    private static final Pattern GREEN_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=green>).+?(?=</color>|$)");
    private static final Pattern EMERALD_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=emerald>).+?(?=</color>|$)");
    private static final Pattern CARMINE_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=carmine>).+?(?=</color>|$)");
    private static final Pattern NICKEL_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=nickel>).+?(?=</color>|$)");
    private static final Pattern MINT_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=mint>).+?(?=</color>|$)");
    private static final Pattern ARMY_GREEN_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=army_green>).+?(?=</color>|$)");
    private static final Pattern PUMPKIN_COLOR_TAG_PATTERN = Pattern.compile("(?<=<color=pumpkin>).+?(?=</color>|$)");
}
