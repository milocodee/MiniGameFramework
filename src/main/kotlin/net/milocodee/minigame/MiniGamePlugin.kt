package net.milocodee.minigame

import net.milocodee.minigame.commands.JoinGameCommand
import net.milocodee.minigame.commands.StartMiniGame
import net.milocodee.minigame.listeners.GameListener
import org.bukkit.plugin.java.JavaPlugin

class MiniGamePlugin : JavaPlugin() {
    lateinit var gameManager: GameManager
        private set

    override fun onEnable() {
        saveDefaultConfig()
        gameManager = GameManager(this)

        server.pluginManager.registerEvents(GameListener(gameManager), this)

        getCommand("startminigame")?.setExecutor(StartMiniGame(gameManager))
        getCommand("joingame")?.setExecutor(JoinGameCommand(gameManager))

        logger.info("MiniGameFramework enabled")
    }

    override fun onDisable() {
        logger.info("MiniGameFramework disabled")
    }
}
