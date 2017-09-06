package me.threadsafe.core.commands;

import me.threadsafe.core.ErrorLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ErrorLogCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            sender.sendMessage(ChatColor.RED + "You cannot do that.");
            return true;
        }

        if(args.length <= 1){
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <plugin> <id>");
            return true;
        }

        Plugin plugin = null;
        for(Plugin p : Bukkit.getPluginManager().getPlugins()){
            if(p.getName().equalsIgnoreCase(args[0])){
                plugin = p;
                break;
            }
        }

        if(plugin == null){
            sender.sendMessage(ChatColor.RED + "No plugin named '" + args[0] + "' was found.");
            return true;
        }

        int id = -1;
        try{
            id = Integer.parseInt(args[1]);
        }catch(NumberFormatException e){
            sender.sendMessage(ChatColor.RED + args[1] + " is not a valid error id.");
            return true;
        }

        String error = ErrorLogger.getErrorLogger(plugin).getError(id);
        if(error == null){
            sender.sendMessage(ChatColor.RED + plugin.getName() + " does not have any errors with the id of " + id + ".");
            return true;
        }

        sender.sendMessage(ChatColor.DARK_GRAY + "\n" + ChatColor.DARK_GRAY + "------------------------------------------------------------\n" + ChatColor.WHITE + error + "\n" + ChatColor.DARK_GRAY + "------------------------------------------------------------");
        return true;
    }

}
