package club.piglin.brimstone.commands.menus

import club.piglin.brimstone.Brimstone
import club.piglin.brimstone.menus.Button
import club.piglin.brimstone.menus.Menu
import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.round

class SkillsGUI : Menu() {
    override fun getButtons(player: Player): Map<Int, Button?> {
        val buttons = HashMap<Int, Button>()
        val profile = Brimstone.instance.profileHandler.getProfile(player.uniqueId)!!
        buttons[2] = object : Button() {
            override fun getName(var1: Player?): String {
                return "${ChatColor.AQUA}${ChatColor.BOLD}Combat ${profile.combatSkillLevel}"
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.IRON_SWORD
            }

            override fun getDescription(var1: Player?): List<String> {
                val levelUpExp = floor(250 * (3.5).pow(profile.combatSkillLevel - 1))
                val percentage = round(((profile.combatSkillExp / levelUpExp) * 100)).toInt()
                val progressBar = if (percentage >= 95) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}██████████"
                } else if (percentage >= 90) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■"
                } else if (percentage >= 80) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■"
                } else if (percentage >= 70) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■"
                } else if (percentage >= 60) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■"
                } else if (percentage >= 50) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■"
                } else if (percentage >= 40) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■"
                } else if (percentage >= 30) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■"
                } else if (percentage >= 20) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■■"
                } else if (percentage >= 10) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■■■"
                } else {
                    "${ChatColor.AQUA}${ChatColor.WHITE}■■■■■■■■■■"
                }
                return listOf(
                    "${ChatColor.WHITE}Progress toward ${ChatColor.AQUA}Lvl ${profile.combatSkillLevel + 1}",
                    "$progressBar",
                    " ",
                    "${ChatColor.DARK_GRAY}\uD83D\uDDE1 \uD83D\uDDE1 \uD83D\uDDE1 \uD83D\uDDE1 \uD83D\uDDE1"
                )
            }
        }

        buttons[3] = object : Button() {
            override fun getName(var1: Player?): String {
                return "${ChatColor.AQUA}${ChatColor.BOLD}Mining ${profile.miningSkillLevel}"
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.IRON_PICKAXE
            }

            override fun getDescription(var1: Player?): List<String> {
                val levelUpExp = floor(250 * (3.5).pow(profile.miningSkillLevel - 1))
                val percentage = round(((profile.miningSkillExp / levelUpExp) * 100)).toInt()
                val progressBar = if (percentage >= 95) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}██████████"
                } else if (percentage >= 90) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■"
                } else if (percentage >= 80) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■"
                } else if (percentage >= 70) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■"
                } else if (percentage >= 60) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■"
                } else if (percentage >= 50) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■"
                } else if (percentage >= 40) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■"
                } else if (percentage >= 30) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■"
                } else if (percentage >= 20) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■■"
                } else if (percentage >= 10) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■■■"
                } else {
                    "${ChatColor.AQUA}${ChatColor.WHITE}■■■■■■■■■■"
                }
                return listOf(
                    "${ChatColor.WHITE}Progress toward ${ChatColor.AQUA}Lvl ${profile.miningSkillLevel + 1}",
                    "$progressBar",
                    " ",
                    "${ChatColor.DARK_GRAY}⛏ ⛏ ⛏ ⛏ ⛏"
                )
            }
        }

        buttons[4] = object : Button() {
            override fun getName(var1: Player?): String {
                return "${ChatColor.AQUA}${ChatColor.BOLD}Farming ${profile.farmingSkillLevel}"
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.IRON_HOE
            }

            override fun getDescription(var1: Player?): List<String> {
                val levelUpExp = floor(250 * (3.5).pow(profile.farmingSkillLevel - 1))
                val percentage = round(((profile.farmingSkillExp / levelUpExp) * 100)).toInt()
                val progressBar = if (percentage >= 95) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}██████████"
                } else if (percentage >= 90) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■"
                } else if (percentage >= 80) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■"
                } else if (percentage >= 70) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■"
                } else if (percentage >= 60) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■"
                } else if (percentage >= 50) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■"
                } else if (percentage >= 40) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■"
                } else if (percentage >= 30) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■"
                } else if (percentage >= 20) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■■"
                } else if (percentage >= 10) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■■■"
                } else {
                    "${ChatColor.AQUA}${ChatColor.WHITE}■■■■■■■■■■"
                }
                return listOf(
                    "${ChatColor.WHITE}Progress toward ${ChatColor.AQUA}Lvl ${profile.farmingSkillLevel + 1}",
                    "$progressBar",
                    " ",
                    "${ChatColor.DARK_GRAY}\uD83C\uDF3A \uD83C\uDF3A \uD83C\uDF3A \uD83C\uDF3A \uD83C\uDF3A"
                )
            }
        }

        buttons[5] = object : Button() {
            override fun getName(var1: Player?): String {
                return "${ChatColor.AQUA}${ChatColor.BOLD}Fishing ${profile.fishingSkillLevel}"
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.FISHING_ROD
            }

            override fun getDescription(var1: Player?): List<String> {
                val levelUpExp = floor(250 * (3.5).pow(profile.fishingSkillLevel - 1))
                val percentage = round(((profile.fishingSkillExp / levelUpExp) * 100)).toInt()
                val progressBar = if (percentage >= 95) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}██████████"
                } else if (percentage >= 90) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■"
                } else if (percentage >= 80) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■"
                } else if (percentage >= 70) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■"
                } else if (percentage >= 60) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■"
                } else if (percentage >= 50) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■"
                } else if (percentage >= 40) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■"
                } else if (percentage >= 30) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■"
                } else if (percentage >= 20) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■■"
                } else if (percentage >= 10) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■■■"
                } else {
                    "${ChatColor.AQUA}${ChatColor.WHITE}■■■■■■■■■■"
                }
                return listOf(
                    "${ChatColor.WHITE}Progress toward ${ChatColor.AQUA}Lvl ${profile.fishingSkillLevel + 1}",
                    "$progressBar",
                    " ",
                    "${ChatColor.DARK_GRAY}\uD83C\uDFA3 \uD83C\uDFA3 \uD83C\uDFA3 \uD83C\uDFA3 \uD83C\uDFA3"
                )
            }
        }

        buttons[6] = object : Button() {
            override fun getName(var1: Player?): String {
                return "${ChatColor.AQUA}${ChatColor.BOLD}Logging ${profile.loggingSkillLevel}"
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.IRON_AXE
            }

            override fun getDescription(var1: Player?): List<String> {
                val levelUpExp = floor(250 * (3.5).pow(profile.loggingSkillLevel - 1))
                val percentage = round(((profile.loggingSkillExp / levelUpExp) * 100)).toInt()
                val progressBar = if (percentage >= 95) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}██████████"
                } else if (percentage >= 90) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■"
                } else if (percentage >= 80) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■"
                } else if (percentage >= 70) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■"
                } else if (percentage >= 60) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■"
                } else if (percentage >= 50) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■"
                } else if (percentage >= 40) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■"
                } else if (percentage >= 30) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■"
                } else if (percentage >= 20) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■■"
                } else if (percentage >= 10) {
                    "${ChatColor.AQUA}${ChatColor.BOLD}■${ChatColor.WHITE}${ChatColor.BOLD}■■■■■■■■■"
                } else {
                    "${ChatColor.AQUA}${ChatColor.WHITE}■■■■■■■■■■"
                }
                return listOf(
                    "${ChatColor.WHITE}Progress toward ${ChatColor.AQUA}Lvl ${profile.loggingSkillLevel + 1}",
                    "$progressBar",
                    " ",
                    "${ChatColor.DARK_GRAY}\uD83E\uDE93 \uD83E\uDE93 \uD83E\uDE93 \uD83E\uDE93 \uD83E\uDE93"
                )
            }
        }

        return buttons
    }


    override fun getTitle(player: Player): String {
        return "Skills"
    }
}