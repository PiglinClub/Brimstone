package club.piglin.brimstone.features

import org.bukkit.Material

enum class ShopCategory(val material: Material, val displayName: String) {
    BUILDING_BLOCKS(Material.BRICKS, "Building Blocks"),
    COLOR_BLOCKS(Material.LIME_WOOL, "Colored Blocks"),
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
    val sellPricePerOne: Double,
    val sellable: Boolean = true,
    val buyable: Boolean = true
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

        init {
            addEntry(Material.GRASS_BLOCK, ShopCategory.BUILDING_BLOCKS, 2.0, 0.5)
            addEntry(Material.DIRT, ShopCategory.BUILDING_BLOCKS, 0.75, 0.1)
            addEntry(Material.COARSE_DIRT, ShopCategory.BUILDING_BLOCKS, 1.5, 0.25)
            addEntry(Material.MUD, ShopCategory.BUILDING_BLOCKS, 2.5, 0.5)
            addEntry(Material.MOSS_BLOCK, ShopCategory.BUILDING_BLOCKS, 7.5, 2.5)
            addEntry(Material.COBBLESTONE, ShopCategory.BUILDING_BLOCKS, 0.75, 0.1)
            addEntry(Material.GRAVEL, ShopCategory.BUILDING_BLOCKS, 2.0, 0.5)
            addEntry(Material.MOSSY_COBBLESTONE, ShopCategory.BUILDING_BLOCKS, 2.0, 0.5)
            addEntry(Material.STONE, ShopCategory.BUILDING_BLOCKS, 0.75, 0.1)
            addEntry(Material.GRANITE, ShopCategory.BUILDING_BLOCKS, 2.0, 0.5)
            addEntry(Material.CALCITE, ShopCategory.BUILDING_BLOCKS, 5.0, 2.5)
            addEntry(Material.DIORITE, ShopCategory.BUILDING_BLOCKS, 1.5, 0.8)
            addEntry(Material.ANDESITE, ShopCategory.BUILDING_BLOCKS, 0.75, 0.1)
            addEntry(Material.OAK_LOG, ShopCategory.BUILDING_BLOCKS, 5.0, 3.0)
            addEntry(Material.BIRCH_LOG, ShopCategory.BUILDING_BLOCKS, 5.0, 3.0)
            addEntry(Material.JUNGLE_LOG, ShopCategory.BUILDING_BLOCKS, 5.0, 3.0)
            addEntry(Material.ACACIA_LOG, ShopCategory.BUILDING_BLOCKS, 5.0, 3.0)
            addEntry(Material.DARK_OAK_LOG, ShopCategory.BUILDING_BLOCKS, 5.0, 3.0)
            addEntry(Material.MANGROVE_LOG, ShopCategory.BUILDING_BLOCKS, 5.0, 3.0)
            addEntry(Material.CHERRY_LOG, ShopCategory.BUILDING_BLOCKS, 5.0, 3.0)
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
            addEntry(Material.DEEPSLATE, ShopCategory.BUILDING_BLOCKS, 0.5, 0.1)
            addEntry(Material.BLACKSTONE, ShopCategory.BUILDING_BLOCKS, 0.5, 0.1)
            addEntry(Material.BASALT, ShopCategory.BUILDING_BLOCKS, 3.5, 0.5)
            addEntry(Material.NETHER_BRICKS, ShopCategory.BUILDING_BLOCKS, 2.5, 1.5)
            addEntry(Material.END_STONE, ShopCategory.BUILDING_BLOCKS, 1.5, 0.5)
            addEntry(Material.PURPUR_BLOCK, ShopCategory.BUILDING_BLOCKS, 8.5, 4.5)
            addEntry(Material.QUARTZ_BLOCK, ShopCategory.BUILDING_BLOCKS, 5.5, 2.5)
            addEntry(Material.CHAIN, ShopCategory.BUILDING_BLOCKS, 20.0, 10.0)
            addEntry(Material.BOOKSHELF, ShopCategory.BUILDING_BLOCKS, 15.0, 5.0)
            addEntry(Material.CHISELED_BOOKSHELF, ShopCategory.BUILDING_BLOCKS, 25.0, 15.0)
            addEntry(Material.SEA_LANTERN, ShopCategory.BUILDING_BLOCKS, 50.0, 35.0)
            addEntry(Material.LANTERN, ShopCategory.BUILDING_BLOCKS, 75.0, 25.0)
            addEntry(Material.GLOWSTONE, ShopCategory.BUILDING_BLOCKS, 25.0, 15.0)
            addEntry(Material.OBSIDIAN, ShopCategory.BUILDING_BLOCKS, 45.0, 15.0)
            addEntry(Material.CRYING_OBSIDIAN, ShopCategory.BUILDING_BLOCKS, 55.0, 25.5)
            addEntry(Material.PRISMARINE_BRICKS, ShopCategory.BUILDING_BLOCKS, 20.0, 10.5)
            addEntry(Material.DARK_PRISMARINE, ShopCategory.BUILDING_BLOCKS, 15.0, 5.5)
            addEntry(Material.PRISMARINE, ShopCategory.BUILDING_BLOCKS, 10.0, 5.5)
            addEntry(Material.PACKED_MUD, ShopCategory.BUILDING_BLOCKS, 2.5, 1.0)
            addEntry(Material.MUD_BRICKS, ShopCategory.BUILDING_BLOCKS, 7.5, 2.5)
            addEntry(Material.BAMBOO_BLOCK, ShopCategory.BUILDING_BLOCKS, 15.5, 7.5)
            addEntry(Material.ICE, ShopCategory.BUILDING_BLOCKS, 7.5, 2.5)
            addEntry(Material.PACKED_ICE, ShopCategory.BUILDING_BLOCKS, 15.5, 7.5)

            addEntry(Material.WHITE_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.LIGHT_GRAY_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.GRAY_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.BLACK_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.BROWN_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.RED_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.ORANGE_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.YELLOW_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.LIME_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.GREEN_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.CYAN_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.LIGHT_BLUE_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.BLUE_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.PURPLE_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.MAGENTA_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)
            addEntry(Material.PINK_WOOL, ShopCategory.COLOR_BLOCKS, 2.0, 0.5)


            addEntry(Material.WHITE_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.LIGHT_GRAY_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.GRAY_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.BLACK_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.BROWN_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.RED_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.ORANGE_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.YELLOW_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.LIME_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.GREEN_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.CYAN_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.LIGHT_BLUE_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.BLUE_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.PURPLE_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.MAGENTA_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)
            addEntry(Material.PINK_CARPET, ShopCategory.COLOR_BLOCKS, 4.0, 0.5)

            addEntry(Material.WHITE_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.LIGHT_GRAY_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.GRAY_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.BLACK_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.BROWN_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.RED_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.ORANGE_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.YELLOW_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.LIME_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.GREEN_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.CYAN_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.LIGHT_BLUE_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.BLUE_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.PURPLE_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.MAGENTA_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)
            addEntry(Material.PINK_STAINED_GLASS, ShopCategory.COLOR_BLOCKS, 15.0, 7.5)

            addEntry(Material.WHITE_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.LIGHT_GRAY_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.GRAY_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.BLACK_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.BROWN_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.RED_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.ORANGE_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.YELLOW_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.LIME_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.GREEN_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.CYAN_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.LIGHT_BLUE_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.BLUE_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.PURPLE_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.MAGENTA_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)
            addEntry(Material.PINK_STAINED_GLASS_PANE, ShopCategory.COLOR_BLOCKS, 7.0, 0.5)

            addEntry(Material.WHITE_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.LIGHT_GRAY_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.GRAY_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.BLACK_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.BROWN_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.RED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.ORANGE_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.YELLOW_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.LIME_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.GREEN_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.CYAN_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.LIGHT_BLUE_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.BLUE_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.PURPLE_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.MAGENTA_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)
            addEntry(Material.PINK_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 5.0, 2.5)

            addEntry(Material.WHITE_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.LIGHT_GRAY_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.GRAY_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.BLACK_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.BROWN_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.RED_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.ORANGE_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.YELLOW_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.LIME_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.GREEN_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.CYAN_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.BLUE_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.PURPLE_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.MAGENTA_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)
            addEntry(Material.PINK_GLAZED_TERRACOTTA, ShopCategory.COLOR_BLOCKS, 10.0, 2.5)

            addEntry(Material.WHITE_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.LIGHT_GRAY_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.GRAY_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.BLACK_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.BROWN_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.RED_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.ORANGE_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.YELLOW_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.LIME_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.GREEN_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.CYAN_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.LIGHT_BLUE_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.BLUE_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.PURPLE_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.MAGENTA_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)
            addEntry(Material.PINK_CONCRETE, ShopCategory.COLOR_BLOCKS, 15.0, 5.5)

            addEntry(Material.WHITE_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.LIGHT_GRAY_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.GRAY_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.BLACK_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.BROWN_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.RED_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.ORANGE_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.YELLOW_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.LIME_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.GREEN_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.CYAN_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.LIGHT_BLUE_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.BLUE_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.PURPLE_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.MAGENTA_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)
            addEntry(Material.PINK_CONCRETE_POWDER, ShopCategory.COLOR_BLOCKS, 12.0, 5.5)

            addEntry(Material.WHITE_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.LIGHT_GRAY_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.GRAY_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.BLACK_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.BROWN_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.RED_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.ORANGE_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.YELLOW_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.LIME_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.GREEN_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.CYAN_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.LIGHT_BLUE_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.BLUE_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.PURPLE_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.MAGENTA_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)
            addEntry(Material.PINK_CANDLE, ShopCategory.COLOR_BLOCKS, 50.0, 25.5)

            addEntry(Material.WHITE_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.LIGHT_GRAY_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.GRAY_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.BLACK_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.BROWN_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.RED_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.ORANGE_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.YELLOW_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.LIME_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.GREEN_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.CYAN_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.LIGHT_BLUE_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.BLUE_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.PURPLE_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.MAGENTA_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)
            addEntry(Material.PINK_BANNER, ShopCategory.COLOR_BLOCKS, 10.0, 5.5)

            addEntry(Material.IRON_INGOT, ShopCategory.MINERALS, 50.0, 25.5)
            addEntry(Material.GOLD_INGOT, ShopCategory.MINERALS, 100.0, 50.5)
            addEntry(Material.REDSTONE, ShopCategory.MINERALS, 25.0, 10.5)
            addEntry(Material.NETHERITE_INGOT, ShopCategory.MINERALS, 650.0, 350.0)
            addEntry(Material.DIAMOND, ShopCategory.MINERALS, 350.0, 150.0)
            addEntry(Material.LAPIS_LAZULI, ShopCategory.MINERALS, 25.0, 10.0)
            addEntry(Material.COAL, ShopCategory.MINERALS, 25.0, 15.0)
            addEntry(Material.CHARCOAL, ShopCategory.MINERALS, 15.0, 7.5)
            addEntry(Material.EMERALD, ShopCategory.MINERALS, 150.0, 75.0)
            addEntry(Material.COPPER_INGOT, ShopCategory.MINERALS, 25.0, 10.0)
            addEntry(Material.FLINT, ShopCategory.MINERALS, 5.0, 2.5)


        }
    }
}