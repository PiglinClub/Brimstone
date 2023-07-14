package club.piglin.brimstone.commands.menus

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.database.towns.Member
import club.piglin.brimstone.menus.Button
import club.piglin.brimstone.menus.pagination.PaginatedMenu
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class TownyMembersGUI : PaginatedMenu() {
    override fun getPrePaginatedTitle(var1: Player?): String {
        return "Town Members"
    }

    override fun getAllPagesButtons(var1: Player?): Map<Int, Button> {
        if (var1 != null) {
            val town = Brimstone.instance.townHandler.getPlayerTown(var1) ?: throw Error("This player must have a town.")
            val buttons: HashMap<Int, Button> = hashMapOf()
            val count = AtomicInteger(0)
            val members = ArrayList<Member>()
            town.members.forEach { member ->
                town.getMember(member["uniqueId"] as UUID)?.let { members.add(it) }
            }
            members.forEach {
                val m = Bukkit.getOfflinePlayer(it.uniqueId)
                buttons[count.get()] = object : Button() {
                    override fun getButtonItem(player: Player?): ItemStack {
                        val item = ItemStack(Material.PLAYER_HEAD)
                        val meta = item.itemMeta as SkullMeta
                        meta.owningPlayer = m
                        meta.displayName(MiniMessage.miniMessage().deserialize("<reset><green>${m.name}"))
                        meta.lore(
                            mutableListOf(
                                MiniMessage.miniMessage().deserialize("<reset><gray>Joined at: <aqua>${SimpleDateFormat("MM/dd/yyyy 'at' h:mm a").format(Date(it.joinedAt))}").decoration(TextDecoration.ITALIC, false),
                                MiniMessage.miniMessage().deserialize("<reset><gray>Role: <aqua>${it.role.uppercase()}").decoration(TextDecoration.ITALIC, false),
                                MiniMessage.miniMessage().deserialize("<reset><gray>Gold deposited: <color:#ffd417>${it.goldDeposited}g").decoration(TextDecoration.ITALIC, false)
                            )
                        )
                        item.itemMeta = meta
                        return item
                    }

                    override fun getDescription(var1: Player?): List<Component>? {
                        return null
                    }

                    override fun getName(var1: Player?): String? {
                        return null
                    }
                }
                count.getAndIncrement()
            }
            return buttons
        }
        throw Error("A player is required for this menu.")
    }
}