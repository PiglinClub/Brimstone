package club.piglin.brimstone.scoreboard

import club.piglin.brimstone.Brimstone
import fr.mrmicky.fastboard.FastBoard
import me.lucko.helper.Schedulers
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import kotlin.math.floor
import kotlin.math.round


class Scoreboard : Listener {
    fun timeToString(ticks: Long): String {
        var t = ticks
        val hours = floor(t / 3600.toDouble()).toInt()
        t -= hours * 3600
        val minutes = floor(t / 60.toDouble()).toInt()
        t -= minutes * 60
        val seconds = t.toInt()
        val output = StringBuilder()
        if (hours > 0) {
            output.append(hours).append('h')
            if (minutes == 0) {
                output.append(minutes).append('m')
            }
        }
        if (minutes > 0) {
            output.append(minutes).append('m')
        }
        output.append(seconds).append('s')
        return output.toString()
    }

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

                if (profile.town == null) {
                    board.updateLines(
                        " ",
                        "${ChatColor.RESET} Players: ${ChatColor.of("#ff5100")}${Bukkit.getOnlinePlayers().size}/${Bukkit.getMaxPlayers()}",
                        "${ChatColor.RESET} Balance: ${ChatColor.of("#ffd417")}${profile.gold}g",
                        "${ChatColor.RESET} Level: ${ChatColor.of("#32b3c9")}${profile.level}",
                        " $progressBar ${ChatColor.of("#28cf1f")}${percentage}%",
                        " "
                    )
                } else {

                    val town = Brimstone.instance.townHandler.getTown(profile.town!!)!!
                    board.updateLines(
                        " ",
                        "${ChatColor.RESET} Players: ${ChatColor.of("#ff5100")}${Bukkit.getOnlinePlayers().size}/${Bukkit.getMaxPlayers()}",
                        "${ChatColor.RESET} Balance: ${ChatColor.of("#ffd417")}${profile.gold}g",
                        "${ChatColor.RESET} Level: ${ChatColor.of("#32b3c9")}${profile.level}",
                        " $progressBar ${ChatColor.of("#28cf1f")}${percentage}%",
                        " ",
                        " ${ChatColor.GREEN}${town.name}${ChatColor.RESET}:",
                        "  ${ChatColor.RESET}Members: ${ChatColor.of("#ff5100")}${town.members.size}/25",
                        "  ${ChatColor.RESET}Claims: ${ChatColor.of("#ff5100")}${town.getClaims().get().size}",
                        "  ${ChatColor.RESET}Balance: ${ChatColor.of("#ffd417")}${town.gold}g",
                        "  ${ChatColor.RESET}Power: ${ChatColor.of("#ff365e")}☄ ${floor(town.power).toInt()}",
                        " "
                    )
                }
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