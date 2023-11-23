package club.malvaceae.malloy.enchantments.list

import club.malvaceae.malloy.enchantments.EnchantmentWrapperHandler
import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class LumberjackEnchantment : Enchantment(NamespacedKey.minecraft("lumberjack")), Listener {
    override fun translationKey(): String {
        return translationKey()
    }

    override fun getName(): String {
        return "Lumberjack"
    }

    override fun getMaxLevel(): Int {
        return 5
    }

    override fun getStartLevel(): Int {
        return 1
    }

    override fun getItemTarget(): EnchantmentTarget {
        return EnchantmentTarget.TOOL
    }

    override fun isTreasure(): Boolean {
        return true
    }

    override fun isCursed(): Boolean {
        return false
    }

    override fun conflictsWith(other: Enchantment): Boolean {
        return (other == Enchantment.LOOT_BONUS_BLOCKS)
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return (
                item.type == Material.NETHERITE_AXE ||
                item.type == Material.DIAMOND_AXE ||
                item.type == Material.GOLDEN_AXE ||
                item.type == Material.IRON_AXE ||
                item.type == Material.STONE_AXE ||
                item.type == Material.WOODEN_AXE
                )
    }

    override fun displayName(level: Int): Component {
        return MiniMessage.miniMessage().deserialize("<gray>Lumberjack ${EnchantmentWrapperHandler.getRomanNumeral(level)}</gray>").decoration(TextDecoration.ITALIC, false)
    }

    override fun isTradeable(): Boolean {
        return true
    }

    override fun isDiscoverable(): Boolean {
        return true
    }

    override fun getMinModifiedCost(level: Int): Int {
        return 65
    }

    override fun getMaxModifiedCost(level: Int): Int {
        return 50
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.COMMON
    }

    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float {
        return 0f
    }

    override fun getActiveSlots(): MutableSet<EquipmentSlot> {
        return mutableSetOf(EquipmentSlot.HAND)
    }

    private fun timberTree(loc: Location, material: Material, player: Player, modifier: Int) {
        for (x in loc.blockX - (modifier * -1)..loc.blockX + modifier) {
            for (y in loc.blockY - (modifier * -1)..loc.blockY + modifier) {
                for (z in loc.blockZ - (modifier * -1)..loc.blockZ + modifier) {
                    val newLoc = Location(loc.world, x.toDouble(), y.toDouble(), z.toDouble())
                    if (loc.world.getBlockAt(x, y, z).type == material) {
                        loc.world.getBlockAt(x, y, z).breakNaturally()
                        loc.world.playSound(newLoc, Sound.BLOCK_WOOD_BREAK, 1f, 1f)
                        player.breakBlock(loc.world.getBlockAt(x, y, z))
                        timberTree(newLoc, material, player, modifier)
                    }
                }
            }
        }
    }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        if (e.player.inventory.itemInMainHand == null) return
        if (!e.player.equipment.itemInMainHand.hasItemMeta()) return
        if (!e.player.inventory.itemInMainHand.containsEnchantment(LumberjackEnchantment())) return
        val logs = listOf(
            Material.OAK_LOG,
            Material.BIRCH_LOG,
            Material.DARK_OAK_LOG,
            Material.ACACIA_LOG,
            Material.CHERRY_LOG,
            Material.JUNGLE_LOG,
            Material.MANGROVE_LOG,
            Material.SPRUCE_LOG,
            Material.CRIMSON_STEM,
            Material.WARPED_STEM
        )
        if (logs.contains(e.block.type)) {
            timberTree(e.block.location, e.block.type, e.player, e.player.equipment.itemInMainHand.getEnchantmentLevel(LumberjackEnchantment()))
        }
    }
}