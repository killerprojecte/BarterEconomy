package org.fastmcmirror.bartereco;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.fastmcmirror.bartereco.command.AdminCommand;
import org.fastmcmirror.bartereco.data.EconomyList;
import org.fastmcmirror.bartereco.data.PlayerDTO;
import org.fastmcmirror.bartereco.database.MySQLData;
import org.fastmcmirror.bartereco.database.SQLData;
import org.fastmcmirror.bartereco.database.SQLiteData;
import org.fastmcmirror.bartereco.listeners.PlayerListener;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

public final class BarterEconomy extends JavaPlugin {

    public static BarterEconomy instance;
    public static SQLData sqlData;
    public static FileConfiguration message;

    @Override
    public void onEnable() {
        saveResource("messages.yml",false);
        saveDefaultConfig();
        instance = this;
        loadMessage();
        loadSQL();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);
        getCommand("beco").setExecutor(new AdminCommand());
        Bukkit.getScheduler().runTaskAsynchronously(this,() -> {
            try {
                for (Player player : Bukkit.getOnlinePlayers()){
                    if (!sqlData.hasPlayer(player.getUniqueId())){
                        PlayerDTO playerDTO = new PlayerDTO();
                        EconomyList list = new EconomyList();
                        list.data = new HashMap<>();
                        playerDTO.setData(list);
                        playerDTO.setUUID(player.getUniqueId());
                        sqlData.createOrUpdate(playerDTO);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
        // Plugin startup logic

    }

    public void loadSQL(){
        if (getConfig().getString("database").equals("mysql")){
            try {
                sqlData = new MySQLData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (getConfig().getString("database").equals("sqlite")) {
            try {
                sqlData = new SQLiteData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            getLogger().severe("Unknow Database Type!");
            setEnabled(false);
        }
    }

    public void loadMessage(){
        message = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "/messages.yml"));
    }

    @Override
    public void onDisable() {
        sqlData.close();
        // Plugin shutdown logic
    }
}
