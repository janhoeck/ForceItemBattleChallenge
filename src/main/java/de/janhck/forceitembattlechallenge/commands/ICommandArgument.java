package de.janhck.forceitembattlechallenge.commands;

import org.bukkit.command.CommandSender;

public interface ICommandArgument {

    public boolean execute(CommandSender sender, String[] args);

}
