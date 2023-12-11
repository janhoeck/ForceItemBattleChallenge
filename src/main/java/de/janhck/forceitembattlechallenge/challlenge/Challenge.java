package de.janhck.forceitembattlechallenge.challlenge;

import de.janhck.forceitembattlechallenge.challlenge.items.ItemDifficultyLevel;
import de.janhck.forceitembattlechallenge.challlenge.items.ItemsManager;
import de.janhck.forceitembattlechallenge.challlenge.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Challenge {

    // Contains all participants
    private final List<ChallengeParticipant> challengeParticipants;
    private final ItemsManager itemsManager;
    // The current item difficulty for the challenge
    private final ItemDifficultyLevel level;
    // The initial amount of jokers
    private int jokerAmount = 1;
    private final Timer timer;

    public Challenge(ItemDifficultyLevel level, int jokerAmount, Timer timer) {
        this.challengeParticipants = new ArrayList<>();
        this.itemsManager = new ItemsManager();
        this.level = level;
        this.timer = timer;
        this.jokerAmount = jokerAmount;
    }

    public void initChallenge(List<Player> players) {
        players.forEach(player -> {
            ChallengeParticipant challengeParticipant = new ChallengeParticipant(player, level, itemsManager);
            challengeParticipant.init(jokerAmount);

            challengeParticipants.add(challengeParticipant);
        });

        // Change the time in "world" to "day"
        Bukkit.getWorld("world").setTime(0);
    }

    /**
     * Find an active instance of a player.
     * @param player
     *  The player
     * @return
     *  The game instance of the player
     */
    public Optional<ChallengeParticipant> getChallengeParticipant(Player player) {
        return this.challengeParticipants
                .stream()
                .filter(instance -> instance.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .findFirst();
    }

    /**
     * Returns all active challenge participants
     * @return
     *  List of challenge participants
     */
    public List<ChallengeParticipant> getChallengeParticipants() {
        return challengeParticipants;
    }

    /**
     * Returns the timer for the challenge
     * @return
     *  Timer of the challenge
     */
    public Timer getTimer() {
        return timer;
    }
}
