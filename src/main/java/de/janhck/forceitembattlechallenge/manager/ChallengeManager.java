package de.janhck.forceitembattlechallenge.manager;

import de.janhck.forceitembattlechallenge.challenges.api.Challenge;
import de.janhck.forceitembattlechallenge.challenges.api.TimedChallenge;
import de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Map;

public class ChallengeManager {

    // Current running challenge
    private Challenge<?> currentChallenge = null;

    public void startChallengeByType(ChallengeType challengeType, Map<String, Object> settings) throws RuntimeException {
        switch (challengeType) {
            case FORCE_ITEM_BATTLE: {
                currentChallenge = new ForceItemBattleChallenge(settings);
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
