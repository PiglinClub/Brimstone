package club.malvaceae.malloy.listeners

import club.malvaceae.malloy.Malloy
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.milkbowl.vault.chat.Chat
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener : Listener {
    private var vaultChat: Chat? = null

    init {
        vaultChat = Bukkit.getServer().servicesManager.load(Chat::class.java)
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.joinMessage(MiniMessage.miniMessage().deserialize("<dark_gray>[</dark_gray><green>+<dark_gray>]</dark_gray> <color:#28cf1f>${e.player.name}"))
        club.malvaceae.malloy.utils.Chat.sendComponent(e.player, "<gray>Warning: This server is in <red>ALPHA</red>, many features may be added/removed later on.")
    }

    @EventHandler
    fun onPlayerChat(e: AsyncChatEvent) {
        e.isCancelled = true
        val level = Malloy.instance.profileHandler.getProfile(e.player.uniqueId)!!.level
        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<dark_gray>[</dark_gray><aqua>${level}</aqua><dark_gray>]</dark_gray> <reset>${e.player.name}<dark_gray>:<reset> ${(e.message() as TextComponent).content()}"))
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        e.quitMessage(MiniMessage.miniMessage().deserialize("<dark_gray>[</dark_gray><red>+<dark_gray>]</dark_gray> <color:#f53527>${e.player.name}"))
    }


}