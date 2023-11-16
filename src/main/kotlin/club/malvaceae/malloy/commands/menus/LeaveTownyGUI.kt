package club.malvaceae.malloy.commands.menus

import club.malvaceae.malloy.menus.Button
import club.malvaceae.malloy.menus.Menu
import club.malvaceae.malloy.utils.Chat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class LeaveTownyGUI : Menu() {
    override fun getTitle(player: Player): String {
        return "Leave your Town?"
    }

    override fun getButtons(player: Player): Map<Int, Button?> {
        val buttons = HashMap<Int, Button>()
        val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(player.uniqueId)
        buttons[3] = object : Button() {
            override fun getName(var1: Player?): Component {
                return MiniMessage.miniMessage().deserialize("<green>Confirm")
            }

            override fun getDescription(var1: Player?): List<Component> {
                return listOf(
                    MiniMessage.miniMessage().deserialize("<gray>Confirm leaving your town.</gray>").decoration(TextDecoration.ITALIC, false)
                )
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.GREEN_TERRACOTTA
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
                playSuccess(player)
                val town = club.malvaceae.malloy.Malloy.instance.townHandler.getPlayerTown(player) ?: return
                town.sendMessage("<red><yellow>${player.name}</yellow> left your town.</red>")
                town.removePlayer(player)
                Chat.sendMessage(player, "&aSuccessfully left your town.")
                player.closeInventory()
            }
        }
        buttons[5] = object : Button() {
            override fun getName(var1: Player?): Component {
                return MiniMessage.miniMessage().deserialize("<red>Decline")
            }

            override fun getDescription(var1: Player?): List<Component>? {
                return listOf(
                    MiniMessage.miniMessage().deserialize("<gray>Decline leaving your town.</gray>").decoration(TextDecoration.ITALIC, false)
                )
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.RED_TERRACOTTA
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
                playFail(player)
                Chat.sendMessage(player, "&aDeclined leaving your town.")
                player.closeInventory()
            }
        }
        return buttons
    }

}