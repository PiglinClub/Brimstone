package club.piglin.brimstone

import net.fabricmc.api.ModInitializer

@Suppress("UNUSED")
object Brimstone : ModInitializer {
    private const val MOD_ID = "brimstone"
    override fun onInitialize() {
        println("Brimstone has been initialized.")
    }
}