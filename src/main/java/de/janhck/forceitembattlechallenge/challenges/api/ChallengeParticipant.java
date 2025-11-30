package de.janhck.forceitembattlechallenge.challenges.api;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
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

    public void updateTabListName(Component tabName) {
        player.playerListName(tabName);
    }

    public void cleanUp() {
        player.hideBossBar(bossBar);
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
