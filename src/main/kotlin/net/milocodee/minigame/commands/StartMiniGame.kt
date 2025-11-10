package net.milocodee.minigame.commands

import net.milocodee.minigame.GameManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class StartMiniGame(private val gameManager: GameManager) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("minigame.start")) {
            sender.sendMessage("§cNo Permissions.")
            return true
        }
        gameManager.start()
        sender.sendMessage("§aMinigame start initiated.")
        return true
    }
}
