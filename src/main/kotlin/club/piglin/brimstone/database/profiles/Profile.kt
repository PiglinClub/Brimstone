package club.piglin.brimstone.database.profiles

import club.piglin.brimstone.utils.Chat
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
    var lastLoginWorld: String = "world",
    var gold: Double = 0.0,
    var xp: Double = 0.0,
    var level: Int = 0,
    var town: UUID? = null
) {
    fun getUniqueId(): UUID {
        return this.uniqueId
    }

    fun addExp(amount: Double) {
        xp += amount
        if (xp >= 1000.0) {
            xp -= 1000.0
            level += 1
            if (level % 5 == 0) {
                Chat.broadcast("&a${name} has leveled up to &eLevel ${level}&a!")
            }
        }
    }

    fun getLastLocation(): Location {
        return Location(Bukkit.getWorld(lastLoginWorld), lastLoginLocationX, lastLoginLocationY, lastLoginLocationZ)
    }
}