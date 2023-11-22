package club.malvaceae.malloy.commands

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.utils.Chat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UnclaimCommand : CommandExecutor {
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
        if (town.getMember(sender.uniqueId)!!.role != "mayor") {
            Chat.sendMessage(sender, "&cYou do not have a high enough town role to use this command.")
            return false
        }
        if (args[0].lowercase() == "all") {
            val claims = town.getClaims().get()
            var accum = 0
            for (claim in claims) {
                val chunk = Bukkit.getWorld(claim.world)!!.getChunkAt(claim.x, claim.z)
                val cost = ((750) + ((claims.size - 1) * 250))
                town.gold += cost
                accum += cost
                town.unclaimChunk(chunk)
            }
            town.sendMessage("<green><yellow>${sender.name}</yellow> unclaimed all chunks for your town and got back <color:#ffd417>${accum}g</color>!")
            return true
        }
        val chunk = sender.location.chunk
        if (town.doWeOwnChunk(chunk.world.name, chunk.x, chunk.z).get() == false) {
            Chat.sendComponent(sender, "<red>You do not own this claim/chunk, therefore you can't refund it.")
            return false
        }
        val claims = town.getClaims().get()
        val cost = ((750) + ((claims.size - 1) * 250))
        town.gold += cost
        town.unclaimChunk(chunk)
        Malloy.instance.townHandler.saveTown(town)
        town.sendMessage("<green><yellow>${sender.name}</yellow> unclaimed a chunk at <yellow>X: ${chunk.x}, Z: ${chunk.z}</yellow> and got back <color:#ffd417>${cost}g</color>!")
        return true
    }
}