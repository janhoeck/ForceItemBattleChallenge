package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSkip implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (!ForceItemBattleChallenge.getTimer().isRunning()) {
            sender.sendMessage(ChatColor.RED + "The game is not running. Start it first with /start");
            return false;
        } else if (args.length == 1) {
            if (Bukkit.getPlayer(args[0]) != null) {
                if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame")) {
                    /////////////////////////////////////// TEAMS ///////////////////////////////////////
                    Bukkit.broadcastMessage(ChatColor.RED + args[0] + " skipped " + ForceItemBattleChallenge.getGamemanager().getMaterialTeamsFromPlayer(Bukkit.getPlayer(args[0])));
                    ForceItemBattleChallenge.getInstance().logToFile("[" + ForceItemBattleChallenge.getTimer().getTime() + "] | " + args[0] + " skipped " + ForceItemBattleChallenge.getGamemanager().getMaterialTeamsFromPlayer(Bukkit.getPlayer(args[0])));
                } else {
                    Bukkit.broadcastMessage(ChatColor.RED + args[0] + " skipped " + ForceItemBattleChallenge.getGamemanager().getCurrentMaterial(Bukkit.getPlayer(args[0])));
                    ForceItemBattleChallenge.getInstance().logToFile("[" + ForceItemBattleChallenge.getTimer().getTime() + "] | " + args[0] + " skipped " + ForceItemBattleChallenge.getGamemanager().getCurrentMaterial(Bukkit.getPlayer(args[0])));
                }
                ForceItemBattleChallenge.getGamemanager().skipItem(args[0]);
            } else {
                sender.sendMessage(ChatColor.RED + "This player is not online");
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /skip <player_name>");
            return false;
        }
    }
}
