package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.challlenge.timer.Timer;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandPause implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ChallengeManager challengeManager = ForceItemBattleChallenge.getGamemanager();
        if(!challengeManager.isRunning()) {
            sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Es wurde noch keine Challenge gestartet.");
            return false;
        }

        Timer timer = challengeManager.getChallenge().getTimer();
        if(timer.isPaused()) {
            timer.continueTimer();
            Bukkit.broadcastMessage(ForceItemBattleChallenge.PREFIX + "Die Challenge geht weiter.");
        } else {
            timer.pauseTimer();
            Bukkit.broadcastMessage(ForceItemBattleChallenge.PREFIX + "Die Challenge wurde pausiert.");
        }
        return true;
    }
}
