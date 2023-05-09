package club.piglin.brimstone.menus.buttons

import club.piglin.brimstone.menus.Button
import club.piglin.brimstone.menus.Menu
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.beans.ConstructorProperties


class BackButton @ConstructorProperties(value = ["back"]) constructor(back: Menu?) : Button() {
    private val back: Menu?
    override fun getMaterial(var1: Player?): Material {
        return Material.REDSTONE
    }

    override fun getDamageValue(player: Player?): Byte {
        return 0
    }

    override fun getName(player: Player?): String {
        return "\u00a7c\u00a7l" + if (back == null) "Close" else "Back"
    }

    override fun getDescription(player: Player?): List<String> {
        val lines: MutableList<String> = ArrayList()
        if (back != null) {
            lines.add("\u00a7c" + "Click here to return to")
            lines.add("\u00a7c" + "the previous menu.")
        } else {
            lines.add("\u00a7c" + "Click here to")
            lines.add("\u00a7c" + "close this menu.")
        }
        return lines
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
        Button.playNeutral(player)
        if (back == null) {
            player.closeInventory()
        } else {
            back.openMenu(player)
        }
    }

    init {
        this.back = back
    }
}


