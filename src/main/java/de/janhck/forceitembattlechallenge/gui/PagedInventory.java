package de.janhck.forceitembattlechallenge.gui;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.gui.builder.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class PagedInventory extends Interactable {

    private final Inventory inventory;
    private final List<PagedInventoryItem<?>> inventoryItems = new LinkedList<>();
    private final PagedInventoryListeners listeners = new PagedInventoryListeners(this);

    public PagedInventory() {
        this.inventory = createInventory();
    }

    public void openInventory(HumanEntity entity) {
        inventory.clear();

        // Predefine all slots with a placeholder
        IntStream.range(0, inventory.getSize()).forEach(slot -> inventory.setItem(slot, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).build()));

        inventoryItems.forEach(item -> {
            inventory.setItem(item.getSlot(), item.getItemStack());
        });

        entity.openInventory(inventory);

        // register listeners
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(listeners, ChallengesPlugin.getInstance());
    }

    public void closeInventory(HumanEntity entity) {
        // unregister listeners
        HandlerList.unregisterAll(listeners);

        // close the inventory
        entity.closeInventory();
    }

    public abstract Inventory createInventory();

    public abstract PagedInventory getNextInventory();

    public abstract PagedInventory getPreviousInventory();

    public Inventory getInventory() {
        return inventory;
    }

    public void addInventoryItem(PagedInventoryItem<?> item) {
        inventoryItems.add(item);
    }

    public Optional<PagedInventoryItem<?>> findInventoryItem(ItemStack itemStack) {
        return inventoryItems
                .stream()
                .filter(item -> item.getItemStack().equals(itemStack))
                .findFirst();
    }

    public Map<String, Object> getResults() {
        return inventoryItems
                .stream()
                .filter(item -> item.getResult() != null)
                .collect(Collectors.toMap(PagedInventoryItem::getKey, PagedInventoryItem::getResult));
    }
}
