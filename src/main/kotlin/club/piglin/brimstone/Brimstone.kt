package club.piglin.brimstone

import club.piglin.brimstone.profiles.ProfileHandler
import club.piglin.brimstone.scoreboard.Scoreboard
import club.piglin.brimstone.utils.Settings
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.mongodb.MongoClient
import com.mongodb.MongoClientException
import com.mongodb.MongoClientURI
import me.lucko.helper.plugin.ExtendedJavaPlugin
import org.bukkit.Bukkit
import java.util.logging.Logger

class Brimstone : ExtendedJavaPlugin() {
    companion object {
        @JvmStatic
        lateinit var instance: Brimstone
        lateinit var log: Logger
    }

    lateinit var protocolManager: ProtocolManager
    lateinit var dataSource: MongoClient
    lateinit var profileHandler: ProfileHandler

    override fun load() {
        protocolManager = ProtocolLibrary.getProtocolManager()
    }

    override fun enable() {
        instance = this
        log = Bukkit.getLogger()
        Settings
        setupDataSource()
        profileHandler = ProfileHandler()

        Bukkit.getPluginManager().registerEvents(Scoreboard(), this)

        log.info("The plugin has successfully loaded.")
    }

    fun setupDataSource() {
        val uri = Settings.data.getString("database.uri")
        if (uri == null) {
            log.severe("No database URI set. Please set it in the config.")
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
        log.info("The plugin has successfully unloaded.")
    }
}