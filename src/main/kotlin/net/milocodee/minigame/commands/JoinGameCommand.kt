package net.milocodee.minigame.commands

import net.milocodee.minigame.GameManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class JoinGameCommand(private val gameManager: GameManager) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can join.")
            return true
        }
        gameManager.join(sender)
        return true
    }
}
