package de.janhck.forceitembattlechallenge.gui;

import org.bukkit.inventory.ItemStack;

public abstract class PagedInventoryItem<R> extends Interactable {

    private int slot;

    public PagedInventoryItem(int slot) {
        this.slot = slot;
    }

    public abstract String getKey();

    public abstract ItemStack getItemStack();

    public abstract R getResult();

    public int getSlot() {
        return slot;
    }

}
