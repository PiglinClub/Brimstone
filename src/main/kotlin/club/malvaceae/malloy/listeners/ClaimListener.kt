package club.malvaceae.malloy.listeners

import club.malvaceae.malloy.database.towns.Claim
import club.malvaceae.malloy.utils.Chat
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerBucketFillEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

class ClaimListener : Listener {
    companion object {
        val claimMap = hashMapOf<UUID, Claim?>()
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        claimMap[e.player.uniqueId] = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.player.chunk.x, e.player.chunk.z).get()
    }

    @EventHandler
    fun onBucketEmpty(e: PlayerBucketEmptyEvent) {
        val claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        val playerTown = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(e.player)
        if (claim.get() != null) {
            val claimTown = club.malvaceae.malloy.Malloy.instance.townHandler.getTown(claim.get()!!.townUniqueId)!!
            if (playerTown != null) {
                if (claim.get()!!.townUniqueId != playerTown.uniqueId) {
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
        val claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z).get()
        val playerTown = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(player)
        if (claim != null) {
            val claimTown = club.malvaceae.malloy.Malloy.instance.townHandler.getTown(claim.townUniqueId)!!
            if (playerTown != null) {
                if (claim.townUniqueId != playerTown.uniqueId) {
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
            val fromClaim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z).get()
            val toClaim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.source.chunk.x, e.source.chunk.z).get()
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
        val claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        val playerTown = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(e.player)
        if (claim.get() != null) {
            val claimTown = club.malvaceae.malloy.Malloy.instance.townHandler.getTown(claim.get()!!.townUniqueId)!!
            if (playerTown != null) {
                if (claim.get()!!.townUniqueId != playerTown.uniqueId) {
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
        val claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        if (claim.get() != null) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockFertilize(e: BlockFertilizeEvent) {
        val allowed = ArrayList(e.blocks)
        val sourceClaim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z).get()
        for (block in e.blocks) {
            val claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(block.chunk.x, block.chunk.z).get()
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
        val claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        val playerTown = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(e.player)
        if (claim.get() != null) {
            val claimTown = club.malvaceae.malloy.Malloy.instance.townHandler.getTown(claim.get()!!.townUniqueId)!!
            if (playerTown != null) {
                if (claim.get()!!.townUniqueId != playerTown.uniqueId) {
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
        claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(b.chunk.x, b.chunk.z).get()
        movingClaim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(b.getRelative(e.direction).chunk.x, b.getRelative(e.direction).chunk.z).get()
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
                claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(b.chunk.x, b.chunk.z).get()
                movingClaim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(b.getRelative(e.direction).chunk.x, b.getRelative(e.direction).chunk.z).get()
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
        var movingClaim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(block.chunk.x, block.chunk.z).get()
        var claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z).get()
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
            claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(b.chunk.x, b.chunk.z).get()
            movingClaim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(b.getRelative(e.direction).chunk.x, b.getRelative(e.direction).chunk.z).get()
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
        val claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        if (e.entity is Player) {
            val playerTown = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(e.entity as Player)
            if (claim.get() != null) {
                val claimTown = club.malvaceae.malloy.Malloy.instance.townHandler.getTown(claim.get()!!.townUniqueId)!!
                if (playerTown != null) {
                    if (claim.get()!!.townUniqueId != playerTown.uniqueId) {
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
                val claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(block.chunk.x, block.chunk.z).get()
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
        val claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        if (e.player != null) {
            val playerTown = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(e.player!!)
            if (claim.get() != null) {
                val claimTown = club.malvaceae.malloy.Malloy.instance.townHandler.getTown(claim.get()!!.townUniqueId)!!
                if (playerTown != null) {
                    if (claim.get()!!.townUniqueId != playerTown.uniqueId) {
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
        val claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        val playerTown = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(e.player)
        if (claim.get() != null) {
            val claimTown = club.malvaceae.malloy.Malloy.instance.townHandler.getTown(claim.get()!!.townUniqueId)!!
            if (playerTown != null) {
                if (claim.get()!!.townUniqueId != playerTown.uniqueId) {
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
    fun onFarmLandDamage(e: EntityChangeBlockEvent) {
        val claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.block.chunk.x, e.block.chunk.z)
        if (e.entity is Player) {
            val playerTown = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(e.entity as Player)
            if (claim.get() != null) {
                val claimTown = club.malvaceae.malloy.Malloy.instance.townHandler.getTown(claim.get()!!.townUniqueId)!!
                if (playerTown != null) {
                    if (claim.get()!!.townUniqueId != playerTown.uniqueId) {
                        e.isCancelled = true
                        Chat.sendComponent(e.entity, "<red>You cannot trample crops as you are not in <yellow>${claimTown.name}</yellow>.</red>")
                    }
                } else {
                    e.isCancelled = true
                    Chat.sendComponent(e.entity, "<red>You cannot trample crops as you are not in <yellow>${claimTown.name}</yellow>.</red>")
                }
                /*
                    Looking at what Saber Factions does[1], it seems like they just treat all EntityChangeBlockEvents done
                    by players as a trampling of crops. I just followed that behavior here, but is it possible that
                    there could be a different EntityChangeBlockEvent that could be done by a player that we *would*
                    want to be possible on claimed land?
                    I mean, that's pretty unlikely... even if this catches some other thing too, it's probably something
                    that we wouldn't want happening on claimed land by players not in the town anyways.

                    [1] https://github.com/SaberLLC/Saber-Factions/blob/1.6.x/src/main/java/com/massivecraft/factions/listeners/FactionsBlockListener.java#L398
                */
            }
        }
    }

    @EventHandler
    fun onItemFrameRemove(e: HangingBreakByEntityEvent) {
        if (e.remover is Player) {
            if (e.entity.type.name.contains("ITEM_FRAME")) {
                val claim = club.malvaceae.malloy.Malloy.instance.claimHandler.getClaimAt(e.entity.location.chunk.x, e.entity.location.chunk.z)
                if (claim.get() != null) {
                    val claimTown = club.malvaceae.malloy.Malloy.instance.townHandler.getTown(claim.get()!!.townUniqueId)!!
                    val playerTown = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(e.remover as Player)
                    if (playerTown != null) {
                        if (claim.get()!!.townUniqueId != playerTown.uniqueId) {
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
    }


}