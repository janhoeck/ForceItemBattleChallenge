package de.janhck.forceitembattlechallenge.gui.items.common;

import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import de.janhck.forceitembattlechallenge.gui.builder.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class StartChallengeItem extends PagedInventoryItem<Void> {

    public StartChallengeItem(int slot) {
        super(slot);
    }

    @Override
    public String getKey() {
        return "done";
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStackBuilder(Material.PLAYER_HEAD)
                .withDisplayName("Challenge starten")
                .asSkull("930f4537d214d38666e6304e9c851cd6f7e41a0eb7c25049c9d22c8c5f6545df")
                .build();
    }

    @Override
    public Void getResult() {
        return null;
    }

}
