package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            try {
                int timeInSeconds = Integer.parseInt(args[0]) * 60;
                int jokerAmount = Integer.parseInt(args[1]);

                if (jokerAmount > 64) {
                    sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Die maximale Anzahl der Joker ist 64.");
                    return false;
                }

                ChallengeManager challengeManager = ForceItemBattleChallenge.getGamemanager();
                if(challengeManager.isRunning()) {
                    sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Das Challenge wurde bereits gestartet.");
                    return false;
                }

                challengeManager.startChallenge(timeInSeconds, jokerAmount);
                Bukkit.broadcastMessage(ForceItemBattleChallenge.PREFIX + "Die Challenge wurde mit " + jokerAmount + " Jokern gestartet. Schwierigkeit: " + challengeManager.getLevel().toString());
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Usage: /start <time in min> <jokers>");
                sender.sendMessage(ForceItemBattleChallenge.PREFIX + "<time> and <jokers> have to be numbers");
                return false;
            }
        }

        sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Usage: /start <time in min> <jokers>");
        return false;
    }
}
