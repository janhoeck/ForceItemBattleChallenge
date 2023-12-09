package de.janhck.forceitembattlechallenge.manager;

import de.janhck.forceitembattlechallenge.ForceItemBattleChallenge;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

public class Gamemanager {
    private static Map<UUID, Integer> score = new HashMap<UUID, Integer>();
    private static Map<UUID, Material> currentMaterial = new HashMap<UUID, Material>();
    private static Map<UUID, ArrayList<Material>> itemList = new HashMap<UUID, ArrayList<Material>>();
    private static Map<UUID, Integer> delay = new HashMap<UUID, Integer>();
    private static boolean ASC = true;
    private static boolean DESC = false;

    /////////////////////////////////////// TEAMS ///////////////////////////////////////
    private static Map<String, Integer> scoreTeams = new HashMap<>();
    private static Map<String, Material> currentMaterialTeams = new HashMap<>();

    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard board = manager.getNewScoreboard();
    Team team1 = board.registerNewTeam("team1");
    Team team2 = board.registerNewTeam("team2");
    Team team3 = board.registerNewTeam("team3");
    Team team4 = board.registerNewTeam("team4");
    Team team5 = board.registerNewTeam("team5");
    Team team6 = board.registerNewTeam("team6");
    Team team7 = board.registerNewTeam("team7");
    Team team8 = board.registerNewTeam("team8");
    Team team9 = board.registerNewTeam("team9");

    public Gamemanager() {
        team1.setDisplayName("[Team1]");
        team1.setPrefix(ChatColor.WHITE + "Team1 | " + ChatColor.WHITE);
        //team1.setColor(ChatColor.WHITE);
        ///////////////////////////////////////////////
        team2.setDisplayName("[Team2]");
        team2.setPrefix(ChatColor.DARK_GRAY + "Team2 | " + ChatColor.WHITE);
        //team2.setColor(ChatColor.DARK_GRAY);
        ///////////////////////////////////////////////
        team3.setDisplayName("[Team3]");
        team3.setPrefix(ChatColor.RED + "Team3 | " + ChatColor.WHITE);
        //team3.setColor(ChatColor.RED);
        ///////////////////////////////////////////////
        team4.setDisplayName("[Team4]");
        team4.setPrefix(ChatColor.YELLOW + "Team4 | " + ChatColor.WHITE);
        //team4.setColor(ChatColor.YELLOW);
        ///////////////////////////////////////////////
        team5.setDisplayName("[Team5]");
        team5.setPrefix(ChatColor.GREEN + "Team5 | " + ChatColor.WHITE);
        //team5.setColor(ChatColor.GREEN);
        ///////////////////////////////////////////////
        team6.setDisplayName("[Team6]");
        team6.setPrefix(ChatColor.AQUA + "Team6 | " + ChatColor.WHITE);
        //team6.setColor(ChatColor.AQUA);
        ///////////////////////////////////////////////
        team7.setDisplayName("[Team7]");
        team7.setPrefix(ChatColor.DARK_BLUE + "Team7 | " + ChatColor.WHITE);
        //team7.setColor(ChatColor.DARK_BLUE);
        ///////////////////////////////////////////////
        team8.setDisplayName("[Team8]");
        team8.setPrefix(ChatColor.DARK_PURPLE + "Team8 | " + ChatColor.WHITE);
        //team8.setColor(ChatColor.DARK_PURPLE);
        ///////////////////////////////////////////////
        team9.setDisplayName("[Team9]");
        team9.setPrefix(ChatColor.LIGHT_PURPLE + "Team9 | " + ChatColor.WHITE);
        //team9.setColor(ChatColor.LIGHT_PURPLE);
    }

    public boolean isPlayerInMaps(Player player) {
        boolean value = true;
        if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame")) {
            /////////////////////////////////////// TEAMS ///////////////////////////////////////
            if (getPlayerTeam(player) == null) value = false;
        } else {
            if (!score.containsKey(player.getUniqueId())) {
                value = false;
            }

            if (!currentMaterial.containsKey(player.getUniqueId())) {
                value = false;
            }
        }
        return value;
    }

    public void checkItem(Player player, Material material) {
        if (!isPlayerInMaps(player)) {
            ForceItemBattleChallenge.getInstance().logToFile("[" + ForceItemBattleChallenge.getTimer().getTime() + "] | " + player.getName() + " is not playing.");
            return;
        }

        UUID playerId = player.getUniqueId();
        if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame")) {
            scoreTeams.put(getPlayerTeamSTRING(player), scoreTeams.get(getPlayerTeamSTRING(player)) + 1);
            currentMaterialTeams.put(getPlayerTeamSTRING(player), generateMaterial());
            board.getTeam(getPlayerTeamSTRING(player)).getEntries().forEach(e -> {
                Bukkit.getPlayer(e).playSound(Bukkit.getPlayer(e).getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
            });
            ForceItemBattleChallenge.getInstance().logToFile("[" + ForceItemBattleChallenge.getTimer().getTime() + "] | (" + getPlayerTeamSTRING(player) + ") " + player.getName() + " got item: " + material.toString() + " | new points: " + getTeamScoreFromPlayer(player));
        } else {
            int newScore = score.get(playerId) + 1;
            score.put(playerId, newScore);

            ArrayList<Material> mat = itemList.get(playerId);
            mat.add(material);
            itemList.put(playerId, mat);

            Material nextMaterial = generateUniqueMaterial(mat);
            currentMaterial.put(playerId, nextMaterial);

            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
            ForceItemBattleChallenge.getInstance().logToFile("[" + ForceItemBattleChallenge.getTimer().getTime() + "] | " + player.getName() + " got item: " + material.toString() + " | new points: " + newScore);
        }
    }

    public boolean isMaterialAlreadyUsedByPlayer(Material nextMaterial, ArrayList<Material> usedMaterialsList) {
        return usedMaterialsList
                .stream()
                .noneMatch((usedMaterial) -> usedMaterial == nextMaterial);
    }

    public Material generateUniqueMaterial(ArrayList<Material> usedMaterialList) {
        Material nextMaterial = null;
        while(nextMaterial == null) {
            Material generatedMaterial = generateMaterial();
            if(isMaterialAlreadyUsedByPlayer(generatedMaterial, usedMaterialList)) {
                nextMaterial = generatedMaterial;
            }
        }
        return nextMaterial;
    }

    public Material generateMaterial() {
        Material item = Material.BARRIER;

        if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("difficulty.easy")) {
            item = ForceItemBattleChallenge.getItemDifficultiesManager().getEasyMaterial();
        } else if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("difficulty.medium")) {
            item = ForceItemBattleChallenge.getItemDifficultiesManager().getMediumMaterial();
        } else if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("difficulty.hard")) {
            item = ForceItemBattleChallenge.getItemDifficultiesManager().getHardMaterial();
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
        if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame")) {
            /////////////////////////////////////// TEAMS ///////////////////////////////////////
            for (int i = 1; i <= 9; i++) {
                scoreTeams.put("team" + i, 0);
                currentMaterialTeams.put("team" + i, generateMaterial());
                Bukkit.getOnlinePlayers().forEach(player -> {
                    delay.put(player.getUniqueId(), 5);
                });
            }
            ForceItemBattleChallenge.getBackpack().clearAllBp();
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> {
                score.put(player.getUniqueId(), 0);
                currentMaterial.put(player.getUniqueId(), generateMaterial());
                delay.put(player.getUniqueId(), 5);

                ArrayList<Material> mat = new ArrayList<>();
                itemList.put(player.getUniqueId(), mat);
            });
        }

    }

    public void startGame(CommandSender sender) {
        try {
            ForceItemBattleChallenge.getTimer().setTime(ForceItemBattleChallenge.getInstance().getConfig().getInt("standard.countdown") * 60);
            initializeMaps();

            int num = ForceItemBattleChallenge.getInstance().getConfig().getInt("standard.jokers");
            if (num > 64) {
                sender.sendMessage(ChatColor.RED + "The maximum amount of jokers is 64.");
                return;
            }
            ItemStack stack = new ItemStack(Material.NETHER_STAR, num);
            ItemMeta m = stack.getItemMeta();
            m.setDisplayName(ChatColor.DARK_PURPLE + "JOKER");
            stack.setItemMeta(m);

            String dif = "";
            if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("difficulty.easy")) {
                dif = "easy";
            } else if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("difficulty.medium")) {
                dif = "medium";
            } else if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("difficulty.hard")) {
                dif = "hard";
            }

            ForceItemBattleChallenge.getInstance().logToFile("The game was started with " + num + " jokers. " + ForceItemBattleChallenge.getInstance().getConfig().getInt("standard.countdown") + " minutes left.");
            ForceItemBattleChallenge.getInstance().logToFile("Difficulty: " + dif +
                    " | Damage: " + ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.damage") +
                    " | PVP: " + ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.pvp") +
                    " | keepInventory: " + ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.keepinventory"));

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.setHealth(20);
                player.setSaturation(20);
                player.getInventory().clear();
                player.setLevel(0);
                player.setExp(0);
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                player.setScoreboard(ForceItemBattleChallenge.getGamemanager().getBoard());
                player.setAllowFlight(ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.fly"));
                player.setFlySpeed(ForceItemBattleChallenge.getInvSettings().getFlySpeed());
                player.playSound(player, Sound.BLOCK_END_PORTAL_SPAWN, 1, 1);
                if (ForceItemBattleChallenge.getGamemanager().isPlayerInMaps(player)) {
                    player.setGameMode(GameMode.SURVIVAL);
                    if (!ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame")) {
                        player.getInventory().setItem(4, stack);
                        ForceItemBattleChallenge.getInstance().logToFile(player.getName() + " -> " + getPlayerTeamSTRING(player));
                    } else ForceItemBattleChallenge.getInstance().logToFile(player.getName());
                } else {
                    player.setGameMode(GameMode.SPECTATOR);
                    ForceItemBattleChallenge.getInstance().logToFile(player.getName() + " -> Spectator");
                }
            });
            if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame"))
                ForceItemBattleChallenge.getBackpack().addToAllBp(stack);
            Bukkit.getWorld("world").setTime(0);
            ForceItemBattleChallenge.getTimer().setRunning(true);

            Bukkit.broadcastMessage(ChatColor.GOLD + "The game was started with " + num + " jokers. " + ForceItemBattleChallenge.getInstance().getConfig().getInt("standard.countdown") + " minutes left.");
            Bukkit.broadcastMessage(ChatColor.GOLD + "Difficulty: " + dif +
                    " | Damage: " + ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.damage") +
                    " | PVP: " + ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.pvp") +
                    " | keepInventory: " + ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.keepinventory"));
            if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("standard.showInfoOnGameStart")) {
                Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "----------------------- " + ChatColor.GREEN + "Info" + ChatColor.DARK_GREEN + " -----------------------");
                Bukkit.broadcastMessage(ChatColor.GREEN + "At the top of the screen you will see an item. Get it as soon as possible. " +
                        "When you have it, a new item will appear at the top. Now try to get as many items that appear at the top as possible. " +
                        "For each item you collect you will get one point. You can see your points next to the countdown. \n" +
                        "Netherstars are jokers. But use them wisely!\n" +
                        "Useful commands:\n" +
                        "- " + ChatColor.GOLD + "/top " + ChatColor.GREEN + "-> teleports you to the surface\n" +
                        "- " + ChatColor.GOLD + "/bp " + ChatColor.GREEN + "-> opens your team-backpack if you play in a team");
                Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "----------------------------------------------------");
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "<standard.countdown> and <standard.jokers> in config have to be numbers");
        }
    }

    public void skipItem(String player) {
        Player p = Bukkit.getPlayer(player);
        if (!isPlayerInMaps(p)) {
            return;
        }

        if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame")) {
            /////////////////////////////////////// TEAMS ///////////////////////////////////////
            currentMaterialTeams.put(getPlayerTeamSTRING(p), generateMaterial());
        } else {
            currentMaterial.put(p.getUniqueId(), generateMaterial());
        }
        ForceItemBattleChallenge.getTimer().sendActionBar();
    }

    public void finishGame() {
        //scoreboard in chat
        if (ForceItemBattleChallenge.getInstance().getConfig().getBoolean("settings.isTeamGame")) {
            /////////////////////////////////////// TEAMS ///////////////////////////////////////
            Map<String, Integer> sortedMapDesc;
            sortedMapDesc = sortByValueTeams(scoreTeams, DESC);
            printMapTeams(sortedMapDesc);

            ForceItemBattleChallenge.getBackpack().clearAllBp();
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.setHealth(20);
                player.setSaturation(20);
                player.getInventory().clear();
                player.setLevel(0);
                player.setExp(0);
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
                //player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                player.setGameMode(GameMode.SURVIVAL);
                player.setAllowFlight(true);
                player.setFlySpeed(0.1f);
                player.getInventory().setItem(0, ForceItemBattleChallenge.getInvSettings().createGuiItem(Material.PAPER, ChatColor.GREEN + "Teams", "right click to choose your team"));
                player.getInventory().setItem(4, ForceItemBattleChallenge.getInvSettings().createGuiItem(Material.COMPASS, ChatColor.GREEN + "Teleporter", "right click to use"));
                if (player.isOp()) {
                    player.getInventory().setItem(7, ForceItemBattleChallenge.getInvSettings().createGuiItem(Material.COMMAND_BLOCK_MINECART, ChatColor.YELLOW + "Settings", "right click to edit"));
                    player.getInventory().setItem(8, ForceItemBattleChallenge.getInvSettings().createGuiItem(Material.LIME_DYE, ChatColor.GREEN + "Start", "right click to start"));
                    player.sendMessage(ChatColor.RED + "Use /reset to reset the world or use /start to start a new round.");
                }
            });
        } else {
            Map<UUID, Integer> sortedMapDesc;
            sortedMapDesc = sortByValue(score, DESC);
            printMap(sortedMapDesc);

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.setHealth(20);
                player.setSaturation(20);
                player.getInventory().clear();
                player.setLevel(0);
                player.setExp(0);
                //player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                player.setGameMode(GameMode.SURVIVAL);
                player.setAllowFlight(true);
                player.setFlySpeed(0.1f);
                player.getInventory().setItem(4, ForceItemBattleChallenge.getInvSettings().createGuiItem(Material.COMPASS, ChatColor.GREEN + "Teleporter", "right click to use"));
                if (player.isOp()) {
                    player.getInventory().setItem(7, ForceItemBattleChallenge.getInvSettings().createGuiItem(Material.COMMAND_BLOCK_MINECART, ChatColor.YELLOW + "Settings", "right click to edit"));
                    player.getInventory().setItem(8, ForceItemBattleChallenge.getInvSettings().createGuiItem(Material.LIME_DYE, ChatColor.GREEN + "Start", "right click to start"));
                    player.sendMessage(ChatColor.RED + "Use /reset to reset the world or use /start to start a new round.");
                }
            });
        }
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

    private static Map<UUID, Integer> sortByValue(Map<UUID, Integer> unsortMap, final boolean order) {
        List<Map.Entry<UUID, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }

    private static Map<String, Integer> sortByValueTeams(Map<String, Integer> unsortMap, final boolean order) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }

    private static void printMap(Map<UUID, Integer> map) {
        map.forEach((key, value) -> {
            Bukkit.broadcastMessage(ChatColor.GOLD.toString() + Bukkit.getPlayer(key).getName() + " | " + value);
            ForceItemBattleChallenge.getInstance().logToFile(Bukkit.getPlayer(key).getName() + " | " + value);
        });
    }

    private void printMapTeams(Map<String, Integer> map) {
        map.forEach((key, value) -> {
            switch (key) {
                case "team1": {
                    if (value > 0) {
                        Bukkit.broadcastMessage(ChatColor.WHITE + "Team 1" + ChatColor.GOLD + " | " + value + " | " + board.getTeam(key).getEntries());
                    }
                    break;
                }
                case "team2": {
                    if (value > 0) {
                        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Team 2" + ChatColor.GOLD + " | " + value + " | " + board.getTeam(key).getEntries());
                    }
                    break;
                }
                case "team3": {
                    if (value > 0) {
                        Bukkit.broadcastMessage(ChatColor.RED + "Team 3" + ChatColor.GOLD + " | " + value + " | " + board.getTeam(key).getEntries());
                    }
                    break;
                }
                case "team4": {
                    if (value > 0) {
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "Team 4" + ChatColor.GOLD + " | " + value + " | " + board.getTeam(key).getEntries());
                    }
                    break;
                }
                case "team5": {
                    if (value > 0) {
                        Bukkit.broadcastMessage(ChatColor.GREEN + "Team 5" + ChatColor.GOLD + " | " + value + " | " + board.getTeam(key).getEntries());
                    }
                    break;
                }
                case "team6": {
                    if (value > 0) {
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Team 6" + ChatColor.GOLD + " | " + value + " | " + board.getTeam(key).getEntries());
                    }
                    break;
                }
                case "team7": {
                    if (value > 0) {
                        Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "Team 7" + ChatColor.GOLD + " | " + value + " | " + board.getTeam(key).getEntries());
                    }
                    break;
                }
                case "team8": {
                    if (value > 0) {
                        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Team 8" + ChatColor.GOLD + " | " + value + " | " + board.getTeam(key).getEntries());
                    }
                    break;
                }
                case "team9": {
                    if (value > 0) {
                        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Team 9" + ChatColor.GOLD + " | " + value + " | " + board.getTeam(key).getEntries());
                    }
                    break;
                }
                default:
                    break;
            }
            ForceItemBattleChallenge.getInstance().logToFile(key + " | " + value + " | " + board.getTeam(key).getEntries());
        });
    }

    public Scoreboard getBoard() {
        return board;
    }

    /////////////////////////////////////// TEAMS ///////////////////////////////////////

    public boolean isTeam(Player player, String teamName) {
        if (board.getEntryTeam(player.getName()) == null) return false;
        return board.getEntryTeam(player.getName()).getName().equalsIgnoreCase(teamName);
    }

    public void addPlayerToTeam(Player player, String team) {
        board.getTeam(team).addEntry(player.getDisplayName());
    }

    public void removePlayerFromTeam(Player player) {
        if (board.getEntryTeam(player.getName()) == null) return;
        board.getEntryTeam(player.getName()).removeEntry(player.getName());
    }

    public List<String> getPlayersInTeam(String team) {
        List<String> l = new ArrayList<String>();
        l.add("<< left click to choose >>");
        l.add("<< right click to remove >>");
        l.add("-- players ----------------");
        l.addAll(board.getTeam(team).getEntries());
        return l;
    }

    public Team getPlayerTeam(Player player) {
        return board.getEntryTeam(player.getName());
    }

    public String getPlayerTeamSTRING(Player player) {
        if (board.getEntryTeam(player.getName()) == null) return null;
        return board.getEntryTeam(player.getName()).getName();
    }

    public int getScoreTeams(String team) {
        return scoreTeams.get(team);
    }

    public int getTeamScoreFromPlayer(Player player) {
        return scoreTeams.get(getPlayerTeamSTRING(player));
    }

    public Material getMaterialTeamsFromPlayer(Player player) {
        return currentMaterialTeams.get(getPlayerTeamSTRING(player));
    }

    public Material getMaterialTeams(String team) {
        return currentMaterialTeams.get(team);
    }
}
