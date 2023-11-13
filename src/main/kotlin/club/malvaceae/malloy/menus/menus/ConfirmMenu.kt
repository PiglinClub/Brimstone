package club.malvaceae.malloy.menus.menus

import club.malvaceae.malloy.menus.Button
import club.malvaceae.malloy.menus.Menu
import club.malvaceae.malloy.menus.buttons.BooleanButton
import club.malvaceae.malloy.menus.buttons.Callback
import org.bukkit.Material
import org.bukkit.entity.Player
import java.beans.ConstructorProperties


class ConfirmMenu @ConstructorProperties(value = ["title", "response"]) constructor(
    private val title: String,
    private var response: Callback<Boolean>
) :
    Menu() {
    override fun getButtons(player: Player): Map<Int, Button> {
        val buttons: HashMap<Int, Button> = HashMap<Int, Button>()
        for (i in 0..8) {
            if (i == 3) {
                buttons[i] = BooleanButton(true, response)
                continue
            }
            if (i == 5) {
                buttons[i] = BooleanButton(false, response)
                continue
            }
            buttons[i] = Button.placeholder(Material.RED_WOOL)
        }
        return buttons
    }

    override fun getTitle(player: Player): String {
        return title
    }

    init {
        this.response = response
    }
}

