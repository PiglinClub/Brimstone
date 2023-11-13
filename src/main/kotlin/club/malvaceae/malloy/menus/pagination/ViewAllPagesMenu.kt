package club.malvaceae.malloy.menus.pagination

import club.malvaceae.malloy.menus.Button
import club.malvaceae.malloy.menus.Menu
import club.malvaceae.malloy.menus.buttons.BackButton
import org.bukkit.entity.Player
import java.beans.ConstructorProperties
import javax.annotation.Nonnull


class ViewAllPagesMenu @ConstructorProperties(value = ["menu"]) constructor(@Nonnull menu: PaginatedMenu?) : Menu() {
    @get:Nonnull
    @Nonnull
    var menu: PaginatedMenu

    override fun getTitle(player: Player): String {
        return "Jump to page"
    }

    override fun getButtons(player: Player): Map<Int, Button> {
        val buttons: HashMap<Int, Button> = HashMap<Int, Button>()
        buttons[0] = BackButton(menu)
        var index = 10
        for (i in 1..menu.getPages(player)) {
            buttons[index++] = JumpToPageButton(i, menu)
            if ((index - 8) % 9 != 0) continue
            index += 2
        }
        return buttons
    }

    override var isAutoUpdate: Boolean = true

    init {
        if (menu == null) {
            throw NullPointerException("menu")
        }
        this.menu = menu
    }
}


