package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.inventory.settings;

import de.janhck.forceitembattlechallenge.constants.ItemDifficultyLevel;
import de.janhck.forceitembattlechallenge.constants.Keys;
import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import de.janhck.forceitembattlechallenge.gui.builder.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DifficultyLevelItem extends PagedInventoryItem<ItemDifficultyLevel> {

    private ItemDifficultyLevel level = ItemDifficultyLevel.EASY;

    public DifficultyLevelItem(int slot) {
        super(slot);
        addClickConsumer(new ClickAction<ItemDifficultyLevel>() {
            @Override
            public void handleClick(Handler<ItemDifficultyLevel> handler) {
                InventoryClickEvent event = handler.getEvent();
                if(event.isLeftClick()) {
                    level = level.next();
                } else if(event.isRightClick()) {
                    level = level.prev();
                }
            }
        });
    }
    @Override
    public String getKey() {
        return Keys.DIFFICULTY;
    }

    @Override
    public ItemStack getItemStack() {
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

        return new ItemStackBuilder(material)
                .withDisplayName("Schwierigkeit")
                .withDescriptionHeadline("§3" + level)
                .withLeftClickDescription("Ändere die Schwierigkeit")
                .build();
    }

    @Override
    public ItemDifficultyLevel getResult() {
        return this.level;
    }
}
