package de.janhck.forceitembattlechallenge.gui.items.settings;

import de.janhck.forceitembattlechallenge.constants.Keys;
import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Stream;

public class JokerAmountItem extends PagedInventoryItem<Integer> {

    private final ItemStack itemStack;
    private int jokerAmount = 1;

    public JokerAmountItem(int slot) {
        super(slot);

        ItemStack itemStack = new ItemStack(Material.NETHER_STAR);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§8» §7Anzahl Joker");
        itemMeta.setLore(Stream.of("§3" + jokerAmount).toList());
        itemStack.setItemMeta(itemMeta);
        this.itemStack = itemStack;

        addClickConsumer(new ClickAction<Integer>() {
            @Override
            public void handleClick(Handler<Integer> handler) {
                InventoryClickEvent event = handler.getEvent();
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
                updateItemStackDescription();
            }
        });
    }

    private void updateItemStackDescription() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Stream.of("§3" + jokerAmount).toList());
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public String getKey() {
        return Keys.JOKER_AMOUNT;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public Integer getResult() {
        return this.jokerAmount;
    }

}
