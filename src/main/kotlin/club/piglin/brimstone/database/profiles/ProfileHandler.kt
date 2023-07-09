package club.piglin.brimstone.database.profiles

import club.piglin.brimstone.Brimstone
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.lucko.helper.Events
import me.lucko.helper.Schedulers
import me.lucko.helper.profiles.plugin.external.caffeine.cache.Cache
import me.lucko.helper.profiles.plugin.external.caffeine.cache.Caffeine
import me.lucko.helper.promise.Promise
import org.bson.Document
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.sql.Timestamp
import java.util.*
import java.util.regex.Pattern

class ProfileHandler {
    private val profilesMap: Cache<UUID, Profile> = Caffeine.newBuilder()
        .build()


    private val MINECRAFT_USERNAME_PATTERN: Pattern = Pattern.compile("^\\w{3,16}$")
    private fun isValidMcUsername(s: String): Boolean {
        return MINECRAFT_USERNAME_PATTERN.matcher(s).matches()
    }

    init {
        Events.subscribe(PlayerLoginEvent::class.java, EventPriority.MONITOR)
            .filter { it.result == PlayerLoginEvent.Result.ALLOWED }
            .handler { event ->
                Promise.start()
                    .thenApplySync {
                        lookupProfile(event.player.uniqueId)
                    }
                    .thenAcceptAsync {
                        it.get().name = event.player.name
                        it.get().lastLogin = System.currentTimeMillis()
                        saveProfile(it.get())
                        updateCache(it.get())
                        Brimstone.log.info("[Profiles] ${event.player.name} joined, loading & saving ${event.player.name}'s profile")
                    }

            }
        Events.subscribe(PlayerQuitEvent::class.java, EventPriority.MONITOR)
            .handler { event ->
                Promise.start()
                    .thenApplySync {
                        lookupProfile(event.player.uniqueId)
                    }
                    .thenAcceptAsync {
                        it.get().name = event.player.name
                        it.get().lastLoginLocationX = event.player.location.x
                        it.get().lastLoginLocationY = event.player.location.y
                        it.get().lastLoginLocationZ = event.player.location.z
                        it.get().lastLoginWorld = event.player.world.name
                        saveProfile(it.get())
                        updateCache(it.get())
                        Brimstone.log.info("[Profiles] ${event.player.name} quit, saving ${event.player.name}'s profile")
                    }
            }
        Brimstone.log.info("[Profiles] Now monitoring for profile data.")
    }

    fun saveProfile(profile: Profile) {
        with (Brimstone.instance.dataSource.getDatabase("piglin").getCollection("profiles")) {
            val filter = Filters.eq("uuid", profile.uniqueId)
            val document = Document("uuid", profile.uniqueId)
                .append("name", profile.name)
                .append("lastLogin", Timestamp(profile.lastLogin))
                .append("firstJoin", Timestamp(profile.firstJoin))
                .append("lastLoginLocationX", profile.lastLoginLocationX)
                .append("lastLoginLocationY", profile.lastLoginLocationY)
                .append("lastLoginLocationZ", profile.lastLoginLocationZ)
                .append("lastLoginWorld", profile.lastLoginWorld)
                .append("xp", profile.xp)
                .append("level", profile.level)
                .append("gold", profile.gold)
                .append("town", profile.town)
                .append("miningSkillExp", profile.miningSkillExp)
                .append("miningSkillLevel", profile.miningSkillLevel)
                .append("farmingSkillExp", profile.farmingSkillExp)
                .append("farmingSkillLevel", profile.farmingSkillLevel)
                .append("combatSkillExp", profile.combatSkillExp)
                .append("combatSkillLevel", profile.combatSkillLevel)
                .append("fishingSkillExp", profile.fishingSkillExp)
                .append("fishingSkillLevel", profile.fishingSkillLevel)
                .append("loggingSkillExp", profile.loggingSkillExp)
                .append("loggingSkillLevel", profile.loggingSkillLevel)
                .append("spelunker", profile.spelunker)
                .append("harvesting", profile.harvesting)
                .append("slaughtering", profile.slaughtering)
                .append("scavenging", profile.scavenging)
                .append("angler", profile.angler)
            this.findOneAndReplace(filter, document, FindOneAndReplaceOptions().upsert(true))
        }
        Brimstone.log.info("[Profiles] ${profile.name}'s profile was just saved.")
    }

    fun getProfile(uniqueId: UUID): Profile? {
        Objects.requireNonNull(uniqueId, "uniqueId")
        return profilesMap.getIfPresent(uniqueId)
    }

    fun lookupProfile(uniqueId: UUID): Promise<Profile> {
        Objects.requireNonNull(uniqueId, "uniqueId")
        val profile = getProfile(uniqueId)
        if (profile != null) {
            return Promise.completed(profile)
        }
        return Schedulers.async().supply {
            try {
                with (Brimstone.instance.dataSource.getDatabase("piglin").getCollection("profiles")) {
                    val filter = Filters.eq("uuid", uniqueId)
                    val document = this.find(filter).first()
                    val p: Profile
                    if (document != null) {
                        p = Profile(
                            uniqueId,
                            (document["name"] as String),
                            ((document["lastLogin"] as Date?)?.time) ?: System.currentTimeMillis(),
                            ((document["firstJoin"] as Date?)?.time) ?: System.currentTimeMillis(),
                            (document["lastLoginLocationX"] as Double?) ?: 0.0,
                            (document["lastLoginLocationY"] as Double?) ?: 100.0,
                            (document["lastLoginLocationZ"] as Double?) ?: 0.0,
                            (document["lastLoginWorld"] as String?) ?: "world",
                            (document["gold"] as Double?) ?: 0.0,
                            (document["xp"] as Double?) ?: 0.0,
                            (document["level"] as Int?) ?: 0,
                            (document["town"] as UUID?),
                            (document["miningSkillExp"] as Double?) ?: 0.0,
                            (document["miningSkillLevel"] as Int?) ?: 1,
                            (document["farmingSkillExp"] as Double?) ?: 0.0,
                            (document["farmingSkillLevel"] as Int?) ?: 1,
                            (document["combatSkillExp"] as Double?) ?: 0.0,
                            (document["combatSkillLevel"] as Int?) ?: 1,
                            (document["fishingSkillExp"] as Double?) ?: 0.0,
                            (document["fishingSkillLevel"] as Int?) ?: 1,
                            (document["loggingSkillExp"] as Double?) ?: 0.0,
                            (document["loggingSkillLevel"] as Int?) ?: 1,
                            (document["spelunker"] as Double?) ?: 0.0,
                            (document["harvesting"] as Double?) ?: 0.0,
                            (document["slaughtering"] as Double?) ?: 0.0,
                            (document["scavenging"] as Double?) ?: 0.0,
                            (document["angler"] as Double?) ?: 0.0,

                            )
                    } else {
                        p = Profile(
                            uniqueId
                        )
                    }
                    updateCache(p)
                    Brimstone.log.info("[Profiles] ${p.name}'s profile was just loaded.")
                    return@supply p
                }
            } catch (e: MongoException) {
                e.printStackTrace()
            }
            return@supply null
        }
    }

    private fun updateCache(profile: Profile) {
        val existing: Profile? = this.profilesMap.getIfPresent(profile.uniqueId)
        if (existing == null || existing.lastLogin < profile.lastLogin) {
            this.profilesMap.put(profile.uniqueId, profile)
        }
    }
}