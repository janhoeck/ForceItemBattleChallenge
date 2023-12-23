package de.janhck.forceitembattlechallenge.gui;

import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class PagedInventoryItem<R> {

    private int slot = 0;
    private List<ClickAction> clickConsumerList = new ArrayList<>();

    public PagedInventoryItem(int slot) {
        this.slot = slot;
    }

    public abstract String getKey();

    public abstract ItemStack getItemStack();

    public abstract R getResult();

    public int getSlot() {
        return slot;
    }

    public void addClickConsumer(ClickAction clickAction) {
        clickConsumerList.add(clickAction);
    }

    public List<ClickAction> getClickConsumers() {
        return clickConsumerList;
    }
}
