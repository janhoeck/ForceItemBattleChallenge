package forceitembattle.commands;

import forceitembattle.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInvsee implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length == 1) {
            if (Bukkit.getPlayer(args[0]) != null) {
                Player p = Bukkit.getPlayer(sender.getName());
                p.openInventory(Bukkit.getPlayer(args[0]).getInventory());
            } else {
                sender.sendMessage(ChatColor.RED + "This player is not online");
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /invsee <player_name>");
            return false;
        }
    }
}
