package club.malvaceae.malloy.enchantments

import club.malvaceae.malloy.Malloy
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import java.lang.reflect.Field


class EnchantmentWrapperHandler {
    companion object {
        val instance = EnchantmentWrapperHandler()
    }

    val MOLTEN = EnchantmentWrapper(NamespacedKey.minecraft("molten"), "Molten", 1)

    fun register() {
        val list = listOf(MOLTEN)
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