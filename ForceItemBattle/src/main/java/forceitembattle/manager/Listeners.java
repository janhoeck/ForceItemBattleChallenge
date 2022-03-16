package forceitembattle.manager;

import forceitembattle.Main;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Main.getGamemanager().checkItem((Player) event.getEntity(), event.getItem().getItemStack().getType());
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        if (event.getInventory().contains(Main.getGamemanager().getCurrentMaterial((Player) event.getWhoClicked()))) {
            Main.getGamemanager().checkItem((Player) event.getWhoClicked(), event.getInventory().getItem(event.getInventory().first(Main.getGamemanager().getCurrentMaterial((Player) event.getWhoClicked()))).getType());
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        HumanEntity p = e.getPlayer();
        if (!Main.getTimer().isRunning()) return;
        if (!(p.getItemInUse().getType() == Material.BARRIER)) return;
        ItemStack stack = e.getPlayer().getInventory().getItem(e.getPlayer().getInventory().first(Material.BARRIER));
        // stack -1
        e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().first(Material.BARRIER), stack);
        e.getPlayer().getInventory().addItem(new ItemStack(Main.getGamemanager().getCurrentMaterial(e.getPlayer())));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!Main.getTimer().isRunning()) return;
        if (!(event.getBlock().getType() == Material.BARRIER)) return;

    }
}
