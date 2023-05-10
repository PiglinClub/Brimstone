package club.piglin.brimstone.commands.menus

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.menus.Button
import club.piglin.brimstone.menus.Menu
import org.bukkit.entity.Player

class TownyGUI : Menu() {
    override fun getTitle(player: Player): String {
        return "Towny Menu"
    }

    override fun getButtons(var1: Player): Map<Int, Button?> {
        val buttons = HashMap<Int, Button>()
        val profile = Brimstone.instance.profileHandler.getProfile(var1.uniqueId)

        return buttons
    }

}