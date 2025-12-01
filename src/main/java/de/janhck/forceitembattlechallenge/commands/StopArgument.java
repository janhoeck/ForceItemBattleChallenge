package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.Challenge;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import de.janhck.forceitembattlechallenge.utils.ValidationUtil;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class StopArgument implements ICommandArgument {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Optional<Challenge<?>> challengeOpt = ValidationUtil.validateRunningChallenge(sender);
        if (challengeOpt.isEmpty()) {
            return false;
        }

        ChallengesPlugin.getInstance().getChallengeManager().endCurrentChallenge();
        return true;
    }

}