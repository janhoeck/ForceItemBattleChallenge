package de.janhck.forceitembattlechallenge.manager.ui.inventories.settings;

import de.janhck.forceitembattlechallenge.manager.ui.inventories.AbstractInventory;
import de.janhck.forceitembattlechallenge.manager.ui.items.common.DoneItem;
import de.janhck.forceitembattlechallenge.manager.ui.items.settings.DifficultyLevelItem;
import de.janhck.forceitembattlechallenge.manager.ui.items.settings.JokerAmountItem;
import de.janhck.forceitembattlechallenge.manager.ui.items.settings.TimeSettingsItem;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ForceItemBattleChallengeSettingsInventory extends AbstractInventory {

    public ForceItemBattleChallengeSettingsInventory() {
        super(9, "Settings", Stream.of(
                new TimeSettingsItem(0),
                new DifficultyLevelItem(1),
                new JokerAmountItem(2),
                new DoneItem(8, () -> {})
            ).collect(Collectors.toList())
        );
    }

}
