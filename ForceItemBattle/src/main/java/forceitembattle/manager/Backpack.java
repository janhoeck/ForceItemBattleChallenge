package forceitembattle.manager;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Backpack {
    private static Map<String, Inventory> bp = new HashMap<String , Inventory>();

    public void addToBp(String team, ItemStack itemStack) {
        if (bp.containsKey(team)) {
            bp.get(team).addItem(itemStack);
        } else {
            //Inventory inv = new Inve
            //bp.put(team, inv);
        }
    }

    public Inventory getBp(String team) {
        return bp.get(team);
    }
}
