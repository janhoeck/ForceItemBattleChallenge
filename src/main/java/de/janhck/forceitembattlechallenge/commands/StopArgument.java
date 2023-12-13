package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.command.CommandSender;

public class StopArgument implements ICommandArgument {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ChallengeManager challengeManager = ForceItemBattleChallenge.getGamemanager();
        if(!challengeManager.isRunning()) {
            sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Es wurde noch keine Challenge gestartet.");
            return false;
        }

        challengeManager.endChallenge();
        return true;
    }

}
