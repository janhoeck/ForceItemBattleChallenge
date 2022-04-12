package forceitembattle.manager;

import forceitembattle.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Main.getTimer().isRunning()) return;
        event.getPlayer().getInventory().clear();
        if (!event.getPlayer().isOp()) return;
        event.getPlayer().getInventory().setItem(7, Main.getInvSettings().createGuiItem(Material.COMMAND_BLOCK_MINECART, ChatColor.YELLOW + "Settings", "right click to edit"));
        event.getPlayer().getInventory().setItem(8, Main.getInvSettings().createGuiItem(Material.LIME_DYE, ChatColor.GREEN + "Start", "right click to start"));
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (!Main.getTimer().isRunning()) return;
        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.NETHER_STAR) return;
        if (Main.getGamemanager().hasDelay(e.getPlayer())) {
            e.getPlayer().sendMessage(ChatColor.RED + "Please wait a second.");
            return;
        }
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

        Main.getGamemanager().setDelay(e.getPlayer(), 2);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (Main.getInstance().getConfig().getBoolean("settings.damage")) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        if (Main.getInstance().getConfig().getBoolean("settings.pvp")) return;
        event.setCancelled(true);
    }
}
