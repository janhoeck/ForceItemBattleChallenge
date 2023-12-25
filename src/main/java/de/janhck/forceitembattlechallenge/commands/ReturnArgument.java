package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.Challenge;
import de.janhck.forceitembattlechallenge.challenges.api.ChallengeParticipant;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReturnArgument implements ICommandArgument {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            return false;
        }

        ChallengeManager challengeManager = ChallengesPlugin.getInstance().getChallengeManager();
        if(!challengeManager.isRunning()) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Es wurde noch keine Challenge gestartet.");
            return false;
        }

        Challenge<?> challenge = challengeManager.getCurrentChallenge();
        if(!challenge.isParticipant(player)) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Du nimmst an der Challenge nicht teil.");
            return false;
        }

        ChallengeParticipant participant = challenge.getChallengeParticipant(player).get();
        Location lastDeathLocation = participant.getLastDeathLocation();
        if(lastDeathLocation == null) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Es scheint so als wärst du noch nicht gestorbem.");
            return false;
        }

        player.teleport(lastDeathLocation);
        return true;
    }

}
