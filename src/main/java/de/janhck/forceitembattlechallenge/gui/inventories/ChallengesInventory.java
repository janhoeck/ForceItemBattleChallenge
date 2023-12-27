package de.janhck.forceitembattlechallenge.gui.inventories;

import de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.inventory.ForceItemBattleChallengeSettingsInventory;
import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import de.janhck.forceitembattlechallenge.gui.PagedInventory;
import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import de.janhck.forceitembattlechallenge.gui.items.challenges.ForceItemBattleChallengeItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

public class ChallengesInventory extends PagedInventory {

    private ChallengeType challengeType;
    private PagedInventory nextInventory;

    public ChallengesInventory() {
        PagedInventory instance = this;

        addInventoryItem(new ForceItemBattleChallengeItem(0));
        addClickConsumer(new ClickAction<ChallengeType>() {
            @Override
            public void handleClick(Handler<ChallengeType> handler) {
                HumanEntity whoClicked = handler.getEvent().getWhoClicked();
                nextInventory = new ForceItemBattleChallengeSettingsInventory(instance);

                // Close the current inventory
                closeInventory(whoClicked);
                // Open the next inventory
                nextInventory.openInventory(whoClicked);
            }
        });
    }

    @Override
    public void openInventory(HumanEntity entity) {
        // Reset the nextInventory
        this.nextInventory = null;
        this.challengeType = null;

        super.openInventory(entity);
    }

    @Override
    public Inventory createInventory() {
        return Bukkit.createInventory(null, 9, "Challenges");
    }

    @Override
    public PagedInventory getNextInventory() {
        return nextInventory;
    }

    @Override
    public PagedInventory getPreviousInventory() {
        // This inventory never has a prev inventory
        return null;
    }

    public ChallengeType getChallengeType() {
        return challengeType;
    }
}
