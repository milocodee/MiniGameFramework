package net.milocodee.minigame.listeners

import net.milocodee.minigame.GameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class GameListener(private val gameManager: GameManager) : Listener {

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        gameManager.leave(e.player)
    }
}
