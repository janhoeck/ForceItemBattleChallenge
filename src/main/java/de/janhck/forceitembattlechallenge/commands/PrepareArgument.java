package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.AbstractChallenge;
import de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.ForceItemBattleChallengeParticipant;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import de.janhck.forceitembattlechallenge.manager.ChallengeType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrepareArgument implements ICommandArgument {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        ChallengeManager challengeManager = ChallengesPlugin.getChallengeManager();
        if (!challengeManager.isRunning()) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "The Challenge wurde noch nicht gestartet. Starte diese erst mit /start.");
            return false;
        }

        Player player = (Player) sender;
        AbstractChallenge<?> challenge = challengeManager.getCurrentChallenge();
        if(!challenge.isParticipant(player)) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Du nimmst nicht an der Challenge teil.");
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
