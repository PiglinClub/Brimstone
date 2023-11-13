package club.malvaceae.malloy.utils

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

enum class DefaultFontInfo(val character: Char, val length: Int) {
    A('A', 5), a('a', 5), B('B', 5), b('b', 5), C('C', 5), c('c', 5), D('D', 5), d('d', 5), E('E', 5), e('e', 5), F(
        'F',
        5
    ),
    f('f', 4), G('G', 5), g('g', 5), H('H', 5), h('h', 5), I('I', 3), i('i', 1), J('J', 5), j('j', 5), K('K', 5), k(
        'k',
        4
    ),
    L('L', 5), l('l', 1), M('M', 5), m('m', 5), N('N', 5), n('n', 5), O('O', 5), o('o', 5), P('P', 5), p('p', 5), Q(
        'Q',
        5
    ),
    q('q', 5), R('R', 5), r('r', 5), S('S', 5), s('s', 5), T('T', 5), t('t', 4), U('U', 5), u('u', 5), V('V', 5), v(
        'v',
        5
    ),
    W('W', 5), w('w', 5), X('X', 5), x('x', 5), Y('Y', 5), y('y', 5), Z('Z', 5), z('z', 5), NUM_1('1', 5), NUM_2(
        '2',
        5
    ),
    NUM_3('3', 5), NUM_4('4', 5), NUM_5('5', 5), NUM_6('6', 5), NUM_7('7', 5), NUM_8('8', 5), NUM_9('9', 5), NUM_0(
        '0',
        5
    ),
    EXCLAMATION_POINT('!', 1), AT_SYMBOL('@', 6), NUM_SIGN('#', 5), DOLLAR_SIGN('$', 5), PERCENT('%', 5), UP_ARROW(
        '^',
        5
    ),
    AMPERSAND('&', 5), ASTERISK('*', 5), LEFT_PARENTHESIS('(', 4), RIGHT_PERENTHESIS(')', 4), MINUS('-', 5), UNDERSCORE(
        '_',
        5
    ),
    PLUS_SIGN('+', 5), EQUALS_SIGN('=', 5), LEFT_CURL_BRACE('{', 4), RIGHT_CURL_BRACE('}', 4), LEFT_BRACKET(
        '[',
        3
    ),
    RIGHT_BRACKET(']', 3), COLON(':', 1), SEMI_COLON(';', 1), DOUBLE_QUOTE('"', 3), SINGLE_QUOTE('\'', 1), LEFT_ARROW(
        '<',
        4
    ),
    RIGHT_ARROW('>', 4), QUESTION_MARK('?', 5), SLASH('/', 5), BACK_SLASH('\\', 5), LINE('|', 1), TILDE('~', 5), TICK(
        '`',
        2
    ),
    PERIOD('.', 1), COMMA(',', 1), SPACE(' ', 3), DEFAULT('a', 4);

    val boldLength: Int
        get() = if (this == SPACE) {
            length
        } else length + 1

    companion object {
        private const val CENTER_PX = 127
        private const val MAX_PX = 240
        private const val CENTER_CHAT_PX = 154
        private const val MAX_CHAT_PX = 250
        fun getDefaultFontInfo(c: Char): DefaultFontInfo {
            for (dFI in values()) {
                if (dFI.character == c) {
                    return dFI
                }
            }
            return DEFAULT
        }
    }
}

class Chat {
    companion object {
        private const val CENTER_PX = 154

        const val dash = "§8»&7"
        const val dot = "§8●§7"
        const val line = "§8§m-----------------------------------------------------"
        const val guiLine = "§8§m-------------------"

        fun colored(message: String): String {
            return (ChatColor.translateAlternateColorCodes('&', message))
        }

        fun broadcast(message: String) {
            Bukkit.broadcastMessage(colored(message))
        }

        fun clear() {
            for (player in Bukkit.getOnlinePlayers()) {
                for (i in 1..1000) {
                    player.sendMessage(" ")
                    player.sendMessage("  ")
                }
            }
        }

        fun scenarioTextWrap(text: String, width: Int): ArrayList<String> {
            val words = text.split(" ")
            val lines = ArrayList<String>()
            var currentLine = ""
            for (word in words) {
                if (currentLine.length + word.length + 1 > width) {
                    lines.add(colored("&7${currentLine}"))
                    currentLine = "&7$word "
                } else {
                    currentLine += "&7$word "
                }
            }
            lines.add(colored("&7${currentLine}"))
            return lines
        }

        /* Function to send centered messages to players */
        fun sendCenteredMessage(player: CommandSender, message: String) {
            var text = message
            if (text == null || text == "") player.sendMessage("")
            text = ChatColor.translateAlternateColorCodes('&', message)
            var messagePxSize = 0
            var previousCode = false
            var isBold = false
            for (c in text.toCharArray()) {
                if (c == '§') {
                    previousCode = true
                    continue
                } else if (previousCode) {
                    previousCode = false
                    if (c == 'l' || c == 'L') {
                        isBold = true
                        continue
                    } else isBold = false
                } else {
                    val dFI: DefaultFontInfo = DefaultFontInfo.getDefaultFontInfo(c)
                    messagePxSize += if (isBold) dFI.boldLength else dFI.length
                    messagePxSize++
                }
            }
            val halvedMessageSize = messagePxSize / 2
            val toCompensate = CENTER_PX - halvedMessageSize
            val spaceLength = DefaultFontInfo.SPACE.length + 1
            var compensated = 0
            val sb = StringBuilder()
            while (compensated < toCompensate) {
                sb.append(" ")
                compensated += spaceLength
            }
            player.sendMessage(sb.toString() + text)
        }

        /* Simple function to send colored messages to players */
        fun sendMessage(player: CommandSender, message: String?) {
            var text = message
            if (text == null || text == "") player.sendMessage("")
            text = message?.let { ChatColor.translateAlternateColorCodes('&', it) }
            if (text != null) {
                player.sendMessage(text)
            }
        }

        fun sendComponent(player: CommandSender, message: String) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(message))
        }
    }
}