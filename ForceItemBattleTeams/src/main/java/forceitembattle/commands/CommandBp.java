package forceitembattle.commands;

import forceitembattle.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!Main.getInstance().getConfig().getBoolean("settings.isTeamGame")) return false;
        if (!Main.getTimer().isRunning()) return false;
        if (!(commandSender instanceof Player)) return false;
        Player p = Bukkit.getPlayer(commandSender.getName());
        Main.getBackpack().openBackpack(p);
        return false;
    }
}
