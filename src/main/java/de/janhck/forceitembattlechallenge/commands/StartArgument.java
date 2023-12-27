package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.gui.inventories.ChallengesInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartArgument implements ICommandArgument {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            return false;
        }

        ChallengesInventory challengesInventory = new ChallengesInventory();
        challengesInventory.openInventory(player);
        return true;
    }
}
