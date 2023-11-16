package club.malvaceae.malloy.listeners

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

class ServerListPingListener : Listener {
    @EventHandler
    fun onServerListPing(e: ServerListPingEvent) {
        e.motd(MiniMessage.miniMessage().deserialize("                   <color:#ff8d0a><b>PIGLIN</b></color><color:#2e2e2e><b>.</b></color><color:#e88009><b>CLUB</b></color> <color:#ff2a00>ALPHA</color>\n           <color:#4ffff9>@piglin_club</color> <color:#2e2e2e><b>·</b></color> <color:#c46900>https://piglin.club</color>"))
    }
}