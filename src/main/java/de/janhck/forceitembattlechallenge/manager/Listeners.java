package de.janhck.forceitembattlechallenge.manager;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (ForceItemBattleChallenge.getTimer().isRunning()) {
            if (!ForceItemBattleChallenge.getGamemanager().isPlayerInMaps(event.getPlayer())) {
                event.getPlayer().getInventory().clear();
                event.getPlayer().setLevel(0);
                event.getPlayer().setExp(0);
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
            } else {
                ForceItemBattleChallenge.getTimer().getBossBar().get(event.getPlayer().getUniqueId()).addPlayer(event.getPlayer());
                event.getPlayer().setWalkSpeed(ForceItemBattleChallenge.getInvSettings().getWalkSpeed());
                event.getPlayer().setAllowFlight(ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.fly"));
                event.getPlayer().setFlySpeed(ForceItemBattleChallenge.getInvSettings().getFlySpeed());
                if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.haste")) {
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999999, ForceItemBattleChallenge.getInvSettings().getHasteLevel(), false, false, false));
                } else event.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
                if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.jumpBoost")) {
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, ForceItemBattleChallenge.getInvSettings().getJumpBoostLevel(), false, false, false));
                } else event.getPlayer().removePotionEffect(PotionEffectType.JUMP);
            }
        } else {
            event.getPlayer().getInventory().clear();
            event.getPlayer().setLevel(0);
            event.getPlayer().setExp(0);
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
            event.getPlayer().getInventory().setItem(4, ForceItemBattleChallenge.getInvSettings().createGuiItem(Material.COMPASS, ChatColor.GREEN + "Teleporter", "right click to use"));
            if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame"))
                event.getPlayer().getInventory().setItem(0, ForceItemBattleChallenge.getInvSettings().createGuiItem(Material.PAPER, ChatColor.GREEN + "Teams", "right click to choose your team"));
            if (event.getPlayer().isOp()) {
                event.getPlayer().getInventory().setItem(7, ForceItemBattleChallenge.getInvSettings().createGuiItem(Material.COMMAND_BLOCK_MINECART, ChatColor.YELLOW + "Settings", "right click to edit"));
                event.getPlayer().getInventory().setItem(8, ForceItemBattleChallenge.getInvSettings().createGuiItem(Material.LIME_DYE, ChatColor.GREEN + "Start", "right click to start"));
            }
            event.getPlayer().setWalkSpeed(ForceItemBattleChallenge.getInvSettings().getWalkSpeed());
            event.getPlayer().setAllowFlight(ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.fly"));
            event.getPlayer().setFlySpeed(ForceItemBattleChallenge.getInvSettings().getFlySpeed());
            if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.haste")) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999999, ForceItemBattleChallenge.getInvSettings().getHasteLevel(), false, false, false));
            } else event.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
            if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.jumpBoost")) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, ForceItemBattleChallenge.getInvSettings().getJumpBoostLevel(), false, false, false));
            } else event.getPlayer().removePotionEffect(PotionEffectType.JUMP);
        }
        event.getPlayer().setScoreboard(ForceItemBattleChallenge.getGamemanager().getBoard());
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) { // triggered if a joker is used
        if (!ForceItemBattleChallenge.getTimer().isRunning()) {
            return;
        }

        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.NETHER_STAR) {
            return;
        }

        if (!ForceItemBattleChallenge.getGamemanager().isPlayerInMaps(e.getPlayer())) {
            return;
        }

        if (ForceItemBattleChallenge.getGamemanager().hasDelay(e.getPlayer())) {
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
        Material mat;
        if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame")) {
            /////////////////////////////////////// TEAMS ///////////////////////////////////////
            mat = ForceItemBattleChallenge.getGamemanager().getMaterialTeamsFromPlayer(e.getPlayer());
        } else {
            mat = ForceItemBattleChallenge.getGamemanager().getCurrentMaterial(e.getPlayer());
        }
        e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().first(Material.NETHER_STAR), stack);
        e.getPlayer().getInventory().addItem(new ItemStack(mat));
        if (!e.getPlayer().getInventory().contains(mat)) {
            e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(mat));
        }
        ForceItemBattleChallenge.getTimer().sendActionBar();
        e.getPlayer().playSound(e.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
        ForceItemBattleChallenge.getGamemanager().setDelay(e.getPlayer(), 10);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (ForceItemBattleChallenge.getTimer().isRunning()) {
            if (!(event.getEntity() instanceof Player)) return;
            if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.damage")) return;
            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
                    event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK ||
                    event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE ||
                    event.getCause() == EntityDamageEvent.DamageCause.THORNS) return;
            event.setCancelled(true);
        } else event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) { // pvp
            event.setCancelled(!ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.pvp"));
        } else if (event.getEntity() instanceof Player && !(event.getDamager() instanceof Player)) { // damage on player by mob
            event.setCancelled(!ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.damage"));
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.damage")) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (ForceItemBattleChallenge.getTimer().isRunning()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (ForceItemBattleChallenge.getTimer().isRunning()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (ForceItemBattleChallenge.getTimer().isRunning()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (ForceItemBattleChallenge.getTimer().isRunning()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (ForceItemBattleChallenge.getTimer().isRunning()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (ForceItemBattleChallenge.getTimer().isRunning()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (ForceItemBattleChallenge.getTimer().isRunning()) return;
        if (event.getTarget() == null) return;
        if (event.getTarget().getType() != EntityType.PLAYER) return;
        event.setTarget(null);
        event.setCancelled(true);
    }
}
