package de.janhck.forceitembattlechallenge.commands;

import de.janhck.forceitembattlechallenge.ChallengesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class ChallengeCommand implements CommandExecutor {

    private void sendCommandOverview(CommandSender sender) {
        sender.sendMessage(ChallengesPlugin.PREFIX);
        sender.sendMessage("/challenge start <time in min> <jokers>");
        sender.sendMessage("/challenge stop");
        sender.sendMessage("/challenge pause");
        sender.sendMessage("/challenge continue");
        sender.sendMessage("/challenge skip <player name>");
        sender.sendMessage("/challenge prepare");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        if(args.length == 0) {
            sendCommandOverview(sender);
            return false;
        }

        String[] remainingArguments = Arrays.copyOfRange(args, 1, args.length);
        String secondaryCommand = args[0].toLowerCase();
        switch(secondaryCommand) {
            case "start":
                return new StartArgument().execute(sender, remainingArguments);
            case "stop":
                return new StopArgument().execute(sender, remainingArguments);
            case "pause":
                return new PauseArgument().execute(sender, remainingArguments);
            case "continue":
                return new ContinueArgument().execute(sender, remainingArguments);
            case "skip":
                return new SkipArgument().execute(sender, remainingArguments);
            case "prepare":
                return new PrepareArgument().execute(sender, remainingArguments);
            default:
                sendCommandOverview(sender);
                return false;
        }
    }

}
