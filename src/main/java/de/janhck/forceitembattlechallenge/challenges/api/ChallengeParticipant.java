package de.janhck.forceitembattlechallenge.challenges.api;

import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public abstract class ChallengeParticipant {

    // The related player
    protected Player player;
    // The displayed bossbar reference
    protected BossBar bossBar;

    public ChallengeParticipant(Player player) {
        this.player = player;
        this.bossBar = null;
    }

    /**
     * Prepares the player for the challenge
     */
    public abstract void prepare();

    public abstract void updateBossBar();

    public Location getLastDeathLocation() {
        return player.getLastDeathLocation();
    }

    public void updateTabListName(String additionalString) {
        player.setPlayerListName(player.getName() + " §7(" + additionalString  + ")");
    }

    public void cleanUp() {
        bossBar.removePlayer(player);
        bossBar = null;
        // reset name in tab list
        player.setPlayerListName(player.getName());
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
