package club.piglin.brimstone.commands.menus

import club.piglin.brimstone.features.ShopCategory
import club.piglin.brimstone.features.ShopHandler
import club.piglin.brimstone.menus.Button
import club.piglin.brimstone.menus.Menu
import club.piglin.brimstone.menus.pagination.PaginatedMenu
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

                    override fun getDescription(var1: Player?): List<String> {
                        return listOf(
                            ""
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