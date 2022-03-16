package forceitembattle.manager;

import forceitembattle.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (!Main.getTimer().isRunning()) return;
        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.NETHER_STAR) return;
        if (!e.getPlayer().getTargetBlock(null, 5).isEmpty()) {
            e.getPlayer().sendMessage(ChatColor.RED + "You can´t look at a block while using a joker.");
            return;
        }
        ItemStack stack = e.getPlayer().getInventory().getItem(e.getPlayer().getInventory().first(Material.NETHER_STAR));
        if (stack.getAmount() > 1) {
            stack.setAmount(stack.getAmount() - 1);
        } else {
            stack.setType(Material.AIR);
        }
        e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().first(Material.NETHER_STAR), stack);
        e.getPlayer().getInventory().addItem(new ItemStack(Main.getGamemanager().getCurrentMaterial(e.getPlayer())));
    }
}
