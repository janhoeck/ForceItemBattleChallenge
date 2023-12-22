package de.janhck.forceitembattlechallenge.manager.ui.items.settings;

import de.janhck.forceitembattlechallenge.manager.ui.inventories.AbstractInventory;
import de.janhck.forceitembattlechallenge.manager.ui.items.AbstractInventoryItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Stream;

public class JokerAmountItem extends AbstractInventoryItem<Integer> {

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
    }

    private void updateItemStackDescription() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Stream.of("§3" + jokerAmount).toList());
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public String getKey() {
        return "jokerAmount";
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public Runnable getLeftClickAction() {
        return () -> {
            jokerAmount = jokerAmount + 1;
            if(jokerAmount >= 64) {
                jokerAmount = 64;
            }

            updateItemStackDescription();
        };
    }

    @Override
    public Runnable getRightClickAction() {
        return () -> {
            jokerAmount = jokerAmount - 1;
            if(jokerAmount <= 1) {
                jokerAmount = 1;
            }

            updateItemStackDescription();
        };
    }

    @Override
    public Integer getResult() {
        return null;
    }

    @Override
    public AbstractInventory getNextInventory() {
        return null;
    }
}
