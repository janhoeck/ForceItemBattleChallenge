package forceitembattle.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandSkip implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0] != null) {
            if (Bukkit.getOnlinePlayers().toString().contains(args[0])) {
                // new random item
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
