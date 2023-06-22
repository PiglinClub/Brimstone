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
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerBucketFillEvent
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
    fun onBlockFromTo(e: BlockFromToEvent) {
        val fromClaim = Brimstone.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z).get()
        val toClaim = Brimstone.instance.claimHandler.getClaimAt(e.toBlock.chunk.x, e.toBlock.chunk.z).get()
        if (fromClaim == null && toClaim != null) {
            e.isCancelled = true
            return
        }
        if (fromClaim != null && toClaim == null) {
            e.isCancelled = true
            return
        }
        if (fromClaim == null && toClaim == null) {
            return
        }
        if (fromClaim!!.townUniqueId != toClaim!!.townUniqueId) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBucketEmpty(e: PlayerBucketEmptyEvent) {
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
    fun onCauldronLevelChange(e: CauldronLevelChangeEvent) {
        if (e.entity !is Player) {
            return
        }
        val player = e.entity as Player
        if (e.reason == CauldronLevelChangeEvent.ChangeReason.EXTINGUISH) {
            e.isCancelled = true
            if (player.fireTicks > 0) {
                player.fireTicks = 0
                Chat.sendComponent(player, "<gray>You're welcome...")
            }
            return
        }
        val claim = Brimstone.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z).get()
        val town = Brimstone.instance.townHandler.getPlayerTown(player)
        if (claim != null) {
            val claimTown = Brimstone.instance.townHandler.getTown(claim.townUniqueId)!!
            if (town != null) {
                if (claim.townUniqueId != town.uniqueId) {
                    e.isCancelled = true
                    Chat.sendComponent(player, "<red>You cannot use this cauldron as you are not in <yellow>${claimTown.name}</yellow>.</red>")
                }
            } else {
                e.isCancelled = true
                Chat.sendComponent(player, "<red>You cannot use this cauldron as you are not in <yellow>${claimTown.name}</yellow>.</red>")
            }
        }
    }

    @EventHandler
    fun onSculkSpread(e: BlockSpreadEvent) {
        val source = e.source.type.name
        if (!source.lowercase().startsWith("sculk")) {
            return
        }
        if (source.lowercase() == "sculk_catalyst") {
            val fromClaim = Brimstone.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z).get()
            val toClaim = Brimstone.instance.claimHandler.getClaimAt(e.source.chunk.x, e.source.chunk.z).get()
            if (fromClaim == null && toClaim != null) {
                e.isCancelled = true
                return
            }
            if (fromClaim != null && toClaim == null) {
                e.isCancelled = true
                return
            }
            if (fromClaim == null && toClaim == null) {
                return
            }
            if (fromClaim!!.townUniqueId != toClaim!!.townUniqueId) {
                e.isCancelled = true
                return
            }
        }
    }

    @EventHandler
    fun onBucketFill(e: PlayerBucketFillEvent) {
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
    fun onBlockBurn(e: BlockBurnEvent) {
        val claim = Brimstone.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        if (claim.get() != null) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockFertilize(e: BlockFertilizeEvent) {
        val allowed = ArrayList(e.blocks)
        val sourceClaim = Brimstone.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z).get()
        for (block in e.blocks) {
            val claim = Brimstone.instance.claimHandler.getClaimAt(block.chunk.x, block.chunk.z).get()
            if (claim == null && sourceClaim == null) {
                return
            }
            if (claim != null && sourceClaim == null) {
                allowed.remove(block)
                continue
            }
            if (claim == null && sourceClaim != null) {
                allowed.remove(block)
                continue
            }
            if (claim!!.townUniqueId != sourceClaim!!.townUniqueId) {
                allowed.remove(block)
            }
        }
        e.blocks.clear()
        e.blocks.addAll(allowed)
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
    fun onPistonExtend(e: BlockPistonExtendEvent) {
        var movingClaim: Claim?
        var claim: Claim?
        val b = e.block.getRelative(e.direction)
        claim = Brimstone.instance.claimHandler.getClaimAt(b.chunk.x, b.chunk.z).get()
        movingClaim = Brimstone.instance.claimHandler.getClaimAt(b.getRelative(e.direction).chunk.x, b.getRelative(e.direction).chunk.z).get()
        if (claim == null && movingClaim != null) {
            e.isCancelled = true
            return
        }
        if (claim != null && movingClaim == null) {
            e.isCancelled = true
            return
        }
        if (claim == null && movingClaim == null) {
            return
        }
        if (claim!!.townUniqueId != movingClaim!!.townUniqueId) {
            e.isCancelled = true
            return
        }
        if (e.blocks.isNotEmpty()) {
            for (b in e.blocks) {
                claim = Brimstone.instance.claimHandler.getClaimAt(b.chunk.x, b.chunk.z).get()
                movingClaim = Brimstone.instance.claimHandler.getClaimAt(b.getRelative(e.direction).chunk.x, b.getRelative(e.direction).chunk.z).get()
                if (claim == null && movingClaim != null) {
                    e.isCancelled = true
                    return
                }
                if (claim != null && movingClaim == null) {
                    e.isCancelled = true
                    return
                }
                if (claim == null && movingClaim == null) {
                    return
                }
                if (claim!!.townUniqueId != movingClaim!!.townUniqueId) {
                    e.isCancelled = true
                    return
                }
            }
        }
    }

    @EventHandler
    fun onPistonRetract(e: BlockPistonRetractEvent) {
        val block = if (e.isSticky) e.block.getRelative(e.direction.oppositeFace) else e.block.getRelative(e.direction)
        var movingClaim = Brimstone.instance.claimHandler.getClaimAt(block.chunk.x, block.chunk.z).get()
        var claim = Brimstone.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z).get()
        if (claim == null && movingClaim != null) {
            e.isCancelled = true
            return
        }
        if (claim != null && movingClaim == null) {
            e.isCancelled = true
            return
        }
        if (claim == null && movingClaim == null) {
            return
        }
        if (claim!!.townUniqueId != movingClaim!!.townUniqueId) {
            e.isCancelled = true
            return
        }
        val blocks = e.blocks
        if (blocks.isEmpty()) {
            return
        }
        for (b in blocks) {
            claim = Brimstone.instance.claimHandler.getClaimAt(b.chunk.x, b.chunk.z).get()
            movingClaim = Brimstone.instance.claimHandler.getClaimAt(b.getRelative(e.direction).chunk.x, b.getRelative(e.direction).chunk.z).get()
            if (claim == null && movingClaim != null) {
                e.isCancelled = true
                return
            }
            if (claim != null && movingClaim == null) {
                e.isCancelled = true
                return
            }
            if (claim == null && movingClaim == null) {
                return
            }
            if (claim!!.townUniqueId != movingClaim!!.townUniqueId) {
                e.isCancelled = true
                return
            }
        }
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
                        Chat.sendComponent(e.entity, "<red>You cannot frost walk as you are not in <yellow>${claimTown.name}</yellow>.</red>")
                    }
                } else {
                    e.isCancelled = true
                    Chat.sendComponent(e.entity, "<red>You cannot frost walk as you are not in <yellow>${claimTown.name}</yellow>.</red>")
                }
            }
        } else {
            if (claim.get() != null) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onExplosion(e: BlockExplodeEvent) {
        if (e.blockList().isNotEmpty()) {
            val newList = ArrayList(e.blockList())
            for (block in e.blockList()) {
                val claim = Brimstone.instance.claimHandler.getClaimAt(block.chunk.x, block.chunk.z).get()
                if (claim != null) {
                    newList.remove(block)
                }
            }
            e.blockList().clear()
            e.blockList().addAll(newList)
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
                        Chat.sendComponent(e.player!!, "<red>You cannot ignite this block as you are not in <yellow>${claimTown.name}</yellow>.</red>")
                    }
                } else {
                    e.isCancelled = true
                    Chat.sendComponent(e.player!!, "<red>You cannot ignite this block as you are not in <yellow>${claimTown.name}</yellow>.</red>")
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