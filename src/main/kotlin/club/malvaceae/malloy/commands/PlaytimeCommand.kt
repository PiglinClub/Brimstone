package club.malvaceae.malloy.commands

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.utils.Chat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.math.floor
import kotlin.math.round

class PlaytimeCommand : CommandExecutor {

    private fun timeToString(ticks: Long): String {
        var t = ticks
        val hours = floor(t / 3600.toDouble()).toInt()
        t -= hours * 3600
        val minutes = floor(t / 60.toDouble()).toInt()
        t -= minutes * 60
        val seconds = t.toInt()
        val output = StringBuilder()
        if (hours > 0) {
            output.append(hours).append('h')
            if (minutes == 0) {
                output.append(minutes).append('m')
            }
        }
        if (minutes > 0) {
            output.append(minutes).append('m')
        }
        output.append(seconds).append('s')
        return output.toString()
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You can't use this command as you are not a player.")
            return false
        }
        val profile = Malloy.instance.profileHandler.getProfile(sender.uniqueId)
        val time = timeToString(round(profile!!.playtime.toDouble() / 1000).toLong())
        Chat.sendComponent(sender, "<green>You've been playing for <yellow>${time}</yellow>.</green>")
        return true
    }
}