package de.janhck.forceitembattlechallenge.utils;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.Challenge;
import de.janhck.forceitembattlechallenge.challenges.api.ChallengeParticipant;
import de.janhck.forceitembattlechallenge.challenges.api.TimedChallenge;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ValidationUtil {

    /**
     * Checks if the sender is a player.
     */
    public static Optional<Player> validatePlayer(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Dieser Befehl kann nur von Spielern ausgeführt werden.");
            return Optional.empty();
        }
        return Optional.of(player);
    }

    /**
     * Checks if a challenge is currently running and returns it.
     */
    public static Optional<Challenge<?>> validateRunningChallenge(CommandSender sender) {
        ChallengeManager challengeManager = ChallengesPlugin.getInstance().getChallengeManager();
        if (!challengeManager.isRunning()) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Es wurde noch keine Challenge gestartet.");
            return Optional.empty();
        }
        return Optional.of(challengeManager.getCurrentChallenge());
    }

    /**
     * Checks if a challenge is currently running AND if it is a TimedChallenge.
     */
    public static Optional<TimedChallenge<?>> validateTimedChallenge(Challenge<?> challenge, CommandSender sender) {
        if (!(challenge instanceof TimedChallenge<?> timedChallenge)) {
            sender.sendMessage(ChallengesPlugin.PREFIX + "Die aktuelle Challenge unterstützt diese Funktion nicht (keine Zeit-Challenge).");
            return Optional.empty();
        }
        return Optional.of(timedChallenge);
    }

    /**
     * Checks if the specified player is participating in the current challenge and returns the participant.
     */
    public static <T extends ChallengeParticipant> Optional<T> validateParticipant(Challenge<?> challenge, Player player) {
        if (!challenge.isParticipant(player)) {
            player.sendMessage(ChallengesPlugin.PREFIX + "Du nimmst an der Challenge nicht teil.");
            return Optional.empty();
        }

        // We assume the caller knows which type to expect, or we cast generically.
        // Unchecked cast here is acceptable as we check isParticipant above.
        return (Optional<T>) challenge.getChallengeParticipant(player);
    }
}