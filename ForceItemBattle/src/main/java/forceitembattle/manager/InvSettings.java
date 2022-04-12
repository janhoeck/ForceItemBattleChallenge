package forceitembattle.manager;

import forceitembattle.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class InvSettings implements Listener{
        private final Inventory inv;

        public InvSettings() {
            // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
            inv = Bukkit.createInventory(null, 27, "Settings");

            // Put the items into the inventory
            initializeItems();
        }

        // You can call this whenever you want to put the items in
        public void initializeItems() {
            int i = 0;
            inv.clear();

            inv.setItem(11, createGuiItem(Material.ENCHANTED_GOLDEN_APPLE, ChatColor.YELLOW + "Damage", String.valueOf(Main.getInstance().getConfig().getBoolean("settings.damage"))));
            inv.setItem(13, createGuiItem(Material.WOODEN_SWORD, ChatColor.YELLOW + "PVP", String.valueOf(Main.getInstance().getConfig().getBoolean("settings.pvp"))));
            inv.setItem(15, createGuiItem(Material.TOTEM_OF_UNDYING, ChatColor.YELLOW + "KeepInventory", String.valueOf(Main.getInstance().getConfig().getBoolean("settings.keepinventory"))));
            inv.setItem(26, createGuiItem(Material.LIME_DYE, ChatColor.GREEN + "Start",
                    "time: " + Main.getInstance().getConfig().getInt("standard.countdown"),
                    "jokers: " + Main.getInstance().getConfig().getInt("standard.jokers"),
                    "damage: " + Main.getInstance().getConfig().getBoolean("settings.damage"),
                    "pvp: " + Main.getInstance().getConfig().getBoolean("settings.pvp"),
                    "keepInventory: " + Main.getInstance().getConfig().getBoolean("settings.keepinventory")));
        }

        // Nice little method to create a gui item with a custom name, and description
        protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
            final ItemStack item = new ItemStack(material, 1);
            final ItemMeta meta = item.getItemMeta();

            // Set the name of the item
            meta.setDisplayName(name);

            // Set the lore of the item
            meta.setLore(Arrays.asList(lore));

            item.setItemMeta(meta);

            return item;
        }

        // You can open the inventory with this
        public void openInventory(final HumanEntity ent) {
            initializeItems();
            ent.openInventory(inv);
        }

        // Check for clicks on items
        @EventHandler
        public void onInventoryClick(final InventoryClickEvent e) {
            if (!e.getInventory().equals(inv)) return;

            e.setCancelled(true);

            final ItemStack clickedItem = e.getCurrentItem();

            // verify current item is not null
            if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

            final Player p = (Player) e.getWhoClicked();

            // Using slots click is a best option for your inventory click's
            //p.sendMessage("You clicked at slot " + e.getRawSlot());
            if (e.getRawSlot() > inv.getSize()) return;

            switch (e.getRawSlot()) {
                case 11: { //toggle damage
                    Main.getInstance().getConfig().set("settings.damage", !Main.getInstance().getConfig().getBoolean("settings.damage"));
                    Main.getInstance().saveConfig();
                    break;
                }
                case 13: { //toggle pvp
                    Main.getInstance().getConfig().set("settings.pvp", !Main.getInstance().getConfig().getBoolean("settings.pvp"));
                    Main.getInstance().saveConfig();
                    break;
                }
                case 15: { //toggle keepInventory
                    Main.getInstance().getConfig().set("settings.keepinventory", !Main.getInstance().getConfig().getBoolean("settings.keepinventory"));
                    Main.getInstance().saveConfig();
                    Bukkit.getWorlds().forEach(world -> world.setGameRule(GameRule.KEEP_INVENTORY, Main.getInstance().getConfig().getBoolean("settings.keepinventory")));
                    break;
                }
                case 26: {
                    Main.getGamemanager().startGame(e.getWhoClicked());
                    e.getWhoClicked().closeInventory();
                    break;
                }
                default:
                    break;
            }

            initializeItems();
        }

        // Cancel dragging in our inventory
        @EventHandler
        public void onInventoryClick(final InventoryDragEvent e) {
            if (e.getInventory().equals(inv)) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onClick(PlayerInteractEvent e) {
            HumanEntity p = e.getPlayer();
            if (p.getInventory().getItemInMainHand().getType() == Material.COMMAND_BLOCK_MINECART) openInventory(p);
            if (p.getInventory().getItemInMainHand().getType() == Material.LIME_DYE) Main.getGamemanager().startGame(p);
        }

    public Inventory getInv() {
        return inv;
    }
}
