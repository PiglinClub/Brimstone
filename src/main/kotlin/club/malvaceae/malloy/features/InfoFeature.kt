package club.malvaceae.malloy.features

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class InfoFeature : BukkitRunnable() {
    val announcements = listOf(
        "<reset>Follow us at Twitter at <aqua>https://twitter.com/piglin_club</aqua>",
        "<reset>Join our Discord at <color:#5865f2>https://discord.gg/M3dAEDzach</color>"
    )

    var timer = 300

    override fun run() {
        timer--
        if (timer == 0) {
            timer = 300
            if (Bukkit.getOnlinePlayers().isNotEmpty()) {
                Bukkit.broadcast(MiniMessage.miniMessage().deserialize(" "))
                Bukkit.broadcast(MiniMessage.miniMessage().deserialize(announcements.random()))
                Bukkit.broadcast(MiniMessage.miniMessage().deserialize(" "))
            }
        }
    }
}