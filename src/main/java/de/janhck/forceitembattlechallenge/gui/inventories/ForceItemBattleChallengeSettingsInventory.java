package de.janhck.forceitembattlechallenge.gui.inventories;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import de.janhck.forceitembattlechallenge.gui.PagedInventory;
import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import de.janhck.forceitembattlechallenge.gui.items.common.BackItem;
import de.janhck.forceitembattlechallenge.gui.items.common.StartChallengeItem;
import de.janhck.forceitembattlechallenge.gui.items.settings.DifficultyLevelItem;
import de.janhck.forceitembattlechallenge.gui.items.settings.JokerAmountItem;
import de.janhck.forceitembattlechallenge.gui.items.settings.TimeSettingsItem;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class ForceItemBattleChallengeSettingsInventory extends PagedInventory {

    private final PagedInventory previousInventory;

    public ForceItemBattleChallengeSettingsInventory(PagedInventory previousInventory) {
        this.previousInventory = previousInventory;

        addInventoryItem(new TimeSettingsItem(0));
        addInventoryItem(new JokerAmountItem(1));
        addInventoryItem(new DifficultyLevelItem(2));

        BackItem backItem = getBackItem();
        addInventoryItem(backItem);

        StartChallengeItem startChallengeItem = getStartChallengeItem();
        addInventoryItem(startChallengeItem);
    }

    private BackItem getBackItem() {
        BackItem backItem = new BackItem(9);
        backItem.addClickConsumer(new ClickAction() {
            @Override
            public void handleClick(Handler handler) {
                HumanEntity whoClicked = handler.getWhoClicked();
                closeInventory(whoClicked);
                getPreviousInventory().openInventory(whoClicked);
            }
        });
        return backItem;
    }

    private StartChallengeItem getStartChallengeItem() {
        StartChallengeItem startChallengeItem = new StartChallengeItem(17);
        startChallengeItem.addClickConsumer(new ClickAction() {
            @Override
            public void handleClick(Handler handler) {
                ChallengeType challengeType = (ChallengeType) getPreviousInventory().getResults().get("challengeType");
                Map<String, Object> parameters = getResults();

                ChallengeManager challengeManager = ChallengesPlugin.getInstance().getChallengeManager();
                challengeManager.startChallengeByType(challengeType, parameters);
                closeInventory(handler.getEvent().getWhoClicked());
            }
        });
        return startChallengeItem;
    }

    @Override
    public Inventory createInventory() {
        return Bukkit.createInventory(null, 18, "Settings");
    }

    @Override
    public PagedInventory getNextInventory() {
        return null;
    }

    @Override
    public PagedInventory getPreviousInventory() {
        return previousInventory;
    }

}
