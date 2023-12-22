package de.janhck.forceitembattlechallenge.manager;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.AbstractChallenge;
import de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.exceptions.MissingParametersException;
import de.janhck.forceitembattlechallenge.items.ItemDifficultyLevel;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Map;

public class ChallengeManager {

    // Current running challenge
    private AbstractChallenge<?> currentChallenge = null;

    public void startChallengeByType(ChallengeType challengeType, Map<String, Object> parameters) throws RuntimeException {
        FileConfiguration config = ChallengesPlugin.getInstance().getConfig();

        switch (challengeType) {
            case FORCE_ITEM_BATTLE: {
                // Check if there are required parameters
                if(!parameters.containsKey("timeInSeconds") || !parameters.containsKey("jokerAmount")) {
                    throw new MissingParametersException();
                }

                int timeInSeconds = (int) parameters.get("timeInSeconds");
                int jokerAmount = (int) parameters.get("jokerAmount");

                // Get the difficulty level from the config
                ItemDifficultyLevel level = ItemDifficultyLevel.valueOf(config.getString("difficulty"));
                // Assign ForceItemBattleChallenge
                currentChallenge = new ForceItemBattleChallenge(level, jokerAmount, timeInSeconds);
                break;
            }
            default:
                break;
        }

        if(currentChallenge != null) {
            // If a challenge was assigned, we can start the challenge
            currentChallenge.startChallenge(new ArrayList<>(Bukkit.getOnlinePlayers()));
        }
    }

    public void endCurrentChallenge() {
        if(currentChallenge == null) {
            return;
        }

        currentChallenge.getTimer().cancel();
        currentChallenge.endChallenge();
        currentChallenge = null;
    }

    public boolean isRunning() {
        return currentChallenge != null;
    }

    public AbstractChallenge<?> getCurrentChallenge() {
        return currentChallenge;
    }
}
