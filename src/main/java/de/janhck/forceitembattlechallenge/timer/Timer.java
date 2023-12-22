package de.janhck.forceitembattlechallenge.timer;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {

    private boolean paused = false;
    // The total time in seconds
    private int totalTimeInSeconds = 0;
    // The remaining time in seconds
    private int remainingTimeInSeconds = 0;
    private final Runnable action;
    private final Runnable endAction;

    public Timer(int timeInSeconds, Runnable action, Runnable endAction) {
        this.action = action;
        this.endAction = endAction;

        this.totalTimeInSeconds = timeInSeconds;
        this.remainingTimeInSeconds = timeInSeconds;
    }

    public void startTimer() {
        runTaskTimer(ChallengesPlugin.getInstance(), 20, 20);
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

        action.run();
        decreaseTime();

        if(remainingTimeInSeconds < 0) {
            endAction.run();
            cancel();
        }
    }
}
