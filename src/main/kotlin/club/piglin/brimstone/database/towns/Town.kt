package club.piglin.brimstone.database.towns

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.utils.Chat
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.lucko.helper.Schedulers
import me.lucko.helper.promise.Promise
import org.bson.Document
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*
import kotlin.random.Random

class Claim(
    val uniqueId: UUID,
    val claimedAt: Long,
    val x: Int,
    val z: Int,
    val health: Int,
    val world: String,
    val townUniqueId: UUID
)

class Member(
    val uniqueId: UUID,
    val joinedAt: Long,
    var role: String,
    var goldDeposited: Double
)

class Town(
    @JvmField val uniqueId: UUID,
    var owner: UUID,
    var name: String? = "Unnamed Town",
    var members: List<Document> = listOf(),
    var gold: Double = 0.0,
    var tax: Double = 0.0,
    var power: Double
) {
    fun getMember(uuid: UUID): Member? {
        for (document in this.members) {
            if (document.containsValue(uuid)) {
                return Member(
                    uuid,
                    document.getLong("joinedAt"),
                    document.getString("role"),
                    document.getDouble("goldDeposited")
                )
            }
        }
        return null
    }

    fun sendMessage(message: String) {
        val list = ArrayList(this.members)
        for (document in list) {
            val player = Bukkit.getOfflinePlayer(document["uniqueId"] as UUID)
            if (player.isOnline) {
                Chat.sendMessage(player as Player, "&9[Town]&r $message")
            }
        }
    }

    fun saveMember(member: Member) {
        val list = ArrayList(this.members)
        for ((index, document) in list.withIndex()) {
            if ((document["uniqueId"] as UUID) == member.uniqueId) {
                val doc = Document("uniqueId", member.uniqueId)
                    .append("role", member.role)
                    .append("joinedAt", member.joinedAt)
                    .append("goldDeposited", member.goldDeposited)
                list[index] = doc
                this.members = list
                return
            }
        }
    }

    fun depositGold(player: Player, gold: Double) {
        val profile = Brimstone.instance.profileHandler.lookupProfile(player.uniqueId).get() ?: throw Error("Couldn't find player's profile.")
        if (profile.gold >= gold) {
            profile.gold -= gold
            val member = getMember(profile.uniqueId)
            if (member != null) {
                member.goldDeposited += gold
                saveMember(member)
            }
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
                .append("goldDeposited", 0.0)
        )
        this.members = list
        profile.town = uniqueId
        sendMessage("&e${player.name}&a joined your town!")
        Brimstone.instance.profileHandler.saveProfile(profile)
        Brimstone.instance.townHandler.saveTown(this)
    }

    fun getClaims(): Promise<List<Document>> {
        return Schedulers.async().supply {
            with (Brimstone.instance.dataSource.getDatabase("piglin").getCollection("claims")) {
                try {
                    val filter = Filters.eq("townUniqueId", uniqueId)
                    return@supply find(filter).toList()
                } catch (e: MongoException) {
                    return@supply listOf()
                }
            }
        }
    }

    fun claimChunk(chunk: Chunk): Promise<Claim?> {
        return Schedulers.async().supply {
            with (Brimstone.instance.dataSource.getDatabase("piglin").getCollection("claims")) {
                try {
                    val filter = Filters.and(
                        Filters.eq("x", chunk.x),
                        Filters.eq("z", chunk.z)
                    )
                    val documents = this.find(filter)
                    if (documents.toList().isNotEmpty()) {
                        return@supply null
                    }
                    val claim = Claim(
                        UUID.randomUUID(),
                        System.currentTimeMillis(),
                        chunk.x,
                        chunk.z,
                        100,
                        chunk.world.name,
                        uniqueId
                    )
                    val document = Document("uuid", claim.uniqueId)
                        .append("claimedAt", claim.claimedAt)
                        .append("x", claim.x)
                        .append("z", claim.z)
                        .append("health", claim.health)
                        .append("world", claim.world)
                        .append("townUniqueId", claim.townUniqueId)
                    this.findOneAndReplace(filter, document, FindOneAndReplaceOptions().upsert(true))
                    power += Random.nextInt(200, 450)
                    Brimstone.instance.townHandler.saveTown(this@Town)
                    return@supply claim
                } catch (e: MongoException) {
                    return@supply null
                }
            }
        }
    }

    fun removePlayer(player: OfflinePlayer) {
        val profile = Brimstone.instance.profileHandler.lookupProfile(player.uniqueId).get() ?: throw Error("Couldn't find player's profile.")
        val list = ArrayList(this.members)
        val toRemove = ArrayList<Document>()
        for (member in list) {
           if (member["uniqueId"] as UUID == player.uniqueId) {
               toRemove.add(member)
           }
        }
        list.removeAll(toRemove)
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