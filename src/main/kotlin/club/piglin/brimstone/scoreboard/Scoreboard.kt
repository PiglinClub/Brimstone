package club.piglin.brimstone.scoreboard

import club.piglin.brimstone.Brimstone
import fr.mrmicky.fastboard.FastBoard
import me.lucko.helper.Schedulers
import net.md_5.bungee.api.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*


class Scoreboard : Listener {
    init {
        Schedulers.sync().runRepeating(Runnable {
            for ((uuid, board) in boards) {
                board.updateLines(
                    "${ChatColor.RESET}Balance: ${ChatColor.of("#ffd417")}⛃ ${Brimstone.instance.profileHandler.getProfile(uuid)}"
                )
            }
        }, 0L, 20L)
    }

    private val boards: Map<UUID, FastBoard> = HashMap()

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val board = FastBoard(e.player)
        board.updateTitle("${ChatColor.of("#ed982f")}PIG${ChatColor.of("#d18426")}LIN${ChatColor.of("#3b3b3b")}.${ChatColor.of("#1c1b1a")}CLUB")
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        val board = boards[e.player.uniqueId]
        if (board != null) {
            boards[e.player.uniqueId]!!.delete()
        }
    }
}