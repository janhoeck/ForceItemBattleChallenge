package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStopTimer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        if (!ForceItemBattleChallenge.getTimer().isRunning()) {
            commandSender.sendMessage(ChatColor.RED + "The game is not running. Start it first with /start");
            return false;
        }
        ForceItemBattleChallenge.getTimer().setTime(1);
        return false;
    }
}
