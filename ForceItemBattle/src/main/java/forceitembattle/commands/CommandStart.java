package forceitembattle.commands;

import forceitembattle.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class CommandStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            try {
                Main.getTimer().setTime(Integer.parseInt(args[0])*60);
                ItemStack stack = new ItemStack(Material.BARRIER, Integer.parseInt(args[1]));
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.getInventory().addItem(stack);
                });
                Main.getTimer().setRunning(true);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Usage: /start <time in min> <jokers>");
                sender.sendMessage(ChatColor.RED + "time and jokers have to be numbers");
            }

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /start <time in min> <jokers>");
            return false;
        }
    }
}
