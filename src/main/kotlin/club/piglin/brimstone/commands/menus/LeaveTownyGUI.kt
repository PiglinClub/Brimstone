package club.piglin.brimstone.commands.menus

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.menus.Button
import club.piglin.brimstone.menus.Menu
import club.piglin.brimstone.utils.Chat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class LeaveTownyGUI : Menu() {
    override fun getTitle(player: Player): String {
        return "Leave your Town?"
    }

    override fun getButtons(player: Player): Map<Int, Button?> {
        val buttons = HashMap<Int, Button>()
        val profile = Brimstone.instance.profileHandler.getProfile(player.uniqueId)
        buttons[3] = object : Button() {
            override fun getName(var1: Player?): String {
                return "${ChatColor.GREEN}Confirm"
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
                val town = Brimstone.instance.townHandler.getPlayerTown(player) ?: return
                town.removePlayer(player)
                Chat.sendMessage(player, "&aSuccessfully left your town.")
                player.closeInventory()
            }
        }
        buttons[5] = object : Button() {
            override fun getName(var1: Player?): String {
                return "${ChatColor.RED}Decline"
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