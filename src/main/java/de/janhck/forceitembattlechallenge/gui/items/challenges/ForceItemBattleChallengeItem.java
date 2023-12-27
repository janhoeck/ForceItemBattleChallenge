package de.janhck.forceitembattlechallenge.gui.items.challenges;

import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import de.janhck.forceitembattlechallenge.constants.Keys;
import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import de.janhck.forceitembattlechallenge.gui.builder.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ForceItemBattleChallengeItem extends PagedInventoryItem<ChallengeType> {

    public ForceItemBattleChallengeItem(int slot) {
        super(slot);
    }

    @Override
    public String getKey() {
        return Keys.CHALLENGE_TYPE;
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStackBuilder(Material.WOODEN_AXE)
                .withDisplayName("Force Item Battle Challenge")
                .build();
    }

    @Override
    public ChallengeType getResult() {
        return ChallengeType.FORCE_ITEM_BATTLE;
    }

}
