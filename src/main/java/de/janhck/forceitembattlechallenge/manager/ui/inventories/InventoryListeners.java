package de.janhck.forceitembattlechallenge.manager.ui.inventories;

import de.janhck.forceitembattlechallenge.manager.ui.items.AbstractInventoryItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

public class InventoryListeners implements Listener {

    private final AbstractInventory abstractInventory;

    public InventoryListeners(AbstractInventory abstractInventory) {
        this.abstractInventory = abstractInventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getInventory().equals(abstractInventory.getInventory())) {
            return;
        }

        event.setCancelled(true);

        Optional<AbstractInventoryItem<?>> optionalInventoryItem = abstractInventory.findInventoryItem(event.getCurrentItem());
        if(optionalInventoryItem.isEmpty()) {
            return;
        }

        AbstractInventoryItem<?> inventoryItem = optionalInventoryItem.get();
        if(event.isLeftClick() && inventoryItem.getLeftClickAction() != null) {
            inventoryItem.getLeftClickAction().run();
        } else if(event.isRightClick() && inventoryItem.getRightClickAction() != null) {
            inventoryItem.getRightClickAction().run();
        }

        // Update the item at the slot, because an action can change the item metadata
        abstractInventory.getInventory().setItem(event.getSlot(), inventoryItem.getItemStack());

        if(inventoryItem.getNextInventory() != null) {
            abstractInventory.closeInventory(event.getWhoClicked());
            inventoryItem.getNextInventory().openInventory(event.getWhoClicked());
        }
    }
}
