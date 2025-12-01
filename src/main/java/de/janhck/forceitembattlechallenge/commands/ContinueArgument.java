package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.Challenge;
import de.janhck.forceitembattlechallenge.challenges.api.TimedChallenge;
import de.janhck.forceitembattlechallenge.challenges.api.timer.Timer;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import de.janhck.forceitembattlechallenge.utils.ValidationUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class ContinueArgument implements ICommandArgument {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Optional<Challenge<?>> challengeOpt = ValidationUtil.validateRunningChallenge(sender);
        if(challengeOpt.isEmpty()) {
            return false;
        }

        Challenge<?> currentChallenge = challengeOpt.get();
        Optional<TimedChallenge<?>> timedChallengeOpt = ValidationUtil.validateTimedChallenge(currentChallenge, sender);
        if(timedChallengeOpt.isEmpty()) {
            return false;
        }

        Timer timer = timedChallengeOpt.get().getTimer();
        if(!timer.isPaused()) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Die aktuelle CHallenge ist nicht pausiert. Nutze /challenge pause um die Challenge zu pausieren.");
            return false;
        }

        timer.continueTimer();
        Bukkit.broadcastMessage(ChallengesPlugin.PREFIX + "Die Challenge geht weiter.");
        return true;
    }
}
