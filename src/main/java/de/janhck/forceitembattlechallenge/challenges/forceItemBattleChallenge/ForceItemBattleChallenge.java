package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.AbstractChallenge;
import de.janhck.forceitembattlechallenge.constants.ItemDifficultyLevel;
import de.janhck.forceitembattlechallenge.manager.ItemsManager;
import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import de.janhck.forceitembattlechallenge.timer.Timer;
import de.janhck.forceitembattlechallenge.utils.TimeUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class ForceItemBattleChallenge extends AbstractChallenge<ForceItemBattleChallengeParticipant> {

    private final ItemsManager itemsManager = new ItemsManager();
    // The current item difficulty for the challenge
    private final ItemDifficultyLevel level;
    // The initial amount of jokers
    private int jokerAmount = 1;
    private int timeInSeconds = 1;
    private final Listener listener = new ForceItemBattleChallengeListeners(this);

    public ForceItemBattleChallenge(ItemDifficultyLevel level, int jokerAmount, int timeInSeconds) {
        super(ChallengeType.FORCE_ITEM_BATTLE);

        this.level = level;
        this.jokerAmount = jokerAmount;
        this.timeInSeconds = timeInSeconds;
    }

    @Override
    public void startChallenge(List<Player> players) {
        Timer timer = initializeTimer(timeInSeconds, this::runEachSecond, this::endChallenge);
        players.forEach(player -> {
            ForceItemBattleChallengeParticipant challengeParticipant = new ForceItemBattleChallengeParticipant(player, level, jokerAmount, itemsManager);
            challengeParticipant.prepare();

            challengeParticipants.add(challengeParticipant);
        });

        // Change the time in "world" to "day"
        Bukkit.getWorld("world").setTime(0);

        // Start the timer
        timer.startTimer();

        // register listeners
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(listener, ChallengesPlugin.getInstance());

        Bukkit.broadcastMessage(ChallengesPlugin.PREFIX + "Die Challenge wurde mit " + jokerAmount + " Jokern gestartet. Schwierigkeit: " + level);
    }

    @Override
    public void endChallenge() {
        Bukkit.broadcastMessage(ChallengesPlugin.PREFIX + "Die Challenge ist beendet!");
        getChallengeParticipants().forEach(playerInstance -> {
            Player player = playerInstance.getPlayer();
            playerInstance.cleanUp();

            Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + ": " + ChatColor.WHITE + playerInstance.getScore());
            player.playSound(player, Sound.BLOCK_END_PORTAL_SPAWN, 1, 1);
        });
        ChallengesPlugin.getInstance().logToFile("<< Force Item Battle is over >>");

        getTimer().cancel();

        // Unregister listeners
        HandlerList.unregisterAll(listener);
    }

    @Override
    public void runEachSecond() {
        boolean last5MinutesReached = timer.getRemainingTimeInSeconds() == (5 * 60);
        challengeParticipants.forEach((playerInstance) -> {
            Player player = playerInstance.getPlayer();
            if(!player.isOnline()) {
                return;
            }

            // Check if the player got the current item in the inventory
            boolean hasCurrentItemInInventory = playerInstance.hasCurrentItemInInventory();
            if(hasCurrentItemInInventory) {
                Material currentItem = playerInstance.getCurrentItem();
                playerInstance.nextItem();

                player.sendMessage(ChatColor.GREEN + "✔ " + ChatColor.WHITE + currentItem);
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);

                ChallengesPlugin.getInstance().logToFile(player.getName() + " got item: " + currentItem + " | new points: " + playerInstance.getScore());
            }

            // Inform the player if we reached the last 5 minutes
            if(last5MinutesReached) {
                player.sendMessage(ChallengesPlugin.PREFIX + "Die letzten 5 Minuten der Challenge laufen!");
                player.playSound(player, Sound.BLOCK_ANVIL_USE, 1, 1);
            }

            // Updating bossbar for the current item
            playerInstance.updateBossBar();

            // Updating action bar for the time and score
            TextComponent textComponent = new TextComponent(
                    ChatColor.GOLD.toString() + ChatColor.BOLD
                            + TimeUtil.formatSeconds(timer.getRemainingTimeInSeconds())
                            + ChatColor.WHITE + ChatColor.BOLD
                            + " | "
                            + ChatColor.GOLD + ChatColor.BOLD
                            + playerInstance.getScore()
            );
            playerInstance.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
        });
    }
}
