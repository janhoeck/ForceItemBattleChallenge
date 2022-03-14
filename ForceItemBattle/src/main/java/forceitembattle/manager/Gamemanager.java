package forceitembattle.manager;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class Gamemanager {
    private static Map<UUID, Integer> score = new HashMap<UUID, Integer>();
    private static Map<UUID, Material> currentMaterial = new HashMap<UUID, Material>();
    private static Map<UUID, ArrayList<Material>> itemList = new HashMap<UUID, ArrayList<Material>>();

    public void checkItem(Player player, Material material) {
        if (material == currentMaterial.get(player.getUniqueId())) {
            score.put(player.getUniqueId(), score.get(player.getUniqueId()) + 1);
            ArrayList<Material> mat = itemList.get(player.getUniqueId());
            mat.add(material);
            itemList.put(player.getUniqueId(), mat);
            currentMaterial.put(player.getUniqueId(), generateMaterial());
        }
    }

    public Material generateMaterial() {
        Random random = new Random();
        List<Material> materials = Arrays.asList(Material.values());
        Material item = materials.get(random.nextInt(materials.size()));
        while (item.toString().contains("WALL_") ||
                item.toString().contains("POTTED_") ||
                item.toString().contains("CANDLE_CAKE") ||
                item.toString().contains("_CAULDRON") ||
                item.toString().contains("COMMAND") ||
                item.toString().contains("VOID") ||
                item.toString().contains("AIR") ||
                item.toString().contains("_PLANT") ||
                item.toString().contains("PISTON_") ||
                item.toString().contains("PUMPKIN_STEM") ||
                item.toString().contains("MELON_STEM") ||
                item.toString().equals("DEBUG_STICK") ||
                item.toString().equals("JIGSAW") ||
                item.toString().equals("NETHER_PORTAL") ||
                item.toString().equals("END_PORTAL") ||
                item.toString().equals("END_GATEWAY") ||
                item.toString().equals("BARRIER") ||
                item.toString().equals("LAVA") ||
                item.toString().equals("WATER") ||
                item.toString().equals("FIRE") ||
                item.toString().equals("SOUL_FIRE") ||
                item.toString().equals("MOVING_PISTON") ||
                item.toString().equals("POTATOES") ||
                item.toString().equals("BUBBLE_COLUMN") ||
                item.toString().equals("POWDER_SNOW") ||
                item.toString().equals("TRIPWIRE") ||
                item.toString().equals("CAVE_VINES") ||
                item.toString().equals("CARROTS") ||
                item.toString().equals("BEETROOTS") ||
                item.toString().equals("BAMBOO_SAPLING") ||
                item.toString().equals("SWEET_BERRY_BUSH") ||
                item.toString().equals("TALL_SEAGRASS") ||
                item.toString().equals("FROSTED_ICE") ||
                item.toString().equals("REDSTONE_WIRE") ||
                item.toString().equals("COCOA")) {
            item = materials.get(random.nextInt(materials.size()));
        }
        return item;
    }

    public int getScore(Player player) {
        return score.get(player.getUniqueId());
    }

    public Material getCurrentMaterial(Player player) {
        return currentMaterial.get(player.getUniqueId());
    }
}
