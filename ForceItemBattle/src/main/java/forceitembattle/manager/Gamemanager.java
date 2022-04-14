package forceitembattle.manager;

import forceitembattle.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class Gamemanager {
    private static Map<UUID, Integer> score = new HashMap<UUID, Integer>();
    private static Map<UUID, Material> currentMaterial = new HashMap<UUID, Material>();
    private static Map<UUID, ArrayList<Material>> itemList = new HashMap<UUID, ArrayList<Material>>();
    private static Map<UUID, Integer> delay = new HashMap<UUID, Integer>();
    private static boolean ASC = true;
    private static boolean DESC = false;

    public void checkItem(Player player, Material material) {
        if (material == currentMaterial.get(player.getUniqueId())) {
            score.put(player.getUniqueId(), score.get(player.getUniqueId()) + 1);
            ArrayList<Material> mat = itemList.get(player.getUniqueId());
            mat.add(material);
            itemList.put(player.getUniqueId(), mat);
            currentMaterial.put(player.getUniqueId(), generateMaterial());
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
        }
    }

    public Material generateMaterial() {
        Material item = Material.BARRIER;

        if (Main.getInstance().getConfig().getBoolean("difficulty.easy")) {
            item = Main.getItemDifficultiesManager().getEasyMaterial();
        } else if (Main.getInstance().getConfig().getBoolean("difficulty.medium")) {
            item = Main.getItemDifficultiesManager().getMediumMaterial();
        } else if (Main.getInstance().getConfig().getBoolean("difficulty.hard")) {
            item = Main.getItemDifficultiesManager().getHardMaterial();
        }
        return item;
    }

    public int getScore(Player player) {
        return score.get(player.getUniqueId());
    }

    public Material getCurrentMaterial(Player player) {
        return currentMaterial.get(player.getUniqueId());
    }

    public void initializeMaps() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            score.put(player.getUniqueId(), 0);
            currentMaterial.put(player.getUniqueId(), generateMaterial());
            delay.put(player.getUniqueId(), 0);

            ArrayList<Material> mat = new ArrayList<>();
            itemList.put(player.getUniqueId(), mat);
        });
    }

    public void startGame(CommandSender sender) {
            try {
                int time = Main.getInstance().getConfig().getInt("standard.countdown");
                Main.getTimer().setTime(time*60);
                initializeMaps();

                int num = Main.getInstance().getConfig().getInt("standard.jokers");
                if (num > 64) {
                    sender.sendMessage(ChatColor.RED + "The maximum amount of jokers is 64.");
                    return;
                }
                ItemStack stack = new ItemStack(Material.NETHER_STAR, num);
                ItemMeta m = stack.getItemMeta();
                m.setDisplayName(ChatColor.DARK_PURPLE + "JOKER");
                stack.setItemMeta(m);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.setHealth(20);
                    player.setSaturation(20);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.getInventory().clear();
                    player.getInventory().setItem(9, stack);
                    player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                });
                Bukkit.getWorld("world").setTime(0);
                Main.getTimer().setRunning(true);
                String dif = "";
                if (Main.getInstance().getConfig().getBoolean("difficulty.easy")) {
                    dif = "easy";
                } else if (Main.getInstance().getConfig().getBoolean("difficulty.medium")) {
                    dif = "medium";
                } else if (Main.getInstance().getConfig().getBoolean("difficulty.hard")) {
                    dif = "hard";
                }
                Bukkit.broadcastMessage(ChatColor.GOLD + "The game was started with " + num + " jokers. " + time + " minutes left.");
                Bukkit.broadcastMessage(ChatColor.GOLD + "Difficulty: " + dif +
                        " | Damage: " + Main.getInstance().getConfig().getBoolean("settings.damage") +
                        " | PVP: " + Main.getInstance().getConfig().getBoolean("settings.pvp") +
                        " | keepInventory: " + Main.getInstance().getConfig().getBoolean("settings.keepinventory"));
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "<standard.countdown> and <standard.jokers> in config have to be numbers");
            }

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

    public void decreaseDelay() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (delay.containsKey(player.getUniqueId())) {
                if (delay.get(player.getUniqueId()) > 0) {
                    delay.replace(player.getUniqueId(), delay.get(player.getUniqueId()) - 1);
                }
            }
        });
    }

    public boolean hasDelay(Player player) {
        return delay.get(player.getUniqueId()) > 0;
    }

    public void setDelay(Player player, int sek) {
        delay.put(player.getUniqueId(), sek);
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
        map.forEach((key, value) -> Bukkit.broadcastMessage(ChatColor.GOLD.toString() + Bukkit.getPlayer(key).getName() + " | " + value));
    }
}
