package club.malvaceae.malloy.database.profiles

import club.malvaceae.malloy.utils.Settings
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.lucko.helper.Events
import me.lucko.helper.Schedulers
import me.lucko.helper.profiles.plugin.external.caffeine.cache.Cache
import me.lucko.helper.profiles.plugin.external.caffeine.cache.Caffeine
import me.lucko.helper.promise.Promise
import org.bson.Document
import org.bukkit.Bukkit
import org.bukkit.Location
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
        val sessions = HashMap<UUID, Long>()
        Events.subscribe(PlayerLoginEvent::class.java, EventPriority.MONITOR)
            .filter { it.result == PlayerLoginEvent.Result.ALLOWED }
            .handler { event ->
                Promise.start()
                    .thenApplySync {
                        if (!exists(event.player.uniqueId)) {
                            Schedulers.sync().runLater({
                                event.player.teleportAsync(Location(Bukkit.getWorld("world"), Settings.data.getDouble("spawn.x"), Settings.data.getDouble("spawn.y"), Settings.data.getDouble("spawn.z"), Settings.data.getDouble("spawn.yaw").toFloat(), Settings.data.getDouble("spawn.pitch").toFloat()))
                            }, 20L)
                        }
                        lookupProfile(event.player.uniqueId)
                    }
                    .thenAcceptAsync {
                        it.get().name = event.player.name
                        it.get().lastLogin = System.currentTimeMillis()
                        sessions[event.player.uniqueId] = System.currentTimeMillis()
                        saveProfile(it.get())
                        updateCache(it.get())
                        club.malvaceae.malloy.Malloy.log.info("[Profiles] ${event.player.name} joined, loading & saving ${event.player.name}'s profile")
                    }
            }
        Events.subscribe(PlayerQuitEvent::class.java, EventPriority.MONITOR)
            .handler { event ->
                Promise.start()
                    .thenApplySync {
                        lookupProfile(event.player.uniqueId).get()
                    }
                    .thenAcceptAsync {
                        it.name = event.player.name
                        it.lastLoginLocationX = event.player.location.x
                        it.lastLoginLocationY = event.player.location.y
                        it.lastLoginLocationZ = event.player.location.z
                        it.lastLoginWorld = event.player.world.name
                        it.playtime += System.currentTimeMillis() - sessions[event.player.uniqueId]!!
                        sessions.remove(event.player.uniqueId)
                        saveProfile(it)
                        updateCache(it)
                        club.malvaceae.malloy.Malloy.log.info("[Profiles] ${event.player.name} quit, saving ${event.player.name}'s profile")
                    }
            }
        club.malvaceae.malloy.Malloy.log.info("[Profiles] Now monitoring for profile data.")
    }

    fun preload(amount: Int = 10000) {
        Schedulers.async().run {
            with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("profiles")) {
                val profiles = this.find().limit(amount)
                for (profile in profiles) {
                    val p = lookupProfile(profile["uuid"] as UUID)
                    saveProfile(p.get())
                    club.malvaceae.malloy.Malloy.log.info("[Profiles] Successfully preloaded ${p.get().name} (${p.get().uniqueId})")
                }
            }
        }
    }

    init {
        preload()
    }

    fun exists(uniqueId: UUID): Boolean {
        return (getProfile(uniqueId) != null)
    }

    fun saveProfile(profile: Profile) {
        with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("profiles")) {
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
                .append("chatMode", profile.chatMode)
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
                .append("job", profile.job)
                .append("votes", profile.votes)
                .append("discordId", profile.discordId)
                .append("playtime", profile.playtime)
            this.findOneAndReplace(filter, document, FindOneAndReplaceOptions().upsert(true))
        }
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
                with (club.malvaceae.malloy.Malloy.instance.dataSource.getDatabase("malloy").getCollection("profiles")) {
                    val filter = Filters.eq("uuid", uniqueId)
                    val document = this.find(filter).first()
                    val p: Profile
                    if (document != null) {
                        p = Profile(
                            uniqueId,
                            (document["name"] as String?),
                            ((document["lastLogin"] as Date?)?.time) ?: System.currentTimeMillis(),
                            ((document["firstJoin"] as Date?)?.time) ?: System.currentTimeMillis(),
                            (document["lastLoginLocationX"] as Double?) ?: 0.0,
                            (document["lastLoginLocationY"] as Double?) ?: 100.0,
                            (document["lastLoginLocationZ"] as Double?) ?: 0.0,
                            (document["lastLoginWorld"] as String?) ?: "world",
                            (document["gold"] as Double?) ?: 1000.0,
                            (document["xp"] as Double?) ?: 0.0,
                            (document["level"] as Int?) ?: 0,
                            (document["town"] as UUID?),
                            (document["chatMode"] as String?) ?: "PUBLIC",
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
                            (document["job"] as String?),
                            (document["votes"] as Int?) ?: 0,
                            (document["discordId"] as String?),
                            (document["playtime"] as Long?) ?: 0L
                            )
                    } else {
                        p = Profile(
                            uniqueId
                        )
                    }
                    updateCache(p)
                    club.malvaceae.malloy.Malloy.log.info("[Profiles] ${p.name}'s profile was just loaded.")
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