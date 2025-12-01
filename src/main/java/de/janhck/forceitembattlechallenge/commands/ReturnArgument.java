package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.Challenge;
import de.janhck.forceitembattlechallenge.challenges.api.ChallengeParticipant;
import de.janhck.forceitembattlechallenge.utils.ValidationUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ReturnArgument implements ICommandArgument {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Optional<Player> playerOpt = ValidationUtil.validatePlayer(sender);
        if (playerOpt.isEmpty()) {
            return false;
        }
        Player player = playerOpt.get();

        Optional<Challenge<?>> challengeOpt = ValidationUtil.validateRunningChallenge(sender);
        if (challengeOpt.isEmpty()) {
            return false;
        }

        Optional<ChallengeParticipant> participantOpt = ValidationUtil.validateParticipant(challengeOpt.get(), player);
        if (participantOpt.isEmpty()) {
            return false;
        }

        ChallengeParticipant participant = participantOpt.get();
        Location lastDeathLocation = participant.getLastDeathLocation();
        if(lastDeathLocation == null) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Es scheint so als wärst du noch nicht gestorbem.");
            return false;
        }

        player.teleport(lastDeathLocation);
        return true;
    }

}