package club.malvaceae.malloy.discord

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import kotlin.random.Random

class SlashCommandListener : ListenerAdapter() {
    companion object {
        val verificationCodes = hashMapOf<Int, String>()
    }

    override fun onSlashCommandInteraction(e: SlashCommandInteractionEvent) {
        if (e.name == "verify") {
            val code = Random.nextInt(1000, 9999)
            verificationCodes[code] = e.member!!.id
            try {
                e.member!!.user
                    .openPrivateChannel()
                    .flatMap { channel -> channel.sendMessage("Your verification code is `${code}`. Log onto `mc.piglin.club` and verify yourself using `/verify ${code}`. ") }
                    .queue()
                e.reply("Sent you the verification in your DMS.").setEphemeral(true).queue()
            } catch (ex: Exception) {
                ex.printStackTrace()
                e.reply("Couldn't DM you the code. Try changing your privacy settings on Discord.").setEphemeral(true).queue()
            }
        }
    }
}