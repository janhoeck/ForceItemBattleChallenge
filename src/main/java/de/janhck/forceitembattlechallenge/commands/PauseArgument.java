package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.Challenge;
import de.janhck.forceitembattlechallenge.challenges.api.TimedChallenge;
import de.janhck.forceitembattlechallenge.challenges.api.timer.Timer;
import de.janhck.forceitembattlechallenge.utils.ValidationUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class PauseArgument implements ICommandArgument {

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
        if(timer.isPaused()) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Die Challenge ist bereits pausiert. Nutze /challenge continue um weiter zu machen.");
            return false;
        }

        timer.pauseTimer();
        Bukkit.broadcastMessage(ChallengesPlugin.PREFIX + "Die Challenge wurde pausiert.");
        return true;
    }

}