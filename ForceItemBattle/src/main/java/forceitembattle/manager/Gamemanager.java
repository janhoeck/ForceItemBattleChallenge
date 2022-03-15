package forceitembattle.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Gamemanager {
    private static Map<UUID, Integer> score = new HashMap<UUID, Integer>();
    private static Map<UUID, Material> currentMaterial = new HashMap<UUID, Material>();
    private static Map<UUID, ArrayList<Material>> itemList = new HashMap<UUID, ArrayList<Material>>();
    private static boolean ASC = true;
    private static boolean DESC = false;

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

    public void skipItem(String player) {
        Player p = Bukkit.getPlayer(player);
        currentMaterial.put(p.getUniqueId(), generateMaterial());
    }

    public void finishGame() {
        //scoreboard in chat
        Map<UUID, Integer> sortedMapDesc = sortByValue(score, DESC);
        printMap(sortedMapDesc);
    }

    private static Map<UUID, Integer> sortByValue(Map<UUID, Integer> unsortMap, final boolean order)
    {
        List<Map.Entry<UUID, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }

    private static void printMap(Map<UUID, Integer> map)
    {
        map.forEach((key, value) -> System.out.println(ChatColor.GOLD.toString() + Bukkit.getPlayer(key).getName() + " | " + value));
    }
}
