package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.inventory.settings;

import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import de.janhck.forceitembattlechallenge.gui.builder.ItemStackBuilder;
import de.janhck.forceitembattlechallenge.utils.TimeUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TimeSettingItem extends PagedInventoryItem<Integer> {

    public static String KEY = "timeInSeconds";
    private int timeInSeconds = 30 * 60; // default 30 min

    public TimeSettingItem(int slot) {
        super(slot);

        addClickConsumer(new ClickAction<Integer>() {
            @Override
            public void handleClick(Handler<Integer> handler) {
                InventoryClickEvent event = handler.event();
                if(event.isLeftClick()) {
                    timeInSeconds = timeInSeconds + (60 * 5);
                } else if(event.isRightClick()) {
                    timeInSeconds = timeInSeconds - (60 * 5);
                    if(timeInSeconds <= 0) {
                        timeInSeconds = 0;
                    }
                }
            }
        });
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStackBuilder(Material.CLOCK)
                .withDisplayName("Dauer der Challenge")
                .withDescriptionHeadline(TimeUtil.formatSeconds(timeInSeconds))
                .withLeftClickDescription("Füge 5 Minuten zur Zeit hinzu")
                .withRightClickDescription("Reduziere um die Zeit um 5 Minuten")
                .build();
    }

    @Override
    public Integer getResult() {
        return timeInSeconds;
    }
}
