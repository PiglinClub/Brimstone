package club.malvaceae.malloy.listeners

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.database.towns.Town
import com.vexsoftware.votifier.model.Vote
import com.vexsoftware.votifier.model.VotifierEvent
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.milkbowl.vault.chat.Chat
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerVote(e: VotifierEvent) {
        val vote = e.vote as Vote
        val player = Bukkit.getOfflinePlayer(vote.username)
        if (player != null) {
            val profile = Malloy.instance.profileHandler.lookupProfile(player.uniqueId).get()
            profile.addExp(500.0)
            profile.gold += 150
            Malloy.instance.profileHandler.saveProfile(profile)
            Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<green><blue>[Vote]</blue> <yellow>${player.name}</yellow> voted for the server and got <aqua>500 xp</aqua> and <color:#ffd417>150g</color>."))
        }
    }

    @EventHandler
    fun onPlayerChat(e: AsyncChatEvent) {
        e.isCancelled = true
        val level = Malloy.instance.profileHandler.getProfile(e.player.uniqueId)!!.level
        val town: Town? = Malloy.instance.townHandler.getPlayerTown(e.player)
        if (town == null) {
            Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<dark_gray>[</dark_gray><aqua>${level}</aqua><dark_gray>]</dark_gray> <reset>${e.player.name}<dark_gray>:<reset> ${(e.message() as TextComponent).content()}"))
        } else {
            Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<dark_gray>[</dark_gray><aqua>${level}</aqua><dark_gray>]</dark_gray> <green>${town.name}</green> <reset>${e.player.name}<dark_gray>:<reset> ${(e.message() as TextComponent).content()}"))
        }
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        e.quitMessage(MiniMessage.miniMessage().deserialize("<dark_gray>[</dark_gray><red>-<dark_gray>]</dark_gray> <color:#f53527>${e.player.name}"))
    }


}