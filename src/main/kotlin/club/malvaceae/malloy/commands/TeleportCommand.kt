package club.malvaceae.malloy.commands

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.database.towns.InviteStatus
import club.malvaceae.malloy.features.CombatTagListener
import club.malvaceae.malloy.utils.Chat
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class TpaTask(
    val from: Player,
    val to: Player,
) : BukkitRunnable() {
    var timer = 60
    val status = InviteStatus.NOTHING_YET

    override fun run() {
        timer -= 1
        if (status == InviteStatus.ACCEPTED || status == InviteStatus.DENIED) {
            TpaHandler.removeInvite(this)
            cancel()
            club.malvaceae.malloy.Malloy.log.info("[Teleports] Request removed because accepted/denied for ${from.name} to ${to.name}.")
        }
        if (CombatTagListener.tags[from.uniqueId] != null || CombatTagListener.tags[to.uniqueId] != null) {
            TpaHandler.removeInvite(this)
            cancel()
            Malloy.log.info("[Teleports] Request removed because one entered combat tag for ${from.name} to ${to.name}.")
        }
        if (timer == 0) {
            TpaHandler.removeInvite(this)
            cancel()
            club.malvaceae.malloy.Malloy.log.info("[Teleports] Request timed out for ${from.name} to ${to.name}.")
        }
    }

}

class TpaHandler {
    companion object {
        val tasks: HashMap<UUID, ArrayList<TpaTask>> = hashMapOf()

        fun createInvite(from: Player, to: Player) {
            if (tasks[to.uniqueId] == null) {
                tasks[to.uniqueId] = ArrayList()
            }
            val task = TpaTask(from, to)
            for (t in tasks[to.uniqueId]!!) {
                if (t.from.uniqueId == from.uniqueId) {
                    from.sendMessage(MiniMessage.miniMessage().deserialize("<red>You already have an outgoing teleportation request."))
                    return
                }
            }
            tasks[to.uniqueId]!!.add(task)
            task.runTaskTimer(club.malvaceae.malloy.Malloy.instance, 0L, 20L)
            if (to.isOnline) {
                (to as Player).sendMessage(
                    MiniMessage.miniMessage().deserialize("<green><yellow>${from.name}</yellow> would like to teleport to you!")
                )
                to.sendMessage(
                    MiniMessage.miniMessage().deserialize("<green><bold><click:run_command:/tpa accept ${from.name}>ACCEPT</click></bold></green> <red><bold><click:run_command:/tpa deny ${from.name}>DENY</click></bold></red>")
                )
            }
            Chat.sendComponent(from, "<blue>[Teleport]</blue> Sent teleportation request to <yellow>${to.name}</yellow>.")
            club.malvaceae.malloy.Malloy.log.info("[Teleports] Created invite for ${to.name} to ${from.name}.")
        }

        fun removeInvite(invite: TpaTask) {
            tasks[invite.to.uniqueId]!!.remove(invite)
            club.malvaceae.malloy.Malloy.log.info("[Teleports] Removed invite for ${invite.to.name} from ${invite.from.name}.")
        }
    }
}

class TeleportCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            Chat.sendComponent(sender, "<red>You need to provide a player to teleport to.")
            return false
        }
        if (CombatTagListener.tags[(sender as Player).uniqueId] != null) {
            Chat.sendComponent(sender, "<red>You currently have an active combat tag, you may not use this command.")
            return false
        }
        if (args[0] == "accept") {
            val from = Bukkit.getPlayer(args[1])
            if (from == null) {
                Chat.sendComponent(sender, "<red>You need to provide an online player to teleport to.")
                return false
            }
            if (TpaHandler.tasks[(sender as Player).uniqueId] == null) {
                Chat.sendComponent(sender, "<red>You have no teleportation requests.")
                return false
            }
            for (task in TpaHandler.tasks[sender.uniqueId]!!.toList()) {
                if (task.from.uniqueId == from.uniqueId) {
                    TpaHandler.removeInvite(task)
                    Chat.sendComponent(sender, "<blue>[Teleport]</blue> Accepted request from <yellow>${from.name}</yellow>, now teleporting them to you now.")
                    Chat.sendComponent(from, "<blue>[Teleport]</blue> <yellow>${sender.name}</yellow> accepted your request, teleporting to them now.")
                    from.teleportAsync(sender.location)
                    return false
                }
            }
            Chat.sendComponent(sender, "<red>Couldn't find teleportation request, they probably didn't send you one.")
            return false
        } else if (args[0] == "deny") {
            val from = Bukkit.getPlayer(args[1])
            if (from == null) {
                Chat.sendComponent(sender, "<red>You need to provide an online player to teleport to.")
                return false
            }
            if (TpaHandler.tasks[(sender as Player).uniqueId] == null) {
                Chat.sendComponent(sender, "<red>You have no teleportation requests.")
                return false
            }
            for (task in TpaHandler.tasks[sender.uniqueId]!!.toList()) {
                if (task.from.uniqueId == from.uniqueId) {
                    TpaHandler.removeInvite(task)
                    Chat.sendComponent(sender, "<blue>[Teleports]</blue> Denied request from <yellow>${from.name}</yellow>.")
                    Chat.sendComponent(from, "<blue>[Teleports]</blue> <yellow>${sender.name}</yellow> denied your request.")
                    return false
                }
            }
            Chat.sendComponent(sender, "<red>This player hasn't sent you a teleportation request.")
            return false
        } else {
            val target = Bukkit.getPlayer(args[0])
            if (target == null) {
                Chat.sendComponent(sender, "<red>You need to provide an online player to teleport to.")
                return false
            }
            if (target == sender as Player) {
                Chat.sendComponent(sender, "<red>You can't send a teleportation request to yourself.")
                return false
            }
            TpaHandler.createInvite(sender as Player, target)
        }

        return true
    }
}