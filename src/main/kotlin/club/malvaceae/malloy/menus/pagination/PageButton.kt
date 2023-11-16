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
            return MiniMessage.miniMessage().deserialize(if (mod > 0) "<gray>Last page" else "<gray>First page")
        }
        val str = "<gray>(<yellow>" + (menu.page + mod) + "<yellow>" + menu.getPages(player) + "<gray>)"
        return MiniMessage.miniMessage().deserialize(if (mod > 0) "<green>⟶" else "<green>⟵")
    }

    override fun getDescription(player: Player?): List<Component> {
        return ArrayList()
    }

    override fun getMaterial(player: Player?): Material {
        return Material.ARROW
    }
}

