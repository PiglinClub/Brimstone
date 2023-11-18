package club.malvaceae.malloy.features

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class InfoFeature : BukkitRunnable() {
    val announcements = listOf(
        "<reset>Follow us at Twitter at <click:open_url:'https://twitter.com/piglin_club'><aqua>https://twitter.com/piglin_club</aqua></click> to get nice posts and more!",
        "<reset>Join our Discord at <click:open_url:'https://discord.gg/M3dAEDzach'><color:#5865f2>https://discord.gg/M3dAEDzach</color></click> to get updates & more about our server!",
        "<reset>Vote for our server at <click:open_url:'https://piglin.club/vote'><color:#ff8d0a>https://piglin.club/vote</color></click> and get <aqua>25 xp</aqua> and <color:#ffd417>1500g</color> per voting link!"
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