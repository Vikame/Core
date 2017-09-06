package me.threadsafe.core;

import me.threadsafe.core.commands.ErrorLogCommand;
import me.threadsafe.core.scoreboard.PlayerScoreboard;
import me.threadsafe.core.scoreboard.ScoreboardUpdater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

    private static Core instance;

    public void onEnable() {
        instance = this;

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            PlayerScoreboard.getScoreboard(p).update();
        }

        new ScoreboardUpdater().runTaskTimerAsynchronously(this, 2, 2);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        getCommand("errorlog").setExecutor(new ErrorLogCommand());
    }

    public void onDisable(){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            PlayerScoreboard.getScoreboard(p).destroy();
        }
    }

    public static Core getInstance() {
        return instance;
    }
}
