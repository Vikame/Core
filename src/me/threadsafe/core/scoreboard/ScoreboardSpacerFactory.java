package me.threadsafe.core.scoreboard;

import me.threadsafe.core.Core;
import me.threadsafe.core.ErrorLogger;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardSpacerFactory {

    private final List<ScoreboardSpacer> availableSpacers = new ArrayList<>();

    public ScoreboardSpacerFactory(){
        int index = 0;
        for(ChatColor keyColor : ChatColor.values()){
            if(index > ChatColor.values().length) break;

            for(ChatColor valueColor : ChatColor.values()){
                if(index > ChatColor.values().length) break;

                availableSpacers.add(new ScoreboardSpacer(ChatColor.values()[index++], keyColor, valueColor));
            }
        }
    }

    public ScoreboardSpacer create(){
        if(availableSpacers.isEmpty()){
            try {
                throw new Exception("No ScoreboardSpacers are available.");
            } catch (Exception e) {
                ErrorLogger.getErrorLogger(Core.getInstance()).logError(e);
            }
        }

        ScoreboardSpacer spacer = availableSpacers.get(0);
        availableSpacers.remove(spacer);

        return spacer;
    }

}
