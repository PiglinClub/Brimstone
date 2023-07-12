package club.piglin.brimstone.features

import org.bukkit.Material

enum class ShopCategory {
    BUILDING_BLOCKS,
    COLOR_BLOCKS,
    MINERALS,
    FOOD,
    FARMING,
    MOB_DROPS,
    WEAPONS_AND_TOOLS,
    REDSTONE,
    DECORATION,
    DYES
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

    }
}