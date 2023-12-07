package club.malvaceae.malloy

import club.malvaceae.malloy.commands.*
import club.malvaceae.malloy.database.profiles.ProfileHandler
import club.malvaceae.malloy.database.towns.ClaimHandler
import club.malvaceae.malloy.database.towns.TownHandler
import club.malvaceae.malloy.discord.SlashCommandListener
import club.malvaceae.malloy.enchantments.EnchantmentWrapperHandler
import club.malvaceae.malloy.enchantments.list.ExtractionEnchantment
import club.malvaceae.malloy.enchantments.list.LumberjackEnchantment
import club.malvaceae.malloy.enchantments.list.MoltenEnchantment
import club.malvaceae.malloy.features.CombatTagListener
import club.malvaceae.malloy.features.InfoFeature
import club.malvaceae.malloy.features.ScoreboardFeature
import club.malvaceae.malloy.features.TeleportCheck
import club.malvaceae.malloy.listeners.ClaimListener
import club.malvaceae.malloy.listeners.PlayerListener
import club.malvaceae.malloy.listeners.ServerListPingListener
import club.malvaceae.malloy.listeners.SkillListener
import club.malvaceae.malloy.utils.Settings
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.mongodb.MongoClient
import com.mongodb.MongoClientException
import com.mongodb.MongoClientURI
import me.lucko.helper.plugin.ExtendedJavaPlugin
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.requests.GatewayIntent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import java.util.logging.Logger


class Malloy : ExtendedJavaPlugin() {
    companion object {
        @JvmStatic
        lateinit var instance: Malloy
        lateinit var log: Logger
    }

    private var protocolManager: ProtocolManager? = null
    lateinit var dataSource: MongoClient
    lateinit var profileHandler: ProfileHandler
    lateinit var townHandler: TownHandler
    lateinit var jda: JDA
    lateinit var claimHandler: ClaimHandler

    override fun load() {
        protocolManager = ProtocolLibrary.getProtocolManager()
    }

    fun registerRecipes() {
        var key = NamespacedKey(this, "molten_book")
        var i = ItemStack(Material.ENCHANTED_BOOK)
        var im: ItemMeta = (i.itemMeta)
        (im as EnchantmentStorageMeta).addStoredEnchant(MoltenEnchantment(), 1, true)
        im.lore(listOf(MiniMessage.miniMessage().deserialize("<gray>Molten I</gray>").decoration(TextDecoration.ITALIC, false)))
        i.itemMeta = im

        Bukkit.getServer().addRecipe(
            ShapedRecipe(key, i)
                .shape("   ", " PP", " PC")
                .setIngredient('P', Material.PAPER)
                .setIngredient('C', ItemStack(Material.COAL_BLOCK, 8))
        )

        key = NamespacedKey(this, "extraction_book")
        i = ItemStack(Material.ENCHANTED_BOOK)
        im = (i.itemMeta)
        (im as EnchantmentStorageMeta).addStoredEnchant(ExtractionEnchantment(), 1, true)
        im.lore(listOf(MiniMessage.miniMessage().deserialize("<gray>Extraction I</gray>").decoration(TextDecoration.ITALIC, false)))
        i.itemMeta = im

        Bukkit.getServer().addRecipe(
            ShapedRecipe(key, i)
                .shape("   ", " PP", " PL")
                .setIngredient('P', Material.PAPER)
                .setIngredient('L', ItemStack(Material.LAPIS_BLOCK, 10))
        )

        key = NamespacedKey(this, "lumberjack_book")
        i = ItemStack(Material.ENCHANTED_BOOK)
        im = (i.itemMeta)
        (im as EnchantmentStorageMeta).addStoredEnchant(LumberjackEnchantment(), 1, true)
        im.lore(listOf(MiniMessage.miniMessage().deserialize("<gray>Lumberjack I</gray>").decoration(TextDecoration.ITALIC, false)))
        i.itemMeta = im

        Bukkit.getServer().addRecipe(
            ShapedRecipe(key, i)
                .shape("   ", " PP", " PL")
                .setIngredient('P', Material.PAPER)
                .setIngredient('L', ItemStack(Material.OAK_LOG, 64))
        )
    }

    override fun enable() {
        instance = this
        log = Bukkit.getLogger()
        Settings
        setupDataSource()
        if (Bukkit.getServer().getPluginManager().getPlugin("Votifier") == null) {
            log.info("Enable Votifier, please.")
        }

        profileHandler = ProfileHandler()
        townHandler = TownHandler()
        claimHandler = ClaimHandler()

        EnchantmentWrapperHandler.instance.register()
        registerRecipes()

        jda = JDABuilder.createDefault(Settings.data.getString("discord.token"),
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_VOICE_STATES
        )
            .addEventListeners(SlashCommandListener())
            .setActivity(Activity.playing("piglin.club"))
            .build()

        jda.updateCommands().addCommands(
            Commands.slash("verify", "Get a code to link your Discord account to your Minecraft account.")
        ).queue()

        Bukkit.getServer().pluginManager.registerEvents(ScoreboardFeature(), this)
        Bukkit.getServer().pluginManager.registerEvents(ClaimListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(SkillListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(WildernessMovementCheck(), this)
        Bukkit.getServer().pluginManager.registerEvents(TeleportCheck(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(CombatTagListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(ServerListPingListener(), this)

        InfoFeature().runTaskTimer(this, 0L, 20L)

        this.getCommand("balance")!!.setExecutor(club.malvaceae.malloy.commands.BalanceCommand())
        this.getCommand("claim")!!.setExecutor(club.malvaceae.malloy.commands.ClaimCommand())
        this.getCommand("unclaim")!!.setExecutor(UnclaimCommand())
        this.getCommand("towny")!!.setExecutor(TownyCommand())
        this.getCommand("reply")!!.setExecutor(ReplyCommand())
        this.getCommand("message")!!.setExecutor(MessageCommand())
        this.getCommand("vote")!!.setExecutor(VoteCommand())
        this.getCommand("tpa")!!.setExecutor(TeleportCommand())
        this.getCommand("pay")!!.setExecutor(PayCommand())
        this.getCommand("wilderness")!!.setExecutor(WildernessCommand())
        this.getCommand("skills")!!.setExecutor(SkillsCommand())
        this.getCommand("shop")!!.setExecutor(ShopCommand())
        this.getCommand("setspawn")!!.setExecutor(SetSpawnCommand())
        this.getCommand("spawn")!!.setExecutor(SpawnCommand())
        this.getCommand("verify")!!.setExecutor(VerifyCommand())
        this.getCommand("baltop")!!.setExecutor(BaltopCommand())
        this.getCommand("twitter")!!.setExecutor(TwitterCommand())
        this.getCommand("discord")!!.setExecutor(DiscordCommand())
        this.getCommand("playtime")!!.setExecutor(PlaytimeCommand())

        club.malvaceae.malloy.Malloy.Companion.log.info("[Malloy] The plugin has successfully loaded.")
    }

    fun setupDataSource() {
        val uri = Settings.data.getString("database.uri")
        if (uri == null) {
            club.malvaceae.malloy.Malloy.Companion.log.severe("No database URI set. Please set it in the config.")
            return
        }
        var client: MongoClient? = null
        try {
            client = MongoClient(MongoClientURI(uri))
        } catch (e: MongoClientException) {
            e.printStackTrace()
        }
        if (client != null) {
            this.dataSource = client
        }
    }

    override fun disable() {
        for (player in Bukkit.getOnlinePlayers()) {
            club.malvaceae.malloy.Malloy.Companion.instance.profileHandler.saveProfile(club.malvaceae.malloy.Malloy.Companion.instance.profileHandler.getProfile(player.uniqueId)!!)
        }
        club.malvaceae.malloy.Malloy.Companion.log.info("[Malloy] The plugin has successfully unloaded.")
    }
}