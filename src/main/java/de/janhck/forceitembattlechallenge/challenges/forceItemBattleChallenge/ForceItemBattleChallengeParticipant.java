package de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import de.janhck.forceitembattlechallenge.challenges.api.ChallengeParticipant;
import de.janhck.forceitembattlechallenge.challenges.forceItemBattleChallenge.inventory.settings.ModeSettingItem;
import de.janhck.forceitembattlechallenge.constants.ItemDifficultyLevel;
import de.janhck.forceitembattlechallenge.manager.ItemsManager;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ForceItemBattleChallengeParticipant extends ChallengeParticipant {

    private final ForceItemBattleChallenge challenge;
    // Contains all finished items
    private final List<Material> finishedItems;
    // The current item the participant needs
    private Material currentItem = null;
    // The difficulty level of the challenge
    private int remainingJokerAmount;
    // Last timestamp when the participant skipped an item
    private long lastSkipTimestampMillis = 0L;

    public ForceItemBattleChallengeParticipant(Player player, ForceItemBattleChallenge challenge) {
        super(player);

        this.finishedItems = new ArrayList<>();
        this.challenge = challenge;
        this.remainingJokerAmount = challenge.getJokerAmount();
    }

    @Override
    public void prepare() {
        // Init the first item
        nextItem();

        // Common stuff
        player.setHealth(20);
        player.setSaturation(20);
        player.setFoodLevel(20);
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
        if(challenge.isWithElytra()) {
            // Give elytra
            ItemStack elytraItemStack = new ItemStack(Material.ELYTRA, 1);
            ItemMeta elytraMeta = elytraItemStack.getItemMeta();
            elytraMeta.setUnbreakable(true);
            elytraItemStack.setItemMeta(elytraMeta);
            player.getInventory().addItem(elytraItemStack);

            // Give firework rockets
            ItemStack fireWorkItemStack = new ItemStack(Material.FIREWORK_ROCKET, 1);
            FireworkMeta fireworkMeta = (FireworkMeta) fireWorkItemStack.getItemMeta();
            fireworkMeta.setPower(2);
            fireworkMeta.setDisplayName("Infinite Firework");
            fireWorkItemStack.setItemMeta(fireworkMeta);
            player.getInventory().addItem(fireWorkItemStack);
        }

        // Give shulker boxes in different colors
        player.getInventory().addItem(new ItemStack(Material.LIME_SHULKER_BOX, 1));
        player.getInventory().addItem(new ItemStack(Material.GREEN_SHULKER_BOX, 1));
        player.getInventory().addItem(new ItemStack(Material.RED_SHULKER_BOX, 1));

        if(remainingJokerAmount > 0) {
            // Create jokers
            ItemStack jokerItemStack = new ItemStack(Material.NETHER_STAR, remainingJokerAmount);
            ItemMeta itemMeta = jokerItemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.DARK_PURPLE + "JOKER");
            jokerItemStack.setItemMeta(itemMeta);
            player.getInventory().setItem(8, jokerItemStack);
        }
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
        ItemsManager itemsManager = ChallengesPlugin.getInstance().getItemsManager();
        ForceItemBattleChallengeMode mode = challenge.getMode();
        ItemDifficultyLevel level = challenge.getItemDifficultyLevel();

        if(currentItem != null) {
            finishedItems.add(currentItem);
        }

        Material item = Material.BARRIER;
        switch (mode) {
            // If the current mode is ALL_SAME_ITEMS, we just pick each item by index.
            // Because of this we can be sure that everyone is getting the same items in the same order.
            case ALL_SAME_ITEMS: {
                List<Material> items = itemsManager.getItems(level);
                int index = currentItem == null
                        // Start with the first item, if no item is selected before
                        ? 0
                        // Always increase by +1, so we get the next item
                        : (items.indexOf(currentItem) + 1);
                item = items.get(index);
                break;
            }
            // If the mode is DEFAULT, we want to pick random items
            default:
                while(item == Material.BARRIER) {
                    Material randomItem = itemsManager.getRandomItem(challenge.getItemDifficultyLevel());
                    boolean materialAlreadyUsed = finishedItems.stream().anyMatch((usedMaterial) -> usedMaterial == randomItem);
                    if(!materialAlreadyUsed) {
                        item = randomItem;
                    }
                }
                break;
        }
        // Update the current item
        currentItem = item;

        if(mode == ForceItemBattleChallengeMode.DEFAULT) {
            // Update tab list name
            updateTabListName( "§f" + item.toString() + " §7| §fScore " + getScore());
        } else if(mode == ForceItemBattleChallengeMode.ALL_SAME_ITEMS) {
            updateTabListName("§f" + getScore());
        }
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
