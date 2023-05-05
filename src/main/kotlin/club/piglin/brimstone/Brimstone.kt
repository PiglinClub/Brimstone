package club.piglin.brimstone

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class Brimstone : JavaPlugin() {
    companion object {
        @JvmStatic
        lateinit var instance: Brimstone
        lateinit var Log: Logger
    }

    lateinit var protocolManager: ProtocolManager

    override fun onEnable() {
        instance = this
        Log = Bukkit.getLogger()
        protocolManager = ProtocolLibrary.getProtocolManager()
        Log.info("The plugin has successfully loaded.")
    }

    override fun onDisable() {
        Log.info("The plugin has successfully unloaded.")
    }
}