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
import kotlin.math.round


class Scoreboard : Listener {
    init {
        Schedulers.sync().runRepeating(Runnable {
            for ((uuid, board) in boards) {
                val profile = Brimstone.instance.profileHandler.getProfile(uuid)!!
                val percentage = round(((profile.xp / 1000) * 100)).toInt()
                val progressBar = if (percentage >= 95) {
                    "${ChatColor.of("#4fe5ff")}${ChatColor.BOLD}██████████"
                } else if (percentage >= 90) {
                    "${ChatColor.of("#4fe5ff")}${ChatColor.BOLD}▉▉▉▉▉▉▉▉▉${ChatColor.RESET}${ChatColor.BOLD}▉"
                } else if (percentage >= 80) {
                    "${ChatColor.of("#4fe5ff")}${ChatColor.BOLD}▉▉▉▉▉▉▉▉${ChatColor.RESET}${ChatColor.BOLD}▉▉"
                } else if (percentage >= 70) {
                    "${ChatColor.of("#4fe5ff")}${ChatColor.BOLD}▉▉▉▉▉▉▉${ChatColor.RESET}${ChatColor.BOLD}▉▉▉"
                } else if (percentage >= 60) {
                    "${ChatColor.of("#4fe5ff")}${ChatColor.BOLD}▉▉▉▉▉▉${ChatColor.RESET}${ChatColor.BOLD}▉▉▉▉"
                } else if (percentage >= 50) {
                    "${ChatColor.of("#4fe5ff")}${ChatColor.BOLD}▉▉▉▉▉${ChatColor.RESET}${ChatColor.BOLD}▉▉▉▉▉"
                } else if (percentage >= 40) {
                    "${ChatColor.of("#4fe5ff")}${ChatColor.BOLD}▉▉▉▉${ChatColor.RESET}${ChatColor.BOLD}▉▉▉▉▉▉"
                } else if (percentage >= 30) {
                    "${ChatColor.of("#4fe5ff")}${ChatColor.BOLD}▉▉▉${ChatColor.RESET}${ChatColor.BOLD}▉▉▉▉▉▉▉"
                } else if (percentage >= 20) {
                    "${ChatColor.of("#4fe5ff")}${ChatColor.BOLD}▉▉${ChatColor.RESET}${ChatColor.BOLD}▉▉▉▉▉▉▉▉"
                } else if (percentage >= 10) {
                    "${ChatColor.of("#4fe5ff")}${ChatColor.BOLD}▉${ChatColor.RESET}${ChatColor.BOLD}▉▉▉▉▉▉▉▉▉"
                } else {
                    "${ChatColor.of("#4fe5ff")}${ChatColor.RESET}▉▉▉▉▉▉▉▉▉▉"
                }
                board.updateLines(
                    " ",
                    "${ChatColor.RESET} Balance: ${ChatColor.of("#ffd417")}${profile.gold}g",
                    "${ChatColor.RESET} Level: ${ChatColor.of("#32b3c9")}${profile.level}",
                    " $progressBar ${ChatColor.of("#28cf1f")}${percentage}%",
                    " ",
                    ""
                )
            }
        }, 0L, 20L)
    }

    private val boards: HashMap<UUID, FastBoard> = HashMap()

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        Brimstone.log.info("Attempting to add ${e.player.name}'s scoreboard.")
        val board = FastBoard(e.player)
        boards[e.player.uniqueId] = board
        board.updateTitle("${ChatColor.of("#ed982f")}${ChatColor.BOLD}PIG${ChatColor.of("#d18426")}${ChatColor.BOLD}LIN${ChatColor.of("#3b3b3b")}${ChatColor.BOLD}.${ChatColor.of("#1c1b1a")}${ChatColor.BOLD}CLUB")
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        Brimstone.log.info("Attempting to remove ${e.player.name}'s scoreboard.")
        val board = boards[e.player.uniqueId]
        if (board != null) {
            boards.remove(e.player.uniqueId)
        }
    }
}