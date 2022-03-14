package forceitembattle.manager;

import forceitembattle.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Main.getGamemanager().checkItem((Player) event.getEntity(), event.getItem().getItemStack().getType());
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        //Main.getGamemanager().checkItem(, event.getItem().getType());
    }
}
