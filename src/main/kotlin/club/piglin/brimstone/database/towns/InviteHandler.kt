package club.piglin.brimstone.database.towns

import club.piglin.brimstone.Brimstone
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

enum class InviteStatus {
    ACCEPTED,
    DENIED,
    NOTHING_YET,
    SOMETHING_ELSE
}

class InviteTask(
    val from: Town,
    val inviter: OfflinePlayer,
    val invitee: OfflinePlayer
) : BukkitRunnable() {
    var timer = 600
    val status = InviteStatus.NOTHING_YET

    override fun run() {
        timer -= 1
        if (status == InviteStatus.ACCEPTED || status == InviteStatus.DENIED) {
            InviteHandler.removeInvite(this)
            cancel()
            Brimstone.log.info("[Invites] Invite removed because accepted/denied for ${invitee.name} to ${from.name} (${from.uniqueId}).")
        }
        if (timer == 0) {
            InviteHandler.removeInvite(this)
            cancel()
            Brimstone.log.info("[Invites] Invite timed out for ${invitee.name} to ${from.name} (${from.uniqueId}).")
        }
    }

}

class InviteHandler {
    companion object {
        val tasks: HashMap<UUID, ArrayList<InviteTask>> = hashMapOf()

        fun createInvite(from: Town, inviter: OfflinePlayer, invitee: OfflinePlayer) {
            if (tasks[invitee.uniqueId] == null) {
                tasks[invitee.uniqueId] = ArrayList()
            }
            val task = InviteTask(from, inviter, invitee)
            tasks[invitee.uniqueId]!!.add(task)
            task.runTaskTimer(Brimstone.instance, 0L, 20L)
            if (invitee.isOnline) {
                (invitee as Player).sendMessage(
                    MiniMessage.miniMessage().deserialize("<green>You have received an invite to join <yellow>${from.name}</yellow> from <yellow>${invitee.name}</yellow>! (Town tax: <gold>${from.tax}g</gold>)")
                )
                invitee.sendMessage(
                    MiniMessage.miniMessage().deserialize("<green><bold><click:run_command:/towny accept ${from.uniqueId}>ACCEPT</click></bold></green> <red><bold><click:run_command:/towny deny ${from.uniqueId}>DENY</click></bold></red>")
                )

            }
            Brimstone.log.info("[Invites] Created invite for ${invitee.name} to ${from.name} (${from.uniqueId}).")
        }

        fun removeInvite(invite: InviteTask) {
            tasks[invite.invitee.uniqueId]!!.remove(invite)
            Brimstone.log.info("[Invites] Removed invite for ${invite.invitee.name} from ${invite.from.name} (${invite.from.uniqueId}).")
        }
    }
}