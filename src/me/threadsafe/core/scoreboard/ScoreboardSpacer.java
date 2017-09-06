package me.threadsafe.core.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardSpacer extends ScoreboardEntry{

    private final ChatColor team;
    private final ChatColor key, value;
    private boolean visible;

    public ScoreboardSpacer(ChatColor team, ChatColor key, ChatColor value){
        this.team = team;
        this.key = key;
        this.value = value;
    }

    public boolean isVisible() {
        return visible;
    }

    public ScoreboardSpacer setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public void update(Player player){}

    @Override
    public final boolean send(Scoreboard scoreboard, int score){
        String t = this.team.toString();
        String teamName = t.replace(ChatColor.COLOR_CHAR + "", "");

        Team team = scoreboard.getTeam(teamName);
        if(team == null){
            team = scoreboard.registerNewTeam(teamName);
            team.setPrefix(key.toString());
            team.setSuffix(value.toString());
        }

        if(!visible){
            for(String s : team.getEntries()){
                scoreboard.resetScores(s);
                team.removeEntry(s);
            }

            return false;
        }

        if(team.getEntries().isEmpty()) team.addEntry(t);
        scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(t).setScore(score);
        return true;
    }

}
