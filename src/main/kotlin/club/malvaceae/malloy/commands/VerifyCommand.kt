package club.malvaceae.malloy.commands

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.discord.SlashCommandListener
import club.malvaceae.malloy.utils.Chat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VerifyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command.")
            return false
        }
        if (args.isEmpty()) {
            Chat.sendComponent(sender, "<red>You must insert a code to verify yourself on Discord.")
            return false
        }
        if (SlashCommandListener.verificationCodes[args[0].toIntOrNull()] == null) {
            Chat.sendComponent(sender, "<red>You must insert a VALID code to verify yourself on Discord.")
            return false
        }
        val user = Malloy.instance.jda.getUserById(SlashCommandListener.verificationCodes[args[0].toIntOrNull()]!!)
        if (user == null) {
            Chat.sendComponent(sender, "<red>This Discord user doesn't seem to exist.")
            return false
        }
        val profile = Malloy.instance.profileHandler.getProfile(sender.uniqueId)!!
        profile.discordId = user.id
        Malloy.instance.profileHandler.saveProfile(profile)
        Chat.sendComponent(sender, "<green>Successfully paired your <color:#5865f2>Discord account</color> (@${user.name}) with your Minecraft account!")
        return true
    }
}