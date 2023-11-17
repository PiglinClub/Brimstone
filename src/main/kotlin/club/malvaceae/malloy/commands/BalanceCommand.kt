package club.malvaceae.malloy.commands

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.utils.Chat
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
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
        if (args.isEmpty()) {
            val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(sender.uniqueId)
            if (profile == null) {
                Chat.sendMessage(sender, "&cCouldn't seem to find your profile...")
                return false
            }
            Chat.sendMessage(sender, "&fYour balance is: ${ChatColor.of("#ffd417")}${profile.gold}g")
        } else {
            val profile = Malloy.instance.profileHandler.getProfile(Bukkit.getOfflinePlayer(args[0]).uniqueId)
            if (profile == null) {
                Chat.sendComponent(sender, "<red>Couldn't seem to find ${Bukkit.getOfflinePlayer(args[0]).name}'s profile.")
                return false
            }
            Chat.sendComponent(sender, "<reset>${Bukkit.getOfflinePlayer(args[0]).name}'s balance is <color:#ffd417>${profile.gold}g</color>")
        }
        return true
    }
}