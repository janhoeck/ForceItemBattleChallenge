package de.janhck.forceitembattlechallenge.gui;

import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Optional;

public class PagedInventoryListeners implements Listener {

    private final PagedInventory pagedInventory;

    public PagedInventoryListeners(PagedInventory abstractInventory) {
        this.pagedInventory = abstractInventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getInventory().equals(pagedInventory.getInventory())) {
            return;
        }

        event.setCancelled(true);

        Optional<PagedInventoryItem<?>> optionalInventoryItem = pagedInventory.findInventoryItem(event.getCurrentItem());
        if(optionalInventoryItem.isEmpty()) {
            return;
        }

        PagedInventoryItem<?> inventoryItem = optionalInventoryItem.get();
        ClickAction.Handler<?> clickActionHandler = new ClickAction.Handler<>(event, inventoryItem);

        // Execute click listeners
        inventoryItem.callClickConsumers(clickActionHandler);
        pagedInventory.callClickConsumers(clickActionHandler);

        // Update the item at the slot, because an action can change the item metadata
        pagedInventory.getInventory().setItem(event.getSlot(), inventoryItem.getItemStack());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!event.getInventory().equals(pagedInventory.getInventory())) {
            return;
        }

        // We have to call closeInventory manually on inventory close (e.g. pressing ESC key) to prevent memory leaks.
        // closeInventory removes unnecessary event listeners.
        pagedInventory.closeInventory(event.getPlayer());
    }
}
