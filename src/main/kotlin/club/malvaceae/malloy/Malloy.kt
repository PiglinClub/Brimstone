package club.malvaceae.malloy

import club.malvaceae.malloy.commands.*
import club.malvaceae.malloy.database.profiles.ProfileHandler
import club.malvaceae.malloy.database.towns.ClaimHandler
import club.malvaceae.malloy.database.towns.TownHandler
import club.malvaceae.malloy.enchantments.EnchantmentWrapperHandler
import club.malvaceae.malloy.features.ScoreboardFeature
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
import org.bukkit.Bukkit
import java.util.logging.Logger

class Malloy : ExtendedJavaPlugin() {
    companion object {
        @JvmStatic
        lateinit var instance: club.malvaceae.malloy.Malloy
        lateinit var log: Logger
    }

    private var protocolManager: ProtocolManager? = null
    lateinit var dataSource: MongoClient
    lateinit var profileHandler: ProfileHandler
    lateinit var townHandler: TownHandler
    lateinit var claimHandler: ClaimHandler

    override fun load() {
        protocolManager = ProtocolLibrary.getProtocolManager()
    }

    override fun enable() {
        club.malvaceae.malloy.Malloy.Companion.instance = this
        club.malvaceae.malloy.Malloy.Companion.log = Bukkit.getLogger()
        Settings
        setupDataSource()

        profileHandler = ProfileHandler()
        townHandler = TownHandler()
        claimHandler = ClaimHandler()

        EnchantmentWrapperHandler.instance.register()

        Bukkit.getServer().pluginManager.registerEvents(ScoreboardFeature(), this)
        Bukkit.getServer().pluginManager.registerEvents(ClaimListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(SkillListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(WildernessMovementCheck(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(ServerListPingListener(), this)

        this.getCommand("balance")!!.setExecutor(club.malvaceae.malloy.commands.BalanceCommand())
        this.getCommand("claim")!!.setExecutor(club.malvaceae.malloy.commands.ClaimCommand())
        this.getCommand("towny")!!.setExecutor(TownyCommand())
        this.getCommand("wilderness")!!.setExecutor(WildernessCommand())
        this.getCommand("skills")!!.setExecutor(SkillsCommand())
        this.getCommand("shop")!!.setExecutor(ShopCommand())


        club.malvaceae.malloy.Malloy.Companion.log.info("The plugin has successfully loaded.")
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
        club.malvaceae.malloy.Malloy.Companion.log.info("The plugin has successfully unloaded.")
    }
}