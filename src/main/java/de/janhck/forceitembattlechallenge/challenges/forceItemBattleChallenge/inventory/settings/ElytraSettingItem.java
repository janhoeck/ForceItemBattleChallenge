package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.inventory.settings;

import de.janhck.forceitembattlechallenge.constants.Keys;
import de.janhck.forceitembattlechallenge.gui.PagedInventoryItem;
import de.janhck.forceitembattlechallenge.gui.actions.ClickAction;
import de.janhck.forceitembattlechallenge.gui.builder.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

public class ElytraSettingItem extends PagedInventoryItem<Boolean> {

    private boolean withElytra = true;

    public ElytraSettingItem(int slot) {
        super(slot);

        addClickConsumer(new ClickAction() {
            @Override
            public void handleClick(Handler handler) {
                withElytra = !withElytra;
            }
        });
    }

    @Override
    public String getKey() {
        return Keys.WITH_ELYTRA;
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStackBuilder(Material.ELYTRA)
                .withDisplayName("Elytra aktivieren")
                .withDescriptionHeadline(withElytra ? "§aAktiviert" : "§cDeaktiviert")
                .withLeftClickDescription("Aktiviert oder deaktiviert diese Einstellung")
                .withDescription(ChatPaginator.paginate("§7Ist diese Einstellung aktiviert, so kann ein Spieler beim Tod nicht mehr seine Items verlieren.", 25))
                .build();
    }

    @Override
    public Boolean getResult() {
        return withElytra;
    }

}
