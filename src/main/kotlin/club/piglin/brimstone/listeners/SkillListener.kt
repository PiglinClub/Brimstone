package club.piglin.brimstone.listeners

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.database.profiles.Skill
import org.bukkit.Material
import org.bukkit.block.data.Ageable
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDeathEvent

class SkillListener : Listener {
    @EventHandler
    fun onLogMine(e: BlockBreakEvent) {
        if (!e.isCancelled) {
            var exp = 0.0
            when (e.block.type) {
                Material.OAK_LOG, Material.OAK_WOOD,
                Material.SPRUCE_LOG, Material.SPRUCE_WOOD,
                Material.BIRCH_LOG, Material.BIRCH_WOOD,
                Material.JUNGLE_LOG, Material.JUNGLE_WOOD,
                Material.ACACIA_LOG, Material.ACACIA_WOOD,
                Material.DARK_OAK_LOG, Material.DARK_OAK_WOOD,
                Material.CHERRY_LOG, Material.CHERRY_WOOD,
                Material.MANGROVE_LOG, Material.MANGROVE_WOOD -> {
                    exp = 10.0
                }
                Material.CRIMSON_STEM, Material.CRIMSON_HYPHAE,
                Material.WARPED_STEM, Material.WARPED_HYPHAE -> {
                    exp = 20.0
                }
                else -> {}
            }
            Brimstone.instance.profileHandler.getProfile(e.player.uniqueId)!!.addSkillExp(Skill.LOGGING, exp)
        }
    }

    @EventHandler
    fun onMobDeath(e: EntityDeathEvent) {
        if (!e.isCancelled) {
            if (e.entity.killer != null && e.entity.killer is Player) {
                var exp = 0.0
                when (e.entityType) {
                    EntityType.ENDER_DRAGON -> {
                        exp = 1500.0
                    }
                    EntityType.WITHER -> {
                        exp = 2500.0
                    }
                    EntityType.BLAZE -> {
                        exp = 100.0
                    }
                    EntityType.HUSK, EntityType.DROWNED, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER -> {
                        exp = 25.0
                    }
                    EntityType.ELDER_GUARDIAN -> {
                        exp = 500.0
                    }
                    EntityType.ENDERMITE, EntityType.SILVERFISH -> {
                        exp = 15.0
                    }
                    EntityType.GUARDIAN -> {
                        exp = 75.0
                    }
                    EntityType.WARDEN -> {
                        exp = 5000.0
                    }
                    EntityType.HOGLIN, EntityType.ZOGLIN -> {
                        exp = 55.0
                    }
                    EntityType.RAVAGER -> {
                        exp = 125.0
                    }
                    EntityType.WITHER_SKELETON, EntityType.ENDERMAN -> {
                        exp = 65.0
                    }
                    EntityType.PILLAGER, EntityType.WITCH, EntityType.VINDICATOR, EntityType.EVOKER -> {
                        exp = 85.0
                    }
                    EntityType.SHULKER -> {
                        exp = 60.0
                    }
                    EntityType.PHANTOM, EntityType.GHAST -> {
                        exp = 50.5
                    }
                    EntityType.SKELETON, EntityType.STRAY -> {
                        exp = 35.0
                    }
                    EntityType.CREEPER -> {
                        exp = 30.0
                    }
                    EntityType.MAGMA_CUBE, EntityType.SLIME -> {
                        exp = 100.0
                        // I really respect anyone that tries to grind magma cubes, they are the absolute bane of my existence.
                    }
                    else -> {}
                }
                Brimstone.instance.profileHandler.getProfile(e.entity.killer!!.uniqueId)!!.addSkillExp(Skill.COMBAT, exp)
            }
        }
    }

    @EventHandler
    fun onFarmMine(e: BlockBreakEvent) {
        if (!e.isCancelled) {
            val blockData = e.block.blockData
            var exp = 0.0
            if (blockData is Ageable) {
                if (blockData.maximumAge == blockData.age) {
                    when (e.block.type) {
                        Material.WHEAT -> {
                            exp = 7.5
                        }
                        Material.POTATOES -> {
                            exp = 7.5
                        }
                        Material.CARROTS -> {
                            exp = 7.5
                        }
                        Material.BEETROOTS -> {
                            exp = 7.5
                        }
                        Material.COCOA -> {
                            exp = 15.5
                        }
                        Material.PITCHER_CROP -> {
                            exp = 125.5
                        }
                        else -> {}
                    }
                }
            } else {
                when (e.block.type) {
                    Material.MELON -> {
                        exp = 7.5
                    }
                    Material.PUMPKIN -> {
                        exp = 7.5
                    }
                    Material.BAMBOO -> {
                        exp = 0.2
                    }
                    Material.SUGAR_CANE -> {
                        exp = 0.45
                    }
                    Material.TORCHFLOWER -> {
                        exp = 125.0
                    }

                    else -> {}
                }
            }
            Brimstone.instance.profileHandler.getProfile(e.player.uniqueId)!!.addSkillExp(Skill.FARMING, exp)
        }
    }


    @EventHandler
    fun onOreMine(e: BlockBreakEvent) {
        if (!e.isCancelled) {
            var exp = 0.0
            when (e.block.type) {
                Material.ANCIENT_DEBRIS -> {
                    exp = 200.0
                }
                Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE -> {
                    exp = 125.0
                }
                Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE -> {
                    exp = 75.0
                }
                Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.NETHER_GOLD_ORE -> {
                    exp = 50.0
                }
                Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.NETHER_QUARTZ_ORE -> {
                    exp = 35.5
                }
                Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE -> {
                    exp = 25.5
                }
                Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.COPPER_ORE -> {
                    exp = 12.5
                }
                Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> {
                    exp = 35.5
                }
                Material.STONE, Material.ANDESITE, Material.GRANITE, Material.DIORITE, Material.DEEPSLATE -> {
                    exp = 1.5
                }
                Material.NETHERRACK -> {
                    exp = 0.5
                }
                else -> {}
            }
            Brimstone.instance.profileHandler.getProfile(e.player.uniqueId)!!.addSkillExp(Skill.MINING, exp)
        }
    }
}