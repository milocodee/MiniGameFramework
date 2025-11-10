package net.milocodee.minigame

import net.milocodee.minigame.util.PlayerState
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.ConcurrentHashMap

class GameManager(private val plugin: MiniGamePlugin) {
    private val lobby = ConcurrentHashMap.newKeySet<Player>()
    private val playerStates = ConcurrentHashMap<String, PlayerState>()
    @Volatile private var running = false

    private val arenaLocation: Location?
        get() = plugin.server.worlds.firstOrNull()?.spawnLocation

    fun join(player: Player) {
        if (running) {
            player.sendMessage("§cThe game is running already.")
            return
        }
        if (lobby.add(player)) {
            savePlayerState(player)
            player.sendMessage("§aYou joined the lobby. Players: ${lobby.size}")
            broadcastToLobby("§e${player.name} joined the Lobby. - Players: ${lobby.size}")
        } else {
            player.sendMessage("§eYou already joined the lobby.")
        }
    }

    fun leave(player: Player) {
        if (lobby.remove(player)) {
            restorePlayerState(player)
            player.sendMessage("§cYou left the lobby.")
        } else {
            player.sendMessage("§eYou're not in the lobby.")
        }
    }

    fun start() {
        if (running) return
        if (lobby.isEmpty()) {
            plugin.server.consoleSender.sendMessage("§cNo players in the lobby. Failed game starting.")
            return
        }
        running = true
        broadcastToLobby("§6Game is starting in 5 seconds.")
        object : BukkitRunnable() {
            var t = 5
            override fun run() {
                if (t <= 0) {
                    beginGame()
                    cancel()
                } else {
                    broadcastToLobby("§e$t...")
                }
                t--
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }

    private fun beginGame() {
        broadcastToLobby("§aSpiel gestartet!")
        val arena: Location? = arenaLocation
        lobby.forEach { p ->
            p.teleport(arenaLocation)
            p.gameMode = org.bukkit.GameMode.SURVIVAL
            p.inventory.clear()
            p.inventory.addItem(org.bukkit.inventory.ItemStack(org.bukkit.Material.WOODEN_SWORD))
        }

        Bukkit.getScheduler().runTaskLater(plugin, Runnable { endGame() }, 20L * 60)
    }

    private fun endGame() {
        broadcastToLobby("§cGame ended. Reset...")
        lobby.forEach { p -> restorePlayerState(p) }
        lobby.clear()
        running = false
    }

    private fun savePlayerState(player: Player) {
        val state = PlayerState(
            location = player.location,
            inventory = player.inventory.contents.mapNotNull { it?.clone() }.toTypedArray(),
            gameMode = player.gameMode
        )
        playerStates[player.uniqueId.toString()] = state
    }

    private fun restorePlayerState(player: Player) {
        val key = player.uniqueId.toString()
        val state = playerStates.remove(key) ?: return
        player.teleport(state.location)
        player.inventory.clear()
        player.inventory.contents = state.inventory
        player.gameMode = state.gameMode
    }

    private fun broadcastToLobby(msg: String) {
        lobby.forEach { it.sendMessage(msg) }
    }
}

private fun Player.teleport(p0: org.bukkit.Location?) {}
