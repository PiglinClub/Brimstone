package club.malvaceae.malloy.commands

import club.malvaceae.malloy.utils.Chat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class DiscordCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        Chat.sendComponent(sender, "<reset>Join our discord at: <click:open_url:'https://discord.gg/M3dAEDzach'><color:#5865f2>https://discord.gg/M3dAEDzach</color></click>.")
        return true
    }
}

class TwitterCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        Chat.sendComponent(sender, "<reset>Follow our Twitter at: <click:open_url:'https://twitter.com/piglin_club'><aqua>https://twitter.com/piglin_club</aqua></click>.")
        return true
    }
}

class VoteCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        Chat.sendComponent(sender, "<reset>Vote for our server using <click:open_url:'https://piglin.club/vote'><color:#ff8d0a>https://piglin.club/vote</color></click>!")
        return true
    }
}