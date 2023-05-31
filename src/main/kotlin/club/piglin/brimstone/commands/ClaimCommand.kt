package club.piglin.brimstone.commands

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.utils.Chat
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.math.pow

class ClaimCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            Chat.sendMessage(sender, "You can't use this command as you are not a player!")
            return false
        }
        val profile = Brimstone.instance.profileHandler.getProfile(sender.uniqueId)
        if (profile == null) {
            Chat.sendMessage(sender, "&cSurprisingly, you don't have a profile?")
            return false
        }
        if (profile.town == null) {
            Chat.sendMessage(sender, "&cYou are not in a Town.")
            return false
        }
        val town = Brimstone.instance.townHandler.getTown(profile.town!!)!!
        if (town.getMember(sender.uniqueId)!!.role != "mayor") {
            Chat.sendMessage(sender, "&cYou do not have a high enough town role to use this command.")
            return false
        }
        val claims = town.getClaims()
        val cost = ((750) * (1.5).pow(claims.get().size))
        if (town.gold < cost) {
            Chat.sendMessage(sender, "&cYou can't afford to purchase this chunk. You are short by ${ChatColor.of("#ffd417")}${cost - town.gold}g&c.")
            return false
        }
        val chunk = sender.location.chunk
        val north = chunk.world.getChunkAt(chunk.x, chunk.z - 1)
        val west = chunk.world.getChunkAt(chunk.x - 1, chunk.z)
        val east = chunk.world.getChunkAt(chunk.x + 1, chunk.z)
        val south = chunk.world.getChunkAt(chunk.x, chunk.z + 1)

        if (claims.get().isNotEmpty()) {
            if (
                town.doWeOwnAdjacentChunk(north.x, north.z).get() == false &&
                town.doWeOwnAdjacentChunk(west.x, west.z).get() == false &&
                town.doWeOwnAdjacentChunk(east.x, east.z).get() == false &&
                town.doWeOwnAdjacentChunk(south.x, south.z).get() == false
            ) {
                Chat.sendMessage(sender, "&cYou must own an adjacent chunk to claim this chunk.")
                return false
            }
        }
        val claim = town.claimChunk(chunk)
        if (claim.get() == null) {
            Chat.sendMessage(sender, "&cThis claim is already occupied by another town.")
        } else {
            town.sendMessage("&e${sender.name}&a claimed a chunk at &eX: ${chunk.x}, Z: ${chunk.z}&a.")
            town.gold -= cost
            Brimstone.instance.townHandler.saveTown(town)
            Brimstone.log.info("Claimed chunk at X: ${chunk.x}, Z: ${chunk.z} for ${town.name} (${town.uniqueId})")
        }
        return true
    }
}