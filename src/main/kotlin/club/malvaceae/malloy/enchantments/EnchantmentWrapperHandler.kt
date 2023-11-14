package club.malvaceae.malloy.enchantments

import club.malvaceae.malloy.Malloy
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import java.lang.reflect.Field


class EnchantmentWrapperHandler {
    companion object {
        val instance = EnchantmentWrapperHandler()
    }

    val MOLTEN = EnchantmentWrapper(NamespacedKey.minecraft("molten"), "Molten", 1)

    fun getEnchants(): List<Enchantment> {
        return listOf(MOLTEN)
    }

    fun checkStoredCustomEnchants(item: ItemStack): HashMap<Enchantment, Int> {
        if (!item.hasItemMeta()) return hashMapOf()
        if ((item.itemMeta as EnchantmentStorageMeta).storedEnchants.isEmpty()) return hashMapOf()
        val final = hashMapOf<Enchantment, Int>()
        for (enchant in (item.itemMeta as EnchantmentStorageMeta).storedEnchants) {
            if (getEnchants().contains(enchant.key)) {
                final[enchant.key as Enchantment] = enchant.value
            }
        }
        return final
    }

    fun checkCustomEnchants(item: ItemStack): HashMap<Enchantment, Int> {
        if (!item.hasItemMeta()) return hashMapOf()
        if ((item).enchantments.isEmpty()) return hashMapOf()
        val final = hashMapOf<Enchantment, Int>()
        for (enchant in (item).enchantments) {
            if (getEnchants().contains(enchant.key)) {
                final[enchant.key as Enchantment] = enchant.value
            }
        }
        return final
    }

    fun register() {
        val list = getEnchants()
        val registered: Boolean = Enchantment.values().toList().containsAll(list)
        if (!registered) {
            Bukkit.getServer().pluginManager.registerEvents(EnchantmentListener(), Malloy.instance)
            for (ench in list) {
                registerEnchantment(ench)
            }
        }
    }

    fun registerEnchantment(ench: Enchantment) {
        var registered = true
        try {
            val accept: Field = Enchantment::class.java.getDeclaredField("acceptingNew")
            accept.setAccessible(true)
            accept.set(null, true)
            Enchantment.registerEnchantment(ench)
        } catch (e: Exception) {
            registered = false
            e.printStackTrace()
        }
        if (registered) {
            Malloy.log.info("[Enchantments] ${ench.name.uppercase()} has been registered")
        }
    }
}