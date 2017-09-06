package me.threadsafe.core;

import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ErrorLogger {

    private static final Map<String, ErrorLogger> instances = new HashMap<>();

    private final Plugin plugin;
    private final Map<Integer, String> errors;
    private int current;

    public ErrorLogger(Plugin owner){
        this.plugin = owner;
        this.errors = new HashMap<>();
        this.current = 0;

        instances.put(owner.getName(), this);
    }

    public void logError(Exception e){
        this.current++;

        String converted = (e.getMessage() == null ? "" : "Reason: " + e.getMessage() + "\n");

        StackTraceElement[] elements = e.getStackTrace();
        for(int i = 0; i < elements.length; i++){
            StackTraceElement element = elements[i];

            converted += element.toString() + (i == elements.length-1 ? "" : "\n");
        }

        errors.put(this.current, converted);
        System.out.println("[" + plugin.getName() + "] An error has occurred. To view this error, type /errorlog " + plugin.getName() + " " + this.current + ".");
    }

    public String getError(int id){
        return errors.get(id);
    }

    public static ErrorLogger getErrorLogger(Plugin plugin){
        if(instances.containsKey(plugin.getName())) return instances.get(plugin.getName());

        return new ErrorLogger(plugin);
    }

}
