package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.inventory.settings;

import de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.ForceItemBattleChallengeMode;
import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import de.janhck.forceitembattlechallenge.gui.builder.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

public class ModeSettingItem extends PagedInventoryItem<ForceItemBattleChallengeMode> {

    public static String KEY = "mode";
    private ForceItemBattleChallengeMode mode = ForceItemBattleChallengeMode.DEFAULT;

    public ModeSettingItem(int slot) {
        super(slot);

        addClickConsumer(new ClickAction<ForceItemBattleChallengeMode>() {
            @Override
            public void handleClick(Handler<ForceItemBattleChallengeMode> handler) {
                InventoryClickEvent event = handler.event();
                if(event.isLeftClick()) {
                    mode = mode.next();
                } else if(event.isRightClick()) {
                    mode = mode.prev();
                }
            }
        });
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStackBuilder(Material.PAPER)
                .withDisplayName("Modus")
                .withDescriptionHeadline(mode.toString())
                .withDescription(ChatPaginator.paginate("§7Steht der Modus auf 'DEFAULT' so erhält jeder Spieler unterschiedliche Items. Ist der Modus auf 'ALL_SAME_ITEMS' eingestellt, so erhählt jeder Spieler die selben Items.", 25))
                .withLeftClickDescription("Ändern des Modus")
                .build();
    }

    @Override
    public ForceItemBattleChallengeMode getResult() {
        return mode;
    }
}
