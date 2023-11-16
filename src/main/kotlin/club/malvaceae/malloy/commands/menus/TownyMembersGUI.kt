package club.malvaceae.malloy.commands.menus

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.database.towns.Member
import club.malvaceae.malloy.menus.Button
import club.malvaceae.malloy.menus.Menu
import club.malvaceae.malloy.menus.buttons.BackButton
import club.malvaceae.malloy.menus.pagination.PaginatedMenu
import club.malvaceae.malloy.utils.Chat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class KickFromTownGUI(val target: OfflinePlayer) : Menu() {
    override fun getTitle(player: Player): String {
        return "Kick ${target.name} from Town?"
    }

    override fun getButtons(player: Player): Map<Int, Button?> {
        val town = Malloy.instance.townHandler.getPlayerTown(player) ?: throw Error("This player must have a town.")
        if (Malloy.instance.townHandler.getPlayerTown(target) != town) {
            throw Error("This player must be in the same town as you.")
        }
        if (target.uniqueId == player.uniqueId) {
            Chat.sendComponent(player, "<red>You can't kick yourself, also, why would you do that.")
            TownyMemberManageGUI(target).openMenu(player)
            return hashMapOf()
        }
        val buttons: HashMap<Int, Button> = hashMapOf()
        var member: Member? = town.getMember(target.uniqueId) ?: throw Error("Odd.")
        buttons[3] = object : Button() {
            override fun getName(var1: Player?): Component? {
                return MiniMessage.miniMessage().deserialize("<green>Yes").decoration(TextDecoration.ITALIC, false)
            }

            override fun getMaterial(var1: Player?): Material? {
                return Material.ANVIL
            }

            override fun getDescription(var1: Player?): List<Component>? {
                return listOf(
                    MiniMessage.miniMessage().deserialize("<gray>This will permanently remove ${target.name} from your town").decoration(TextDecoration.ITALIC, false),
                    MiniMessage.miniMessage().deserialize("<gray>until reinvited.").decoration(TextDecoration.ITALIC, false)
                )
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
                town.removePlayer(target)
                Chat.sendComponent(player, "<green>Successfully removed <yellow>${target.name}</yellow> from your town!")
                if (target.isOnline) {
                    Chat.sendComponent(target as Player, "<red>You've been removed from <yellow>${town.name}</yellow>!")
                }
                TownyMembersGUI().openMenu(player)
            }
        }
        buttons[5] = BackButton(TownyMemberManageGUI(target))
        return buttons
    }
}

class ChangeTownyRoleGUI(val target: OfflinePlayer) : Menu() {
    override fun getTitle(player: Player): String {
        return "Change ${target.name}'s role"
    }

    override fun getButtons(player: Player): Map<Int, Button?> {
        val town = Malloy.instance.townHandler.getPlayerTown(player) ?: throw Error("This player must have a town.")
        if (Malloy.instance.townHandler.getPlayerTown(target) != town) {
            throw Error("This player must be in the same town as you.")
        }
        if (target.uniqueId == player.uniqueId) {
            Chat.sendComponent(player, "<red>You can't kick yourself, also, why would you do that.")
            TownyMemberManageGUI(target).openMenu(player)
            return hashMapOf()
        }
        val buttons: HashMap<Int, Button> = hashMapOf()
        var member: Member? = town.getMember(target.uniqueId) ?: throw Error("Odd.")
        buttons[3] = object : Button() {
            override fun getName(var1: Player?): Component? {
                if (member!!.role.lowercase() == "officer") {
                    return MiniMessage.miniMessage().deserialize("<red>Demote to Resident").decoration(TextDecoration.ITALIC, false)
                } else {
                    return MiniMessage.miniMessage().deserialize("<green>Promote to Officer").decoration(TextDecoration.ITALIC, false)
                }
            }

            override fun getMaterial(var1: Player?): Material? {
                return Material.OAK_SIGN
            }

            override fun getDescription(var1: Player?): List<Component>? {
                return listOf(MiniMessage.miniMessage().deserialize("<gray>Click to change ${target.name}'s role.").decoration(TextDecoration.ITALIC, false))
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
                if (member!!.role.lowercase() == "officer") {
                    member!!.role = "resident"
                    town.sendMessage("<green><yellow>${target.name}</yellow> has been promoted to Resident!")
                } else {
                    member!!.role = "officer"
                    town.sendMessage("<green><yellow>${target.name}</yellow> has been promoted to Officer!")
                }
                town.saveMember(member)
                TownyMemberManageGUI(target).openMenu(player)
            }
        }
        buttons[5] = BackButton(TownyMemberManageGUI(target))
        return buttons
    }
}

class TownyMemberManageGUI(val target: OfflinePlayer) : Menu() {
    override fun getTitle(player: Player): String {
        return "Manage ${target.name}"
    }

    override fun getButtons(player: Player): Map<Int, Button?> {
        val town = Malloy.instance.townHandler.getPlayerTown(player) ?: throw Error("This player must have a town.")
        if (Malloy.instance.townHandler.getPlayerTown(target) != town) {
            throw Error("This player must be in the same town as you.")
        }
        val buttons: HashMap<Int, Button> = hashMapOf()
        var members: Member? = null
        town.members.forEach { member ->
            if ((member["uniqueId"] as UUID) == target.uniqueId) {
                members = town.getMember((member["uniqueId"] as UUID))!!
            }
        }
        if (members == null) {
            throw Error("Odd.")
        }
        buttons[0] = object : Button() {
            override fun getButtonItem(player: Player?): ItemStack {
                val item = ItemStack(Material.PLAYER_HEAD)
                val meta = item.itemMeta as SkullMeta
                meta.owningPlayer = target
                meta.displayName(MiniMessage.miniMessage().deserialize("<reset><green>${target.name}").decoration(TextDecoration.ITALIC, false))
                meta.lore(
                    mutableListOf(
                        MiniMessage.miniMessage().deserialize("<reset><gray>Joined at: <aqua>${SimpleDateFormat("MM/dd/yyyy 'at' h:mm a").format(Date(members!!.joinedAt))}").decoration(TextDecoration.ITALIC, false),
                        MiniMessage.miniMessage().deserialize("<reset><gray>Role: <aqua>${members!!.role.uppercase()}").decoration(TextDecoration.ITALIC, false),
                        MiniMessage.miniMessage().deserialize("<reset><gray>Gold deposited: <color:#ffd417>${members!!.goldDeposited}g").decoration(TextDecoration.ITALIC, false),
                        MiniMessage.miniMessage().deserialize("<reset><gray>Hijacked? ${if (members!!.hijacked) "<green>Yes" else "<red>No"}").decoration(TextDecoration.ITALIC, false)
                    )
                )
                item.itemMeta = meta
                return item
            }

            override fun getName(var1: Player?): Component? {
                return null
            }

            override fun getDescription(var1: Player?): List<Component>? {
                return null
            }
        }
        buttons[6] = object : Button() {
            override fun getName(var1: Player?): Component? {
                return MiniMessage.miniMessage().deserialize("<color:#ff2e17>Kick ${target.name}</color>").decoration(TextDecoration.ITALIC, false)
            }

            override fun getMaterial(var1: Player?): Material? {
                return Material.ANVIL
            }

            override fun getDescription(var1: Player?): List<Component>? {
                return listOf(
                    MiniMessage.miniMessage().deserialize("<gray>Kick ${target.name} from your town?").decoration(TextDecoration.ITALIC, false),
                    MiniMessage.miniMessage().deserialize("<gray>This action cannot be undone.").decoration(TextDecoration.ITALIC, false)
                )
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
                KickFromTownGUI(target).openMenu(player)
            }
        }
        buttons[7] = object : Button() {
            override fun getName(var1: Player?): Component? {
                return MiniMessage.miniMessage().deserialize("<yellow>Change role").decoration(TextDecoration.ITALIC, false)
            }

            override fun getMaterial(var1: Player?): Material? {
                return Material.OAK_SIGN
            }

            override fun getDescription(var1: Player?): List<Component>? {
                return listOf(
                    MiniMessage.miniMessage().deserialize("<gray>Change ${target.name}'s role.").decoration(TextDecoration.ITALIC, false)
                )
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
                if (town.getMember(player.uniqueId)!!.role.lowercase() == "mayor") {
                    ChangeTownyRoleGUI(target).openMenu(player)
                }
            }
        }
        buttons[8] = BackButton(TownyMembersGUI())
        return buttons
    }
}

class TownyMembersGUI : PaginatedMenu() {
    override fun getPrePaginatedTitle(var1: Player?): String {
        return "Town Members"
    }

    override fun getAllPagesButtons(var1: Player?): Map<Int, Button> {
        if (var1 != null) {
            val town = Malloy.instance.townHandler.getPlayerTown(var1) ?: throw Error("This player must have a town.")
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
                        meta.displayName(MiniMessage.miniMessage().deserialize("<reset><green>${m.name}").decoration(TextDecoration.ITALIC, false))
                        meta.lore(
                            mutableListOf(
                                MiniMessage.miniMessage().deserialize("<reset><gray>Joined at: <aqua>${SimpleDateFormat("MM/dd/yyyy 'at' h:mm a").format(Date(it.joinedAt))}").decoration(TextDecoration.ITALIC, false),
                                MiniMessage.miniMessage().deserialize("<reset><gray>Role: <aqua>${it.role.uppercase()}").decoration(TextDecoration.ITALIC, false),
                                MiniMessage.miniMessage().deserialize("<reset><gray>Gold deposited: <color:#ffd417>${it.goldDeposited}g").decoration(TextDecoration.ITALIC, false),
                                MiniMessage.miniMessage().deserialize("<reset><gray>Hijacked? ${if (it.hijacked) "<green>Yes" else "<red>No"}").decoration(TextDecoration.ITALIC, false)
                            )
                        )
                        item.itemMeta = meta
                        return item
                    }

                    override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
                        if (town.getMember(player.uniqueId)!!.role.lowercase() == "mayor") {
                            TownyMemberManageGUI(m).openMenu(player)
                        }
                    }

                    override fun getDescription(var1: Player?): List<Component>? {
                        return null
                    }

                    override fun getName(var1: Player?): Component? {
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