package club.malvaceae.malloy.commands

import club.malvaceae.malloy.utils.Chat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class DiscordCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        Chat.sendComponent(sender, "<reset>Join our discord at: <color:#5865f2>https://discord.gg/M3dAEDzach</color>.")
        return true
    }
}

class TwitterCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        Chat.sendComponent(sender, "<reset>Follow our Twitter at: <aqua>https://twitter.com/piglin_club</aqua>.")
        return true
    }
}