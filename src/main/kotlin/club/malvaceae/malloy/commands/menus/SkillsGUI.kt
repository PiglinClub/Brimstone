package club.malvaceae.malloy.commands.menus

import club.malvaceae.malloy.menus.Button
import club.malvaceae.malloy.menus.Menu
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.round

class SkillsGUI : Menu() {
    override fun getButtons(player: Player): Map<Int, Button?> {
        val buttons = HashMap<Int, Button>()
        val profile = club.malvaceae.malloy.Malloy.instance.profileHandler.getProfile(player.uniqueId)!!
        buttons[2] = object : Button() {
            override fun getName(var1: Player?): Component? {
                return MiniMessage.miniMessage().deserialize("${ChatColor.AQUA}<bold>Combat ${profile.combatSkillLevel}")
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.IRON_SWORD
            }

            override fun getDescription(var1: Player?): List<Component> {
                val levelUpExp = floor(250 * (3.5).pow(profile.combatSkillLevel - 1))
                val percentage = round(((profile.combatSkillExp / levelUpExp) * 100)).toInt()
                val progressBar = if (percentage >= 95) {
                    "<aqua><bold>██████████"
                } else if (percentage >= 90) {
                    "<aqua><bold>■■■■■■■■■<white><bold>■"
                } else if (percentage >= 80) {
                    "<aqua><bold>■■■■■■■■<white><bold>■■"
                } else if (percentage >= 70) {
                    "<aqua><bold>■■■■■■■<white><bold>■■■"
                } else if (percentage >= 60) {
                    "<aqua><bold>■■■■■■<white><bold>■■■■"
                } else if (percentage >= 50) {
                    "<aqua><bold>■■■■■<white><bold>■■■■■"
                } else if (percentage >= 40) {
                    "<aqua><bold>■■■■<white><bold>■■■■■■"
                } else if (percentage >= 30) {
                    "<aqua><bold>■■■<white><bold>■■■■■■■"
                } else if (percentage >= 20) {
                    "<aqua><bold>■■<white><bold>■■■■■■■■"
                } else if (percentage >= 10) {
                    "<aqua><bold>■<white><bold>■■■■■■■■■"
                } else {
                    "<aqua><white>■■■■■■■■■■"
                }
                return listOf(
                    MiniMessage.miniMessage().deserialize("<white>Progress toward <aqua>Lvl ${profile.combatSkillLevel + 1}").decoration(TextDecoration.ITALIC, false),
                        MiniMessage.miniMessage().deserialize("$progressBar").decoration(TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize(" ").decoration(TextDecoration.ITALIC, false),
                                MiniMessage.miniMessage().deserialize("<dark_gray>\uD83D\uDDE1 \uD83D\uDDE1 \uD83D\uDDE1 \uD83D\uDDE1 \uD83D\uDDE1").decoration(TextDecoration.ITALIC, false)
                )
            }
        }

        buttons[3] = object : Button() {
            override fun getName(var1: Player?): Component {
                return MiniMessage.miniMessage().deserialize("<aqua><bold>Mining ${profile.miningSkillLevel}")
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.IRON_PICKAXE
            }

            override fun getDescription(var1: Player?): List<Component> {
                val levelUpExp = floor(250 * (3.5).pow(profile.miningSkillLevel - 1))
                val percentage = round(((profile.miningSkillExp / levelUpExp) * 100)).toInt()
                val progressBar = if (percentage >= 95) {
                    "<aqua><bold>██████████"
                } else if (percentage >= 90) {
                    "<aqua><bold>■■■■■■■■■<white><bold>■"
                } else if (percentage >= 80) {
                    "<aqua><bold>■■■■■■■■<white><bold>■■"
                } else if (percentage >= 70) {
                    "<aqua><bold>■■■■■■■<white><bold>■■■"
                } else if (percentage >= 60) {
                    "<aqua><bold>■■■■■■<white><bold>■■■■"
                } else if (percentage >= 50) {
                    "<aqua><bold>■■■■■<white><bold>■■■■■"
                } else if (percentage >= 40) {
                    "<aqua><bold>■■■■<white><bold>■■■■■■"
                } else if (percentage >= 30) {
                    "<aqua><bold>■■■<white><bold>■■■■■■■"
                } else if (percentage >= 20) {
                    "<aqua><bold>■■<white><bold>■■■■■■■■"
                } else if (percentage >= 10) {
                    "<aqua><bold>■<white><bold>■■■■■■■■■"
                } else {
                    "<aqua><white>■■■■■■■■■■"
                }
                return listOf(
                    MiniMessage.miniMessage().deserialize("<white>Progress toward <aqua>Lvl ${profile.miningSkillLevel + 1}").decoration(TextDecoration.ITALIC, false),
                        MiniMessage.miniMessage().deserialize("$progressBar").decoration(TextDecoration.ITALIC, false),
                            MiniMessage.miniMessage().deserialize(" ").decoration(TextDecoration.ITALIC, false),
                                MiniMessage.miniMessage().deserialize("<dark_gray>⛏ ⛏ ⛏ ⛏ ⛏").decoration(TextDecoration.ITALIC, false)
                )
            }
        }

        buttons[4] = object : Button() {
            override fun getName(var1: Player?): Component {
                return MiniMessage.miniMessage().deserialize("<aqua><bold>Farming ${profile.farmingSkillLevel}")
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.IRON_HOE
            }

            override fun getDescription(var1: Player?): List<Component> {
                val levelUpExp = floor(250 * (3.5).pow(profile.farmingSkillLevel - 1))
                val percentage = round(((profile.farmingSkillExp / levelUpExp) * 100)).toInt()
                val progressBar = if (percentage >= 95) {
                    "<aqua><bold>██████████"
                } else if (percentage >= 90) {
                    "<aqua><bold>■■■■■■■■■<white><bold>■"
                } else if (percentage >= 80) {
                    "<aqua><bold>■■■■■■■■<white><bold>■■"
                } else if (percentage >= 70) {
                    "<aqua><bold>■■■■■■■<white><bold>■■■"
                } else if (percentage >= 60) {
                    "<aqua><bold>■■■■■■<white><bold>■■■■"
                } else if (percentage >= 50) {
                    "<aqua><bold>■■■■■<white><bold>■■■■■"
                } else if (percentage >= 40) {
                    "<aqua><bold>■■■■<white><bold>■■■■■■"
                } else if (percentage >= 30) {
                    "<aqua><bold>■■■<white><bold>■■■■■■■"
                } else if (percentage >= 20) {
                    "<aqua><bold>■■<white><bold>■■■■■■■■"
                } else if (percentage >= 10) {
                    "<aqua><bold>■<white><bold>■■■■■■■■■"
                } else {
                    "<aqua><white>■■■■■■■■■■"
                }
                return listOf(
                    MiniMessage.miniMessage().deserialize("<white>Progress toward <aqua>Lvl ${profile.farmingSkillLevel + 1}").decoration(TextDecoration.ITALIC, false),
                    MiniMessage.miniMessage().deserialize("$progressBar").decoration(TextDecoration.ITALIC, false),
                    MiniMessage.miniMessage().deserialize(" ").decoration(TextDecoration.ITALIC, false),
                    MiniMessage.miniMessage().deserialize("<dark_gray>\uD83C\uDF3A \uD83C\uDF3A \uD83C\uDF3A \uD83C\uDF3A \uD83C\uDF3A").decoration(TextDecoration.ITALIC, false)
                )
            }
        }

        buttons[5] = object : Button() {
            override fun getName(var1: Player?): Component {
                return MiniMessage.miniMessage().deserialize("<aqua><bold>Fishing ${profile.fishingSkillLevel}")
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.FISHING_ROD
            }

            override fun getDescription(var1: Player?): List<Component> {
                val levelUpExp = floor(250 * (3.5).pow(profile.fishingSkillLevel - 1))
                val percentage = round(((profile.fishingSkillExp / levelUpExp) * 100)).toInt()
                val progressBar = if (percentage >= 95) {
                    "<aqua><bold>██████████"
                } else if (percentage >= 90) {
                    "<aqua><bold>■■■■■■■■■<white><bold>■"
                } else if (percentage >= 80) {
                    "<aqua><bold>■■■■■■■■<white><bold>■■"
                } else if (percentage >= 70) {
                    "<aqua><bold>■■■■■■■<white><bold>■■■"
                } else if (percentage >= 60) {
                    "<aqua><bold>■■■■■■<white><bold>■■■■"
                } else if (percentage >= 50) {
                    "<aqua><bold>■■■■■<white><bold>■■■■■"
                } else if (percentage >= 40) {
                    "<aqua><bold>■■■■<white><bold>■■■■■■"
                } else if (percentage >= 30) {
                    "<aqua><bold>■■■<white><bold>■■■■■■■"
                } else if (percentage >= 20) {
                    "<aqua><bold>■■<white><bold>■■■■■■■■"
                } else if (percentage >= 10) {
                    "<aqua><bold>■<white><bold>■■■■■■■■■"
                } else {
                    "<aqua><white>■■■■■■■■■■"
                }
                return listOf(
                    MiniMessage.miniMessage().deserialize("<white>Progress toward <aqua>Lvl ${profile.fishingSkillLevel + 1}").decoration(TextDecoration.ITALIC, false),
                    MiniMessage.miniMessage().deserialize("$progressBar").decoration(TextDecoration.ITALIC, false),
                    MiniMessage.miniMessage().deserialize(" ").decoration(TextDecoration.ITALIC, false),
                    MiniMessage.miniMessage().deserialize("<dark_gray>\uD83C\uDFA3 \uD83C\uDFA3 \uD83C\uDFA3 \uD83C\uDFA3 \uD83C\uDFA3").decoration(TextDecoration.ITALIC, false)
                )
            }
        }

        buttons[6] = object : Button() {
            override fun getName(var1: Player?): Component {
                return MiniMessage.miniMessage().deserialize("<aqua><bold>Logging ${profile.loggingSkillLevel}")
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.IRON_AXE
            }

            override fun getDescription(var1: Player?): List<Component> {
                val levelUpExp = floor(250 * (3.5).pow(profile.loggingSkillLevel - 1))
                val percentage = round(((profile.loggingSkillExp / levelUpExp) * 100)).toInt()
                val progressBar = if (percentage >= 95) {
                    "<aqua><bold>██████████"
                } else if (percentage >= 90) {
                    "<aqua><bold>■■■■■■■■■<white><bold>■"
                } else if (percentage >= 80) {
                    "<aqua><bold>■■■■■■■■<white><bold>■■"
                } else if (percentage >= 70) {
                    "<aqua><bold>■■■■■■■<white><bold>■■■"
                } else if (percentage >= 60) {
                    "<aqua><bold>■■■■■■<white><bold>■■■■"
                } else if (percentage >= 50) {
                    "<aqua><bold>■■■■■<white><bold>■■■■■"
                } else if (percentage >= 40) {
                    "<aqua><bold>■■■■<white><bold>■■■■■■"
                } else if (percentage >= 30) {
                    "<aqua><bold>■■■<white><bold>■■■■■■■"
                } else if (percentage >= 20) {
                    "<aqua><bold>■■<white><bold>■■■■■■■■"
                } else if (percentage >= 10) {
                    "<aqua><bold>■<white><bold>■■■■■■■■■"
                } else {
                    "<aqua><white>■■■■■■■■■■"
                }
                return listOf(
                    MiniMessage.miniMessage().deserialize("<white>Progress toward <aqua>Lvl ${profile.loggingSkillLevel + 1}").decoration(TextDecoration.ITALIC, false),
                    MiniMessage.miniMessage().deserialize("$progressBar").decoration(TextDecoration.ITALIC, false),
                    MiniMessage.miniMessage().deserialize(" ").decoration(TextDecoration.ITALIC, false),
                    MiniMessage.miniMessage().deserialize("<dark_gray>\uD83E\uDE93 \uD83E\uDE93 \uD83E\uDE93 \uD83E\uDE93 \uD83E\uDE93").decoration(TextDecoration.ITALIC, false)
                )
            }
        }

        return buttons
    }


    override fun getTitle(player: Player): String {
        return "Skills"
    }
}