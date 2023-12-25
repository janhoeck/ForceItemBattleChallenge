package de.janhck.forceitembattlechallenge.manager;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.Challenge;
import de.janhck.forceitembattlechallenge.challenges.api.TimedChallenge;
import de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import de.janhck.forceitembattlechallenge.constants.Keys;
import de.janhck.forceitembattlechallenge.challenges.api.exceptions.MissingParametersException;
import de.janhck.forceitembattlechallenge.constants.ItemDifficultyLevel;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Map;

public class ChallengeManager {

    // Current running challenge
    private Challenge<?> currentChallenge = null;

    public void startChallengeByType(ChallengeType challengeType, Map<String, Object> parameters) throws RuntimeException {
        FileConfiguration config = ChallengesPlugin.getInstance().getConfig();

        switch (challengeType) {
            case FORCE_ITEM_BATTLE: {
                // Check if there are required parameters
                if(!parameters.containsKey(Keys.TIME_IN_SECONDS) || !parameters.containsKey(Keys.JOKER_AMOUNT)) {
                    throw new MissingParametersException();
                }

                int timeInSeconds = (int) parameters.get(Keys.TIME_IN_SECONDS);
                int jokerAmount = (int) parameters.get(Keys.JOKER_AMOUNT);
                ItemDifficultyLevel level = ItemDifficultyLevel.valueOf(config.getString(Keys.DIFFICULTY));
                if(parameters.containsKey(Keys.DIFFICULTY)) {
                    level = (ItemDifficultyLevel) parameters.get(Keys.DIFFICULTY);
                }

                // Assign ForceItemBattleChallenge
                currentChallenge = new ForceItemBattleChallenge(level, jokerAmount, timeInSeconds);
                break;
            }
            default:
                break;
        }

        if(currentChallenge != null) {
            if(currentChallenge instanceof TimedChallenge<?> timedChallenge) {
                // Listen when the challenge ends. Set currentChallenge to null
                timedChallenge.addChallengeEndsConsumer(() -> this.currentChallenge = null);
            }

            // If a challenge was assigned, we can start the challenge
            currentChallenge.startChallenge(new ArrayList<>(Bukkit.getOnlinePlayers()));
        }
    }

    public void endCurrentChallenge() {
        if(currentChallenge == null) {
            return;
        }

        currentChallenge.endChallenge();
        currentChallenge = null;
    }

    public boolean isRunning() {
        return currentChallenge != null;
    }

    public Challenge<?> getCurrentChallenge() {
        return currentChallenge;
    }
}
