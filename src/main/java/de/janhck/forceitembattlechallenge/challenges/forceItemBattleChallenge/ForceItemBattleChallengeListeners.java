package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.AbstractChallengeParticipant;
import org.bukkit.Location;
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
        if (!ChallengesPlugin.getChallengeManager().isRunning()) {
            return;
        }

        Player player = event.getPlayer();
        Optional<ForceItemBattleChallengeParticipant> optionalPlayerInstance = challenge.getChallengeParticipant(player);
        if(!optionalPlayerInstance.isPresent()) {
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
        if (!ChallengesPlugin.getChallengeManager().isRunning()) {
            return;
        }

        Player player = event.getEntity();
        boolean isParticipant = ChallengesPlugin.getChallengeManager().getCurrentChallenge().isParticipant(player);
        if(isParticipant) {
            Location location = player.getLocation();
            player.sendMessage(ChallengesPlugin.PREFIX + "Du bist bei X:" + location.getBlockX() + " Y:" + location.getBlockY() + " Z:" + location.getBlockZ() + " gestorben.");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!ChallengesPlugin.getChallengeManager().isRunning()) {
            return;
        }

        Player player = event.getPlayer();
        Optional<ForceItemBattleChallengeParticipant> optionalPlayerInstance = challenge.getChallengeParticipantByUUID(player.getUniqueId());
        if(optionalPlayerInstance.isPresent()) {
            AbstractChallengeParticipant challengeParticipant = optionalPlayerInstance.get();
            challengeParticipant.updatePlayer(player);
        }
    }
}
