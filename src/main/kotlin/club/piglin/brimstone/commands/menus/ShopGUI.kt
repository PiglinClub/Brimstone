package club.piglin.brimstone.commands.menus

import club.piglin.brimstone.menus.Button
import club.piglin.brimstone.menus.Menu
import org.bukkit.entity.Player

class ShopGUI : Menu() {
    override fun getButtons(player: Player): Map<Int, Button?> {
        val buttons = HashMap<Int, Button>()
        return buttons
    }
}