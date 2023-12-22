package de.janhck.forceitembattlechallenge.challenges;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public abstract class AbstractChallengeParticipant {

    // The related player
    protected Player player;
    // The displayed bossbar reference
    protected BossBar bossBar;

    public AbstractChallengeParticipant(Player player) {
        this.player = player;
        this.bossBar = null;
    }

    /**
     * Prepares the player for the challenge
     */
    public abstract void prepare();

    public abstract void updateBossBar();

    public void cleanUp() {
        bossBar.removePlayer(player);
        bossBar = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void updatePlayer(Player player) {
        this.player = player;

        // If we update the player, we also need to reinitialize the boss bar
        this.bossBar = null;
        this.updateBossBar();
    }
}
