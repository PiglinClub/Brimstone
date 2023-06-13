package club.piglin.brimstone.listeners

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.database.towns.Claim
import club.piglin.brimstone.utils.Chat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
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

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val claim = Brimstone.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        val town = Brimstone.instance.townHandler.getPlayerTown(e.player)
        if (claim.get() != null) {
            val claimTown = Brimstone.instance.townHandler.getTown(claim.get()!!.townUniqueId)!!
            if (town != null) {
                if (claim.get()!!.townUniqueId != town.uniqueId) {
                    e.isCancelled = true
                    Chat.sendComponent(e.player, "<red>You cannot build here as you are not in <yellow>${claimTown.name}</yellow>.</red>")
                }
            } else {
                e.isCancelled = true
                Chat.sendComponent(e.player, "<red>You cannot build here as you are not in <yellow>${claimTown.name}</yellow>.</red>")
            }
        }
    }

    @EventHandler
    fun onPistonRetract(e: BlockPistonRetractEvent) {

    }

    @EventHandler
    fun onBlockForm(e: EntityBlockFormEvent) {
        val claim = Brimstone.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        if (e.entity is Player) {
            val town = Brimstone.instance.townHandler.getPlayerTown(e.entity as Player)
            if (claim.get() != null) {
                val claimTown = Brimstone.instance.townHandler.getTown(claim.get()!!.townUniqueId)!!
                if (town != null) {
                    if (claim.get()!!.townUniqueId != town.uniqueId) {
                        e.isCancelled = true
                        Chat.sendComponent(e.entity, "<red>You cannot build here as you are not in <yellow>${claimTown.name}</yellow>.</red>")
                    }
                } else {
                    e.isCancelled = true
                    Chat.sendComponent(e.entity, "<red>You cannot build here as you are not in <yellow>${claimTown.name}</yellow>.</red>")
                }
            }
        }
    }

    @EventHandler
    fun onBlockIgnite(e: BlockIgniteEvent) {
        val claim = Brimstone.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        if (e.player != null) {
            val town = Brimstone.instance.townHandler.getPlayerTown(e.player!!)
            if (claim.get() != null) {
                val claimTown = Brimstone.instance.townHandler.getTown(claim.get()!!.townUniqueId)!!
                if (town != null) {
                    if (claim.get()!!.townUniqueId != town.uniqueId) {
                        e.isCancelled = true
                        Chat.sendComponent(e.player!!, "<red>You cannot build here as you are not in <yellow>${claimTown.name}</yellow>.</red>")
                    }
                } else {
                    e.isCancelled = true
                    Chat.sendComponent(e.player!!, "<red>You cannot build here as you are not in <yellow>${claimTown.name}</yellow>.</red>")
                }
            }
        }
    }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        val claim = Brimstone.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        val town = Brimstone.instance.townHandler.getPlayerTown(e.player)
        if (claim.get() != null) {
            val claimTown = Brimstone.instance.townHandler.getTown(claim.get()!!.townUniqueId)!!
            if (town != null) {
                if (claim.get()!!.townUniqueId != town.uniqueId) {
                    e.isCancelled = true
                    Chat.sendComponent(e.player, "<red>You cannot build here as you are not in <yellow>${claimTown.name}</yellow>.</red>")
                }
            } else {
                e.isCancelled = true
                Chat.sendComponent(e.player, "<red>You cannot build here as you are not in <yellow>${claimTown.name}</yellow>.</red>")
            }
        }
    }
}