package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame")) return false;
        if (!ForceItemBattleChallenge.getTimer().isRunning()) return false;
        if (!(commandSender instanceof Player)) return false;
        Player p = Bukkit.getPlayer(commandSender.getName());
        ForceItemBattleChallenge.getBackpack().openBackpack(p);
        return false;
    }
}
