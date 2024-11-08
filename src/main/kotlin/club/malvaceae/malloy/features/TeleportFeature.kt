package club.malvaceae.malloy.features

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.commands.BackCommand
import club.malvaceae.malloy.utils.Chat
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.math.floor

class TeleportFeature {
    companion object {
        val tasks = hashMapOf<UUID, TeleportTask?>()

        fun startTeleport(player: Player, location: Location, revealLocation: Boolean = true) {
            val task = TeleportTask(player, location)
            tasks[player.uniqueId] = task
            tasks[player.uniqueId]!!.runTaskTimer(Malloy.instance, 0L, 20L)
            if (revealLocation) {
                Chat.sendComponent(player, "<blue>[Teleport]</blue> Starting teleportation to <yellow>X: ${floor(location.x)}, Y: ${floor(location.y)}, Z: ${floor(location.z)}</yellow>")
            } else {
                Chat.sendComponent(player, "<blue>[Teleport]</blue> Starting teleportation...")
            }
        }
    }
}

class TeleportCheck : Listener {
    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (e.hasExplicitlyChangedBlock()) {
            if (TeleportFeature.tasks[e.player.uniqueId] != null) {
                TeleportFeature.tasks[e.player.uniqueId]!!.cancel()
                TeleportFeature.tasks[e.player.uniqueId] = null
                Chat.sendComponent(e.player, "<blue>[Teleport]</blue> <red>You moved! Cancelling teleportation.</red>")
            }
        }
    }

    @EventHandler
    fun onPlayerDamage(e: EntityDamageEvent) {
        if (e.entityType == EntityType.PLAYER) {
            val player = e.entity as Player;
            if (TeleportFeature.tasks[player.uniqueId] != null) {
                TeleportFeature.tasks[player.uniqueId]!!.cancel()
                TeleportFeature.tasks[player.uniqueId] = null
                Chat.sendComponent(player, "<blue>[Teleport]</blue> <red>You took damage! Cancelling teleportation.</red>")
            }
        }
    }
}

class TeleportTask(val player: Player, val location: Location) : BukkitRunnable() {
    var timer = 5

    override fun run() {
        player.sendActionBar(MiniMessage.miniMessage().deserialize("<blue>[Teleport]</blue> Teleporting in <yellow>${timer}s</yellow>..."))
        timer--
        if (timer == 0) {
            cancel()
            TeleportFeature.tasks[player.uniqueId] = null
            BackCommand.locations[player.uniqueId] = player.location
            player.teleportAsync(location)
            Malloy.log.info("[Teleports] Teleported ${player.name} from ${Chat.displayLocation(BackCommand.locations[player.uniqueId]!!)} to ${Chat.displayLocation(player.location)}")
        }
    }
}