package club.piglin.brimstone.database.towns

import club.piglin.brimstone.Brimstone
import org.bson.Document
import org.bukkit.Chunk
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
    var claims: List<Document> = listOf<Document>(),
    var gold: Double,
    var nextFee: Long = System.currentTimeMillis() + 86400000
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

    fun addClaim(c: Chunk): Claim {
        val list = ArrayList(this.claims)
        val chunk = Claim(
            UUID.randomUUID(),
            System.currentTimeMillis(),
            c.x,
            c.z
        )
        list.add(Document("uniqueId", chunk.uniqueId)
            .append("claimedAt", chunk.claimedAt)
            .append("x", chunk.x)
            .append("z", chunk.z)
        )
        this.claims = list
        Brimstone.instance.townHandler.saveTown(this)
        return chunk
    }

    fun getClaim(uuid: UUID): Claim? {
        for (document in this.claims) {
            if (document.containsValue(uuid)) {
                return Claim(
                    uuid,
                    document.getLong("claimedAt"),
                    document.getInteger("x"),
                    document.getInteger("z")
                )
            }
        }
        return null
    }
}