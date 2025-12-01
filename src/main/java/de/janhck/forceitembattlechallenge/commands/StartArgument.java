package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.gui.inventories.ChallengesInventory;
import de.janhck.forceitembattlechallenge.utils.ValidationUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class StartArgument implements ICommandArgument {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Optional<Player> playerOpt = ValidationUtil.validatePlayer(sender);
        if (playerOpt.isEmpty()) {
            return false;
        }

        ChallengesInventory challengesInventory = new ChallengesInventory();
        challengesInventory.openInventory(playerOpt.get());
        return true;
    }
}