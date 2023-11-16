package club.malvaceae.malloy.database.towns

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.lucko.helper.Events
import me.lucko.helper.Schedulers
import me.lucko.helper.profiles.plugin.external.caffeine.cache.Cache
import me.lucko.helper.profiles.plugin.external.caffeine.cache.Caffeine
import me.lucko.helper.promise.Promise
import org.bson.Document
import org.bukkit.OfflinePlayer
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerLoginEvent
import java.util.*

class ClaimHandler {
    val claimsMap: Cache<UUID, Claim> = Caffeine.newBuilder()
        .build()

    fun preload(amount: Int = 10000) {
        Schedulers.async().run {
            with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("claims")) {
                val claims = this.find().limit(amount)
                for (claim in claims) {
                    val t = lookupClaim(claim["uuid"] as UUID)
                    val ownerTown = club.malvaceae.malloy.Malloy.instance.townHandler.lookupTown(claim["townUniqueId"] as UUID)
                    if (ownerTown == null) {
                        Schedulers.async().run {
                            with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("claims")) {
                                val filter = Filters.eq("uuid", claim["uuid"] as UUID)
                                val document = this.find(filter).first()
                                if (document != null) {
                                    this.findOneAndDelete(filter)
                                    club.malvaceae.malloy.Malloy.instance.claimHandler.claimsMap.invalidate(claim["uuid"] as UUID)
                                    club.malvaceae.malloy.Malloy.log.info("[Claims] Deleted X: ${claim["x"] as Int}, Z: ${claim["z"] as Int} due to owner town not existing.")
                                }
                            }
                        }
                    }
                    t.get()?.let { saveClaim(it) }
                    club.malvaceae.malloy.Malloy.log.info("[Claims] Successfully preloaded X: ${t.get()!!.x}, Z: ${t.get()!!.z} (${t.get()!!.uniqueId})")
                }
            }
        }
    }

    init {
        preload()
    }

    fun updateCache(claim: Claim) {
        val existing: Claim? = this.claimsMap.getIfPresent(claim.uniqueId)
        if (existing == null) {
            this.claimsMap.put(claim.uniqueId, claim)
        }
    }

    fun getClaimAt(x: Int, z: Int): Promise<Claim?> {
        for (claim in this.claimsMap.asMap().values) {
            if (claim.x == x && claim.z == z) {
                return Promise.completed(claim)
            }
        }
        return Schedulers.async().supply {
            try {
                with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("claims")) {
                    val filter = Filters.and(
                        Filters.eq("x", x),
                        Filters.eq("z", z)
                    )
                    val document = this.find(filter).first()
                    if (document != null) {
                        val claim = Claim(
                            document["uuid"] as UUID,
                            document["claimedAt"] as Long,
                            document["x"] as Int,
                            document["z"] as Int,
                            document["health"] as Int,
                            document["world"] as String,
                            document["townUniqueId"] as UUID
                        )
                        updateCache(claim)
                        return@supply claim
                    } else {
                        return@supply null
                    }
                }
            } catch (e: MongoException) {
                e.printStackTrace()
                return@supply null
            }
        }
    }

    fun getClaim(uuid: UUID): Claim? {
        return this.claimsMap.getIfPresent(uuid)
    }

    fun saveClaim(claim: Claim) {
        with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("claims")) {
            val filter = Filters.eq("uuid", claim.uniqueId)
            val document = Document("uuid", claim.uniqueId)
                .append("claimedAt", claim.claimedAt)
                .append("x", claim.x)
                .append("z", claim.z)
                .append("health", claim.health)
                .append("world", claim.world)
                .append("townUniqueId", claim.townUniqueId)
            this.findOneAndReplace(filter, document, FindOneAndReplaceOptions().upsert(true))
        }
    }

    fun lookupClaim(uuid: UUID): Promise<Claim?> {
        val cachedClaim = getClaim(uuid)
        if (cachedClaim != null) {
            return Promise.completed(cachedClaim)
        }
        return Schedulers.async().supply {
            with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("claims")) {
                val filter = Filters.eq("uuid", uuid)
                val document = this.find(filter).first()
                if (document != null) {
                    val claim = Claim(
                        document["uuid"] as UUID,
                        document["claimedAt"] as Long,
                        document["x"] as Int,
                        document["z"] as Int,
                        document["health"] as Int,
                        document["world"] as String,
                        document["townUniqueId"] as UUID
                    )
                    updateCache(claim)
                    return@supply claim
                }
                return@supply null
            }
        }
    }
}

class TownHandler {
    val townsMap: Cache<UUID, Town> = Caffeine.newBuilder()
        .build()

    fun preload(amount: Int = 10000) {
        Schedulers.async().run {
            with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("towns")) {
                val towns = this.find().limit(amount)
                for (town in towns) {
                    val t = lookupTown(town["uuid"] as UUID)
                    saveTown(t.get())
                    club.malvaceae.malloy.Malloy.log.info("[Towns] Successfully preloaded ${t.get().name} (${t.get().uniqueId})")
                }
            }
        }
    }

    init {
        preload()
        Events.subscribe(PlayerLoginEvent::class.java, EventPriority.MONITOR)
            .filter { it.result == PlayerLoginEvent.Result.ALLOWED }
            .handler { event ->
                Promise.start()
                    .thenApplyAsync {
                        club.malvaceae.malloy.Malloy.instance.profileHandler.lookupProfile(event.player.uniqueId).get()
                    }
                    .thenAcceptAsync {
                        if (it.town != null) {
                            val t = lookupTown(it.town!!)
                            if (t.get() == null) {
                                it.town = null
                                return@thenAcceptAsync
                            }
                            saveTown(t.get())
                        }
                    }
            }
    }

    fun getTown(uniqueId: UUID): Town? {
        Objects.requireNonNull(uniqueId, "uniqueId")
        return townsMap.getIfPresent(uniqueId)
    }

    fun getPlayerTown(player: OfflinePlayer): Town? {
        val p = club.malvaceae.malloy.Malloy.instance.profileHandler.lookupProfile(player.uniqueId).get()
        return if (p.town == null) {
            null
        } else {
            lookupTown(p.town!!).get()
        }
    }

    fun saveTown(town: Town) {
        with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("towns")) {
            val filter = Filters.eq("uuid", town.uniqueId)
            val document = Document("uuid", town.uniqueId)
                .append("name", town.name)
                .append("owner", town.owner)
                .append("members", town.members)
                .append("gold", town.gold)
                .append("tax", town.tax)
                .append("power", town.power)
                .append("createdAt", town.createdAt)
            this.findOneAndReplace(filter, document, FindOneAndReplaceOptions().upsert(true))
        }
    }

    fun updateCache(town: Town) {
        val existing: Town? = this.townsMap.getIfPresent(town.uniqueId)
        if (existing == null) {
            this.townsMap.put(town.uniqueId, town)
        }
    }

    fun checkIfAvailable(name: String) : Promise<Boolean> {
        return Schedulers.async().supply {
            try {
                with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("towns")) {
                    val filter = Filters.eq("name", name)
                    val documents = this.find(filter).toList()
                    return@supply documents.isEmpty()
                }
            } catch (e: MongoException) {
                e.printStackTrace()
                return@supply true
            }
            return@supply true
        }
    }

    fun lookupTown(uuid: UUID): Promise<Town> {
        val town = getTown(uuid)
        if (town != null) {
            return Promise.completed(town)
        }
        return Schedulers.async().supply {
            try {
                with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("towns")) {
                    val filter = Filters.eq("uuid", uuid)
                    val document = this.find(filter).first()
                    val t: Town
                    if (document != null) {
                        t = Town(
                            uuid,
                            (document["owner"] as UUID),
                            (document["name"] as String),
                            (document["members"] as List<Document>),
                            (document["gold"] as Double),
                            ((document["tax"] as Double?) ?: 0.0),
                            ((document["power"] as Double?) ?: 0.0),
                            ((document["createdAt"] as Long?) ?: System.currentTimeMillis())
                        )
                    } else {
                        return@supply null
                    }
                    updateCache(t)
                    return@supply t
                }
            } catch (e: MongoException) {
                e.printStackTrace()
            }
            return@supply null
        }
    }
}