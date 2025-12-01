package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.Challenge;
import de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.ForceItemBattleChallengeParticipant;
import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import de.janhck.forceitembattlechallenge.utils.ValidationUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SkipArgument implements ICommandArgument {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Optional<Player> playerOpt = ValidationUtil.validatePlayer(sender);
        if (playerOpt.isEmpty()) {
            return false;
        }

        if(args.length != 1) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Usage: /challenge skip <player_name>");
            return false;
        }

        Optional<Challenge<?>> challengeOpt = ValidationUtil.validateRunningChallenge(sender);
        if (challengeOpt.isEmpty()) {
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if(targetPlayer == null) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Dieser Spieler scheint nicht online zu sein.");
            return false;
        }

        Challenge<?> challenge = challengeOpt.get();

        if(challenge.getType() == ChallengeType.FORCE_ITEM_BATTLE) {
            Optional<ForceItemBattleChallengeParticipant> participantOpt = ValidationUtil.validateParticipant(challenge, targetPlayer);
            if(participantOpt.isEmpty()) {
                sender.sendMessage(ChallengesPlugin.PREFIX + "Dieser Spieler nimmt nicht an der Challenge teil.");
                return false;
            }

            ForceItemBattleChallengeParticipant challengeParticipant = participantOpt.get();
            challengeParticipant.skipItem();
            Bukkit.broadcastMessage(ChallengesPlugin.PREFIX + targetPlayer.getName() + " hat " + challengeParticipant.getCurrentItem().toString() + " geskippt.");
            return true;
        }

        sender.sendMessage(ChallengesPlugin.PREFIX + "Usage: /challenge skip <player_name>");
        return false;
    }
}