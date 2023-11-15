package club.malvaceae.malloy.enchantments.list

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.enchantments.EnchantmentWrapperHandler
import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.net.http.WebSocket.Listener

class ExtractionEnchantment : Enchantment(NamespacedKey.minecraft("extraction")), Listener {
    /**
     * Gets the translation key.
     *
     * @return the translation key
     * @since 4.8.0
     */
    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        if (e.player.inventory.itemInMainHand == null) return
        if (!e.player.equipment.itemInMainHand.hasItemMeta()) return
        if (!e.player.equipment.itemInMainHand.containsEnchantment(ExtractionEnchantment())) return
        if (!e.player.isSneaking) return
        val level = e.player.equipment.itemInMainHand.getEnchantmentLevel(ExtractionEnchantment())
        val blocks = listOf(
            Material.ANCIENT_DEBRIS,
            Material.DIAMOND_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.LAPIS_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.GOLD_ORE,
            Material.NETHER_GOLD_ORE,
            Material.NETHER_QUARTZ_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.COPPER_ORE,
            Material.COAL_ORE,
            Material.DEEPSLATE_COAL_ORE,
            Material.DEEPSLATE_REDSTONE_ORE,
            Material.REDSTONE_ORE
        )
        if (blocks.contains(e.block.type)) {
            veinMine(e.block.location, e.block.type, e.player, level)
        }
    }

    private fun veinMine(loc: Location, material: Material, player: Player, level: Int) {
        object : BukkitRunnable() {
            override fun run() {
                for (x in (1 - level) - loc.blockX - (1 - level)..loc.blockX + (1 + level)) {
                    for (y in (1 - level) - loc.blockY - (1 - level)..loc.blockY + (1 + level)) {
                        for (z in (1 - level) - loc.blockZ - (1 - level)..loc.blockZ + (1 + level)) {
                            val block = loc.world.getBlockAt(x, y, z)
                            if (block.type == material) {
                                veinMine(block.location, material, player, level)
                                player.breakBlock(block)
                            }
                        }
                    }
                }
            }
        }.runTaskLater(Malloy.instance, 2)
    }

    override fun translationKey(): String {
        return translationKey()
    }

    /**
     * Gets the unique name of this enchantment
     *
     * @return Unique name
     */
    override fun getName(): String {
        return "Extraction"
    }

    /**
     * Gets the maximum level that this Enchantment may become.
     *
     * @return Maximum level of the Enchantment
     */
    override fun getMaxLevel(): Int {
        return 5
    }

    /**
     * Gets the level that this Enchantment should start at
     *
     * @return Starting level of the Enchantment
     */
    override fun getStartLevel(): Int {
        return 1
    }

    /**
     * Gets the type of [ItemStack] that may fit this Enchantment.
     *
     * @return Target type of the Enchantment
     */
    override fun getItemTarget(): EnchantmentTarget {
        return EnchantmentTarget.TOOL
    }

    /**
     * Checks if this enchantment is a treasure enchantment.
     * <br></br>
     * Treasure enchantments can only be received via looting, trading, or
     * fishing.
     *
     * @return true if the enchantment is a treasure enchantment
     */
    override fun isTreasure(): Boolean {
        return true
    }

    /**
     * Checks if this enchantment is a cursed enchantment
     * <br></br>
     * Cursed enchantments are found the same way treasure enchantments are
     *
     * @return true if the enchantment is cursed
     */
    override fun isCursed(): Boolean {
        return false
    }

    /**
     * Check if this enchantment conflicts with another enchantment.
     *
     * @param other The enchantment to check against
     * @return True if there is a conflict.
     */
    override fun conflictsWith(other: Enchantment): Boolean {
        return (other == Enchantment.LOOT_BONUS_BLOCKS || other == MoltenEnchantment())
    }

    /**
     * Checks if this Enchantment may be applied to the given [ ].
     *
     *
     * This does not check if it conflicts with any enchantments already
     * applied to the item.
     *
     * @param item Item to test
     * @return True if the enchantment may be applied, otherwise False
     */
    override fun canEnchantItem(item: ItemStack): Boolean {
        return (
                item.type == Material.NETHERITE_PICKAXE ||
                        item.type == Material.DIAMOND_PICKAXE ||
                        item.type == Material.GOLDEN_PICKAXE ||
                        item.type == Material.IRON_PICKAXE ||
                        item.type == Material.STONE_PICKAXE ||
                        item.type == Material.WOODEN_PICKAXE
                )
    }

    /**
     * Get the name of the enchantment with its applied level.
     *
     *
     * If the given `level` is either less than the [.getStartLevel] or greater than the [.getMaxLevel],
     * the level may not be shown in the numeral format one may otherwise expect.
     *
     *
     * @param level the level of the enchantment to show
     * @return the name of the enchantment with `level` applied
     */
    override fun displayName(level: Int): Component {
        return MiniMessage.miniMessage().deserialize("<gray>Extraction ${EnchantmentWrapperHandler.getRomanNumeral(level)}</gray>").decoration(
            TextDecoration.ITALIC, false)
    }

    /**
     * Checks if this enchantment can be found in villager trades.
     *
     * @return true if the enchantment can be found in trades
     */
    override fun isTradeable(): Boolean {
        return true
    }

    /**
     * Checks if this enchantment can be found in an enchanting table
     * or use to enchant items generated by loot tables.
     *
     * @return true if the enchantment can be found in a table or by loot tables
     */
    override fun isDiscoverable(): Boolean {
        return true
    }

    /**
     * Gets the minimum modified cost of this enchantment at a specific level.
     *
     *
     * Note this is not the number of experience levels needed, and does not directly translate to the levels shown in an enchanting table.
     * This value is used in combination with factors such as tool enchantability to determine a final cost.
     * See [https://minecraft.wiki/w/Enchanting/Levels](https://minecraft.wiki/w/Enchanting/Levels) for more information.
     *
     * @param level The level of the enchantment
     * @return The modified cost of this enchantment
     */
    override fun getMinModifiedCost(level: Int): Int {
        return 45
    }

    /**
     * Gets the maximum modified cost of this enchantment at a specific level.
     *
     *
     * Note this is not the number of experience levels needed, and does not directly translate to the levels shown in an enchanting table.
     * This value is used in combination with factors such as tool enchantability to determine a final cost.
     * See [https://minecraft.wiki/w/Enchanting/Levels](https://minecraft.wiki/w/Enchanting/Levels) for more information.
     *
     * @param level The level of the enchantment
     * @return The modified cost of this enchantment
     */
    override fun getMaxModifiedCost(level: Int): Int {
        return 65
    }

    /**
     * Gets the rarity of this enchantment.
     *
     * @return the rarity
     */
    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.VERY_RARE
    }

    /**
     * Gets the damage increase as a result of the level and entity category specified
     *
     * @param level the level of enchantment
     * @param entityCategory the category of entity
     * @return the damage increase
     */
    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float {
        return 0f
    }

    /**
     * Gets the equipment slots where this enchantment is considered "active".
     *
     * @return the equipment slots
     */
    override fun getActiveSlots(): MutableSet<EquipmentSlot> {
        return mutableSetOf(EquipmentSlot.HAND)
    }

}