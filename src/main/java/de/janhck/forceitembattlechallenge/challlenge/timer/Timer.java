package de.janhck.forceitembattlechallenge.challlenge.timer;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class Timer extends BukkitRunnable {

    private boolean paused = false;
    // The total time in seconds
    private int totalTimeInSeconds = 0;
    // The remaining time in seconds
    private int remainingTimeInSeconds = 0;
    private final Consumer<Timer> action;
    private final Runnable endAction;

    public Timer(int timeInSeconds, Consumer<Timer> action, Runnable endAction) {
        this.action = action;
        this.endAction = endAction;

        this.totalTimeInSeconds = timeInSeconds;
        this.remainingTimeInSeconds = timeInSeconds;
    }

    public void startTimer() {
        runTaskTimer(ForceItemBattleChallenge.getInstance(), 20, 20);
    }

    public void pauseTimer() {
        paused = true;
    }

    public void continueTimer() {
        paused = false;
    }

    public void decreaseTime() {
        remainingTimeInSeconds--;
    }

    public int getRemainingTimeInSeconds() {
        return remainingTimeInSeconds;
    }

    public int getTotalTimeInSeconds() {
        return totalTimeInSeconds;
    }

    public boolean isPaused() {
        return paused;
    }

    @Override
    public void run() {
        if(paused) {
            return;
        }

        action.accept(this);
        decreaseTime();

        if(remainingTimeInSeconds < 0) {
            endAction.run();
            cancel();
        }
    }
}
