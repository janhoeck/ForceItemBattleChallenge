package de.janhck.forceitembattlechallenge.manager;

import de.janhck.forceitembattlechallenge.constants.ItemDifficultyLevel;
import org.apache.commons.io.IOUtils;
import org.bukkit.Material;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemsManager {
    Map<ItemDifficultyLevel, List<Material>> difficultyLevelListMap = new HashMap<>();

    public ItemsManager() {
        load();
    }

    private void load() {
        loadItems(ItemDifficultyLevel.EASY, "items/easy.txt");
        loadItems(ItemDifficultyLevel.MEDIUM, "items/medium.txt");
        loadItems(ItemDifficultyLevel.HARD, "items/hard.txt");
    }

    public void loadItems(ItemDifficultyLevel level, String path) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        List<String> lines = IOUtils.readLines(inputStream, "UTF-8");
        List<Material> materials = lines
                .stream()
                .map(Material::valueOf)
                .collect(Collectors.toList());
        // Random shuffle the list of items
        Collections.shuffle(materials);
        difficultyLevelListMap.put(level, materials);
    }

    public List<Material> getItems(ItemDifficultyLevel level) {
        List<Material> easyItems = difficultyLevelListMap.get(ItemDifficultyLevel.EASY);
        List<Material> mediumItems = difficultyLevelListMap.get(ItemDifficultyLevel.MEDIUM);
        List<Material> hardItems = difficultyLevelListMap.get(ItemDifficultyLevel.HARD);

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
        Random random = new Random();
        return items.get(random.nextInt(items.size()));
    }
}
