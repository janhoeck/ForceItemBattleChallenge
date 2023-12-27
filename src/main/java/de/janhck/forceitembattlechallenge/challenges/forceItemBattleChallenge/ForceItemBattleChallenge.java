package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.TimedChallenge;
import de.janhck.forceitembattlechallenge.constants.ItemDifficultyLevel;
import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import de.janhck.forceitembattlechallenge.constants.Keys;
import de.janhck.forceitembattlechallenge.utils.TimeUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.Map;

public class ForceItemBattleChallenge extends TimedChallenge<ForceItemBattleChallengeParticipant> {

    private final Listener listener = new ForceItemBattleChallengeListeners(this);

    public ForceItemBattleChallenge(Map<String, Object> settings) {
        super(ChallengeType.FORCE_ITEM_BATTLE, (Integer) settings.get(Keys.TIME_IN_SECONDS), settings);
    }

    @Override
    public void startChallenge(List<Player> players) {
        players.forEach(player -> {
            ForceItemBattleChallengeParticipant challengeParticipant = new ForceItemBattleChallengeParticipant(player, this);
            challengeParticipant.prepare();

            getChallengeParticipants().add(challengeParticipant);
        });

        Bukkit.getWorlds().forEach(world -> {
            world.setTime(0);
            // keep the inventory, if "withEltrya" is set to true
            world.setGameRule(GameRule.KEEP_INVENTORY, isWithElytra());
        });

        // register listeners
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(listener, ChallengesPlugin.getInstance());

        Bukkit.broadcastMessage(ChallengesPlugin.PREFIX + "Die Challenge wurde mit " + getJokerAmount() + " Jokern gestartet. Schwierigkeit: " + getItemDifficultyLevel());
        super.startChallenge(players);
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

        Bukkit.getWorlds().forEach(world -> {
            world.setTime(0);
            world.setGameRule(GameRule.KEEP_INVENTORY, false);
        });

        ChallengesPlugin.getInstance().logToFile("<< Force Item Battle is over >>");

        // Unregister listeners
        HandlerList.unregisterAll(listener);
        super.endChallenge();
    }

    @Override
    public void runEachSecond() {
        boolean last5MinutesReached = getTimer().getRemainingTimeInSeconds() == (5 * 60);
        getChallengeParticipants().forEach((playerInstance) -> {
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
                            + TimeUtil.formatSeconds(getTimer().getRemainingTimeInSeconds())
                            + ChatColor.WHITE + ChatColor.BOLD
                            + " | "
                            + ChatColor.GOLD + ChatColor.BOLD
                            + playerInstance.getScore()
            );
            playerInstance.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
        });
    }

    public ItemDifficultyLevel getItemDifficultyLevel() {
        return getSetting(Keys.DIFFICULTY);
    }

    public int getJokerAmount() {
        return getSetting(Keys.JOKER_AMOUNT);
    }

    public boolean isWithElytra() {
        return getSetting(Keys.WITH_ELYTRA);
    }
}
