package club.piglin.brimstone.menus.pagination

import club.piglin.brimstone.menus.Button
import club.piglin.brimstone.menus.Menu
import org.bukkit.entity.Player


abstract class PaginatedMenu : Menu() {
    var page = 1
        private set

    override fun getTitle(player: Player?): String {
        return getPrePaginatedTitle(player) + " - " + page + "/" + getPages(player)
    }

    fun modPage(player: Player?, mod: Int) {
        page += mod
        buttons.clear()
        openMenu(player!!)
    }

    fun getPages(player: Player?): Int {
        val buttonAmount = getAllPagesButtons(player).size
        return if (buttonAmount == 0) {
            1
        } else Math.ceil(buttonAmount.toDouble() / getMaxItemsPerPage(player).toDouble()).toInt()
    }

    override fun getButtons(player: Player?): MutableMap<Int, Button> {
        val minIndex = ((page - 1).toDouble() * getMaxItemsPerPage(player).toDouble()).toInt()
        val maxIndex = (page.toDouble() * getMaxItemsPerPage(player).toDouble()).toInt()
        val buttons = HashMap<Int, Button>()
        buttons[0] = PageButton(-1, this)
        buttons[8] = PageButton(1, this)
        for ((ind, value) in getAllPagesButtons(player)) {
            if (ind < minIndex || ind >= maxIndex) continue
            buttons[ind - ((getMaxItemsPerPage(player).toDouble() * (page - 1).toDouble()).toInt() - 9)] =
                value
        }
        val global = getGlobalButtons(player)
        if (global != null) {
            for ((key, value) in global) {
                buttons[key] = value
            }
        }
        return buttons
    }

    fun getMaxItemsPerPage(player: Player?): Int {
        return 18
    }

    fun getGlobalButtons(player: Player?): Map<Int, Button>? {
        return null
    }

    abstract fun getPrePaginatedTitle(var1: Player?): String
    abstract fun getAllPagesButtons(var1: Player?): Map<Int, Button>
}

