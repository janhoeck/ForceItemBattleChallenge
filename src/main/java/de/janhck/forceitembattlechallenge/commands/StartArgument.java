package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import de.janhck.forceitembattlechallenge.gui.inventories.ChallengesInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class StartArgument implements ICommandArgument {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            return false;
        }

        if(args.length <= 1) {
            ChallengesInventory challengesInventory = new ChallengesInventory();
            challengesInventory.openInventory(player);
            return false;
        }

        ChallengeType challengeType = ChallengeType.valueOf(args[0]);
        if(challengeType == ChallengeType.FORCE_ITEM_BATTLE) {
            if(args.length == 3) {
                try {
                    int timeInSeconds = Integer.parseInt(args[1]) * 60;
                    int jokerAmount = Integer.parseInt(args[2]);

                    if (jokerAmount > 64) {
                        sender.sendMessage(ChallengesPlugin.PREFIX + "Die maximale Anzahl der Joker ist 64.");
                        return false;
                    }

                    ChallengeManager challengeManager = ChallengesPlugin.getChallengeManager();
                    if(challengeManager.isRunning()) {
                        sender.sendMessage(ChallengesPlugin.PREFIX + "Das Challenge wurde bereits gestartet.");
                        return false;
                    }

                    HashMap<String, Object> parameters = new HashMap<>();
                    parameters.put("jokerAmount", jokerAmount);
                    parameters.put("timeInSeconds", timeInSeconds);

                    challengeManager.startChallengeByType(ChallengeType.FORCE_ITEM_BATTLE, parameters);
                    return true;
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChallengesPlugin.PREFIX + "Usage: /challenge start <time in min> <jokers>");
                    sender.sendMessage(ChallengesPlugin.PREFIX + "<time> and <jokers> have to be numbers");
                    return false;
                }
            }
        }

        sender.sendMessage(ChallengesPlugin.PREFIX + "Usage: /challenge <CHALLENGE_TYPE>");
        sender.sendMessage(ChallengesPlugin.PREFIX + "Verfügbare Challenges: " + ChallengeType.FORCE_ITEM_BATTLE);
        return false;
    }
}
