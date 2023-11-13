package club.malvaceae.malloy.utils

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

class Settings private constructor() {
    companion object {
        var data: FileConfiguration
        var f: File
        init {
            val plugin = club.malvaceae.malloy.Malloy.instance
            if (!plugin.dataFolder.exists()) {
                plugin.dataFolder.mkdir()
            }
            f = File(plugin.dataFolder, "config.yml")
            if (!f.exists()) {
                try {
                    f.createNewFile()
                    club.malvaceae.malloy.Malloy.log.info("Successfully created new config file.")
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            data = YamlConfiguration.loadConfiguration(f)
        }

        fun save() {
            try {
                data.save(f)
                club.malvaceae.malloy.Malloy.log.info("Saved config.yml")
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }

        fun reload() {
            data = YamlConfiguration.loadConfiguration(f)
            club.malvaceae.malloy.Malloy.log.info("Reloaded config.yml")
        }
    }
}