package me.threadsafe.core.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardUpdater extends BukkitRunnable {

    public void run(){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            PlayerScoreboard.getScoreboard(p).update();
        }
    }

}
