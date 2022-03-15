package forceitembattle;

import forceitembattle.commands.CommandReset;
import forceitembattle.commands.CommandSkip;
import forceitembattle.commands.CommandStart;
import forceitembattle.manager.Gamemanager;
import forceitembattle.manager.Listeners;
import forceitembattle.manager.Timer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static Gamemanager gamemanager;
    private static Timer timer;

    @Override
    public void onLoad() {
        instance = this;

        saveConfig();
        if (!getConfig().contains("timer.time")) { getConfig().set("timer.time", 0); }

        if (!getConfig().contains("player_0")) { getConfig().set("player_0", 0); }
        if (!getConfig().contains("player_1")) { getConfig().set("player_1", 1); }
        if (!getConfig().contains("player_2")) { getConfig().set("player_2", 2); }


        saveConfig();
        if (!getConfig().contains("isReset")){
            getConfig().set("isReset" , false);
            saveConfig();
            return;
        }
        if (getConfig().getBoolean("isReset")){

            try {
                File world = new File(Bukkit.getWorldContainer() , "world");
                File nether = new File(Bukkit.getWorldContainer() , "world_nether");
                File end = new File(Bukkit.getWorldContainer() , "world_the_end");

                Files.walk(world.toPath())
                        .sorted(Comparator.reverseOrder())
                        . map(Path::toFile)
                        . forEach(File::delete);
                Files.walk(nether.toPath())
                        .sorted(Comparator.reverseOrder())
                        . map(Path::toFile)
                        . forEach(File::delete);
                Files.walk(end.toPath())
                        .sorted(Comparator.reverseOrder())
                        . map(Path::toFile)
                        . forEach(File::delete);

                //////////////////////////////////////////////////////////////////////////////

                world.mkdirs();
                nether.mkdirs();
                end.mkdirs();

                new File(world , "data").mkdirs();
                new File(world , "datapacks").mkdirs();
                new File(world , "playerdata").mkdirs();
                new File(world , "poi").mkdirs();
                new File(world , "region").mkdirs();

                new File(nether , "data").mkdirs();
                new File(nether , "datapacks").mkdirs();
                new File(nether , "playerdata").mkdirs();
                new File(nether , "poi").mkdirs();
                new File(nether , "region").mkdirs();

                new File(end , "data").mkdirs();
                new File(end , "datapacks").mkdirs();
                new File(end , "playerdata").mkdirs();
                new File(end , "poi").mkdirs();
                new File(end , "region").mkdirs();
            } catch (IOException e) {
                e.printStackTrace();
            }

            getConfig().set("isReset" , false);
            saveConfig();

        }
    }

    @Override
    public void onEnable() {
        gamemanager = new Gamemanager();
        timer = new Timer();

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new Listeners(), this);

        getCommand("reset").setExecutor(new CommandReset());
        getCommand("start").setExecutor(new CommandStart());
        getCommand("skip").setExecutor(new CommandSkip());
    }

    @Override
    public void onDisable() {
        if (getConfig().getBoolean("isReset")) {
            getConfig().set("timer.time", 0);
        } else timer.save();
        saveConfig();
    }

    public static Main getInstance() {
        return instance;
    }

    public static Gamemanager getGamemanager() {
        return gamemanager;
    }

    public static Timer getTimer() {
        return timer;
    }
}
