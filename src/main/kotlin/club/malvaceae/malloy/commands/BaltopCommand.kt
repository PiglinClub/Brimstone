package club.malvaceae.malloy.commands

import club.malvaceae.malloy.Malloy
import club.malvaceae.malloy.utils.Chat
import com.mongodb.client.model.Sorts
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.*
import kotlin.math.floor

class BaltopCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        Chat.sendComponent(sender, "<st><dark_gray>                    </dark_gray></st> <gold><bold>Top Balances</bold></gold> <st><dark_gray>                    </dark_gray></st>")
        with (Malloy.instance.dataSource.getDatabase("malloy").getCollection("profiles")) {
            val top = this.find().sort(Sorts.descending("gold")).limit(10)
            for ((index, document) in top.withIndex()) {
                if (document != null && document["uuid"] != null) {
                    val profile = Malloy.instance.profileHandler.lookupProfile(document["uuid"] as UUID).get()
                    Chat.sendComponent(sender, "<yellow>#${index + 1}</yellow> <reset>${profile.name} <dark_gray>-</dark_gray> <color:#ffd417>${floor(profile.gold)}g</color>")
                }
            }
        }
        Chat.sendComponent(sender, "<st><dark_gray>                                                             </dark_gray></st>")
        return true
    }
}