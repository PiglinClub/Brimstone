package club.malvaceae.malloy.utils

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.random.Random


class BlockUtils {
    companion object {
        fun getBlocks(start: Block, radius: Int): ArrayList<Block> {
            val blocks = ArrayList<Block>()
            var x = start.location.x - radius
            while (x <= start.location.x + radius) {
                var y = start.location.y - radius
                while (y <= start.location.y + radius) {
                    var z = start.location.z - radius
                    while (z <= start.location.z + radius) {
                        val loc: Location = Location(start.world, x, y, z)
                        blocks.add(loc.block)
                        z++
                    }
                    y++
                }
                x++
            }
            return blocks
        }

        fun degradeDurability(player: Player) {
            val item = player.inventory.itemInMainHand

            if ((item.type == Material.AIR) || (item.type == Material.BOW) || item.type.maxDurability.toInt() == 0 || item.itemMeta.isUnbreakable) {
                return
            }

            var durability = item.durability

            if (item.containsEnchantment(Enchantment.DURABILITY)) {
                val chance = (100 / (item.getEnchantmentLevel(Enchantment.DURABILITY) + 1))

                if (Random.nextDouble() <= (chance / 100)) {
                    durability--
                }
            } else {
                durability--
            }

            if (durability >= item.type.maxDurability) {
                player.world.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F)
                player.itemInHand.type = Material.AIR
                return
            }

            item.durability = durability
            player.inventory.setItemInMainHand(item)
        }

    }
}

class InventoryUtils {
    companion object {
        fun similarItems(one: ItemStack?, two: ItemStack?): Boolean {
            if (one == null || two == null) {
                return one === two
            }
            if (one.isSimilar(two)) {
                return true
            }
            if (one.type != two.type || one.durability != two.durability || one.hasItemMeta() && two.hasItemMeta() && one.itemMeta.javaClass != two.itemMeta.javaClass) {
                return false
            }
            if (!one.hasItemMeta() && !two.hasItemMeta()) {
                return true
            }
            val oneMeta = one.itemMeta
            val twoMeta = two.itemMeta
            if (oneMeta === twoMeta || oneMeta == null || twoMeta == null) {
                return oneMeta === twoMeta
            }
            val oneSerMeta = oneMeta.serialize()
            val twoSerMeta = twoMeta.serialize()
            if (oneSerMeta == twoSerMeta) {
                return true
            }
            return false
        }

        private fun effectiveSize(inventory: Inventory): Int {
            return getStorageContents(inventory).size
        }

        private var legacyContents: Boolean? = null
        private fun getStorageContents(inventory: Inventory): Array<ItemStack?> {
            if (legacyContents == null) {
                try {
                    inventory.storageContents
                    legacyContents = false
                } catch (e: NoSuchMethodError) {
                    legacyContents = true
                }
            }
            return if (legacyContents == true) inventory.contents else inventory.storageContents
        }

        fun removeManually(item: ItemStack, inventory: Inventory): Int {
            var amountLeft = item.amount
            var currentSlot = 0
            while (currentSlot < effectiveSize(inventory) && amountLeft > 0) {
                val currentItem = inventory.getItem(currentSlot)
                if (currentItem != null && similarItems(currentItem, item)) {
                    val neededToRemove = Math.min(currentItem.amount, amountLeft)
                    currentItem.amount = currentItem.amount - neededToRemove
                    inventory.setItem(currentSlot, currentItem)
                    amountLeft -= neededToRemove
                }
                currentSlot++
            }
            return amountLeft
        }

        fun amount(item: ItemStack, inventory: Inventory): Int {
            if (!inventory.contains(item.type)) {
                return 0
            }

            if (inventory.type == null) {
                return Integer.MAX_VALUE
            }

            val items = inventory.all(item.type)
            var itemAmount = 0

            for (iStack in items.values) {
                if (!similarItems(iStack, item)) {
                    continue
                }
                itemAmount += iStack.amount
            }

            return itemAmount
        }
    }
}

class PlayerUtils {
    companion object {
        fun inventoryFull(player: Player): Boolean {
            return player.inventory.firstEmpty() == -1
        }

        fun bulkItems(player: Player, bulk: ArrayList<ItemStack>) {
            for (item in bulk) {
                if (!inventoryFull(player)) {
                    player.inventory.addItem(item)
                } else {
                    player.world.dropItemNaturally(player.location, item)
                }
            }
        }
    }
}