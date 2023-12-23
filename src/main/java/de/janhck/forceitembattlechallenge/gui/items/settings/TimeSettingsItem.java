package de.janhck.forceitembattlechallenge.gui.items.settings;

import de.janhck.forceitembattlechallenge.constants.Keys;
import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import de.janhck.forceitembattlechallenge.utils.TimeUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Stream;

public class TimeSettingsItem extends PagedInventoryItem<Integer> {

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

        addClickConsumer(new ClickAction<Integer>() {
            @Override
            public void handleClick(Handler<Integer> handler) {
                InventoryClickEvent event = handler.getEvent();
                if(event.isLeftClick()) {
                    timeInSeconds = timeInSeconds + (60 * 5);
                } else if(event.isRightClick()) {
                    timeInSeconds = timeInSeconds - (60 * 5);
                    if(timeInSeconds <= 0) {
                        timeInSeconds = 0;
                    }
                }
                updateItemStackDescription();
            }
        });
    }

    private void updateItemStackDescription() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Stream.of("§3" + TimeUtil.formatSeconds(timeInSeconds)).toList());
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public String getKey() {
        return Keys.TIME_IN_SECONDS;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public Integer getResult() {
        return timeInSeconds;
    }
}
