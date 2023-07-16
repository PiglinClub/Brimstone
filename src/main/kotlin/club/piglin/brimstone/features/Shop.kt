package club.piglin.brimstone.features

import org.bukkit.Material

enum class ShopCategory(val material: Material, val displayName: String) {
    BUILDING_BLOCKS(Material.BRICKS, "Building Blocks"),
    COLOR_BLOCKS(Material.LIME_WOOL, "Color Blocks"),
    MINERALS(Material.DIAMOND_ORE, "Minerals"),
    FOOD(Material.COOKED_BEEF, "Food"),
    FARMING(Material.WHEAT_SEEDS, "Farming"),
    MOB_DROPS(Material.ROTTEN_FLESH, "Mob Drops"),
    WEAPONS_AND_TOOLS(Material.DIAMOND_PICKAXE, "Weapons & Tools"),
    REDSTONE(Material.REDSTONE, "Redstone"),
    DECORATION(Material.BLUE_BANNER, "Decoration"),
    DYES(Material.ORANGE_DYE, "Dyes")
}

class ShopEntry(
    val material: Material,
    val category: ShopCategory,
    val pricePerOne: Double,
    val sellPricePerOne: Double
) {
    fun getBulkBuyPrice(amount: Int) : Double {
        return pricePerOne * amount
    }

    fun getBulkSellPrice(amount: Int) : Double {
        return sellPricePerOne * amount
    }
}

class ShopHandler {
    companion object {
        val entries = ArrayList<ShopEntry>()

        fun addEntry(material: Material, category: ShopCategory, pricePerOne: Double, sellPricePerOne: Double) {
            entries.add(ShopEntry(
                material, category, pricePerOne, sellPricePerOne
            ))
        }

        fun getEntriesInCategory(category: ShopCategory): List<ShopEntry> {
            val list = arrayListOf<ShopEntry>()
            for (entry in entries) {
                if (entry.category == category) {
                    list.add(entry)
                }
            }
            return list
        }
    }

    init {
        addEntry(Material.GRASS_BLOCK, ShopCategory.BUILDING_BLOCKS, 2.0, 0.5)
        addEntry(Material.DIRT, ShopCategory.BUILDING_BLOCKS, 0.75, 0.1)
        addEntry(Material.COBBLESTONE, ShopCategory.BUILDING_BLOCKS, 0.75, 0.1)
        addEntry(Material.GRAVEL, ShopCategory.BUILDING_BLOCKS, 2.0, 0.5)
        addEntry(Material.MOSSY_COBBLESTONE, ShopCategory.BUILDING_BLOCKS, 2.0, 0.5)
        addEntry(Material.STONE, ShopCategory.BUILDING_BLOCKS, 0.75, 0.1)
        addEntry(Material.GRANITE, ShopCategory.BUILDING_BLOCKS, 2.0, 0.5)
        addEntry(Material.DIORITE, ShopCategory.BUILDING_BLOCKS, 1.5, 0.8)
        addEntry(Material.ANDESITE, ShopCategory.BUILDING_BLOCKS, 0.75, 0.1)
        addEntry(Material.OAK_LOG, ShopCategory.BUILDING_BLOCKS, 5.0, 3.0)
        addEntry(Material.BIRCH_LOG, ShopCategory.BUILDING_BLOCKS, 5.0, 3.0)
        addEntry(Material.JUNGLE_LOG, ShopCategory.BUILDING_BLOCKS, 5.0, 3.0)
        addEntry(Material.ACACIA_LOG, ShopCategory.BUILDING_BLOCKS, 5.0, 3.0)
        addEntry(Material.DARK_OAK_LOG, ShopCategory.BUILDING_BLOCKS, 5.0, 3.0)
        addEntry(Material.SNOW_BLOCK, ShopCategory.BUILDING_BLOCKS, 1.5, 0.01)
        addEntry(Material.ICE, ShopCategory.BUILDING_BLOCKS, 3.0, 0.5)
        addEntry(Material.SAND, ShopCategory.BUILDING_BLOCKS, 3.0, 0.5)
        addEntry(Material.RED_SAND, ShopCategory.BUILDING_BLOCKS, 3.0, 0.5)
        addEntry(Material.RED_SANDSTONE, ShopCategory.BUILDING_BLOCKS, 3.0, 0.5)
        addEntry(Material.SANDSTONE, ShopCategory.BUILDING_BLOCKS, 3.0, 0.5)
        addEntry(Material.GLASS, ShopCategory.BUILDING_BLOCKS, 5.0, 1.0)
        addEntry(Material.CLAY, ShopCategory.BUILDING_BLOCKS, 12.0, 2.0)
        addEntry(Material.TERRACOTTA, ShopCategory.BUILDING_BLOCKS, 15.0, 5.0)
        addEntry(Material.BRICKS, ShopCategory.BUILDING_BLOCKS, 20.0, 8.0)
    }
}