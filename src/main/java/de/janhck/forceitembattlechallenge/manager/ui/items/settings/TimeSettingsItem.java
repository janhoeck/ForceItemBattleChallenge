package de.janhck.forceitembattlechallenge.manager.ui.items.settings;

import de.janhck.forceitembattlechallenge.manager.ui.inventories.AbstractInventory;
import de.janhck.forceitembattlechallenge.manager.ui.items.AbstractInventoryItem;
import de.janhck.forceitembattlechallenge.utils.TimeUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Stream;

public class TimeSettingsItem extends AbstractInventoryItem<Integer> {

    private final ItemStack itemStack;
    private int timeInSeconds = 0;

    public TimeSettingsItem(int slot) {
        super(slot);

        ItemStack itemStack = new ItemStack(Material.CLOCK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§8» §7Dauer der Challenge");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        this.itemStack = itemStack;

        updateItemStackDescription();
    }

    private void updateItemStackDescription() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Stream.of("§3" + TimeUtil.formatSeconds(timeInSeconds)).toList());
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public String getKey() {
        return "timeInSeconds";
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public Runnable getLeftClickAction() {
        return () -> {
            timeInSeconds = timeInSeconds + (60 * 5);
            updateItemStackDescription();
        };
    }

    @Override
    public Runnable getRightClickAction() {
        return () -> {
            timeInSeconds = timeInSeconds - (60 * 5);
            if(timeInSeconds <= 0) {
                timeInSeconds = 0;
            }
            updateItemStackDescription();
        };
    }

    @Override
    public Integer getResult() {
        return timeInSeconds;
    }

    @Override
    public AbstractInventory getNextInventory() {
        return null;
    }
}
