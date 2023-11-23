package club.malvaceae.malloy.commands

import club.malvaceae.malloy.commands.menus.ShopGUI
import club.malvaceae.malloy.features.CombatTagListener
import club.malvaceae.malloy.utils.Chat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ShopCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (CombatTagListener.tags[(sender as Player).uniqueId] != null) {
            Chat.sendComponent(sender, "<red>You currently have an active combat tag, you may not use this command.")
            return false
        }
        ShopGUI().openMenu(sender as Player)
        return true
    }
}