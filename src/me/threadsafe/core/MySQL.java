package me.threadsafe.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class MySQL {

    private String host, user, password, database;
    private Connection connection;

    public MySQL(String host, String user, String password, String database){
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public Connection getConnection(){
        try {
            if (connection == null || connection.isClosed()){
                connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, password);
            }

            return connection;
        }catch(SQLException e){
            ErrorLogger.getErrorLogger(Core.getInstance()).logError(e);
        }

        return null;
    }

    public void closeConnection(){
        try {
            if(connection == null || connection.isClosed()) return;

            connection.close();
        } catch (SQLException e) {
            ErrorLogger.getErrorLogger(Core.getInstance()).logError(e);
        }
    }

    public static final class InsertData {

        private final Map<String, Object> values;
        private final List<String> update;

        public InsertData(){
            this.values = new HashMap<>();
            this.update = new ArrayList<>();
        }

        public InsertData add(String key, Object value){
            values.put(key, value);
            return this;
        }

        public InsertData update(String key){
            update.add(key);
            return this;
        }

        public String create(String table){
            String keys = "";
            String values = "";
            String update = "";

            for(Map.Entry<String, Object> entry : this.values.entrySet()){
                keys += entry.getKey() + ", ";
                values += entry.getValue().toString() + ", ";

                if(this.update.contains(entry.getKey())){
                    update += entry.getKey() + "=" + entry.getValue() + ", ";
                }
            }

            if(keys.endsWith(", ")) keys = keys.substring(0, keys.length()-2);
            if(values.endsWith(", ")) values = values.substring(0, values.length()-2);
            if(update.endsWith(", ")) update = update.substring(0, update.length()-2);

            return "INSERT INTO " + table + " (" + keys + ") VALUES (" + values + ")" + (update.isEmpty() ? "" : " ON DUPLICATE KEY UPDATE " + update) + ";";
        }

    }

}
