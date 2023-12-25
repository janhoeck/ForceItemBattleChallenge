package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.command.CommandSender;

public class StopArgument implements ICommandArgument {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ChallengeManager challengeManager = ChallengesPlugin.getInstance().getChallengeManager();
        if(!challengeManager.isRunning()) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Es wurde noch keine Challenge gestartet.");
            return false;
        }

        challengeManager.endCurrentChallenge();
        return true;
    }

}
