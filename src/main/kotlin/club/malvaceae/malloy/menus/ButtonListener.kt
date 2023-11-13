package club.malvaceae.malloy.menus

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent


class ButtonListener : Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    fun onButtonPress(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val openMenu = Menu.currentlyOpenedMenus!![player.name]
        if (openMenu != null) {
            if (event.slot != event.rawSlot) {
                if (event.click == ClickType.SHIFT_LEFT || event.click == ClickType.SHIFT_RIGHT) {
                    event.isCancelled = true
                    if (openMenu.isNoncancellingInventory && event.currentItem != null) {
                        player.openInventory.topInventory.addItem(event.currentItem!!)
                        event.currentItem = null
                    }
                }
                return
            }
            if (openMenu.buttons.containsKey(event.slot)) {
                var newMenu: Menu
                val button = openMenu.buttons[event.slot]
                val cancel = button!!.shouldCancel(player, event.slot, event.click)
                if (!(cancel || event.click != ClickType.SHIFT_LEFT && event.click != ClickType.SHIFT_RIGHT)) {
                    event.isCancelled = true
                    if (event.currentItem != null) {
                        player.inventory.addItem(event.currentItem!!)
                    }
                } else {
                    event.isCancelled = cancel
                }
                button.clicked(player, event.slot, event.click)
                if (
                    Menu.currentlyOpenedMenus!!.containsKey(player.name)
                ) {
                    newMenu = Menu.currentlyOpenedMenus!![player.name]!!
                    if (newMenu == openMenu && newMenu.isUpdateAfterClick && !button.isIgnoreUpdateOnClick) {
                        newMenu.openMenu(player)
                    }
                }
                if (event.isCancelled) {
                    Bukkit.getScheduler().runTaskLater(
                        club.malvaceae.malloy.Malloy.instance,
                        Runnable { player.updateInventory() }, 1L
                    )
                }
            } else if (event.click == ClickType.SHIFT_LEFT || event.click == ClickType.SHIFT_RIGHT) {
                event.isCancelled = true
                if (openMenu.isNoncancellingInventory && event.currentItem != null) {
                    player.openInventory.topInventory.addItem(event.currentItem!!)
                    event.currentItem = null
                }
            }
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as Player
        val openMenu = Menu.currentlyOpenedMenus!![player.name]
        if (openMenu != null) {
            openMenu.onClose(player)
            Menu.cancelCheck(player)
            Menu.currentlyOpenedMenus!!.remove(player.name)
        }
    }
}


