package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.timer.Timer;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ContinueArgument implements ICommandArgument {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ChallengeManager challengeManager = ChallengesPlugin.getChallengeManager();
        if(!challengeManager.isRunning()) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Es wurde noch keine Challenge gestartet.");
            return false;
        }

        Timer timer = challengeManager.getCurrentChallenge().getTimer();
        if(!timer.isPaused()) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Die aktuelle CHallenge ist nicht pausiert.");
            return false;
        }

        timer.continueTimer();
        Bukkit.broadcastMessage(ChallengesPlugin.PREFIX + "Die Challenge geht weiter.");
        return true;
    }
}
