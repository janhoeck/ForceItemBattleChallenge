package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.Challenge;
import de.janhck.forceitembattlechallenge.challenges.api.TimedChallenge;
import de.janhck.forceitembattlechallenge.challenges.api.timer.Timer;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ContinueArgument implements ICommandArgument {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ChallengeManager challengeManager = ChallengesPlugin.getInstance().getChallengeManager();
        if(!challengeManager.isRunning()) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Es wurde noch keine Challenge gestartet.");
            return false;
        }

        Challenge<?> currentChallenge = challengeManager.getCurrentChallenge();
        if(!(currentChallenge instanceof TimedChallenge<?> timedChallenge)) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Die aktuelle Challenge kann nicht pausiert werden.");
            return false;
        }

        Timer timer = timedChallenge.getTimer();
        if(!timer.isPaused()) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Die aktuelle CHallenge ist nicht pausiert. Nutze /challenge pause um die Challenge zu pausieren.");
            return false;
        }

        timer.continueTimer();
        Bukkit.broadcastMessage(ChallengesPlugin.PREFIX + "Die Challenge geht weiter.");
        return true;
    }
}
