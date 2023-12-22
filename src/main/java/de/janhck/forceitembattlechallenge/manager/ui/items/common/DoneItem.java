package de.janhck.forceitembattlechallenge.manager.ui.items.common;

import de.janhck.forceitembattlechallenge.manager.ui.inventories.AbstractInventory;
import de.janhck.forceitembattlechallenge.manager.ui.items.AbstractInventoryItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class DoneItem extends AbstractInventoryItem<Void> {

    private final ItemStack itemStack;
    private Runnable action;

    public DoneItem(int slot, Runnable action) {
        super(slot);

        this.action = action;

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        itemMeta.setDisplayName("§8» §7Weiter");
        itemMeta.setOwner("MHF_ArrowRight");
        itemStack.setItemMeta(itemMeta);
        this.itemStack = itemStack;
    }

    @Override
    public String getKey() {
        return "done";
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public Runnable getLeftClickAction() {
        return action;
    }

    @Override
    public Runnable getRightClickAction() {
        return null;
    }

    @Override
    public Void getResult() {
        return null;
    }

    @Override
    public AbstractInventory getNextInventory() {
        return null;
    }

}
