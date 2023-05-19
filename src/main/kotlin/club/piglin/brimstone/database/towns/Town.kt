package club.piglin.brimstone.database.towns

import club.piglin.brimstone.Brimstone
import com.mongodb.client.model.Filters
import me.lucko.helper.Schedulers
import org.bson.Document
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class Claim(
    val uniqueId: UUID,
    val claimedAt: Long,
    val x: Int,
    val z: Int
)

class Member(
    val uniqueId: UUID,
    val joinedAt: Long,
    val role: String
)

class Town(
    @JvmField val uniqueId: UUID,
    var owner: UUID,
    var name: String? = "Unnamed Town",
    var members: List<Document> = listOf(),
    var gold: Double
) {
    fun getMember(uuid: UUID): Member? {
        for (document in this.members) {
            if (document.containsValue(uuid)) {
                return Member(
                    uuid,
                    document.getLong("joinedAt"),
                    document.getString("role")
                )
            }
        }
        return null
    }

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
        list.add(
            Document("uniqueId", player.uniqueId)
                .append("role", "resident")
                .append("joinedAt", System.currentTimeMillis())
        )
        this.members = list
        profile.town = uniqueId
        Brimstone.instance.profileHandler.saveProfile(profile)
        Brimstone.instance.townHandler.saveTown(this)
    }

    fun removePlayer(player: OfflinePlayer) {
        val profile = Brimstone.instance.profileHandler.lookupProfile(player.uniqueId).get() ?: throw Error("Couldn't find player's profile.")
        val list = ArrayList(this.members)
        for (member in list) {
           if (member["uniqueId"] as UUID == player.uniqueId) {
               list.remove(member)
           }
        }
        this.members = list
        profile.town = null
        Brimstone.instance.profileHandler.saveProfile(profile)
        Brimstone.instance.townHandler.saveTown(this)
        val uuid = this.uniqueId
        if (this.members.isEmpty()) {
            Schedulers.async().run {
                with (Brimstone.instance.dataSource.getDatabase("piglin").getCollection("towns")) {
                    val filter = Filters.eq("uuid", uuid)
                    val document = this.find(filter).first()
                    if (document != null) {
                        this.findOneAndDelete(filter)
                        Brimstone.instance.townHandler.townsMap.invalidate(uuid)
                        Brimstone.log.info("[Towns] Deleted $uuid due to all members leaving.")
                    }
                }
            }
        }
    }
}