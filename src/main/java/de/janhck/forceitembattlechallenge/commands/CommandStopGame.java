package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStopGame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ChallengeManager challengeManager = ForceItemBattleChallenge.getGamemanager();
        if(!challengeManager.isRunning()) {
            sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Es wurde noch keine Challenge gestartet.");
            return false;
        }

        challengeManager.endChallenge();
        return true;
    }
}
