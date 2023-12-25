package de.janhck.forceitembattlechallenge.challenges.api;

import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class Challenge<P extends ChallengeParticipant> {

    private final ChallengeType type;
    protected final List<P> challengeParticipants = new ArrayList<>();

    public Challenge(ChallengeType type) {
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
     * Returns the type of the challenge
     * @return
     */
    public ChallengeType getType() {
        return type;
    }
}
