package de.janhck.forceitembattlechallenge.challenges;

import de.janhck.forceitembattlechallenge.manager.ChallengeType;
import de.janhck.forceitembattlechallenge.timer.Timer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractChallenge<P extends AbstractChallengeParticipant> {

    private final ChallengeType type;
    protected final List<P> challengeParticipants = new ArrayList<>();
    protected Timer timer = null;

    public AbstractChallenge(ChallengeType type) {
        this.type = type;
    }

    /**
     * Will be called, when the challenge gets started.
     * @param players
     *  List of players which should be part of the challenge
     */
    public abstract void startChallenge(List<Player> players);

    /**
     * Will be called, when the challenge ends.
     */
    public abstract void endChallenge();

    public abstract void runEachSecond();

    /**
     * Find an active instance of a player.
     * @param player
     *  The player
     * @return
     *  The game instance of the player
     */
    public Optional<P> getChallengeParticipant(Player player) {
        return getChallengeParticipantByUUID(player.getUniqueId());
    }

    public Optional<P> getChallengeParticipantByUUID(UUID uuid) {
        return challengeParticipants
                .stream()
                .filter(instance -> instance.getPlayer().getUniqueId().equals(uuid))
                .findFirst();
    }

    public boolean isParticipant(Player player) {
        return getChallengeParticipant(player).isPresent();
    }

    /**
     * Returns all active challenge participants
     * @return
     *  List of challenge participants
     */
    public List<P> getChallengeParticipants() {
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

    public Timer initializeTimer(int timeInSeconds, Runnable action, Runnable endAction) {
        Timer timer = new Timer(timeInSeconds, action, endAction);
        this.timer = timer;
        return timer;
    }

    /**
     * Returns the type of the challenge
     * @return
     */
    public ChallengeType getType() {
        return type;
    }
}
