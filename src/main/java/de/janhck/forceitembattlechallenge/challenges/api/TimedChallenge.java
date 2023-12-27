package de.janhck.forceitembattlechallenge.challenges.api;

import de.janhck.forceitembattlechallenge.challenges.api.timer.Timer;
import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class TimedChallenge<P extends ChallengeParticipant> extends Challenge<P> {

    private final Timer timer;
    // All consumers who want to be notified if the challenge ends.
    private final List<Runnable> challengeEndsConsumers;

    public TimedChallenge(ChallengeType type, int timeInSeconds, Map<String, Object> settings) {
        super(type, settings);

        this.challengeEndsConsumers = new ArrayList<>();
        this.timer = new Timer(timeInSeconds, this::runEachSecond, this::endChallenge);
    }

    @Override
    public void startChallenge(List<Player> players) {
        timer.startTimer();
    }

    @Override
    public void endChallenge() {
        timer.cancel();
        // Inform all end consumers
        challengeEndsConsumers.forEach(Runnable::run);
    }

    public abstract void runEachSecond();

    /**
     * Returns the timer for the challenge
     * @return
     *  Timer of the challenge
     */
    public Timer getTimer() {
        return timer;
    }

    public void addChallengeEndsConsumer(Runnable runnable) {
        this.challengeEndsConsumers.add(runnable);
    }
}
