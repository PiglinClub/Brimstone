package club.malvaceae.malloy.menus.buttons

import club.malvaceae.malloy.menus.Button
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.beans.ConstructorProperties


interface Callback<T> {
    fun callback(var1: T)
}


class BooleanButton @ConstructorProperties(value = ["confirm", "callback"]) constructor(
    private val confirm: Boolean,
    private val callback: Callback<Boolean>
) :
    Button() {
    override fun clicked(player: Player, i: Int, clickType: ClickType?) {
        if (confirm) {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_COW_BELL, 20.0f, 0.1f)
        } else {
            player.playSound(player.location, Sound.BLOCK_GRAVEL_BREAK, 20.0f, 0.1f)
        }
        player.closeInventory()
        callback.callback(confirm)
    }

    override fun getName(player: Player?): Component? {
        return MiniMessage.miniMessage().deserialize(if (confirm) "\u00a7aConfirm" else "\u00a7cCancel")
    }

    override fun getDescription(player: Player?): List<Component> {
        return ArrayList()
    }

    override fun getMaterial(player: Player?): Material {
        return if (confirm) Material.LIME_WOOL else Material.RED_WOOL
    }
}


