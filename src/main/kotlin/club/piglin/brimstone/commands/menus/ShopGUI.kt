package club.piglin.brimstone.commands.menus

import club.piglin.brimstone.features.ShopCategory
import club.piglin.brimstone.features.ShopHandler
import club.piglin.brimstone.menus.Button
import club.piglin.brimstone.menus.Menu
import club.piglin.brimstone.menus.pagination.PaginatedMenu
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.concurrent.atomic.AtomicInteger

class ShopGUI : Menu() {
    override fun getButtons(player: Player): Map<Int, Button?> {
        val buttons = HashMap<Int, Button>()
        return buttons
    }
}

class ShopPage(val category: ShopCategory) : PaginatedMenu() {
    override fun getPrePaginatedTitle(var1: Player?): String {
        return "Shop: ${category.toString().replaceFirstChar { it.uppercase() }}"
    }

    override fun getAllPagesButtons(var1: Player?): Map<Int, Button> {
        if (var1 != null) {
            val buttons = hashMapOf<Int, Button>()
            val count = AtomicInteger(0)
            val entries = ShopHandler.getEntriesInCategory(category)
            for (entry in entries) {
                buttons[count.get()] = object : Button() {
                    override fun getMaterial(var1: Player?): Material {
                        return entry.material
                    }

                    override fun getName(var1: Player?): String {
                        return "${ChatColor.AQUA}${entry.material.name}"
                    }

                    override fun getDescription(var1: Player?): List<Component> {
                        return listOf(
                            MiniMessage.miniMessage().deserialize("<st><dark_gray>                                  </dark_gray></st>").decoration(
                                TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize(" "),
                            MiniMessage.miniMessage().deserialize(" <white>▐</white> <color:#33ff05>PURCHASE PRICE:</color> <color:#ffd417>${entry.pricePerOne}g</color><white></white>").decoration(
                                TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize(" <white>▐</white> <color:#33ff05>SELLING PRICE:</color> <color:#ffd417>${entry.sellPricePerOne}g</color><white></white>").decoration(
                                TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize(" "),
                            MiniMessage.miniMessage().deserialize(" <color:#33ff05>*</color> <color:#26ff00>Left click to <color:#159c00>buy</color>!</color>").decoration(
                                TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize(" <color:#33ff05>*</color> <color:#fbff29>Right click to <color:#d6d923>sell</color>!</color>").decoration(
                                TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize(" <color:#33ff05>*</color> <color:#d6241e>Middle click to <color:#911914>sell all</color>!</color>").decoration(
                                TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize("  "),
                            MiniMessage.miniMessage().deserialize("<st><dark_gray>                                  </dark_gray></st>").decoration(
                                TextDecoration.ITALIC, false),
                        )
                    }
                }
                count.getAndIncrement()
            }
            return buttons
        }
        return hashMapOf()
    }
}