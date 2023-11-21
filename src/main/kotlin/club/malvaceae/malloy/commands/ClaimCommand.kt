package club.malvaceae.malloy.commands

import club.malvaceae.malloy.utils.Chat
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClaimCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            Chat.sendMessage(sender, "You can't use this command as you are not a player!")
            return false
        }
        val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(sender.uniqueId)
        if (profile == null) {
            Chat.sendMessage(sender, "&cSurprisingly, you don't have a profile?")
            return false
        }
        if (profile.town == null) {
            Chat.sendMessage(sender, "&cYou are not in a Town.")
            return false
        }
        val town = club.malvaceae.malloy.Malloy.instance.townHandler.getTown(profile.town!!)!!
        if (town.getMember(sender.uniqueId)!!.role != "mayor" && town.getMember(sender.uniqueId)!!.role != "officer") {
            Chat.sendMessage(sender, "&cYou do not have a high enough town role to use this command.")
            return false
        }
        val claims = town.getClaims()
        val cost = if (!town.adminOnly) ((750) + (claims.get().size * 250)) else 0
        if (town.gold < cost) {
            Chat.sendMessage(sender, "&cYour town can't afford to purchase this chunk. The town is short by ${ChatColor.of("#ffd417")}${Math.ceil(cost - town.gold)}g&c.")
            return false
        }
        val chunk = sender.location.chunk
        val north = chunk.world.getChunkAt(chunk.x, chunk.z - 1)
        val west = chunk.world.getChunkAt(chunk.x - 1, chunk.z)
        val east = chunk.world.getChunkAt(chunk.x + 1, chunk.z)
        val south = chunk.world.getChunkAt(chunk.x, chunk.z + 1)

        if (claims.get().isNotEmpty()) {
            if (
                town.doWeOwnChunk(north.x, north.z).get() == false &&
                town.doWeOwnChunk(west.x, west.z).get() == false &&
                town.doWeOwnChunk(east.x, east.z).get() == false &&
                town.doWeOwnChunk(south.x, south.z).get() == false
            ) {
                Chat.sendMessage(sender, "&cYou must own an adjacent chunk to claim this chunk.")
                return false
            }
        }
        val claim = town.claimChunk(chunk)
        if (claim.get() == null) {
            if (town.doWeOwnChunk(chunk.x, chunk.z).get() == true) {
                Chat.sendMessage(sender, "&cWe already own this claim.")
                return true
            }
            Chat.sendMessage(sender, "&cThis claim is already occupied by another town.")
        } else {
            town.sendMessage("<green><yellow>${sender.name}</yellow> claimed a chunk at <yellow>X: ${chunk.x}, Z: ${chunk.z}</yellow>.")
            town.gold -= cost
            club.malvaceae.malloy.Malloy.instance.townHandler.saveTown(town)
            club.malvaceae.malloy.Malloy.log.info("Claimed chunk at X: ${chunk.x}, Z: ${chunk.z} for ${town.name} (${town.uniqueId})")
        }
        return true
    }
}