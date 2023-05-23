package club.piglin.brimstone.commands

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.commands.menus.LeaveTownyGUI
import club.piglin.brimstone.database.towns.InviteHandler
import club.piglin.brimstone.database.towns.Town
import club.piglin.brimstone.utils.Chat
import net.md_5.bungee.api.ChatColor
import org.bson.Document
import org.bukkit.Bukkit
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
                        listOf(
                            Document("uniqueId", sender.uniqueId)
                                .append("role", "mayor")
                                .append("joinedAt", System.currentTimeMillis())
                        ),
                        0.0
                    )
                    profile.town = town.uniqueId
                    profile.addExp(100.0)
                    Brimstone.instance.townHandler.updateCache(town)
                    Brimstone.instance.townHandler.saveTown(town)
                    Brimstone.instance.profileHandler.saveProfile(profile)
                    Chat.broadcast("&a${sender.name} created a new town: &e${name}&a!")
                }
                "tax" -> {
                    val profile = Brimstone.instance.profileHandler.getProfile(sender.uniqueId)
                    if (profile == null) {
                        Chat.sendMessage(sender, "&cThis literally isn't supposed to happen, but you don't have a profile?")
                        return false
                    }
                    if (profile.town == null) {
                        Chat.sendMessage(sender, "&cYou currently are not in a Town.")
                        return false
                    }
                    val town = Brimstone.instance.townHandler.getPlayerTown(sender)!!
                    if (args.size < 2) {
                        Chat.sendMessage(sender, "&cInvalid usage: /towny tax <gold>")
                        return false
                    }
                    if (args[2].toDoubleOrNull() !is Double && args[2].toIntOrNull() !is Int) {
                        Chat.sendMessage(sender, "&cYou need a valid number to set your town tax to.")
                        return false
                    }
                    if (town.getMember(profile.uniqueId)!!.role != "mayor") {
                        Chat.sendMessage(sender, "&cYou do not have a high enough town role to use this command.")
                        return false
                    }
                    town.tax = args[2].toDouble()
                    Chat.sendMessage(sender, "&aSuccessfully set your town tax to ${ChatColor.of("#ffd417")}${town.tax}g&a, new residents joining your town will now pay that tax.")
                    Brimstone.instance.townHandler.saveTown(town)
                }
                "invite" -> {
                    val profile = Brimstone.instance.profileHandler.getProfile(sender.uniqueId)
                    if (profile == null) {
                        Chat.sendMessage(sender, "&cThis literally isn't supposed to happen, but you don't have a profile?")
                        return false
                    }
                    if (profile.town == null) {
                        Chat.sendMessage(sender, "&cYou currently are not in a Town.")
                        return false
                    }
                    val town = Brimstone.instance.townHandler.getPlayerTown(sender)!!
                    if (args.size < 2) {
                        Chat.sendMessage(sender, "&cInvalid usage: /towny invite <player>")
                        return false
                    }
                    if (town.getMember(profile.uniqueId)!!.role != "officer" &&
                        town.getMember(profile.uniqueId)!!.role != "mayor") {
                        Chat.sendMessage(sender, "&cYou do not have a high enough town role to use this command.")
                        return false
                    }
                    val target = Bukkit.getPlayer(args[2])
                    if (target == null) {
                        Chat.sendMessage(sender, "&cInvalid target, the target must be an online player.")
                        return false
                    }
                    val targetTown = Brimstone.instance.townHandler.getPlayerTown(target)
                    if (targetTown != null) {
                        Chat.sendMessage(sender, "&cThis player is already in a town.")
                        return false
                    }
                    Chat.sendMessage(sender, "&aOkay, sending an invite to &e${target.name}&a now...")
                    InviteHandler.createInvite(town, sender, target)
                    Chat.sendMessage(sender, "&aSuccessfully sent an invite! They have &e10 minutes&a to accept.")
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
                    LeaveTownyGUI().openMenu(sender)
                }

            }
        }
        return true
    }
}