package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.inventory.settings;

import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import de.janhck.forceitembattlechallenge.gui.builder.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

public class ElytraSettingItem extends PagedInventoryItem<Boolean> {

    public static String KEY = "withElytra";
    private boolean isElytraActive = true;

    public ElytraSettingItem(int slot) {
        super(slot);

        addClickConsumer(new ClickAction<Boolean>() {
            @Override
            public void handleClick(Handler<Boolean> handler) {
                isElytraActive = !isElytraActive;
            }
        });
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStackBuilder(Material.ELYTRA)
                .withDisplayName("Elytra aktivieren")
                .withDescriptionHeadline(isElytraActive ? "§aAktiviert" : "§cDeaktiviert")
                .withLeftClickDescription("Aktiviert oder deaktiviert diese Einstellung")
                .withDescription(ChatPaginator.paginate("§7Ist diese Einstellung aktiviert, so kann ein Spieler beim Tod nicht mehr seine Items verlieren.", 25))
                .build();
    }

    @Override
    public Boolean getResult() {
        return isElytraActive;
    }

}
