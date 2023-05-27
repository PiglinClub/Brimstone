package club.piglin.brimstone.commands

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.commands.menus.LeaveTownyGUI
import club.piglin.brimstone.commands.menus.TownyMembersGUI
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
            when (args[0].lowercase()) {
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
                                .append("goldDeposited", 0.0)
                        ),
                        0.0,
                        0.0,
                        0.0
                    )
                    profile.town = town.uniqueId
                    profile.addExp(100.0)
                    Brimstone.instance.townHandler.updateCache(town)
                    Brimstone.instance.townHandler.saveTown(town)
                    Brimstone.instance.profileHandler.saveProfile(profile)
                    Chat.broadcast("&a${sender.name} created a new town: &e${name}&a!")
                }
                "joinTax" -> {
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
                        Chat.sendMessage(sender, "&cInvalid usage: /towny joinTax <gold>")
                        return false
                    }
                    if (args[1].toDoubleOrNull() == null && args[1].toIntOrNull() == null) {
                        Chat.sendMessage(sender, "&cYou need a valid number to set your join tax to.")
                        return false
                    }
                    if (town.getMember(profile.uniqueId)!!.role != "mayor") {
                        Chat.sendMessage(sender, "&cYou do not have a high enough town role to use this command.")
                        return false
                    }
                    town.tax = args[1].toDouble()
                    town.sendMessage("&e${sender.name}&a set the join tax to ${ChatColor.of("#ffd417")}${town.tax}g&a.")
                    Brimstone.instance.townHandler.saveTown(town)
                }
                "rename" -> {
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
                        Chat.sendMessage(sender, "&cInvalid usage: /towny rename <name>")
                        return false
                    }
                    if (town.getMember(profile.uniqueId)!!.role != "mayor") {
                        Chat.sendMessage(sender, "&cYou do not have a high enough town role to use this command.")
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
                    town.name = name
                    Brimstone.instance.townHandler.saveTown(town)
                    town.sendMessage("&e${sender.name}&a renamed the town to &e${town.name}&a!")
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
                    val target = Bukkit.getPlayer(args[1])
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
                    if (InviteHandler.tasks[target.uniqueId] != null) {
                        for (task in InviteHandler.tasks[target.uniqueId]!!) {
                            if (task.from.uniqueId == town.uniqueId) {
                                Chat.sendMessage(sender, "&cYou already have an outgoing invite sent to this player.")
                                return false
                            }
                        }
                    }
                    town.sendMessage("&e${sender.name}&a invited &e${target.name}&a to join the town!")
                    InviteHandler.createInvite(town, sender, target)
                    Chat.sendMessage(sender, "&aSuccessfully sent an invite! They have &e10 minutes&a to accept.")
                }
                "accept" -> {
                    val profile = Brimstone.instance.profileHandler.getProfile(sender.uniqueId)
                    if (profile == null) {
                        Chat.sendMessage(sender, "&cThis literally isn't supposed to happen, but you don't have a profile?")
                        return false
                    }
                    if (profile.town != null) {
                        Chat.sendMessage(sender, "&cYou can't accept invites while in a Town.")
                        return false
                    }
                    if (args.size < 2) {
                        Chat.sendMessage(sender, "&cInvalid usage: /towny accept <town_id>")
                        return false
                    }
                    if (InviteHandler.tasks[sender.uniqueId] == null) {
                        Chat.sendMessage(sender, "&cYou currently have no incoming invites.")
                        return false
                    }
                    for (task in InviteHandler.tasks[sender.uniqueId]!!) {
                        if (task.from.uniqueId == UUID.fromString(args[1])) {
                            Chat.sendMessage(sender, "&aOkay, joining &e${task.from.name}&a now!")
                            if (profile.gold < task.from.tax) {
                                Chat.sendMessage(sender, "&cYou can't join this town yet as you cannot pay the town join tax.")
                                return false
                            }
                            profile.gold -= task.from.tax
                            Brimstone.instance.townHandler.getTown(task.from.uniqueId)!!.gold += task.from.tax
                            Brimstone.instance.profileHandler.saveProfile(profile)
                            Brimstone.instance.townHandler.saveTown(task.from)
                            Brimstone.instance.townHandler.getTown(task.from.uniqueId)!!.addPlayer(sender)
                            InviteHandler.removeInvite(task)
                            return true
                        }
                    }
                    Chat.sendMessage(sender, "&cCould not find a town invite with that ID.")
                }
                "deny" -> {
                    val profile = Brimstone.instance.profileHandler.getProfile(sender.uniqueId)
                    if (profile == null) {
                        Chat.sendMessage(sender, "&cThis literally isn't supposed to happen, but you don't have a profile?")
                        return false
                    }
                    if (args.size < 2) {
                        Chat.sendMessage(sender, "&cInvalid usage: /towny deny <town_id>")
                        return false
                    }
                    if (InviteHandler.tasks[sender.uniqueId] == null) {
                        Chat.sendMessage(sender, "&cYou currently have no incoming invites.")
                        return false
                    }
                    for (task in InviteHandler.tasks[sender.uniqueId]!!) {
                        if (task.from.uniqueId == UUID.fromString(args[1])) {
                            InviteHandler.removeInvite(task)
                            task.from.sendMessage("&e${sender.name}&a has declined your invite.")
                            Chat.sendMessage(sender, "&aSuccessfully denied &e${task.from.name}&a's invite.")
                            return true
                        }
                    }
                    Chat.sendMessage(sender, "&cCould not find a town invite with that ID.")
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
                "members" -> {
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
                    TownyMembersGUI().openMenu(sender)
                }
                "withdraw" -> {
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
                        Chat.sendMessage(sender, "&cInvalid usage: /towny withdraw <gold|all>")
                        return false
                    }
                    if (args[1] == "all") {
                        val amount = town.gold
                        town.gold -= amount
                        profile.gold += amount
                        town.sendMessage("&e${sender.name}&a withdrew ${ChatColor.of("#ffd417")}${amount}g&a from the town treasury.")
                        Brimstone.instance.townHandler.saveTown(town)
                        Brimstone.instance.profileHandler.saveProfile(profile)
                    } else {
                        if (args[1].toDoubleOrNull() == null && args[1].toIntOrNull() == null) {
                            Chat.sendMessage(sender, "&cYou need a valid number to withdraw.")
                            return false
                        }
                        val amount = args[1].toDouble()
                        if (amount > town.gold) {
                            Chat.sendMessage(sender, "&cYou're attempting to withdraw more than what your town actually has.")
                            return false
                        }
                        town.gold -= amount
                        profile.gold += amount
                        town.sendMessage("&e${sender.name}&a withdrew ${ChatColor.of("#ffd417")}${amount}g&a from the town treasury.")
                        Brimstone.instance.townHandler.saveTown(town)
                        Brimstone.instance.profileHandler.saveProfile(profile)
                    }
                }
                "deposit" -> {
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
                        Chat.sendMessage(sender, "&cInvalid usage: /towny deposit <gold|all>")
                        return false
                    }
                    if (args[1] == "all") {
                        val amount = profile.gold
                        town.gold += amount
                        profile.gold -= amount
                        town.sendMessage("&e${sender.name}&a deposited ${ChatColor.of("#ffd417")}${amount}g&a into the town treasury.")
                        Brimstone.instance.townHandler.saveTown(town)
                        Brimstone.instance.profileHandler.saveProfile(profile)
                    } else {
                        if (args[1].toDoubleOrNull() == null && args[1].toIntOrNull() == null) {
                            Chat.sendMessage(sender, "&cYou need a valid number to withdraw.")
                            return false
                        }
                        val amount = args[1].toDouble()
                        if (amount > profile.gold) {
                            Chat.sendMessage(sender, "&cYou're attempting to deposit more than what your profile actually has.")
                            return false
                        }
                        town.gold += amount
                        profile.gold -= amount
                        town.sendMessage("&e${sender.name}&a deposited ${ChatColor.of("#ffd417")}${amount}g&a into the town treasury.")
                        Brimstone.instance.townHandler.saveTown(town)
                        Brimstone.instance.profileHandler.saveProfile(profile)
                    }
                }
            }
        }
        return true
    }
}