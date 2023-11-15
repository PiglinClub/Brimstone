package club.malvaceae.malloy.enchantments

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

class EnchantmentListener : Listener {

    @EventHandler
    fun onAnvilCombine(e: PrepareAnvilEvent) {
        if (e.inventory.secondItem == null || e.inventory.firstItem == null) return
        if (e.inventory.secondItem!!.type != Material.ENCHANTED_BOOK) return
        if (EnchantmentWrapperHandler.instance.checkStoredCustomEnchants(e.inventory.secondItem!!).isEmpty()) return
        for (enchant in EnchantmentWrapperHandler.instance.checkStoredCustomEnchants(e.inventory.secondItem!!).keys) {
            if (!enchant.canEnchantItem(e.inventory.firstItem!!)) return
            if (e.inventory.firstItem!!.enchantments.isNotEmpty()) {
                for (enchant2 in e.inventory.firstItem!!.enchantments.keys) {
                    if (enchant.conflictsWith(enchant2)) {
                        e.result = ItemStack(Material.AIR)
                        return
                    }
                }
            }
        }
        val temp = e.inventory.firstItem!!.clone()
        val stored = (e.inventory.secondItem!!.itemMeta as EnchantmentStorageMeta).storedEnchants
        for ((enchant, level) in stored.entries) {
            if (temp.containsEnchantment(enchant) && temp.enchantments[enchant] == level && enchant.maxLevel > level) {
                temp.removeEnchantment(enchant)
                temp.addUnsafeEnchantment(enchant, level + 1)
            } else {
                temp.addUnsafeEnchantment(enchant, level)
            }
        }
        for ((enchant, level) in temp.enchantments) {
            val lore = arrayListOf<Component>()
            if (temp.lore() != null) temp.lore()
            lore.add(enchant.displayName(level))
            temp.lore(lore)
        }
        e.inventory.repairCostAmount = 1
        e.inventory.repairCost = 1
        e.result = temp
    }
}