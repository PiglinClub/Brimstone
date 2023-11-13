package club.malvaceae.malloy.enchantments

import org.bukkit.Material
import org.bukkit.entity.ExperienceOrb
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class EnchantmentListener : Listener {
    @EventHandler
    fun onMoltenBlockBreak(e: BlockBreakEvent) {
        if (e.player.inventory.itemInMainHand == null) return
        if (!e.player.equipment.itemInMainHand.hasItemMeta()) return
        if (!e.player.inventory.itemInMainHand.containsEnchantment(EnchantmentWrapperHandler.instance.MOLTEN)) return
        when (e.block.type) {
            Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.IRON_INGOT))
            }
            else -> {}
        }
    }
}