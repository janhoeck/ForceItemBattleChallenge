package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.challlenge.timer.Timer;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class PauseArgument implements ICommandArgument {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ChallengeManager challengeManager = ForceItemBattleChallenge.getGamemanager();
        if(!challengeManager.isRunning()) {
            sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Es wurde noch keine Challenge gestartet.");
            return false;
        }

        Timer timer = challengeManager.getChallenge().getTimer();
        if(timer.isPaused()) {
            sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Die Challenge ist bereits pausiert. Nutze /challenge continue um weiter zu machen.");
            return false;
        }

        timer.pauseTimer();
        Bukkit.broadcastMessage(ForceItemBattleChallenge.PREFIX + "Die Challenge wurde pausiert.");
        return true;
    }

}
