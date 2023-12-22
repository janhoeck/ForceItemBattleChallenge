package de.janhck.forceitembattlechallenge.manager.ui.items.challenges;

import de.janhck.forceitembattlechallenge.manager.ChallengeType;
import de.janhck.forceitembattlechallenge.manager.ui.inventories.AbstractInventory;
import de.janhck.forceitembattlechallenge.manager.ui.inventories.settings.ForceItemBattleChallengeSettingsInventory;
import de.janhck.forceitembattlechallenge.manager.ui.items.AbstractInventoryItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ForceItemBattleChallengeItem extends AbstractInventoryItem<ChallengeType> {

    private final ItemStack itemStack;

    public ForceItemBattleChallengeItem(int slot) {
        super(slot);

        ItemStack itemStack = new ItemStack(Material.WOODEN_AXE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Force Item Battle Challenge");
        itemStack.setItemMeta(itemMeta);
        this.itemStack = itemStack;
    }

    @Override
    public String getKey() {
        return "challengeType";
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public Runnable getLeftClickAction() {
        return () -> {};
    }

    @Override
    public Runnable getRightClickAction() {
        return () -> {};
    }

    @Override
    public ChallengeType getResult() {
        return ChallengeType.FORCE_ITEM_BATTLE;
    }

    @Override
    public AbstractInventory getNextInventory() {
        return new ForceItemBattleChallengeSettingsInventory();
    }

}
