package dev.vxrp.util.color
import dev.vxrp.util.enums.DCColor
import dev.vxrp.util.enums.DCColor.*
import java.util.regex.Pattern
import kotlin.math.max

class ColorTool {
    private val filler: String =
        "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E " +
                "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E " +
                "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E " +
                "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E " +
                "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E " +
                "\u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E \u200E " +
                "\u200E \u200E \u200E \u200E \u200E \u200E \u200E"

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


    fun apply(color: DCColor?, text: String): String {
        when (color) {
            DARK_GRAY -> {
                return "$darkGray$text$reset"
            }

            RED -> {
                return "$red$text$reset"
            }

            GREEN -> {
                return "$green$text$reset"
            }

            GOLD -> {
                return "$gold$text$reset"
            }

            LIGHT_BLUE -> {
                return "$lightBlue$text$reset"
            }

            PINK -> {
                return "$pink$text$reset"
            }

            TEAL -> {
                return "$teal$text$reset"
            }

            WHITE -> {
                return "$white$text$reset"
            }

            BOLD -> {
                return "$bold$text$reset"
            }

            UNDERLINE -> {
                return "$underline$text$reset"
            }

            null -> {
                throw RuntimeException()
            }
        }
    }

    fun useCustomColorCodes(text: String): String {
        val matcher = Pattern.compile("(?<=&filler<).+?(?=>&|$)").matcher(text)
        while (matcher.find()) {
            val count = Math.round(matcher.group().toInt().toFloat() / 2)

            return text.replace(
                "&filler<" + matcher.group() + ">&", "\u200E ".repeat(
                    max(0.0, count.toDouble()).toInt()
                )
            )
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
            .replace("&filler&", filler)
    }
}