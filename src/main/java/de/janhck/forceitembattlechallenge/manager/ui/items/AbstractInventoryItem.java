package de.janhck.forceitembattlechallenge.manager.ui.items;

import de.janhck.forceitembattlechallenge.manager.ui.inventories.AbstractInventory;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractInventoryItem<V> {

    private int slot = 0;

    public AbstractInventoryItem(int slot) {
        this.slot = slot;
    }

    public abstract String getKey();

    public abstract ItemStack getItemStack();

    public abstract Runnable getLeftClickAction();

    public abstract Runnable getRightClickAction();

    public abstract V getResult();

    public abstract AbstractInventory getNextInventory();

    public int getSlot() {
        return slot;
    }
}
