package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.ChallengeParticipant;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ForceItemBattleChallengeListeners implements Listener {

    private final ForceItemBattleChallenge challenge;

    public ForceItemBattleChallengeListeners(ForceItemBattleChallenge challenge) {
        this.challenge = challenge;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) { // triggered if a joker is used
        ChallengeManager challengeManager = ChallengesPlugin.getInstance().getChallengeManager();
        if (!challengeManager.isRunning()) {
            return;
        }

        Player player = event.getPlayer();
        Optional<ForceItemBattleChallengeParticipant> optionalPlayerInstance = challenge.getChallengeParticipant(player);
        if(optionalPlayerInstance.isEmpty()) {
            return;
        }

        if(event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (player.getInventory().getItemInMainHand().getType() != Material.NETHER_STAR) {
            return;
        }

        ForceItemBattleChallengeParticipant challengeParticipant = optionalPlayerInstance.get();
        if (event.getAction() != Action.RIGHT_CLICK_AIR) {
            player.sendMessage(ChallengesPlugin.PREFIX + "Du kannst den Joker nicht benutzen, während du einen Block ansiehst.");
            return;
        }

        if(!challengeParticipant.canSkip()) {
            player.sendMessage(ChallengesPlugin.PREFIX + "Bitte warte 5 Sekunden, bevor du einen weiteren Joker verwenden kannst.");
            return;
        }

        ItemStack netherStarStack = player.getInventory().getItem(player.getInventory().first(Material.NETHER_STAR));
        if(netherStarStack != null) {
            if (netherStarStack.getAmount() > 1) {
                netherStarStack.setAmount(netherStarStack.getAmount() - 1);
            } else {
                netherStarStack.setType(Material.AIR);
            }
            player.getInventory().setItem(player.getInventory().first(Material.NETHER_STAR), netherStarStack);
        }

        challengeParticipant.decreaseRemainingJokerAmount();
        challengeParticipant.skipItem();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) { // triggered if the player dies
        ChallengeManager challengeManager = ChallengesPlugin.getInstance().getChallengeManager();
        if (!challengeManager.isRunning()) {
            return;
        }

        Player player = event.getEntity();
        boolean isParticipant = challengeManager.getCurrentChallenge().isParticipant(player);
        if(!isParticipant) {
           return;
        }

        player.sendMessage(ChallengesPlugin.PREFIX + "War das ein Bug? Gebe " + ChatColor.WHITE + ChatColor.BOLD + "/challenge return" + ChatColor.GOLD + " ein um zurück zu deinem Todesort zurück zu kommen.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ChallengeManager challengeManager = ChallengesPlugin.getInstance().getChallengeManager();
        if (!challengeManager.isRunning()) {
            return;
        }

        Player player = event.getPlayer();
        Optional<ForceItemBattleChallengeParticipant> optionalPlayerInstance = challenge.getChallengeParticipantByUUID(player.getUniqueId());
        if(optionalPlayerInstance.isPresent()) {
            ChallengeParticipant challengeParticipant = optionalPlayerInstance.get();
            challengeParticipant.updatePlayer(player);
        }
    }
}
