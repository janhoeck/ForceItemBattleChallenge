package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.challlenge.Challenge;
import de.janhck.forceitembattlechallenge.challlenge.ChallengeParticipant;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrepareArgument implements ICommandArgument {

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

        Player player = (Player) sender;
        Challenge challenge = challengeManager.getChallenge();
        if(!challenge.isParticipant(player)) {
            sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Du nimmst nicht an der Challenge teil.");
            return false;
        }

        ChallengeParticipant challengeParticipant = challenge.getChallengeParticipant(player).get();
        challengeParticipant.giveStarterItems();
        sender.sendMessage(ForceItemBattleChallenge.PREFIX + "Viel Spaß mit deinen neuem Starter Equipment!");
        return true;
    }

}
