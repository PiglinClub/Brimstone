package club.piglin.brimstone.profiles

import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

class Profile(
    @JvmField val uniqueId: UUID,
    var name: String? = null,
    var lastLogin: Long = System.currentTimeMillis(),
    val firstJoin: Long = System.currentTimeMillis(),
    var lastLoginLocationX: Double = -35.5,
    var lastLoginLocationY: Double = 34.5,
    var lastLoginLocationZ: Double = -87.5,
    var gold: Double = 0.0,
    var xp: Double = 0.0,
    var level: Int = 0
) {
    fun getUniqueId(): UUID {
        return this.uniqueId
    }

    fun getLastLocation(): Location {
        return Location(Bukkit.getWorld("world_nether"), lastLoginLocationX, lastLoginLocationY, lastLoginLocationZ)
    }
}