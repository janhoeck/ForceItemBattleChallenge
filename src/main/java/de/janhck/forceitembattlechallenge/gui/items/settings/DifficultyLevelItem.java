package de.janhck.forceitembattlechallenge.gui.items.settings;

import de.janhck.forceitembattlechallenge.constants.ItemDifficultyLevel;
import de.janhck.forceitembattlechallenge.constants.Keys;
import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Stream;

public class DifficultyLevelItem extends PagedInventoryItem<ItemDifficultyLevel> {

    private ItemStack itemStack;
    private ItemDifficultyLevel level = ItemDifficultyLevel.EASY;

    public DifficultyLevelItem(int slot) {
        super(slot);
        this.itemStack = getItemStackByLevel(level);

        addClickConsumer(new ClickAction<ItemDifficultyLevel>() {
            @Override
            public void handleClick(Handler<ItemDifficultyLevel> handler) {
                InventoryClickEvent event = handler.getEvent();
                if(event.isLeftClick()) {
                    level = level.next();
                    itemStack = getItemStackByLevel(level);
                } else if(event.isRightClick()) {
                    level = level.prev();
                    itemStack = getItemStackByLevel(level);
                }
            }
        });
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
        return Keys.DIFFICULTY;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public ItemDifficultyLevel getResult() {
        return this.level;
    }
}
