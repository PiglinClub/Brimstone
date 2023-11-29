package club.malvaceae.malloy.commands

import club.malvaceae.malloy.features.TeleportFeature
import club.malvaceae.malloy.utils.Chat
import club.malvaceae.malloy.utils.Settings
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpawnCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (Settings.data.getDouble("spawn.x") == null) {
            Chat.sendComponent(sender, "<red>The spawn is currently not set.")
            return false
        }
        if (sender !is Player) {
            sender.sendMessage("You can't use this command.")
            return false
        }
        val location = Location(Bukkit.getWorld("world"), Settings.data.getDouble("spawn.x"), Settings.data.getDouble("spawn.y"), Settings.data.getDouble("spawn.z"), Settings.data.getDouble("spawn.yaw").toFloat(), Settings.data.getDouble("spawn.pitch").toFloat())
        TeleportFeature.startTeleport(sender as Player, location, false)
        return false
    }
}