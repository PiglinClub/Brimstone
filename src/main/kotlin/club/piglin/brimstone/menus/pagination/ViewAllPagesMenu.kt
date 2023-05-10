package club.piglin.brimstone.menus.pagination

import club.piglin.brimstone.menus.Button
import club.piglin.brimstone.menus.Menu
import club.piglin.brimstone.menus.buttons.BackButton
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


