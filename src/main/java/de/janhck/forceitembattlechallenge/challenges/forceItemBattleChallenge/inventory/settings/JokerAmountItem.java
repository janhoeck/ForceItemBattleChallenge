package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.inventory.settings;

import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import de.janhck.forceitembattlechallenge.gui.builder.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class JokerAmountItem extends PagedInventoryItem<Integer> {

    public static String KEY = "jokerAmount";
    private int jokerAmount = 1;

    public JokerAmountItem(int slot) {
        super(slot);

        addClickConsumer(new ClickAction<Integer>() {
            @Override
            public void handleClick(Handler<Integer> handler) {
                InventoryClickEvent event = handler.event();
                if(event.isLeftClick()) {
                    jokerAmount = jokerAmount + 1;
                    if(jokerAmount >= 64) {
                        jokerAmount = 64;
                    }
                } else if(event.isRightClick()) {
                    jokerAmount = jokerAmount - 1;
                    if(jokerAmount <= 1) {
                        jokerAmount = 1;
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
        return new ItemStackBuilder(Material.NETHER_STAR)
                .withDisplayName("Anzahl Joker")
                .withAmount(jokerAmount)
                .withDescriptionHeadline("§3" + jokerAmount)
                .withLeftClickDescription("Erhöhe die Joker Anzahl um 1")
                .withRightClickDescription("Verringere die Joker Anzahl um 1")
                .build();
    }

    @Override
    public Integer getResult() {
        return this.jokerAmount;
    }

}
