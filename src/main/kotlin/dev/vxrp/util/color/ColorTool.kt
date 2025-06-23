/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 *  may obtain the license at
 *
 *  https://mit-license.org/
 *
 *  This software may be used commercially if the usage is license compliant. The software
 *  is provided without any sort of WARRANTY, and the authors cannot be held liable for
 *  any form of claim, damages or other liabilities.
 *
 *  Note: This is no legal advice, please read the license conditions
 */

package dev.vxrp.util.color

import dev.vxrp.util.color.enums.DCColor
import dev.vxrp.util.color.enums.DCColor.*
import java.util.regex.Pattern
import kotlin.math.max
import kotlin.math.roundToInt

class ColorTool {
    private val singleFiller: String = "\u200E "

    private val darkGray: String = "\u001B[2;30m"
    private val red: String = "\u001B[31m"
    private val green: String = "\u001B[32m"
    private val gold: String = "\u001B[33m"
    private val lightBlue: String = "\u001B[34m"
    private val pink: String = "\u001B[35m"
    private val teal: String = "\u001B[36m"
    private val white: String = "\u001B[37m"
    private val bold: String = "\u001B[1;2m"
    private val underline: String = "\u001B[4;2m"
    private val reset: String = "\u001B[0m"

    fun apply(color: DCColor, text: String): String {
        return when (color) {
            DARK_GRAY -> "$darkGray$text$reset"
            RED -> "$red$text$reset"
            GREEN -> "$green$text$reset"
            GOLD -> "$gold$text$reset"
            LIGHT_BLUE -> "$lightBlue$text$reset"
            PINK -> "$pink$text$reset"
            TEAL -> "$teal$text$reset"
            WHITE -> "$white$text$reset"
            BOLD -> "$bold$text$reset"
            UNDERLINE -> "$underline$text$reset"
        }
    }

    fun parse(text: String): String {
        val matcher = Pattern.compile("(?<=&filler<).+?(?=>&|$)").matcher(text)
        while (matcher.find()) {
            val count = (matcher.group().toInt().toFloat() / 2).roundToInt()
            return text.replace("&filler<" + matcher.group() + ">&", singleFiller.repeat(max(0.0, count.toDouble()).toInt()))
        }

        return text
            .replace("&dark_gray&", darkGray)
            .replace("&red&", red)
            .replace("&green&", green)
            .replace("&gold&", gold)
            .replace("&light_blue&", lightBlue)
            .replace("&pink&", pink)
            .replace("&teal&", teal)
            .replace("&white&", white)
            .replace("&bold&", bold)
            .replace("&reset&", reset)
            .replace("&underline&", underline)
            .replace("&filler&", singleFiller.repeat(max(0.0, 144.toDouble()).toInt()))
            .replace("&singleFiller&", singleFiller)
    }
}