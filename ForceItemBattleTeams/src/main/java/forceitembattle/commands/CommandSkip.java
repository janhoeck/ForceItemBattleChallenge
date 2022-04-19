package forceitembattle.commands;

import forceitembattle.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSkip implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (!Main.getTimer().isRunning()) {
            sender.sendMessage(ChatColor.RED + "The game is not running. Start it first with /start");
            return false;
        } else
        if (args.length == 1) {
            if (Bukkit.getPlayer(args[0]) != null) {
                if (Main.getInstance().getConfig().getBoolean("settings.isTeamGame")) {
                    /////////////////////////////////////// TEAMS ///////////////////////////////////////
                    Bukkit.broadcastMessage(ChatColor.RED + args[0] + " skipped " + Main.getGamemanager().getMaterialTeamsFromPlayer(Bukkit.getPlayer(args[0])));
                    Main.getInstance().logToFile("[" + Main.getTimer().getTime() + "] | " + args[0] + " skipped " + Main.getGamemanager().getMaterialTeamsFromPlayer(Bukkit.getPlayer(args[0])));
                } else {
                    Bukkit.broadcastMessage(ChatColor.RED + args[0] + " skipped " + Main.getGamemanager().getCurrentMaterial(Bukkit.getPlayer(args[0])));
                    Main.getInstance().logToFile("[" + Main.getTimer().getTime() + "] | " + args[0] + " skipped " + Main.getGamemanager().getCurrentMaterial(Bukkit.getPlayer(args[0])));
                }
                Main.getGamemanager().skipItem(args[0]);
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
