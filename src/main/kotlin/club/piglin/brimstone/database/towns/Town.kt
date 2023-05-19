package club.piglin.brimstone.database.towns

import club.piglin.brimstone.Brimstone
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class Claim(
    val uniqueId: UUID,
    val claimedAt: Long,
    val x: Int,
    val z: Int
)

class Town(
    @JvmField val uniqueId: UUID,
    var owner: UUID,
    var name: String? = "Unnamed Town",
    var members: List<UUID> = listOf(),
    var gold: Double
) {
    fun depositGold(player: Player, gold: Double) {
        val profile = Brimstone.instance.profileHandler.lookupProfile(player.uniqueId).get() ?: throw Error("Couldn't find player's profile.")
        if (profile.gold >= gold) {
            profile.gold -= gold
            this.gold += gold
        } else {
            return
        }
        Brimstone.instance.townHandler.saveTown(this)
    }

    fun addPlayer(player: OfflinePlayer) {
        val profile = Brimstone.instance.profileHandler.lookupProfile(player.uniqueId).get() ?: throw Error("Couldn't find player's profile.")
        val list = ArrayList(this.members)
        list.add(player.uniqueId)
        this.members = list
        profile.town = uniqueId
        Brimstone.instance.profileHandler.saveProfile(profile)
        Brimstone.instance.townHandler.saveTown(this)
    }

    fun removePlayer(player: OfflinePlayer) {
        val profile = Brimstone.instance.profileHandler.lookupProfile(player.uniqueId).get() ?: throw Error("Couldn't find player's profile.")
        val list = ArrayList(this.members)
        list.remove(player.uniqueId)
        this.members = list
        profile.town = null
        Brimstone.instance.profileHandler.saveProfile(profile)
        Brimstone.instance.townHandler.saveTown(this)
    }
}