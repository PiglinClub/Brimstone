package club.malvaceae.malloy.commands

import club.malvaceae.malloy.utils.Chat
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class MessageHandler {
    companion object {
        val replies = hashMapOf<UUID, UUID>()
    }
}

class MessageCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You can't use this command as you technically aren't a player.")
            return false
        }
        if (args.isEmpty()) {
            Chat.sendComponent(sender, "<red>You need a user to send a message to.")
            return false
        }
        if (args.size < 2) {
            Chat.sendComponent(sender, "<red>You need a message to send to the user.")
            return false
        }
        var message = ""
        for (i in 1 until args.size) message += args[i] + " "
        val target = Bukkit.getPlayer(args[0])
        if (target == null) {
            Chat.sendComponent(sender,"<red>You need a valid user to send this to.")
            return false
        }

        Chat.sendComponent(sender, "<dark_gray>[<color:#3bffba>✉→ ${target.name}</color>] <white>$message</white>")
        Chat.sendComponent(target, "<dark_gray>[<color:#3bffba>→✉ ${sender.name}</color>] <white>$message</white>")

        MessageHandler.replies[target.uniqueId] = sender.uniqueId
        MessageHandler.replies[sender.uniqueId] = target.uniqueId
        target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
        return true
    }
}

class ReplyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You can't use this command as you technically aren't a player.")
            return false
        }
        if (args.isEmpty()) {
            Chat.sendComponent(sender, "<red>You need a user to send a message to.")
            return false
        }
        if (args.size < 1) {
            Chat.sendComponent(sender, "<red>You need a message to send to the user.")
            return false
        }
        var message = ""
        for (i in 0 until args.size) message += args[i] + " "
        val target = Bukkit.getPlayer(MessageHandler.replies[sender.uniqueId]!!)
        if (target == null) {
            Chat.sendComponent(sender,"<red>You need an online user to send this to.")
            return false
        }

        Chat.sendComponent(sender, "<dark_gray>[<color:#3bffba>✉→ ${target.name}</color>] <white>$message</white>")
        Chat.sendComponent(target, "<dark_gray>[<color:#3bffba>→✉ ${sender.name}</color>] <white>$message</white>")

        MessageHandler.replies[target.uniqueId] = sender.uniqueId
        MessageHandler.replies[sender.uniqueId] = target.uniqueId
        target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
        return true
    }
}