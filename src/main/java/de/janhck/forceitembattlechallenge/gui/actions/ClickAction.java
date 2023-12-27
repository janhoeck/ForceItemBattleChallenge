package de.janhck.forceitembattlechallenge.gui.actions;

import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class ClickAction<R> {

    public abstract void handleClick(Handler<R> handler);

    public static class Handler<R> {

        private final InventoryClickEvent event;
        private final PagedInventoryItem<R> item;

        public Handler(InventoryClickEvent event, PagedInventoryItem<R> item) {
            this.event = event;
            this.item = item;
        }

        public InventoryClickEvent getEvent() {
            return event;
        }

        public PagedInventoryItem<R> getItem() {
            return item;
        }

        public HumanEntity getWhoClicked() {
            return event.getWhoClicked();
        }
    }

}
