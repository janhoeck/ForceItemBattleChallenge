package forceitembattle.manager;

import forceitembattle.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Listeners implements Listener {
    private static Map<UUID, ArrayList<ItemStack>> stars = new HashMap<UUID, ArrayList<ItemStack>>();

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

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().forEach(itemStack -> {
            if (itemStack.getType().equals(Material.NETHER_STAR)) {
                if (stars.containsKey(event.getEntity().getUniqueId())) {
                    stars.get(event.getEntity().getUniqueId()).add(itemStack);
                } else {
                    ArrayList<ItemStack> list = new ArrayList<ItemStack>();
                    stars.put(event.getEntity().getUniqueId(), list);
                }
                event.getDrops().remove(itemStack);
            }
        });
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (stars.containsKey(event.getPlayer().getUniqueId())) {
            stars.get(event.getPlayer().getUniqueId()).forEach(itemStack -> {
                event.getPlayer().getInventory().addItem(itemStack);
            });
            stars.remove(event.getPlayer().getUniqueId());
        }
    }
}
