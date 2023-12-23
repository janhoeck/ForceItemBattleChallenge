package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge;

import de.janhck.forceitembattlechallenge.challenges.AbstractChallengeParticipant;
import de.janhck.forceitembattlechallenge.constants.ItemDifficultyLevel;
import de.janhck.forceitembattlechallenge.manager.ItemsManager;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ForceItemBattleChallengeParticipant extends AbstractChallengeParticipant {

    // Contains all finished items
    private final List<Material> finishedItems;
    // The current item the participant needs
    private Material currentItem = null;
    // The difficulty level of the challenge
    private final ItemDifficultyLevel level;
    private int remainingJokerAmount = 0;
    // Last timestamp when the participant skipped an item
    private long lastSkipTimestampMillis = 0L;
    // Reference to the itemsManager
    private final ItemsManager itemsManager;

    public ForceItemBattleChallengeParticipant(Player player, ItemDifficultyLevel level, int jokerAmount, ItemsManager itemsManager) {
        super(player);

        this.itemsManager = itemsManager;
        this.finishedItems = new ArrayList<>();
        this.level = level;
        this.remainingJokerAmount = jokerAmount;
    }

    @Override
    public void prepare() {
        // Init the first item
        nextItem();

        // Common stuff
        player.setHealth(20);
        player.setSaturation(20);
        player.getInventory().clear();
        player.setLevel(0);
        player.setExp(0);
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        player.playSound(player, Sound.BLOCK_END_PORTAL_SPAWN, 1, 1);
        player.setGameMode(GameMode.SURVIVAL);

        // Starter items
        giveStarterItems();
    }

    public void giveStarterItems() {
        if(remainingJokerAmount > 0) {
            // Create jokers
            ItemStack jokerItemStack = new ItemStack(Material.NETHER_STAR, remainingJokerAmount);
            ItemMeta itemMeta = jokerItemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.DARK_PURPLE + "JOKER");
            jokerItemStack.setItemMeta(itemMeta);
            player.getInventory().addItem(jokerItemStack);
        }

        // Give elytra
        ItemStack elytraItemStack = new ItemStack(Material.ELYTRA, 1);
        elytraItemStack.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().addItem(elytraItemStack);

        // Give firework rockets
        player.getInventory().addItem(new ItemStack(Material.FIREWORK_ROCKET, 128));

        // Give shulker boxes in different colors
        player.getInventory().addItem(new ItemStack(Material.LIME_SHULKER_BOX, 1));
        player.getInventory().addItem(new ItemStack(Material.GREEN_SHULKER_BOX, 1));
        player.getInventory().addItem(new ItemStack(Material.RED_SHULKER_BOX, 1));
    }


    public boolean hasCurrentItemInInventory() {
        return player.getInventory().contains(currentItem);
    }

    /**
     * Next item for the participant.
     * It saves the last item in the finishedItems array.
     * The next item will always be unique and not used before.
     */
    public void nextItem() {
        if(currentItem != null) {
            finishedItems.add(currentItem);
        }

        Material item = Material.BARRIER;
        while(item == Material.BARRIER) {
            Material randomItem = itemsManager.getRandomItem(level);
            boolean materialAlreadyUsed = finishedItems.stream().anyMatch((usedMaterial) -> usedMaterial == randomItem);
            if(!materialAlreadyUsed) {
                item = randomItem;
            }
        }
        currentItem = item;
    }

    /**
     * Gives the current item to the player. If the inventory is full, it drops it.
     */
    public void skipItem() {
        lastSkipTimestampMillis = System.currentTimeMillis();

        Material currentItem = getCurrentItem();
        ItemStack itemStack = new ItemStack(currentItem);

        player.getInventory().addItem(itemStack);
        if(!player.getInventory().contains(currentItem)) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        }
    }

    @Override
    public void updateBossBar() {
        if(currentItem != null) {
            if(bossBar == null) {
                bossBar = Bukkit.createBossBar(currentItem.toString(), BarColor.WHITE, BarStyle.SOLID);
                bossBar.addPlayer(player);
            }

            bossBar.setTitle(currentItem.toString());
        }
    }


    public List<Material> getFinishedItems() {
        return finishedItems;
    }

    public Material getCurrentItem() {
        return currentItem;
    }

    public int getScore() {
        return finishedItems.size();
    }

    public void decreaseRemainingJokerAmount() {
        remainingJokerAmount--;
    }

    public boolean canSkip() {
        if(lastSkipTimestampMillis == 0) {
            return true;
        }

        long currentTimestampMillis = System.currentTimeMillis();
        return currentTimestampMillis - lastSkipTimestampMillis >= 5000;
    }

}