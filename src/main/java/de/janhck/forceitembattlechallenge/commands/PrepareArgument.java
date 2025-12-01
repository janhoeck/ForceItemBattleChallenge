package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.Challenge;
import de.janhck.forceitembattlechallenge.challenges.api.ChallengeParticipant;
import de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.ForceItemBattleChallengeParticipant;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import de.janhck.forceitembattlechallenge.utils.ValidationUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PrepareArgument implements ICommandArgument {

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

        Challenge<?> challenge = challengeOpt.get();
        Optional<ChallengeParticipant> challengeParticipantOpt = ValidationUtil.validateParticipant(challenge, player);
        if(challengeParticipantOpt.isEmpty()) {
            return false;
        }

        if(challenge.getType() == ChallengeType.FORCE_ITEM_BATTLE) {
            ForceItemBattleChallengeParticipant challengeParticipant = (ForceItemBattleChallengeParticipant) challenge.getChallengeParticipant(player).get();
            challengeParticipant.giveStarterItems();
            sender.sendMessage(ChallengesPlugin.PREFIX + "Viel Spaß mit deinen neuem Starter Equipment!");
            return true;
        }

        sender.sendMessage(ChallengesPlugin.PREFIX + "Für die aktuelle Challenge kannst du den /challenge prepare Befehl nicht benutzen.");
        return false;
    }

}
