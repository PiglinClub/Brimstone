package club.malvaceae.malloy.menus.pagination

import club.malvaceae.malloy.menus.Button
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.beans.ConstructorProperties


class PageButton @ConstructorProperties(value = ["mod", "menu"]) constructor(
    private val mod: Int,
    private val menu: PaginatedMenu
) :
    Button() {
    override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
        if (clickType == ClickType.RIGHT) {
            ViewAllPagesMenu(menu).openMenu(player)
            playNeutral(player)
        } else if (hasNext(player)) {
            menu.modPage(player, mod)
            Button.playNeutral(player)
        } else {
            Button.playFail(player)
        }
    }

    private fun hasNext(player: Player): Boolean {
        val pg: Int = menu.page + mod
        return pg > 0 && menu.getPages(player) >= pg
    }

    override fun getName(player: Player?): Component {
        if (!hasNext(player!!)) {
            return MiniMessage.miniMessage().deserialize(if (mod > 0) "\u00a77Last page" else "\u00a77First page")
        }
        val str = "(\u00a7e" + (menu.page + mod) + "/\u00a7e" + menu.getPages(player) + "\u00a7a)"
        return MiniMessage.miniMessage().deserialize(if (mod > 0) "\u00a7a\u27f6" else "\u00a7c\u27f5")
    }

    override fun getDescription(player: Player?): List<Component> {
        return ArrayList()
    }

    override fun getMaterial(player: Player?): Material {
        return Material.ARROW
    }
}

