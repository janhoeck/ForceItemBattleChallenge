package forceitembattle.manager;

import forceitembattle.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Timer {
    private boolean running;
    private int time;
    private Map<UUID, BossBar> bossBar = new HashMap<UUID, BossBar>();

    public Timer() {
        this.running = false;

        if (Main.getInstance().getConfig().contains("timer.time")) {
            this.time = Main.getInstance().getConfig().getInt("timer.time");
        } else {
            this.time = 0;
        }

        run();
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String formatSeconds(int inputSeconds) {
        int seconds = inputSeconds % 60;
        int minutes = (inputSeconds / 60) % 60;
        int hours = inputSeconds / 60 / 60;
        return hours + "h " + minutes + "m " + seconds + "s";
    }

    public void sendActionBar() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!isRunning()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED +
                        "Timer paused"));
                continue;
            }

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD.toString() +
                    ChatColor.BOLD + formatSeconds(getTime()) + " | " + Main.getGamemanager().getScore(player)));

            if (!bossBar.get(player.getUniqueId()).getTitle().equalsIgnoreCase(Main.getGamemanager().getCurrentMaterial(player).toString())) {
                //bossBar = Bukkit.createBossBar(Main.getGamemanager().getCurrentMaterial(player).toString(), BarColor.WHITE, BarStyle.SOLID);
                //bossBar.get(player.getUniqueId()).setColor(BarColor.WHITE);
                //bossBar.get(player.getUniqueId()).setStyle(BarStyle.SOLID);
                bossBar.get(player.getUniqueId()).setTitle(Main.getGamemanager().getCurrentMaterial(player).toString());
                bossBar.get(player.getUniqueId()).addPlayer(player);
            }

        }
    }

    public void save() {
        Main.getInstance().getConfig().set("timer.time", time);
    }

    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {

                sendActionBar();
                if (!isRunning()) {
                    return;
                }
                setTime(getTime() - 1);

                if (getTime() == 0) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "<< Force Item Battle is over >>");
                    setRunning(false);
                    Main.getGamemanager().finishGame();
                }
            }
        }.runTaskTimer(Main.getInstance(), 20, 20);
    }
}
