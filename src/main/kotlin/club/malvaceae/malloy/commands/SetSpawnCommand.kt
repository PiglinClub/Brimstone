package club.malvaceae.malloy.commands

import club.malvaceae.malloy.utils.Chat
import club.malvaceae.malloy.utils.Settings
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetSpawnCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (!sender.hasPermission("piglin.setspawn")) {
            Chat.sendComponent(sender, "<red>You do not have permission to use this command.")
            return false
        }
        if (sender !is Player) {
            sender.sendMessage("You probably shouldn't use this command.")
            return false
        }
        Settings.data.set("spawn.x", sender.location.x)
        Settings.data.set("spawn.y", sender.location.y)
        Settings.data.set("spawn.z", sender.location.z)
        Settings.data.set("spawn.yaw", sender.location.yaw)
        Settings.data.set("spawn.pitch", sender.location.pitch)
        Settings.save()
        Chat.sendComponent(sender, "<green>Succesfully set the spawn to your location.")
        return true
    }
}