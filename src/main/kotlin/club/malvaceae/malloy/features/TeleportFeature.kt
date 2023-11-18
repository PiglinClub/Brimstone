package club.malvaceae.malloy.features

import club.malvaceae.malloy.utils.Chat
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.math.floor
import kotlin.math.round
import kotlin.random.Random

class TeleportFeature {
    companion object {
        val tasks = hashMapOf<UUID, TeleportTask?>()

        fun startTeleport(player: Player, location: Location) {
            val task = TeleportTask(player, location)
            tasks[player.uniqueId] = task
            tasks[player.uniqueId]!!.runTaskTimer(club.malvaceae.malloy.Malloy.instance, 0L, 20L)
            Chat.sendComponent(player, "<blue>[Teleport]</blue> Starting teleportation to <yellow>X: ${floor(location.x)}, Y: ${floor(location.y)}, Z: ${floor(location.z)}</yellow>")
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
}

class TeleportTask(val player: Player, val location: Location) : BukkitRunnable() {
    var timer = 5

    override fun run() {
        player.sendActionBar(MiniMessage.miniMessage().deserialize("<blue>[Teleport]</blue> Teleporting in <yellow>${timer}s</yellow>..."))
        timer--
        if (timer == 0) {
            cancel()
            TeleportFeature.tasks[player.uniqueId] = null
            player.teleportAsync(location)
            Chat.sendComponent(player, "<blue>[Teleport]</blue> <reset>Teleported you to <yellow>X: ${floor(location.x)}, Y: ${floor(location.y)}, Z: ${floor(location.z)}</yellow>.")
        }
    }
}