package net.milocodee.minigame.util

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.inventory.ItemStack

data class PlayerState(
    val location: Location,
    val inventory: Array<ItemStack?> = arrayOf(),
    val gameMode: GameMode = GameMode.SURVIVAL
)
