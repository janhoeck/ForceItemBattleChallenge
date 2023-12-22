package de.janhck.forceitembattlechallenge.manager.ui.inventories;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.manager.ui.items.AbstractInventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractInventory {

    private final Inventory inventory;
    private final List<AbstractInventoryItem<?>> items;
    private final InventoryListeners listeners = new InventoryListeners(this);

    public AbstractInventory(int size, String title, List<AbstractInventoryItem<?>> items) {
        this.inventory = Bukkit.createInventory(null, size, title);
        this.items = items;
    }

    public void openInventory(HumanEntity entity) {
        inventory.clear();

        items.forEach(item -> {
            inventory.setItem(item.getSlot(), item.getItemStack());
        });

        entity.openInventory(inventory);

        // register listeners
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(listeners, ChallengesPlugin.getInstance());
    }

    public void closeInventory(HumanEntity entity) {
        entity.closeInventory();

        // unregister listeners
        HandlerList.unregisterAll(listeners);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<AbstractInventoryItem<?>> getItems() {
        return items;
    }

    public Optional<AbstractInventoryItem<?>> findInventoryItem(ItemStack itemStack) {
        return items
                .stream()
                .filter(item -> item.getItemStack().equals(itemStack))
                .findFirst();
    }

    public Map<String, Object> collectResults() {
        Map<String, Object> map = new LinkedHashMap<>();
        items.forEach(item -> map.put(item.getKey(), item.getResult()));
        return map;
    }
}
