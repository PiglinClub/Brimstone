package club.malvaceae.malloy.database.profiles

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.utils.Chat
import net.dv8tion.jda.api.entities.User
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
    var gold: Double = 1000.0,
    var xp: Double = 0.0,
    var level: Int = 0,
    var town: UUID? = null,
    var chatMode: String = "PUBLIC",
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
    var harvesting: Double = 0.0,
    var slaughtering: Double = 0.0,
    var scavenging: Double = 0.0,
    var angler: Double = 0.0,
    var job: String? = null,
    var votes: Int = 0,
    var discordId: String? = null,
    var playtime: Long = 0L
) {
    fun getUniqueId(): UUID {
        return this.uniqueId
    }

    fun getDiscord(): User? {
        if (discordId == null) return null
        return Malloy.instance.jda.getUserById(discordId!!)
    }

    fun addSkillExp(skill: Skill, amount: Double) {
        if (skill == Skill.MINING) {
            val levelUpExp = floor(250 * (1.15).pow(miningSkillLevel - 1))
            miningSkillExp += amount
            if (amount > 0.0) {
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    val percentage = floor((miningSkillExp / levelUpExp) * 100.0) / 100.0
                    val left = floor(levelUpExp - miningSkillExp)
                    player.sendActionBar(MiniMessage.miniMessage().deserialize("<aqua>⛏ Mining $miningSkillLevel ${percentage * 100}% (${left}xp left)</aqua>"))
                }
            }
            if (miningSkillExp >= levelUpExp) {
                miningSkillExp -= levelUpExp
                miningSkillLevel += 1
                spelunker += 2
                addExp(Random.nextDouble(100.0, 175.0))
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    Chat.sendComponent(player, "<gold><bold>⛏ MINING LEVEL UP! ⛏</bold></gold>")
                    Chat.sendComponent(player, "<gray>(Level</gray> <aqua>${miningSkillLevel - 1} → ${miningSkillLevel}</aqua><gray>)</gray>")
                    Chat.sendComponent(player, " <dark_gray>■</dark_gray> <green>+2 <hover:show_text:'<reset>Allows you to get have a chance to gain more drops from ores.'>Spelunker</hover></green>")
                }
            }
        }
        if (skill == Skill.FARMING) {
            val levelUpExp = floor(250 * (1.15).pow(farmingSkillLevel - 1))
            farmingSkillExp += amount
            if (amount > 0.0) {
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    val percentage = floor((farmingSkillExp / levelUpExp) * 100.0) / 100.0
                    val left = floor(levelUpExp - farmingSkillExp)
                    player.sendActionBar(MiniMessage.miniMessage().deserialize("<aqua>\uD83C\uDF3A Farming $farmingSkillLevel ${percentage * 100}% (${left}xp left)</aqua>"))
                }
            }
            if (farmingSkillExp >= levelUpExp) {
                farmingSkillExp -= levelUpExp
                farmingSkillLevel += 1
                harvesting += 2
                addExp(Random.nextDouble(100.0, 175.0))
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    Chat.sendComponent(player, "<gold><bold>\uD83C\uDF3A FARMING LEVEL UP! \uD83C\uDF3A</bold></gold>")
                    Chat.sendComponent(player, "<gray>(Level</gray> <aqua>${farmingSkillLevel - 1} → ${farmingSkillLevel}</aqua><gray>)</gray>")
                    Chat.sendComponent(player, " <dark_gray>■</dark_gray> <green>+2 <hover:show_text:'<reset>Allows you to get have a chance to gain more drops from farming.'>Harvesting</hover></green>")
                }
            }
        }
        if (skill == Skill.COMBAT) {
            val levelUpExp = floor(250 * (1.15).pow(combatSkillLevel - 1))
            combatSkillExp += amount
            if (amount > 0.0) {
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    val percentage = floor((combatSkillExp / levelUpExp) * 100.0) / 100.0
                    val left = floor(levelUpExp - combatSkillExp)
                    player.sendActionBar(MiniMessage.miniMessage().deserialize("<aqua>\uD83D\uDDE1 Combat $combatSkillLevel ${percentage * 100}% (${left}xp left)</aqua>"))
                }
            }
            if (combatSkillExp >= levelUpExp) {
                combatSkillExp -= levelUpExp
                combatSkillLevel += 1
                slaughtering += 2
                addExp(Random.nextDouble(100.0, 175.0))
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    Chat.sendComponent(player, "<gold><bold>\uD83D\uDDE1 COMBAT LEVEL UP! \uD83D\uDDE1</bold></gold>")
                    Chat.sendComponent(player, "<gray>(Level</gray> <aqua>${combatSkillLevel - 1} → ${combatSkillLevel}</aqua><gray>)</gray>")
                    Chat.sendComponent(player, " <dark_gray>■</dark_gray> <green>+2% <hover:show_text:'<reset>Allows you to get have a chance to gain more drops from slaughtering.'>Slaughtering</hover></green>")
                }
            }
        }
        if (skill == Skill.LOGGING) {
            val levelUpExp = floor(250 * (1.15).pow(loggingSkillLevel - 1))
            loggingSkillExp += amount
            if (amount > 0.0) {
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    val percentage = floor((loggingSkillExp / levelUpExp) * 100.0) / 100.0
                    val left = floor(levelUpExp - loggingSkillExp)
                    player.sendActionBar(MiniMessage.miniMessage().deserialize("<aqua>\uD83E\uDE93 Logging $loggingSkillLevel ${percentage * 100}% (${left}xp left)</aqua>"))
                }
            }
            if (loggingSkillExp >= levelUpExp) {
                loggingSkillExp -= levelUpExp
                loggingSkillLevel += 1
                scavenging += 2
                addExp(Random.nextDouble(100.0, 175.0))
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    Chat.sendComponent(player, "<gold><bold>\uD83E\uDE93 LOGGING LEVEL UP! \uD83E\uDE93</bold></gold>")
                    Chat.sendComponent(player, "<gray>(Level</gray> <aqua>${loggingSkillLevel - 1} → ${loggingSkillLevel}</aqua><gray>)</gray>")
                    Chat.sendComponent(player, " <dark_gray>■</dark_gray> <green>+2% <hover:show_text:'<reset>Allows you to get have a chance to gain more drops from logging.'>Scavenging</hover></green>")
                }
            }
        }
        if (skill == Skill.FISHING) {
            val levelUpExp = floor(250 * (1.15).pow(fishingSkillLevel - 1))
            fishingSkillExp += amount
            if (amount > 0.0) {
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    val percentage = floor((fishingSkillExp / levelUpExp) * 100.0) / 100.0
                    val left = floor(levelUpExp - fishingSkillExp)
                    player.sendActionBar(MiniMessage.miniMessage().deserialize("<aqua>\uD83C\uDFA3 Fishing $fishingSkillLevel ${percentage * 100}% (${left}xp left)</aqua>"))
                }
            }
            if (fishingSkillExp >= levelUpExp) {
                fishingSkillExp -= levelUpExp
                fishingSkillLevel += 1
                angler += 2
                addExp(Random.nextDouble(100.0, 175.0))
                if (Bukkit.getOfflinePlayer(uniqueId).isOnline) {
                    val player = Bukkit.getOfflinePlayer(uniqueId) as Player
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    Chat.sendComponent(player, "<gold><bold>\uD83C\uDFA3 FISHING LEVEL UP! \uD83C\uDFA3</bold></gold>")
                    Chat.sendComponent(player, "<gray>(Level</gray> <aqua>${fishingSkillLevel - 1} → ${fishingSkillLevel}</aqua><gray>)</gray>")
                    Chat.sendComponent(player, " <dark_gray>■</dark_gray> <green>+2% <hover:show_text:'<reset>Allows you to get have a chance to gain more drops from fishing.'>Angler</hover></green>")
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
                val t = club.malvaceae.malloy.Malloy.instance.townHandler.getTown(town!!)
                if (t != null) {
                    t.power += Random.nextInt(0, 100)
                }
            }
        }
    }

    fun getLastLocation(): Location {
        return Location(Bukkit.getWorld(lastLoginWorld), lastLoginLocationX, lastLoginLocationY, lastLoginLocationZ)
    }
}