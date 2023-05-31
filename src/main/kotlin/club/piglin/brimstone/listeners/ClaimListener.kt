package club.piglin.brimstone.listeners

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.database.towns.Claim
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.title.Title
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*

class ClaimListener : Listener {
    val claimMap = hashMapOf<UUID, Claim?>()

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (e.from.chunk != e.to.chunk) {
            val claim = Brimstone.instance.claimHandler.getClaimAt(e.to.chunk.x, e.to.chunk.z).get()
            print(claimMap[e.player.uniqueId])
            if (claimMap[e.player.uniqueId] == null) {
                if (claim != null) {
                    val town = Brimstone.instance.townHandler.getTown(claim.townUniqueId)
                    if (town != null) {
                        e.player.showTitle(Title.title(
                            Component.text(" "),
                            MiniMessage.miniMessage().deserialize("<reset>Now entering: <yellow>${town.name}</yellow>")
                        ))
                    }
                } else {
                    return
                }
            } else {
                if (claim != null) {
                    if (claim.townUniqueId != claimMap[e.player.uniqueId]!!.townUniqueId) {
                        val town = Brimstone.instance.townHandler.getTown(claim.townUniqueId)
                        if (town != null) {
                            e.player.showTitle(Title.title(
                                Component.text(" "),
                                MiniMessage.miniMessage().deserialize("<reset>Now entering: <yellow>${town.name}</yellow>")
                            ))
                        }
                    }
                } else {
                    e.player.showTitle(Title.title(
                        Component.text(" "),
                        MiniMessage.miniMessage().deserialize("<reset>Now entering: <dark_green>Wilderness</dark_green>")
                    ))
                }
            }
            claimMap[e.player.uniqueId] = claim
        }
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        claimMap[e.player.uniqueId] = Brimstone.instance.claimHandler.getClaimAt(e.player.chunk.x, e.player.chunk.z).get()
    }
}