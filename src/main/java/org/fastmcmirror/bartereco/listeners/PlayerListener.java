package org.fastmcmirror.bartereco.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.fastmcmirror.bartereco.BarterEconomy;
import org.fastmcmirror.bartereco.data.EconomyList;
import org.fastmcmirror.bartereco.data.PlayerDTO;

import java.sql.SQLException;
import java.util.HashMap;

public class PlayerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        try {
            if (!BarterEconomy.sqlData.hasPlayer(event.getPlayer().getUniqueId())) {
                PlayerDTO playerDTO = new PlayerDTO();
                EconomyList list = new EconomyList();
                list.data = new HashMap<>();
                playerDTO.setData(list);
                playerDTO.setUUID(event.getPlayer().getUniqueId());
                BarterEconomy.sqlData.createOrUpdate(playerDTO);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
