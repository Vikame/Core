package me.threadsafe.core.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerScoreboard {

    private static final Map<UUID, PlayerScoreboard> scoreboards = new HashMap<>();
    private static final String SCOREBOARD_NAME = ChatColor.GOLD + "Main 1";
    private static final DecimalFormat DF = new DecimalFormat("0.0");

    public final Scoreboard scoreboard;
    public final Objective objective;
    public final Map<Integer, ScoreboardEntry> entries;
    public final UUID uuid;
    public final ScoreboardSpacerFactory spacerFactory;
    private int maximum = 0;

    public PlayerScoreboard(Player player){
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.getObjective("PlayerScoreboard");
        if(objective == null) objective = scoreboard.registerNewObjective("PlayerScoreboard", "dummy");
        this.objective = objective;
        this.entries = new HashMap<>();
        this.uuid = player.getUniqueId();
        this.spacerFactory = new ScoreboardSpacerFactory();

        placeSpacer(15);
        placeEntry(14, new ScoreboardEntry(ChatColor.GOLD + "Server: ", ChatColor.WHITE + "Main 1"));
        placeSpacer(13);

        scoreboards.put(player.getUniqueId(), this);
    }

    public void placeSpacer(int preferredScore){
        placeEntry(preferredScore, spacerFactory.create());
    }

    public void placeEntry(int preferredScore, ScoreboardEntry entry){
        if(preferredScore < 0 || preferredScore > 15) return;

        entries.put(preferredScore, entry);
        if(preferredScore > maximum) maximum = preferredScore;
    }

    public void update(){
        Player player = Bukkit.getPlayer(this.uuid);
        if(player == null || !player.isOnline()){
            destroy();
            return;
        }

        if(!this.objective.getDisplayName().equals(SCOREBOARD_NAME)) this.objective.setDisplayName(SCOREBOARD_NAME);
        if(this.objective.getDisplaySlot() != DisplaySlot.SIDEBAR) this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int current = 1;
        for(int i = 1; i <= maximum; i++){
            ScoreboardEntry entry = entries.get(i);
            if(entry == null) continue;

            entry.update(player);
            if(entry.send(scoreboard, current)) current++;
        }

        if(player.getScoreboard() != scoreboard) player.setScoreboard(scoreboard);
    }

    public void destroy(){
        scoreboards.remove(uuid);
        for(Team t : scoreboard.getTeams()) t.unregister();
        objective.unregister();
        entries.clear();
    }

    public static PlayerScoreboard getScoreboard(Player p) {
        return scoreboards.containsKey(p.getUniqueId()) ? scoreboards.get(p.getUniqueId()) : new PlayerScoreboard(p);
    }
}
