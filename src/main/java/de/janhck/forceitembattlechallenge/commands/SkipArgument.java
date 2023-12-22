package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.AbstractChallenge;
import de.janhck.forceitembattlechallenge.challenges.AbstractChallengeParticipant;
import de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.ForceItemBattleChallengeParticipant;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import de.janhck.forceitembattlechallenge.manager.ChallengeType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SkipArgument implements ICommandArgument {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if(args.length != 1) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Usage: /challenge skip <player_name>");
            return false;
        }

        ChallengeManager challengeManager = ChallengesPlugin.getChallengeManager();
        if (!challengeManager.isRunning()) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "The Challenge wurde noch nicht gestartet. Starte diese erst mit /start.");
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if(targetPlayer == null) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Dieser Spieler scheint nicht online zu sein.");
            return false;
        }

        AbstractChallenge challenge = challengeManager.getCurrentChallenge();
        if(challenge.getType() == ChallengeType.FORCE_ITEM_BATTLE) {
            Optional<ForceItemBattleChallengeParticipant> optionalPlayerInstance = challenge.getChallengeParticipant(targetPlayer);
            if(!optionalPlayerInstance.isPresent()) {
                sender.sendMessage(ChallengesPlugin.PREFIX + "Dieser Spieler nimmt nicht an der Challenge teil.");
                return false;
            }

            ForceItemBattleChallengeParticipant challengeParticipant = optionalPlayerInstance.get();
            challengeParticipant.skipItem();
            Bukkit.broadcastMessage(ChallengesPlugin.PREFIX + targetPlayer.getName() + " hat " + challengeParticipant.getCurrentItem().toString() + " geskippt.");
            return true;
        }

        sender.sendMessage(ChallengesPlugin.PREFIX + "Usage: /challenge skip <player_name>");
        return false;
    }
}
