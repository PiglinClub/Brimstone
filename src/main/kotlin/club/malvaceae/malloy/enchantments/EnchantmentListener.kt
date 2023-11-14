package club.malvaceae.malloy.enchantments

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.entity.ExperienceOrb
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
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
                e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.IRON_INGOT))
            }
            Material.RAW_IRON_BLOCK -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.IRON_BLOCK))
            }
            Material.RAW_GOLD_BLOCK -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.GOLD_BLOCK))
            }
            Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.COPPER_INGOT))
            }
            Material.ANCIENT_DEBRIS -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 10
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.NETHERITE_SCRAP))
            }
            Material.GOLD_ORE, Material.NETHER_GOLD_ORE -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.GOLD_ORE))
            }
            Material.CACTUS -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.GREEN_DYE))
            }
            Material.SEA_PICKLE -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.LIME_DYE))
            }
            Material.NETHERRACK -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.NETHER_BRICKS))
            }
            Material.CLAY -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.TERRACOTTA))
            }
            Material.COBBLESTONE -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.STONE))
            }
            Material.STONE -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.SMOOTH_STONE))
            }
            Material.STONE_BRICKS -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.CRACKED_STONE_BRICKS))
            }
            Material.COBBLED_DEEPSLATE -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.DEEPSLATE))
            }
            Material.DEEPSLATE_BRICKS -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.CRACKED_DEEPSLATE_BRICKS))
            }
            Material.SANDSTONE -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.SMOOTH_SANDSTONE))
            }
            Material.RED_SANDSTONE -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.SMOOTH_RED_SANDSTONE))
            }
            Material.SAND, Material.RED_SAND -> {
                e.isCancelled = true
                (e.block.location.world.spawn(e.block.location, ExperienceOrb::class.java) as ExperienceOrb).experience = 1
                e.block.location.world.dropItemNaturally(e.block.location, ItemStack(Material.GLASS))
            }
            else -> {}
        }
    }

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

    @EventHandler
    fun onAnvilCombine(e: PrepareAnvilEvent) {
        if (e.inventory.secondItem == null || e.inventory.firstItem == null) return
        if (e.inventory.secondItem!!.type != Material.ENCHANTED_BOOK) return
        if (EnchantmentWrapperHandler.instance.checkStoredCustomEnchants(e.inventory.secondItem!!).isEmpty()) return
        if (EnchantmentWrapperHandler.instance.checkCustomEnchants(e.inventory.firstItem!!).isNotEmpty()) return
        val temp = e.inventory.firstItem!!.clone()
        for ((enchant, level) in EnchantmentWrapperHandler.instance.checkStoredCustomEnchants(e.inventory.secondItem!!).entries) {
            temp.addUnsafeEnchantment(enchant, level)
            val lore = arrayListOf<Component>()
            if (temp.lore() != null) lore.addAll(temp.lore()!!.toList())
            lore.add(MiniMessage.miniMessage().deserialize("<gray>${enchant.name} ${getRomanNumeral(level)}</gray>").decoration(TextDecoration.ITALIC, false))
            temp.lore(lore)
        }
        e.inventory.repairCostAmount = 1
        e.inventory.repairCost = 1
        e.result = temp
    }
}