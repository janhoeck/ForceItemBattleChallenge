package de.janhck.forceitembattlechallenge.gui.items.common;

import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import de.janhck.forceitembattlechallenge.gui.builder.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BackItem extends PagedInventoryItem<Void> {

    public BackItem(int slot) {
        super(slot);
    }

    @Override
    public String getKey() {
        return "back";
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStackBuilder(Material.PLAYER_HEAD)
                .withDisplayName("Zurück")
                .asSkull("f7aacad193e2226971ed95302dba433438be4644fbab5ebf818054061667fbe2")
                .build();
    }

    @Override
    public Void getResult() {
        return null;
    }
}
