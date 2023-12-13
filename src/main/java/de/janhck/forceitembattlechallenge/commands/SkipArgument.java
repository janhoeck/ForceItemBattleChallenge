package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.challlenge.ChallengeParticipant;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
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

        ChallengeManager challengeManager = ForceItemBattleChallenge.getGamemanager();
        if (!challengeManager.isRunning()) {
            sender.sendMessage(ForceItemBattleChallenge.PREFIX + "The Challenge wurde noch nicht gestartet. Starte diese erst mit /start.");
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if(targetPlayer == null) {
            sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Dieser Spieler scheint nicht online zu sein.");
            return false;
        }

        Optional<ChallengeParticipant> optionalPlayerInstance = challengeManager.getChallenge().getChallengeParticipant(targetPlayer);
        if(!optionalPlayerInstance.isPresent()) {
            sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Dieser Spieler nimmt nicht an der Challenge teil.");
            return false;
        }

        ChallengeParticipant challengeParticipant = optionalPlayerInstance.get();
        if (args.length == 1) {
            challengeParticipant.skipItem();
            Bukkit.broadcastMessage(ForceItemBattleChallenge.PREFIX + targetPlayer.getName() + " hat " + challengeParticipant.getCurrentItem().toString() + " geskippt.");
            return true;
        } else {
            sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Usage: /challenge skip <player_name>");
            return false;
        }
    }
}
