package club.malvaceae.malloy.commands

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.utils.Chat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PayCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            Chat.sendComponent(sender, "<red>You must insert a player to send money to.")
            return false
        }
        if (args.size < 2) {
            Chat.sendComponent(sender, "<red>You must insert an amount of money to send.")
            return false
        }
        val target = Bukkit.getPlayer(args[0])
        if (target == null) {
            Chat.sendComponent(sender, "<red>You must insert a valid player to send money to.")
            return false
        }
        if (args[1].toDoubleOrNull() == null && args[1].toIntOrNull() == null) {
            Chat.sendMessage(sender, "&cYou need a valid number to withdraw.")
            return false
        }
        val profile = Malloy.instance.profileHandler.getProfile((sender as Player).uniqueId)!!
        val amount = args[1].toDouble()
        if (amount > profile.gold) {
            Chat.sendMessage(sender, "&cYou're attempting to pay more than what you actually have.")
            return false
        }
        val targetProfile = Malloy.instance.profileHandler.getProfile(target.uniqueId)!!
        profile.gold -= amount
        targetProfile.gold += amount
        Malloy.instance.profileHandler.saveProfile(profile)
        Malloy.instance.profileHandler.saveProfile(targetProfile)
        Chat.sendComponent(sender, "<green>Successfully sent <color:#ffd417>${amount}g</color> to <yellow>${target.name}</yellow>!")
        Chat.sendComponent(target, "<green>You received <color:#ffd417>${amount}g</color> from <yellow>${sender.name}</yellow>!")
        return true
    }
}