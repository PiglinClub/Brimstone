package club.piglin.brimstone.listeners

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.database.profiles.Skill
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import kotlin.random.Random

class SkillListener : Listener {
    @EventHandler
    fun onOreMine(e: BlockBreakEvent) {
        if (!e.isCancelled) {
            var exp = 0.0
            when (e.block.type) {
                Material.ANCIENT_DEBRIS -> {
                    exp = Random.nextDouble(25.0, 35.0)
                }
                Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE -> {
                    exp = Random.nextDouble(15.0, 25.0)
                }
                Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE -> {
                    exp = Random.nextDouble(10.0, 15.0)
                }
                Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.NETHER_GOLD_ORE -> {
                    exp = Random.nextDouble(15.0, 20.0)
                }
                Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.NETHER_QUARTZ_ORE -> {
                    exp = Random.nextDouble(5.0, 15.0)
                }
                Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE -> {
                    exp = Random.nextDouble(5.0, 10.0)
                }
                Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.COPPER_ORE -> {
                    exp = Random.nextDouble(5.0, 7.0)
                }
                Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> {
                    exp = Random.nextDouble(10.0, 15.0)
                }
                Material.STONE, Material.ANDESITE, Material.GRANITE, Material.DIORITE, Material.DEEPSLATE -> {
                    exp = Random.nextDouble(0.5, 1.5)
                }
                else -> {}
            }
            Brimstone.instance.profileHandler.getProfile(e.player.uniqueId)!!.addSkillExp(Skill.MINING, exp)
        }
    }
}