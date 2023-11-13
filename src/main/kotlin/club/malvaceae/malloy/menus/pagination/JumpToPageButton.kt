package club.malvaceae.malloy.menus.pagination

import club.malvaceae.malloy.menus.Button
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.beans.ConstructorProperties


class JumpToPageButton @ConstructorProperties(value = ["page", "menu"]) constructor(
    private val page: Int,
    menu: PaginatedMenu
) :
    Button() {
    private val menu: PaginatedMenu
    override fun getName(player: Player?): String {
        return "\u00a7ePage " + page
    }

    override fun getDescription(player: Player?): List<Component>? {
        return null
    }

    override fun getMaterial(player: Player?): Material {
        return Material.BOOK
    }

    override fun getAmount(player: Player?): Int {
        return page
    }

    override fun getDamageValue(player: Player?): Byte {
        return 0
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
        menu.modPage(player, page - menu.page)
        playNeutral(player)
    }

    init {
        this.menu = menu
    }
}


