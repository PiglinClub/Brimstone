package club.malvaceae.malloy.enchantments

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.enchantments.list.ExtractionEnchantment
import club.malvaceae.malloy.enchantments.list.LumberjackEnchantment
import club.malvaceae.malloy.enchantments.list.MoltenEnchantment
import org.bukkit.Bukkit
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import java.lang.reflect.Field


class EnchantmentWrapperHandler {
    companion object {
        val instance = EnchantmentWrapperHandler()

        fun getRomanNumeral(number: Int): String {
            when (number) {
                1 -> {
                    return "I"
                }
                2 -> {
                    return "II"
                }
                3 -> {
                    return "III"
                }
                4 -> {
                    return "IV"
                }
                5 -> {
                    return "V"
                }
                6 -> {
                    return "VI"
                }
                7 -> {
                    return "VII"
                }
                8 -> {
                    return "VIII"
                }
                9 -> {
                    return "IX"
                }
                10 -> {
                    return "X"
                }
                else -> {
                    return "???"
                }
            }
        }
    }

    fun getEnchants(): List<Enchantment> {
        return listOf(
            MoltenEnchantment(),
            ExtractionEnchantment(),
            LumberjackEnchantment()
        )
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
                if (ench is Listener) {
                    Bukkit.getServer().pluginManager.registerEvents(ench, Malloy.instance)
                }
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