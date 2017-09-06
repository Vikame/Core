package me.threadsafe.core.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;

public class ScoreboardEntry {

    public String key, value;

    public ScoreboardEntry(){}
    public ScoreboardEntry(String key, String value){
        this.key = key;
        this.value = value;
    }

    public void update(Player player){}

    public boolean send(Scoreboard scoreboard, int score){
        Team team = scoreboard.getTeam("" + score);
        if(team == null) team = scoreboard.registerNewTeam("" + score);

        if(key == null || value == null){
            for(String s : team.getEntries()){
                scoreboard.resetScores(s);
                team.removeEntry(s);
            }

            return false;
        }

        String key = this.key;
        if(key.length() > 16) key = key.substring(0, 16);
        String value = this.value;
        if(value.length() > 16) value = key.substring(0, 16);

        String lastColor = ChatColor.getLastColors(key);
        if(lastColor.isEmpty()) lastColor = ChatColor.RESET.toString();
        String newName = ChatColor.values()[score].toString() + lastColor;

        Iterator<String> it = team.getEntries().iterator();
        if(it.hasNext()){
            String name = it.next();

            if(!name.equalsIgnoreCase(newName)){
                team.addEntry(newName);
                team.removeEntry(name);
            }
        }else team.addEntry(newName);

        if(!team.getPrefix().equals(key)) team.setPrefix(key);
        if(!team.getSuffix().equals(key)) team.setSuffix(value);

        scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(newName).setScore(score);
        return true;
    }

}
