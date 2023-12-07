package club.malvaceae.malloy.commands

import club.malvaceae.malloy.features.TeleportFeature
import club.malvaceae.malloy.utils.Chat
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class BackCommand : CommandExecutor {
    companion object {
        val locations = HashMap<UUID, Location>()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command.")
            return false
        }
        if (locations[sender.uniqueId] == null) {
            Chat.sendComponent(sender, "<red>You do not have a previously saved location, you must use a teleportation command to have your location saved.")
            return false
        }
        TeleportFeature.startTeleport(sender, BackCommand.locations[sender.uniqueId]!!)
        return true
    }
}