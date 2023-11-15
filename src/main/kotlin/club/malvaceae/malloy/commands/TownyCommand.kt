package club.malvaceae.malloy.commands

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.commands.menus.LeaveTownyGUI
import club.malvaceae.malloy.commands.menus.TownyMembersGUI
import club.malvaceae.malloy.database.towns.InviteHandler
import club.malvaceae.malloy.database.towns.Town
import club.malvaceae.malloy.utils.Chat
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import me.lucko.helper.Schedulers
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
                    val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(sender.uniqueId)
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
                    if (club.malvaceae.malloy.Malloy.instance.townHandler.checkIfAvailable(name).get() == false) {
                        Chat.sendMessage(sender, "&cThis name is already taken.")
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
                    club.malvaceae.malloy.Malloy.instance.townHandler.updateCache(town)
                    club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(town)
                    club.malvaceae.malloy.Malloy.instance.profileHandler.saveProfile(profile)
                    Chat.broadcast("&a${sender.name} created a new town: &e${name}&a!")
                }
                "joinTax" -> {
                    val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(sender.uniqueId)
                    if (profile == null) {
                        Chat.sendMessage(sender, "&cThis literally isn't supposed to happen, but you don't have a profile?")
                        return false
                    }
                    if (profile.town == null) {
                        Chat.sendMessage(sender, "&cYou currently are not in a Town.")
                        return false
                    }
                    val town = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(sender)!!
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
                    club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(town)
                }
                "rename" -> {
                    val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(sender.uniqueId)
                    if (profile == null) {
                        Chat.sendMessage(sender, "&cThis literally isn't supposed to happen, but you don't have a profile?")
                        return false
                    }
                    if (profile.town == null) {
                        Chat.sendMessage(sender, "&cYou currently are not in a Town.")
                        return false
                    }
                    val town = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(sender)!!
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
                    if (club.malvaceae.malloy.Malloy.instance.townHandler.checkIfAvailable(name).get() == false) {
                        Chat.sendMessage(sender, "&cThis name is already taken.")
                        return false
                    }
                    town.name = name
                    club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(town)
                    town.sendMessage("&e${sender.name}&a renamed the town to &e${town.name}&a!")
                }
                "invite" -> {
                    val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(sender.uniqueId)
                    if (profile == null) {
                        Chat.sendMessage(sender, "&cThis literally isn't supposed to happen, but you don't have a profile?")
                        return false
                    }
                    if (profile.town == null) {
                        Chat.sendMessage(sender, "&cYou currently are not in a Town.")
                        return false
                    }
                    val town = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(sender)!!
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
                    val targetTown = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(target)
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
                "hijack" -> {
                    val profile = Malloy.instance.profileHandler.getProfile(sender.uniqueId)
                    if (!sender.hasPermission("piglin.hijack")) {
                        Chat.sendComponent(sender, "<red>You don't have permission to use this command.")
                        return false
                    }
                    if (args.size < 2) {
                        Chat.sendComponent(sender, "<red>You need a town to hijack.")
                        return false
                    }
                    val name = args[1]
                    Chat.sendComponent(sender, "<yellow>Okay... getting town now...")
                    var town: Document? = null
                    Schedulers.async().run {
                        try {
                            with (Malloy.instance.dataSource.getDatabase("malloy").getCollection("towns")) {
                                val filter = Filters.eq("name", name)
                                val documents = this.find(filter).toList()
                                if (documents.size == 0) {
                                    Chat.sendComponent(sender, "<red>Couldn't find this town. Names must be case-sensitive.")
                                } else {
                                    town = documents.first()
                                }
                                return@run
                            }
                        } catch (e: MongoException) {
                            e.printStackTrace()
                            Chat.sendComponent(sender, "<red>An error occurred while attempting to find the town.")
                            return@run
                        }
                    }
                    if (town == null) {
                        Chat.sendComponent(sender, "<red>Well, something clearly wrong happened here.")
                        return false
                    }
                    Malloy.instance.townHandler.getTown(town!!["uuid"] as UUID)!!.addPlayer(sender, true)
                }
                "accept" -> {
                    val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(sender.uniqueId)
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
                            club.malvaceae.malloy.Malloy.instance.townHandler.getTown(task.from.uniqueId)!!.gold += task.from.tax
                            club.malvaceae.malloy.Malloy.instance.profileHandler.saveProfile(profile)
                            club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(task.from)
                            club.malvaceae.malloy.Malloy.instance.townHandler.getTown(task.from.uniqueId)!!.addPlayer(sender)
                            InviteHandler.removeInvite(task)
                            return true
                        }
                    }
                    Chat.sendMessage(sender, "&cCould not find a town invite with that ID.")
                }
                "deny" -> {
                    val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(sender.uniqueId)
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
                    val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(sender.uniqueId)
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
                    val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(sender.uniqueId)
                    if (profile == null) {
                        Chat.sendMessage(sender, "&cThis literally isn't supposed to happen, but you don't have a profile?")
                        return false
                    }
                    if (profile.town == null) {
                        Chat.sendMessage(sender, "&cYou currently are not in a Town.")
                        return false
                    }
                    val town = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(sender)!!
                    TownyMembersGUI().openMenu(sender)
                }
                "withdraw" -> {
                    val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(sender.uniqueId)
                    if (profile == null) {
                        Chat.sendMessage(sender, "&cThis literally isn't supposed to happen, but you don't have a profile?")
                        return false
                    }
                    if (profile.town == null) {
                        Chat.sendMessage(sender, "&cYou currently are not in a Town.")
                        return false
                    }
                    val town = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(sender)!!
                    if (args.size < 2) {
                        Chat.sendMessage(sender, "&cInvalid usage: /towny withdraw <gold|all>")
                        return false
                    }
                    if (town.getMember(profile.uniqueId)!!.role != "mayor") {
                        Chat.sendMessage(sender, "&cYou do not have a high enough town role to use this command.")
                        return false
                    }
                    if (args[1] == "all") {
                        val amount = town.gold
                        town.gold -= amount
                        profile.gold += amount
                        town.sendMessage("&e${sender.name}&a withdrew ${ChatColor.of("#ffd417")}${amount}g&a from the town treasury.")
                        club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(town)
                        club.malvaceae.malloy.Malloy.instance.profileHandler.saveProfile(profile)
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
                        club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(town)
                        club.malvaceae.malloy.Malloy.instance.profileHandler.saveProfile(profile)
                    }
                }
                "deposit" -> {
                    val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(sender.uniqueId)
                    if (profile == null) {
                        Chat.sendMessage(sender, "&cThis literally isn't supposed to happen, but you don't have a profile?")
                        return false
                    }
                    if (profile.town == null) {
                        Chat.sendMessage(sender, "&cYou currently are not in a Town.")
                        return false
                    }
                    val town = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(sender)!!
                    if (args.size < 2) {
                        Chat.sendMessage(sender, "&cInvalid usage: /towny deposit <gold|all>")
                        return false
                    }
                    if (args[1] == "all") {
                        val amount = profile.gold
                        town.gold += amount
                        profile.gold -= amount
                        val member = town.getMember(sender.uniqueId)!!
                        member.goldDeposited += amount
                        town.saveMember(member)
                        town.sendMessage("&e${sender.name}&a deposited ${ChatColor.of("#ffd417")}${amount}g&a into the town treasury.")
                        club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(town)
                        club.malvaceae.malloy.Malloy.instance.profileHandler.saveProfile(profile)
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
                        val member = town.getMember(sender.uniqueId)!!
                        member.goldDeposited += amount
                        town.saveMember(member)
                        town.sendMessage("&e${sender.name}&a deposited ${ChatColor.of("#ffd417")}${amount}g&a into the town treasury.")
                        club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(town)
                        club.malvaceae.malloy.Malloy.instance.profileHandler.saveProfile(profile)
                    }
                }
            }
        }
        return true
    }
}