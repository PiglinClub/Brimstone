package club.piglin.brimstone.database.profiles

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.utils.Chat
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random

enum class Skill {
    MINING,
    FISHING,
    LOGGING,
    COMBAT,
    FARMING
}

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
    var town: UUID? = null,
    var miningSkillExp: Double = 0.0,
    var miningSkillLevel: Int = 1,
    var farmingSkillExp: Double = 0.0,
    var farmingSkillLevel: Int = 1,
    var combatSkillExp: Double = 0.0,
    var combatSkillLevel: Int = 1,
    var fishingSkillExp: Double = 0.0,
    var fishingSkillLevel: Int = 1,
    var loggingSkillExp: Double = 0.0,
    var loggingSkillLevel: Int = 1,
    var spelunker: Double = 0.0,
    var jackhammering: Double = 0.0,
    var harvesting: Double = 0.0,
    var slaughtering: Double = 0.0,
    var scavenging: Double = 0.0,
    var sweep: Double = 0.0,
    var angler: Double = 0.0
) {
    fun getUniqueId(): UUID {
        return this.uniqueId
    }

    fun addSkillExp(skill: Skill, amount: Double) {
        if (skill == Skill.MINING) {
            val levelUpExp = floor(250 * (3.5).pow(miningSkillLevel - 1))
            miningSkillExp += amount
            if (amount > 0.0) {
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    val percentage = floor((miningSkillExp / levelUpExp) * 100.0) / 100.0
                    player.sendActionBar(MiniMessage.miniMessage().deserialize("<aqua>⛏ Mining $miningSkillLevel (${floor(percentage * 100)}%)</aqua>"))
                }
            }
            if (miningSkillExp >= levelUpExp) {
                miningSkillExp -= levelUpExp
                miningSkillLevel += 1
                jackhammering += 0.5
                spelunker += 2
                addExp(Random.nextDouble(10.0, 50.0))
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    Chat.sendComponent(player, "<gold><bold>⛏ MINING LEVEL UP! ⛏</bold></gold>")
                    Chat.sendComponent(player, "<gray>(Level</gray> <aqua>${miningSkillLevel - 1} → ${miningSkillLevel}</aqua><gray>)</gray>")
                    Chat.sendComponent(player, " <dark_gray>■</dark_gray> <green>+0.5% <hover:show_text:'<reset>Allows you to get have a chance to mine a vein.'>Jackhammer</hover></green>")
                    Chat.sendComponent(player, " <dark_gray>■</dark_gray> <green>+2 <hover:show_text:'<reset>Allows you to get have a chance to gain more drops from ores.'>Spelunker</hover></green>")
                }
            }
        }
        if (skill == Skill.FARMING) {
            val levelUpExp = floor(250 * (3.5).pow(farmingSkillLevel - 1))
            farmingSkillExp += amount
            if (amount > 0.0) {
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    val percentage = floor((farmingSkillExp / levelUpExp) * 100.0) / 100.0
                    player.sendActionBar(MiniMessage.miniMessage().deserialize("<aqua>\uD83C\uDF3A Farming $farmingSkillLevel (${percentage * 100}%)</aqua>"))
                }
            }
            if (farmingSkillExp >= levelUpExp) {
                farmingSkillExp -= levelUpExp
                farmingSkillLevel += 1
                addExp(Random.nextDouble(10.0, 50.0))
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    Chat.sendComponent(player, "<gold><bold>\uD83C\uDF3A FARMING LEVEL UP! \uD83C\uDF3A</bold></gold>")
                    Chat.sendComponent(player, "<gray>(Level</gray> <aqua>${farmingSkillLevel - 1} → ${farmingSkillLevel}</aqua><gray>)</gray>")
                    Chat.sendComponent(player, " <dark_gray>■</dark_gray> <green>+2 <hover:show_text:'<reset>Allows you to get have a chance to gain more drops from farming.'>Harvest</hover></green>")
                }
            }
        }
    }

    fun addExp(amount: Double) {
        xp += amount
        if (xp >= 1000.0) {
            xp -= 1000.0
            level += 1
            if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
            }
            if (level % 5 == 0) {
                Chat.broadcast("&a${name} has leveled up to &eLevel ${level}&a!")
            } else {
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    Chat.sendComponent(Bukkit.getOfflinePlayer(uniqueId) as Player, "<green>Congratulations, you have leveled up to <yellow>Level ${level}</yellow>!</green>!")
                }
            }
            if (town != null) {
                val t = Brimstone.instance.townHandler.getTown(town!!)
                if (t != null) {
                    t.power += Random.nextInt(0, 100)
                    Brimstone.instance.townHandler.saveTown(t)
                }
            }
        }
    }

    fun getLastLocation(): Location {
        return Location(Bukkit.getWorld(lastLoginWorld), lastLoginLocationX, lastLoginLocationY, lastLoginLocationZ)
    }
}