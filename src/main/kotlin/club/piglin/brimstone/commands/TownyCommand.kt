package club.piglin.brimstone.commands

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.database.towns.Town
import club.piglin.brimstone.utils.Chat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class TownyCommand : CommandExecutor {
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

        } else {
            when (args[0]) {
                "create" -> {
                    if (args.size < 2) {
                        Chat.sendMessage(sender, "&cInvalid usage: /towny create <name>")
                        return false
                    }
                    val profile = Brimstone.instance.profileHandler.getProfile(sender.uniqueId)
                    if (profile == null) {
                        Chat.sendMessage(sender, "&cThis literally isn't supposed to happen, but you don't have a profile?")
                        return false
                    }
                    if (profile.town != null) {
                        Chat.sendMessage(sender, "&cYou already are in a Town. Leave your current town using /towny leave.")
                        return false
                    }
                    var name = ""
                    for (i in 1 until args.size) {
                        if (i == (args.size - 1)) name += args[i]
                        else name += args[i] + " "
                    }
                    if (name.length > 32) {
                        Chat.sendMessage(sender, "&cYour Town's name must be less than 32 characters.")
                        return false
                    }
                    if (name.length < 2) {
                        Chat.sendMessage(sender, "&cYour Town's name must be more than 2 characters.")
                        return false
                    }
                    if (profile.gold < 1000.0) {
                        Chat.sendMessage(sender, "&cYou don't have enough gold to purchase a town. (Short ${1000.0 - profile.gold}g)")
                        return false
                    }
                    profile.gold -= 1000.0
                    val town = Town(
                        UUID.randomUUID(),
                        sender.uniqueId,
                        name,
                        listOf(sender.uniqueId),
                        0.0
                    )
                    profile.town = town.uniqueId
                    profile.addExp(100.0)
                    Brimstone.instance.townHandler.updateCache(town)
                    Brimstone.instance.townHandler.saveTown(town)
                    Chat.broadcast("&a${sender.name} created a new town: &e${name}&a!")
                }
                "leave" -> {
                    val profile = Brimstone.instance.profileHandler.getProfile(sender.uniqueId)
                    if (profile == null) {
                        Chat.sendMessage(sender, "&cThis literally isn't supposed to happen, but you don't have a profile?")
                        return false
                    }
                    if (profile.town == null) {
                        Chat.sendMessage(sender, "&cYou currently are not in a Town.")
                        return false
                    }

                }
            }
        }
        return true
    }
}