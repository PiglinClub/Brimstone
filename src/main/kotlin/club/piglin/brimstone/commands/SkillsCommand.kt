package club.piglin.brimstone.commands

import club.piglin.brimstone.commands.menus.SkillsGUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SkillsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You can't use this command as you are not a player.")
            return false
        }
        SkillsGUI().openMenu(sender)
        return true
    }

}