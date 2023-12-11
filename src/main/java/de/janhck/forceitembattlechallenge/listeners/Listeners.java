package de.janhck.forceitembattlechallenge.listeners;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import de.janhck.forceitembattlechallenge.challlenge.ChallengeParticipant;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class Listeners implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) { // triggered if a joker is used
        if (!ForceItemBattleChallenge.getGamemanager().isRunning()) {
            return;
        }

        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() != Material.NETHER_STAR) {
            return;
        }

        Optional<ChallengeParticipant> optionalPlayerInstance = ForceItemBattleChallenge.getGamemanager().getChallenge().getChallengeParticipant(player);
        if(!optionalPlayerInstance.isPresent()) {
            return;
        }

        ChallengeParticipant challengeParticipant = optionalPlayerInstance.get();
        if (!player.getTargetBlock(null, 5).isEmpty()) {
            player.sendMessage(ForceItemBattleChallenge.PREFIX + "Du kannst den Joker nicht benutzen, während du einen Block ansiehst.");
            return;
        }

        if(!challengeParticipant.canSkip()) {
            player.sendMessage(ForceItemBattleChallenge.PREFIX + "Bitte warte 5 Sekunden, bevor du einen weiteren Joker verwenden kannst.");
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

        challengeParticipant.skipItem();
    }

}
