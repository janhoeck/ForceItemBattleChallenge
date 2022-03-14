package forceitembattle.commands;

import forceitembattle.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            try {
                Main.getTimer().setTime(Integer.parseInt(args[1]));
                // joker vergeben
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Usage: /start <time> <jokers>");
                sender.sendMessage(ChatColor.RED + "time and jokers have to be numbers");
            }

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /start <time> <jokers>");
            return false;
        }
    }
}
