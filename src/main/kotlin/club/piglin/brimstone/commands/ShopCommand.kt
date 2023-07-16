package club.piglin.brimstone.commands

import club.piglin.brimstone.commands.menus.ShopGUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ShopCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        ShopGUI().openMenu(sender as Player)
        return true
    }
}