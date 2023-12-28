package de.janhck.forceitembattlechallenge.gui.actions;

import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class ClickAction<R> {

    public abstract void handleClick(Handler<R> handler);

    public record Handler<R>(InventoryClickEvent event, PagedInventoryItem<R> item) {

        public HumanEntity getWhoClicked() {
                return event.getWhoClicked();
            }
        }

}
