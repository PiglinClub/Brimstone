package club.piglin.brimstone.commands

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.utils.Chat
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BalanceCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) {
            Chat.sendMessage(sender, "You can't use this command as you are not a player!")
            return false
        }
        val profile = Brimstone.instance.profileHandler.getProfile(sender.uniqueId)
        if (profile == null) {
            Chat.sendMessage(sender, "&cCouldn't seem to find your profile...")
            return false
        }
        Chat.sendMessage(sender, "&fYour balance is: ${ChatColor.of("#ffd417")}${profile.gold}g")
        return true
    }
}