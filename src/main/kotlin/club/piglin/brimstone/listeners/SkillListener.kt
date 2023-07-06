package club.piglin.brimstone.listeners

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.database.profiles.Skill
import org.bukkit.Material
import org.bukkit.block.data.Ageable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class SkillListener : Listener {
    @EventHandler
    fun onFarmMine(e: BlockBreakEvent) {
        if (!e.isCancelled) {
            val blockData = e.block.blockData
            var exp = 0.0
            if (blockData is Ageable) {
                if (blockData.maximumAge == blockData.age) {
                    when (e.block.type) {
                        Material.WHEAT -> {
                            exp = 4.0
                        }
                        Material.POTATOES -> {
                            exp = 4.0
                        }
                        Material.CARROTS -> {
                            exp = 4.0
                        }
                        Material.BEETROOTS -> {
                            exp = 4.0
                        }
                        Material.COCOA -> {
                            exp = 5.0
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
                    Material.PITCHER_CROP -> {
                        exp = 5.5
                    }
                    Material.PITCHER_PLANT -> {
                        exp = 5.5
                    }
                    Material.TORCHFLOWER_CROP -> {
                        exp = 5.5
                    }
                    Material.TORCHFLOWER -> {
                        exp = 5.5
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
                    exp = 35.0
                }
                Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE -> {
                    exp = 25.0
                }
                Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE -> {
                    exp = 15.0
                }
                Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.NETHER_GOLD_ORE -> {
                    exp = 15.0
                }
                Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.NETHER_QUARTZ_ORE -> {
                    exp = 12.5
                }
                Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE -> {
                    exp = 7.5
                }
                Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.COPPER_ORE -> {
                    exp = 1.5
                }
                Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> {
                    exp = 2.5
                }
                Material.STONE, Material.ANDESITE, Material.GRANITE, Material.DIORITE, Material.DEEPSLATE -> {
                    exp = 0.5
                }
                else -> {}
            }
            Brimstone.instance.profileHandler.getProfile(e.player.uniqueId)!!.addSkillExp(Skill.MINING, exp)
        }
    }
}