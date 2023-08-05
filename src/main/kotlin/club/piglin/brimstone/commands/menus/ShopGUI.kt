package club.piglin.brimstone.commands.menus

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.features.ShopCategory
import club.piglin.brimstone.features.ShopEntry
import club.piglin.brimstone.features.ShopHandler
import club.piglin.brimstone.menus.Button
import club.piglin.brimstone.menus.Menu
import club.piglin.brimstone.menus.buttons.BackButton
import club.piglin.brimstone.menus.pagination.PaginatedMenu
import club.piglin.brimstone.utils.Chat
import club.piglin.brimstone.utils.InventoryUtils
import club.piglin.brimstone.utils.PlayerUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.apache.commons.lang3.text.WordUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.ceil

class ShopGUI : Menu() {
    override fun getButtons(player: Player): Map<Int, Button?> {
        val buttons = HashMap<Int, Button>()
        for ((index, category) in ShopCategory.values().withIndex()) {
            buttons[index] = object : Button() {
                override fun getMaterial(var1: Player?): Material? {
                    return category.material
                }

                override fun getName(var1: Player?): String? {
                    return "${net.md_5.bungee.api.ChatColor.of("#ed982f")}${category.displayName}"
                }

                override fun getDescription(var1: Player?): List<Component>? {
                    return listOf(
                        MiniMessage.miniMessage().deserialize("<st><dark_gray>                              </dark_gray></st>").decoration(TextDecoration.ITALIC, false),
                        MiniMessage.miniMessage().deserialize("<yellow>Click to see ${category.displayName} items").decoration(TextDecoration.ITALIC, false),
                        MiniMessage.miniMessage().deserialize("<st><dark_gray>                              </dark_gray></st>").decoration(TextDecoration.ITALIC, false)
                    )
                }

                override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
                    ShopPage(category).openMenu(player)
                }
            }
        }
        return buttons
    }
}

enum class ShopConfirmType {
    BUY,
    SELL
}

class ShopConfirmButton(val item: ShopEntry, val type: ShopConfirmType, val amount: Int = 1) : Button() {
    override fun getDescription(var1: Player?): List<Component>? {
        if (type == ShopConfirmType.BUY) {
            return listOf(
                MiniMessage.miniMessage().deserialize("<st><dark_gray>                              </dark_gray></st>").decoration(
                    TextDecoration.ITALIC, false),
                MiniMessage.miniMessage().deserialize(" "),
                MiniMessage.miniMessage().deserialize("<color:#33ff05>PURCHASE PRICE:</color> <color:#ffd417>${item.pricePerOne * amount}g</color><white></white>").decoration(
                    TextDecoration.ITALIC, false),
                MiniMessage.miniMessage().deserialize(" "),
                MiniMessage.miniMessage().deserialize("<st><dark_gray>                              </dark_gray></st>").decoration(
                    TextDecoration.ITALIC, false)
            )
        } else {
            return listOf(
                MiniMessage.miniMessage().deserialize("<st><dark_gray>                              </dark_gray></st>").decoration(
                    TextDecoration.ITALIC, false),
                MiniMessage.miniMessage().deserialize(" "),
                MiniMessage.miniMessage().deserialize("<color:#33ff05>SELL PRICE:</color> <color:#ffd417>${item.sellPricePerOne * amount}g</color><white></white>").decoration(
                    TextDecoration.ITALIC, false),
                MiniMessage.miniMessage().deserialize(" "),
                MiniMessage.miniMessage().deserialize("<st><dark_gray>                              </dark_gray></st>").decoration(
                    TextDecoration.ITALIC, false)
            )
        }
    }

    override fun getMaterial(var1: Player?): Material? {
        return item.material
    }

    override fun getAmount(player: Player?): Int {
        return amount
    }

    override fun getName(var1: Player?): String {
        return "${ChatColor.AQUA}${WordUtils.capitalize(item.material.name.lowercase().replace("_", " "))}"
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
        if (type == ShopConfirmType.BUY) {
            if (Brimstone.instance.profileHandler.getProfile(player.uniqueId)!!.gold < (item.pricePerOne * amount)) {
                player.playSound(player.location, Sound.ENTITY_WARDEN_TENDRIL_CLICKS, 1f, 1f)
                Chat.sendComponent(player, "<red>You can't afford to purchase this item. You are short by </red><color:#ffd417>${ceil(item.pricePerOne - Brimstone.instance.profileHandler.getProfile(player.uniqueId)!!.gold)}g</color><red>.</red>")
                return
            }
            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
            Brimstone.instance.profileHandler.getProfile(player.uniqueId)!!.gold -= (item.pricePerOne * amount)
            Brimstone.instance.profileHandler.saveProfile(Brimstone.instance.profileHandler.getProfile(player.uniqueId)!!)
            PlayerUtils.bulkItems(player, arrayListOf(ItemStack(item.material, amount)))
            Chat.sendComponent(player, "<green>Successfully purchased <aqua>${amount}x ${WordUtils.capitalize(item.material.name.lowercase().replace("_", " "))}</aqua><green> for <color:#ffd417>${item.pricePerOne * amount}g</color><green>.")
        } else {
            if (InventoryUtils.amount(ItemStack(item.material), player.inventory) < amount) {
                Chat.sendComponent(player, "<red>You do not have the required items.</red>")
                player.playSound(player.location, Sound.ENTITY_WARDEN_TENDRIL_CLICKS, 1f, 1f)
                return
            }
            InventoryUtils.removeManually(ItemStack(item.material, amount), player.inventory)
            Brimstone.instance.profileHandler.getProfile(player.uniqueId)!!.gold += item.sellPricePerOne * amount
            Brimstone.instance.profileHandler.saveProfile(Brimstone.instance.profileHandler.getProfile(player.uniqueId)!!)
            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
            Chat.sendComponent(player, "<green>Successfully sold <aqua>${amount}x ${WordUtils.capitalize(item.material.name.lowercase().replace("_", " "))}</aqua><green> for <color:#ffd417>${item.sellPricePerOne * amount}g</color><green>.")
        }
    }
}

class ShopConfirm(val item: ShopEntry, val type: ShopConfirmType) : Menu() {
    override fun getTitle(player: Player): String {
        return "${type.toString().lowercase().replaceFirstChar { it.uppercase() }}: ${ChatColor.AQUA}${WordUtils.capitalize(item.material.name.lowercase().replace("_", " "))}"
    }

    override fun getButtons(player: Player): Map<Int, Button?> {
        val buttons = HashMap<Int, Button>()
        buttons[0] = BackButton(ShopPage(item.category))
        buttons[2] = ShopConfirmButton(item, type, 1)
        buttons[3] = ShopConfirmButton(item, type, 4)
        buttons[4] = ShopConfirmButton(item, type, 16)
        buttons[5] = ShopConfirmButton(item, type, 32)
        buttons[6] = ShopConfirmButton(item, type, 64)
        return buttons
    }
}

class ShopPage(val category: ShopCategory) : PaginatedMenu() {
    override fun getPrePaginatedTitle(var1: Player?): String {
        return "Shop: ${category.displayName}"
    }

    override fun getPreviousMenu(player: Player?): Menu {
        return ShopGUI()
    }

    override fun getMaxItemsPerPage(player: Player?): Int {
        return 45
    }

    override fun getAllPagesButtons(var1: Player?): Map<Int, Button> {
        if (var1 != null) {
            val buttons = hashMapOf<Int, Button>()
            val count = AtomicInteger(0)
            val entries = ShopHandler.getEntriesInCategory(category)
            for (entry in entries) {
                buttons[count.get()] = object : Button() {
                    override fun getMaterial(var1: Player?): Material {
                        return entry.material
                    }

                    override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
                        if (clickType == ClickType.MIDDLE) {
                            if (InventoryUtils.amount(ItemStack(entry.material), player.inventory) <= 0) {
                                Chat.sendComponent(player, "<red>You do not have the required items.</red>")
                                player.playSound(player.location, Sound.ENTITY_WARDEN_TENDRIL_CLICKS, 1f, 1f)
                                return
                            }
                            val amount = InventoryUtils.amount(ItemStack(entry.material), player.inventory)
                            InventoryUtils.removeManually(ItemStack(entry.material, amount), player.inventory)
                            Brimstone.instance.profileHandler.getProfile(player.uniqueId)!!.gold += entry.sellPricePerOne * amount
                            Brimstone.instance.profileHandler.saveProfile(Brimstone.instance.profileHandler.getProfile(player.uniqueId)!!)
                            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                            Chat.sendComponent(player, "<green>Successfully sold <aqua>${amount}x ${WordUtils.capitalize(entry.material.name.lowercase().replace("_", " "))}</aqua><green> for <color:#ffd417>${entry.sellPricePerOne * amount}g</color><green>.")
                        } else if (clickType == ClickType.RIGHT) {
                            ShopConfirm(entry, ShopConfirmType.SELL).openMenu(player)
                        } else if (clickType == ClickType.LEFT) {
                            ShopConfirm(entry, ShopConfirmType.BUY).openMenu(player)
                        }
                    }

                    override fun getName(var1: Player?): String {
                        return "${ChatColor.AQUA}${WordUtils.capitalize(entry.material.name.lowercase().replace("_", " "))}"
                    }

                    override fun getDescription(var1: Player?): List<Component> {
                        return listOf(
                            MiniMessage.miniMessage().deserialize("<st><dark_gray>                              </dark_gray></st>").decoration(
                                TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize(" "),
                            MiniMessage.miniMessage().deserialize("<white>▐</white> <color:#33ff05>PURCHASE PRICE:</color> <color:#ffd417>${entry.pricePerOne}g</color><white></white>").decoration(
                                TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize("<white>▐</white> <color:#33ff05>SELLING PRICE:</color> <color:#ffd417>${entry.sellPricePerOne}g</color><white></white>").decoration(
                                TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize(" "),
                            MiniMessage.miniMessage().deserialize(" <color:#33ff05>*</color> <color:#26ff00>Left click to <color:#159c00>buy</color>!</color>").decoration(
                                TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize(" <color:#33ff05>*</color> <color:#fbff29>Right click to <color:#d6d923>sell</color>!</color>").decoration(
                                TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize(" <color:#33ff05>*</color> <color:#d6241e>Middle click to <color:#911914>sell all</color>!</color>").decoration(
                                TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize("  "),
                            MiniMessage.miniMessage().deserialize("<st><dark_gray>                              </dark_gray></st>").decoration(
                                TextDecoration.ITALIC, false),
                        )
                    }
                }
                count.getAndIncrement()
            }
            return buttons
        }
        return hashMapOf()
    }
}