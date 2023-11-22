package club.malvaceae.malloy.database.towns

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.utils.Chat
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.lucko.helper.Schedulers
import me.lucko.helper.promise.Promise
import org.bson.Document
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.OfflinePlayer
import org.bukkit.World
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
    var goldDeposited: Double,
    var hijacked: Boolean // This is a secret property for members disallowing town management from kicking members that hijacked into the town.
)

class Town(
    @JvmField val uniqueId: UUID,
    var owner: UUID,
    var name: String? = "Unnamed Town",
    var members: List<Document> = listOf(),
    var gold: Double = 0.0,
    var tax: Double = 0.0,
    var power: Double,
    var createdAt: Long,
    var adminOnly: Boolean,
    var homeX: Double?,
    var homeY: Double?,
    var homeZ: Double?,
    var homeWorld: String?
) {
    fun getMember(uuid: UUID): Member? {
        for (document in this.members) {
            if (document.containsValue(uuid)) {
                return Member(
                    uuid,
                    document.getLong("joinedAt"),
                    document.getString("role"),
                    document.getDouble("goldDeposited"),
                    document.getBoolean("hijacked")
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
                Chat.sendComponent(player as Player, "<blue>[Town]<reset> $message")
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
                    .append("hijacked", member.hijacked)
                list[index] = doc
                this.members = list
                return
            }
        }
        Malloy.instance.townHandler.saveTown(this)
    }

    fun depositGold(player: Player, gold: Double) {
        val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.lookupProfile(player.uniqueId).get() ?: throw Error("Couldn't find player's profile.")
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
        club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(this)
    }

    fun addPlayer(player: OfflinePlayer, hijacked: Boolean = false) {
        val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.lookupProfile(player.uniqueId).get() ?: throw Error("Couldn't find player's profile.")
        val list = ArrayList(this.members)
        list.add(
            Document("uniqueId", player.uniqueId)
                .append("role", "resident")
                .append("joinedAt", System.currentTimeMillis())
                .append("goldDeposited", 0.0)
                .append("hijacked", hijacked)
        )
        this.members = list
        profile.town = uniqueId
        sendMessage("<green><yellow>${player.name}</yellow> joined your town!")
        club.malvaceae.malloy.Malloy.instance.profileHandler.saveProfile(profile)
        club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(this)
    }

    fun getClaimsInWorld(world: World): Promise<ArrayList<Claim>> {
        return Schedulers.async().supply {
            with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("claims")) {
                try {
                    val filter = Filters.and(
                        Filters.eq("world", world.name),
                        Filters.eq("townUniqueId", uniqueId)
                    )
                    val list = arrayListOf<Claim>()
                    for (document in find(filter).toList()) {
                        val claim = Claim(
                            document["uuid"] as UUID,
                            document["claimedAt"] as Long,
                            document["x"] as Int,
                            document["z"] as Int,
                            document["health"] as Int,
                            document["world"] as String,
                            document["townUniqueId"] as UUID
                        )
                        list.add(claim)
                    }
                    return@supply list
                } catch (e: MongoException) {
                    e.printStackTrace()
                    return@supply arrayListOf()
                }
            }
        }
    }

    fun getClaims(): Promise<ArrayList<Claim>> {
        return Schedulers.async().supply {
            with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("claims")) {
                try {
                    val filter = Filters.eq("townUniqueId", uniqueId)
                    val list = arrayListOf<Claim>()
                    for (document in find(filter).toList()) {
                        val claim = Claim(
                            document["uuid"] as UUID,
                            document["claimedAt"] as Long,
                            document["x"] as Int,
                            document["z"] as Int,
                            document["health"] as Int,
                            document["world"] as String,
                            document["townUniqueId"] as UUID
                        )
                        list.add(claim)
                    }
                    return@supply list
                } catch (e: MongoException) {
                    e.printStackTrace()
                    return@supply arrayListOf()
                }
            }
        }
    }

    fun doWeOwnChunk(world: String, x: Int, z: Int) : Promise<Boolean> {
        return Schedulers.async().supply {
            with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("claims")) {
                try {
                    val filter = Filters.and(
                        Filters.eq("x", x),
                        Filters.eq("z", z),
                        Filters.eq("world", world),
                        Filters.eq("townUniqueId", uniqueId)
                    )
                    val documents = this.find(filter).toList()
                    if (documents.size == 0) {
                        return@supply false
                    } else {
                        return@supply true
                    }
                } catch (e: MongoException) {
                    e.printStackTrace()
                    return@supply false
                }
            }
        }
    }

    fun unclaimChunk(chunk: Chunk): Promise<Boolean?> {
        return Schedulers.async().supply {
            with(club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("claims")) {
                try {
                    val filter = Filters.and(
                        Filters.eq("x", chunk.x),
                        Filters.eq("z", chunk.z),
                        Filters.eq("world", chunk.world.name)
                    )
                    val documents = this.find(filter)
                    if (documents.toList().isEmpty()) {
                        return@supply true
                    }
                    if (documents.first()!!["townUniqueId"] != uniqueId) {
                        return@supply false
                    }
                    this.findOneAndDelete(filter)
                    power -= 450
                    Malloy.instance.townHandler.saveTown(this@Town)
                    return@supply true
                } catch (e: MongoException) {
                    e.printStackTrace()
                    return@supply false
                }
            }
        }
    }

    fun claimChunk(chunk: Chunk): Promise<Claim?> {
        return Schedulers.async().supply {
            with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("claims")) {
                try {
                    val filter = Filters.and(
                        Filters.eq("x", chunk.x),
                        Filters.eq("z", chunk.z),
                        Filters.eq("world", chunk.world)
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
                    club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(this@Town)
                    return@supply claim
                } catch (e: MongoException) {
                    return@supply null
                }
            }
        }
    }

    fun removePlayer(player: OfflinePlayer) {
        val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.lookupProfile(player.uniqueId).get() ?: throw Error("Couldn't find player's profile.")
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
        club.malvaceae.malloy.Malloy.instance.profileHandler.saveProfile(profile)
        club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(this)
        val uuid = this.uniqueId
        if (this.members.isEmpty() && !this.adminOnly) {
            Schedulers.async().run {
                with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("towns")) {
                    val filter = Filters.eq("uuid", uuid)
                    val document = this.find(filter).first()
                    if (document != null) {
                        this.findOneAndDelete(filter)
                        club.malvaceae.malloy.Malloy.instance.townHandler.townsMap.invalidate(uuid)
                        club.malvaceae.malloy.Malloy.log.info("[Towns] Deleted $uuid due to all members leaving.")
                    }
                }
            }
        }
    }
}