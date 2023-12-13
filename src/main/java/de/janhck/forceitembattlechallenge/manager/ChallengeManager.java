package de.janhck.forceitembattlechallenge.manager;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.challlenge.Challenge;
import de.janhck.forceitembattlechallenge.challlenge.items.ItemDifficultyLevel;
import de.janhck.forceitembattlechallenge.challlenge.timer.Timer;
import de.janhck.forceitembattlechallenge.utils.TimeUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ChallengeManager {

    private ItemDifficultyLevel level = ItemDifficultyLevel.EASY;
    private Challenge challenge = null;

    public ChallengeManager(FileConfiguration config) {
        this.level = ItemDifficultyLevel.valueOf(config.getString("difficulty"));
    }

    public boolean isRunning() {
        return challenge != null;
    }

    private void runEachSecond(Timer timer) {
        if(challenge == null) {
            return;
        }

        boolean last5MinutesReached = timer.getRemainingTimeInSeconds() == (5 * 60);
        challenge.getChallengeParticipants().forEach((playerInstance) -> {
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

                ForceItemBattleChallenge.getInstance().logToFile(player.getName() + " got item: " + currentItem + " | new points: " + playerInstance.getScore());
            }

            // Inform the player if we reached the last 5 minutes
            if(last5MinutesReached) {
                player.sendMessage(ForceItemBattleChallenge.PREFIX + "Die letzten 5 Minuten der Challenge laufen!");
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

    public void endChallenge() {
        Bukkit.broadcastMessage(ForceItemBattleChallenge.PREFIX + "Die Challenge ist beendet!");
        challenge.getChallengeParticipants().forEach(playerInstance -> {
            Player player = playerInstance.getPlayer();
            playerInstance.cleanUp();

            Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + ": " + ChatColor.WHITE + playerInstance.getScore());
            player.playSound(player, Sound.BLOCK_END_PORTAL_SPAWN, 1, 1);
        });
        ForceItemBattleChallenge.getInstance().logToFile("<< Force Item Battle is over >>");

        this.challenge.getTimer().cancel();
        this.challenge = null;
    }

    public void startChallenge(int timeInSeconds, int jokerAmount) {
        Timer timer = new Timer(timeInSeconds, this::runEachSecond, this::endChallenge);
        Challenge challenge = new Challenge(level, jokerAmount, timer);
        challenge.initChallenge(new ArrayList<>(Bukkit.getOnlinePlayers()));

        timer.startTimer();
        this.challenge = challenge;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public ItemDifficultyLevel getLevel() {
        return this.level;
    }
}
