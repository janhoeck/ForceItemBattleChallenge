package forceitembattle.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = Bukkit.getPlayer(sender.getName());
        Location loc;
        if (p.getWorld().equals(Bukkit.getWorld("world_nether"))) {
            loc = Bukkit.getWorld("world").getSpawnLocation();
        } else {
            loc = new Location(p.getWorld(), p.getLocation().getX(), p.getWorld().getHighestBlockYAt(p.getLocation().getBlockX(), p.getLocation().getBlockZ()) +1, p.getLocation().getZ());
        }
        p.teleport(loc);
        return false;
    }
}
