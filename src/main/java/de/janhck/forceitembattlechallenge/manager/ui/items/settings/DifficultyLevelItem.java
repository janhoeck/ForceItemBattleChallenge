package de.janhck.forceitembattlechallenge.manager.ui.items.settings;

import de.janhck.forceitembattlechallenge.items.ItemDifficultyLevel;
import de.janhck.forceitembattlechallenge.manager.ui.inventories.AbstractInventory;
import de.janhck.forceitembattlechallenge.manager.ui.items.AbstractInventoryItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Stream;

public class DifficultyLevelItem extends AbstractInventoryItem<DifficultyLevelItem> {

    private ItemStack itemStack;
    private ItemDifficultyLevel level = ItemDifficultyLevel.EASY;

    public DifficultyLevelItem(int slot) {
        super(slot);
        this.itemStack = getItemStackByLevel(level);
    }

    private ItemStack getItemStackByLevel(ItemDifficultyLevel level) {
        Material material = null;

        switch(level) {
            case EASY: {
                material = Material.WOODEN_SWORD;
                break;
            }
            case MEDIUM: {
                material = Material.IRON_SWORD;
                break;
            }
            case HARD: {
                material = Material.DIAMOND_SWORD;
                break;
            }
        }

        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setDisplayName("§8» §7Schwierigkeit");
        itemMeta.setLore(Stream.of("§3" + level).toList());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getKey() {
        return "difficulty";
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public Runnable getLeftClickAction() {
        return () -> {
            level = level.next();
            itemStack = getItemStackByLevel(level);
        };
    }

    @Override
    public Runnable getRightClickAction() {
        return () -> {
            level = level.prev();
            itemStack = getItemStackByLevel(level);
        };
    }

    @Override
    public DifficultyLevelItem getResult() {
        return null;
    }

    @Override
    public AbstractInventory getNextInventory() {
        return null;
    }
}
