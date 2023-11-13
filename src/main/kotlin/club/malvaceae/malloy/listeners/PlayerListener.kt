package club.malvaceae.malloy.listeners

import club.malvaceae.malloy.enchantments.EnchantmentWrapperHandler
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.milkbowl.vault.chat.Chat
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta

class PlayerListener : Listener {
    private var vaultChat: Chat? = null

    init {
        vaultChat = Bukkit.getServer().servicesManager.load(Chat::class.java)
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        var i = ItemStack(Material.ENCHANTED_BOOK)
        var im: ItemMeta = (i.itemMeta)
        print((im as EnchantmentStorageMeta).addStoredEnchant(EnchantmentWrapperHandler.instance.MOLTEN, 1, true))
        im.lore(listOf(MiniMessage.miniMessage().deserialize("<gray>Molten I</gray>").decoration(TextDecoration.ITALIC, false)))
        i.itemMeta = im
        e.player.inventory.addItem(i)

        i = ItemStack(Material.DIAMOND_PICKAXE)
        im = (i.itemMeta as ItemMeta)
        print(im.addEnchant(EnchantmentWrapperHandler.instance.MOLTEN, 1, true))
        im.lore(listOf(MiniMessage.miniMessage().deserialize("<gray>Molten I</gray>").decoration(TextDecoration.ITALIC, false)))
        i.itemMeta = im
        e.player.inventory.addItem(i)

        e.joinMessage(MiniMessage.miniMessage().deserialize("<dark_gray>[</dark_gray><green>+<dark_gray>]</dark_gray> <color:#28cf1f>${e.player.name}"))
        club.malvaceae.malloy.utils.Chat.sendComponent(e.player, "<gray>Warning: This server is in <red>ALPHA</red>, many features may be added/removed later on.")
    }

    @EventHandler
    fun onPlayerChat(e: AsyncChatEvent) {
        e.isCancelled = true
        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<reset>${e.player.name}<dark_gray>:<reset> ${(e.message() as TextComponent).content()}"))
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        e.quitMessage(MiniMessage.miniMessage().deserialize("<dark_gray>[</dark_gray><red>+<dark_gray>]</dark_gray> <color:#f53527>${e.player.name}"))
    }


}