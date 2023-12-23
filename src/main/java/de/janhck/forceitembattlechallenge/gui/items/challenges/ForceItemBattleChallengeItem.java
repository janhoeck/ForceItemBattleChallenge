package de.janhck.forceitembattlechallenge.gui.items.challenges;

import de.janhck.forceitembattlechallenge.constants.ChallengeType;
import de.janhck.forceitembattlechallenge.constants.Keys;
import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ForceItemBattleChallengeItem extends PagedInventoryItem<ChallengeType> {

    private final ItemStack itemStack;

    public ForceItemBattleChallengeItem(int slot) {
        super(slot);

        ItemStack itemStack = new ItemStack(Material.WOODEN_AXE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Force Item Battle Challenge");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        this.itemStack = itemStack;
    }

    @Override
    public String getKey() {
        return Keys.CHALLENGE_TYPE;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public ChallengeType getResult() {
        return ChallengeType.FORCE_ITEM_BATTLE;
    }

}
