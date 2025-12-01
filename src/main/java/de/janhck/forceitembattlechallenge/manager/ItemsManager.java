package de.janhck.forceitembattlechallenge.manager;

import de.janhck.forceitembattlechallenge.constants.ItemDifficultyLevel;
import org.apache.commons.io.IOUtils;
import org.bukkit.Material;

import java.io.IOException; // Import
import java.io.InputStream;
import java.nio.charset.StandardCharsets; // Import
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.concurrent.ThreadLocalRandom;

public class ItemsManager {

    private final Map<ItemDifficultyLevel, List<Material>> fileCache = new HashMap<>();
    private final Map<ItemDifficultyLevel, List<Material>> difficultyLevelListMap = new HashMap<>();

    public ItemsManager() {
        loadFileContent(ItemDifficultyLevel.EASY, "items/easy.txt");
        loadFileContent(ItemDifficultyLevel.MEDIUM, "items/medium.txt");
        loadFileContent(ItemDifficultyLevel.HARD, "items/hard.txt");

        load();
    }

    private void loadFileContent(ItemDifficultyLevel level, String path) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) return;
            List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            List<Material> materials = lines
                    .stream()
                    .map(name -> {
                        try {
                            return Material.valueOf(name);
                        } catch (IllegalArgumentException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            fileCache.put(level, materials);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        for (ItemDifficultyLevel level : ItemDifficultyLevel.values()) {
            if (fileCache.containsKey(level)) {
                List<Material> materials = new ArrayList<>(fileCache.get(level));
                difficultyLevelListMap.put(level, materials);
            }
        }
    }

    public List<Material> getItems(ItemDifficultyLevel level) {
        List<Material> easyItems = difficultyLevelListMap.getOrDefault(ItemDifficultyLevel.EASY, new ArrayList<>());
        List<Material> mediumItems = difficultyLevelListMap.getOrDefault(ItemDifficultyLevel.MEDIUM, new ArrayList<>());
        List<Material> hardItems = difficultyLevelListMap.getOrDefault(ItemDifficultyLevel.HARD, new ArrayList<>());

        List<Material> items = new ArrayList<>();
        switch(level) {
            case EASY: {
                items = easyItems;
                break;
            }
            case MEDIUM: {
                items = Stream.of(easyItems, mediumItems)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
                break;
            }
            case HARD: {
                items = Stream.of(easyItems, mediumItems, hardItems)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
                break;
            }
        }
        return items;
    }

    public Material getRandomItem(ItemDifficultyLevel level) {
        List<Material> items = getItems(level);
        if (items.isEmpty()) return Material.STONE; // Fallback
        return items.get(ThreadLocalRandom.current().nextInt(items.size()));
    }
}