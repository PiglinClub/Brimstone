package club.piglin.brimstone.menus.buttons

import club.piglin.brimstone.menus.Button
import club.piglin.brimstone.menus.Menu
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
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

    override fun getDescription(player: Player?): List<Component> {
        val lines: MutableList<Component> = ArrayList()
        if (back != null) {
            lines.add(MiniMessage.miniMessage().deserialize("Click here to return to").decoration(TextDecoration.ITALIC, false))
            lines.add(MiniMessage.miniMessage().deserialize("the previous menu.").decoration(TextDecoration.ITALIC, false))
        } else {
            lines.add(MiniMessage.miniMessage().deserialize("Click here to").decoration(TextDecoration.ITALIC, false))
            lines.add(MiniMessage.miniMessage().deserialize("close this menu.").decoration(TextDecoration.ITALIC, false))
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


