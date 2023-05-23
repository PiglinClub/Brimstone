package club.piglin.brimstone.database.towns

import club.piglin.brimstone.Brimstone
import org.bukkit.OfflinePlayer
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
        }
        if (timer == 0) {
            InviteHandler.removeInvite(this)
            cancel()
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
            Brimstone.log.info("[Invites] Created invite for ${invitee.name} to ${from.name} (${from.uniqueId}).")
        }

        fun removeInvite(invite: InviteTask) {
            tasks[invite.invitee.uniqueId]!!.remove(invite)
            Brimstone.log.info("[Invites] Removed invite for ${invite.invitee.name} from ${invite.from.name} (${invite.from.uniqueId}).")
        }
    }
}