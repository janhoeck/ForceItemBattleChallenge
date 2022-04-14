package forceitembattle.commands;

import forceitembattle.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            try {
                Main.getTimer().setTime(Integer.parseInt(args[0])*60);
                Main.getGamemanager().initializeMaps();

                if (Integer.parseInt(args[1]) > 64) {
                    sender.sendMessage(ChatColor.RED + "The maximum amount of jokers is 64.");
                    return false;
                }
                ItemStack stack = new ItemStack(Material.NETHER_STAR, Integer.parseInt(args[1]));
                ItemMeta m = stack.getItemMeta();
                m.setDisplayName(ChatColor.DARK_PURPLE + "JOKER");
                stack.setItemMeta(m);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.setHealth(20);
                    player.setSaturation(20);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.getInventory().clear();
                    player.getInventory().setItem(9, stack);
                    player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                });
                Bukkit.getWorld("world").setTime(0);
                Main.getTimer().setRunning(true);
                String dif = "";
                if (Main.getInstance().getConfig().getBoolean("difficulty.easy")) {
                    dif = "easy";
                } else if (Main.getInstance().getConfig().getBoolean("difficulty.medium")) {
                    dif = "medium";
                } else if (Main.getInstance().getConfig().getBoolean("difficulty.hard")) {
                    dif = "hard";
                }
                Bukkit.broadcastMessage(ChatColor.GOLD + "The game was started with " + args[1] + " jokers. " + args[0] + " minutes left.");
                Bukkit.broadcastMessage(ChatColor.GOLD + "Difficulty: " + dif +
                        " | Damage: " + Main.getInstance().getConfig().getBoolean("settings.damage") +
                        " | PVP: " + Main.getInstance().getConfig().getBoolean("settings.pvp") +
                        " | keepInventory: " + Main.getInstance().getConfig().getBoolean("settings.keepinventory"));
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Usage: /start <time in min> <jokers>");
                sender.sendMessage(ChatColor.RED + "<time> and <jokers> have to be numbers");
            }
        } else if (args.length == 0) {
            Main.getGamemanager().startGame(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /start <time in min> <jokers>");
        }
        return false;
    }
}
