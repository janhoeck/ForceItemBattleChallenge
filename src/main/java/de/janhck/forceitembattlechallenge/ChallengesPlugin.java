package de.janhck.forceitembattlechallenge;

import de.janhck.forceitembattlechallenge.commands.ChallengeCommand;
import de.janhck.forceitembattlechallenge.manager.ChallengeManager;
import de.janhck.forceitembattlechallenge.manager.ItemsManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class ChallengesPlugin extends JavaPlugin {

    private static ChallengesPlugin instance;
    private ChallengeManager challengeManager;
    private ItemsManager itemsManager;
    public static final String PREFIX = ChatColor.WHITE + "[" + ChatColor.GREEN + "ForceItemBattleChallenge" + ChatColor.WHITE + "]" + ChatColor.GOLD + " ";

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        challengeManager = new ChallengeManager();
        itemsManager = new ItemsManager();

        // Register commands
        ChallengeCommand command = new ChallengeCommand();
        getCommand("challenge").setExecutor(command);
        getCommand("c").setExecutor(command);
    }

    @Override
    public void onDisable() {
        if(challengeManager.isRunning()) {
            challengeManager.endCurrentChallenge();
        }
    }

    public void logToFile(String message) {
        try {
            File dataFolder = getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            File saveTo = new File(getDataFolder(), "logs_plugin.txt");
            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(saveTo, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println("[" + getTime() + "] | " + message);
            printWriter.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public String getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public static ChallengesPlugin getInstance() {
        return instance;
    }

    public ChallengeManager getChallengeManager() {
        return challengeManager;
    }

    public ItemsManager getItemsManager() {
        return itemsManager;
    }
}
