package de.janhck.forceitembattlechallenge.manager.ui.inventories.challenges;

import de.janhck.forceitembattlechallenge.manager.ui.inventories.AbstractInventory;
import de.janhck.forceitembattlechallenge.manager.ui.items.AbstractInventoryItem;
import de.janhck.forceitembattlechallenge.manager.ui.items.challenges.ForceItemBattleChallengeItem;
import de.janhck.forceitembattlechallenge.manager.ui.items.common.DoneItem;
import de.janhck.forceitembattlechallenge.manager.ui.items.settings.DifficultyLevelItem;
import de.janhck.forceitembattlechallenge.manager.ui.items.settings.JokerAmountItem;
import de.janhck.forceitembattlechallenge.manager.ui.items.settings.TimeSettingsItem;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChallengesInventory extends AbstractInventory {

    private Map<AbstractInventoryItem, List<AbstractInventoryItem>> challengesMapping = new LinkedHashMap<>();

    public ChallengesInventory() {
        super(9, "Challenges", Stream.of(new ForceItemBattleChallengeItem(0)).collect(Collectors.toList()));

        challengesMapping.put(
                new ForceItemBattleChallengeItem(0),
                Stream.of(
                        new TimeSettingsItem(0),
                        new DifficultyLevelItem(1),
                        new JokerAmountItem(2),
                        new DoneItem(8, () -> {
                        })
                ).collect(Collectors.toList()));

    }


}
