package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.manager.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPauseTimer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;

        if (!ForceItemBattleChallenge.getTimer().isRunning()) {
            commandSender.sendMessage(ChatColor.RED + "The game is not running. Start it first with /start");
            return false;
        }

        Timer timer = ForceItemBattleChallenge.getTimer();
        if (timer.isPaused()) {
            timer.setPaused(false);
            Bukkit.broadcastMessage("Timer resumed");
        } else {
            timer.setPaused(true);
            Bukkit.broadcastMessage("Timer paused");
        }

        return false;
    }
}
