package de.janhck.forceitembattlechallenge.manager;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Backpack {
    private static Map<String, Inventory> bp = new HashMap<>();

    public Backpack() {
        for (int i = 1; i <= 9; i++) {
            bp.put("team" + i, Bukkit.createInventory(null, ForceItemBattleChallenge.getInstance().getConfig().getInt("standard.backpackSize"), "Team " + i + " | Backpack"));
        }
    }

    public void openBackpack(Player player) {
        if (ForceItemBattleChallenge.getGamemanager().getPlayerTeamSTRING(player) == null) return;
        player.openInventory(bp.get(ForceItemBattleChallenge.getGamemanager().getPlayerTeamSTRING(player)));
    }

    public void addToAllBp(ItemStack itemStack) {
        bp.forEach((key, value) -> {
            value.addItem(itemStack);
        });
    }

    public void clearAllBp() {
        bp.forEach((key, value) -> {
            value.clear();
        });
    }

    public Inventory getBp(String team) {
        return bp.get(team);
    }

    public boolean isInMap(String team) {
        return bp.containsKey(team);
    }
}
