package pine.toast.library.utilities

enum class WonderlandColors(val code: String) {

    BLACK("§0"),
    DARK_BLUE("§1"),
    DARK_GREEN("§2"),
    DARK_AQUA("§3"),
    DARK_RED("§4"),
    DARK_PURPLE("§5"),
    GOLD("§6"),
    GRAY("§7"),
    DARK_GRAY("§8"),
    BLUE("§9"),
    GREEN("§a"),
    AQUA("§b"),
    RED("§c"),
    LIGHT_PURPLE("§d"),
    YELLOW("§e"),
    WHITE("§f"),

    RESET("§r"),
    BOLD("§l"),
    ITALIC("§o"),
    UNDERLINE("§n"),
    STRIKETHROUGH("§m");


    companion object {
        @JvmStatic
        fun getByCode(code: String): WonderlandColors? {
            for (color in entries) {
                if (color.code == "§$code") {
                    return color
                }
            }
            return null
        }

        @JvmStatic
        fun getColorByScore(score: Int): WonderlandColors {
            return when (score) {
                0 -> BLACK
                1 -> DARK_BLUE
                2 -> DARK_GREEN
                3 -> DARK_AQUA
                4 -> DARK_RED
                5 -> DARK_PURPLE
                6 -> GOLD
                7 -> GRAY
                8 -> DARK_GRAY
                9 -> BLUE
                10 -> GREEN
                11 -> AQUA
                12 -> RED
                13 -> LIGHT_PURPLE
                14 -> YELLOW
                15 -> WHITE
                else -> WHITE // default to white if score is out of range
            }
        }

        @JvmStatic
        fun isFormat(color: WonderlandColors): Boolean {
            return when (color) {
                BOLD, ITALIC, UNDERLINE, STRIKETHROUGH -> true
                else -> false
            }
        }

        @JvmStatic
        fun getLastColors(input: String): String {
            var lastColor = ""
            for (i in input.length - 1 downTo 0) {
                if (input[i] == '§' && i < input.length - 1) {
                    val color = getByCode(input[i + 1].toString())
                    if (color != null && !isFormat(color)) {
                        lastColor = color.code
                        break
                    }
                }
            }
            return lastColor
        }
    }




    operator fun plus(s: String): String {
        return "$code$s".replace("&", "§")
    }


}