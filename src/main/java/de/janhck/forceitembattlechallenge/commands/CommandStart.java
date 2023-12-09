package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import org.bukkit.*;
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
                ForceItemBattleChallenge.getTimer().setTime(Integer.parseInt(args[0]) * 60);
                ForceItemBattleChallenge.getGamemanager().initializeMaps();

                if (Integer.parseInt(args[1]) > 64) {
                    sender.sendMessage(ChatColor.RED + "The maximum amount of jokers is 64.");
                    return false;
                }
                ItemStack stack = new ItemStack(Material.NETHER_STAR, Integer.parseInt(args[1]));
                ItemMeta m = stack.getItemMeta();
                m.setDisplayName(ChatColor.DARK_PURPLE + "JOKER");
                stack.setItemMeta(m);

                String dif = "";
                if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("difficulty.easy")) {
                    dif = "easy";
                } else if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("difficulty.medium")) {
                    dif = "medium";
                } else if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("difficulty.hard")) {
                    dif = "hard";
                }

                ForceItemBattleChallenge.getInstance().logToFile("The game was started with " + args[1] + " jokers. " + args[0] + " minutes left.");
                ForceItemBattleChallenge.getInstance().logToFile("Difficulty: " + dif +
                        " | Damage: " + ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.damage") +
                        " | PVP: " + ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.pvp") +
                        " | keepInventory: " + ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.keepinventory"));

                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.setHealth(20);
                    player.setSaturation(20);
                    player.getInventory().clear();
                    player.setLevel(0);
                    player.setExp(0);
                    player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                    player.setScoreboard(ForceItemBattleChallenge.getGamemanager().getBoard());
                    player.setAllowFlight(ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.fly"));
                    player.setFlySpeed(ForceItemBattleChallenge.getInvSettings().getFlySpeed());
                    player.playSound(player, Sound.BLOCK_END_PORTAL_SPAWN, 1, 1);
                    if (ForceItemBattleChallenge.getGamemanager().isPlayerInMaps(player)) {
                        player.setGameMode(GameMode.SURVIVAL);
                        if (!ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame")) {
                            player.getInventory().setItem(4, stack);
                            ForceItemBattleChallenge.getInstance().logToFile(player.getName() + " -> " + ForceItemBattleChallenge.getGamemanager().getPlayerTeamSTRING(player));
                        } else ForceItemBattleChallenge.getInstance().logToFile(player.getName());
                    } else {
                        player.setGameMode(GameMode.SPECTATOR);
                        ForceItemBattleChallenge.getInstance().logToFile(player.getName() + " -> Spectator");
                    }
                });
                if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame"))
                    ForceItemBattleChallenge.getBackpack().addToAllBp(stack);
                Bukkit.getWorld("world").setTime(0);
                ForceItemBattleChallenge.getTimer().setRunning(true);

                Bukkit.broadcastMessage(ChatColor.GOLD + "The game was started with " + args[1] + " jokers. " + args[0] + " minutes left.");
                Bukkit.broadcastMessage(ChatColor.GOLD + "Difficulty: " + dif +
                        " | Damage: " + ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.damage") +
                        " | PVP: " + ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.pvp") +
                        " | keepInventory: " + ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.keepinventory"));
                if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("standard.showInfoOnGameStart")) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "----------------------- " + ChatColor.GREEN + "Info" + ChatColor.DARK_GREEN + " -----------------------");
                    Bukkit.broadcastMessage(ChatColor.GREEN + "At the top of the screen you will see an item. Get it as soon as possible. " +
                            "When you have it, a new item will appear at the top. Now try to get as many items that appear at the top as possible. " +
                            "For each item you collect you will get one point. You can see your points next to the countdown. \n" +
                            "Netherstars are jokers. But use them wisely!\n" +
                            "Useful commands:\n" +
                            "- " + ChatColor.GOLD + "/top " + ChatColor.GREEN + "-> teleports you to the surface\n" +
                            "- " + ChatColor.GOLD + "/bp " + ChatColor.GREEN + "-> opens your team-backpack if you play in a team");
                    Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "----------------------------------------------------");
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Usage: /start <time in min> <jokers>");
                sender.sendMessage(ChatColor.RED + "<time> and <jokers> have to be numbers");
            }
        } else if (args.length == 0) {
            ForceItemBattleChallenge.getGamemanager().startGame(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /start <time in min> <jokers>");
        }
        return false;
    }
}
