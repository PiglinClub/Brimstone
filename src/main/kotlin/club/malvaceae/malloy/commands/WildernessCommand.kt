package club.malvaceae.malloy.commands

import club.malvaceae.malloy.utils.Chat
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.math.round
import kotlin.random.Random

class WildernessMovementCheck : Listener {
    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (e.hasExplicitlyChangedBlock()) {
            if (WildernessCommand.tasks[e.player.uniqueId] != null) {
                WildernessCommand.tasks[e.player.uniqueId]!!.cancel()
                WildernessCommand.tasks[e.player.uniqueId] = null
                Chat.sendComponent(e.player, "<dark_green>[Wilderness]</dark_green> <red>You moved! Cancelling teleportation.</red>")
            }
        }
    }
}

class WildernessTask(val player: Player) : BukkitRunnable(), Listener {
    var timer = 5

    override fun run() {
        player.sendActionBar(MiniMessage.miniMessage().deserialize("<dark_green>[Wilderness]</dark_green> Teleporting in <green>${timer}s</green>..."))
        timer--
        if (timer == 0) {
            cancel()
            WildernessCommand.tasks[player.uniqueId] = null
            val location = Location(
                player.world,
                Random.nextDouble(player.location.x - 5000, player.location.x + 5000),
                256.0,
                Random.nextDouble(player.location.z - 5000, player.location.z + 5000)
            )
            player.teleportAsync(location.world.getHighestBlockAt(location).location)
            Chat.sendComponent(player, "<dark_green>[Wilderness]</dark_green> <reset>Teleported you to <green>X: ${round(location.x)}, Z: ${round(location.z)}</green>.")
        }
    }
}

class WildernessCommand : CommandExecutor {
    companion object {
        val tasks = hashMapOf<UUID, WildernessTask?>()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You can't use this command as you aren't a player.")
            return false
        }
        val task = WildernessTask(sender)
        tasks[sender.uniqueId] = task
        tasks[sender.uniqueId]!!.runTaskTimer(club.malvaceae.malloy.Malloy.instance, 0L, 20L)
        Chat.sendComponent(sender, "<dark_green>[Wilderness]</dark_green> Teleporting you to a random location in 5 seconds, don't move...")
        return true
    }
}