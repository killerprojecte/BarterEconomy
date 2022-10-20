package org.fastmcmirror.bartereco.api;

import org.bukkit.OfflinePlayer;
import org.fastmcmirror.bartereco.BarterEconomy;
import org.fastmcmirror.bartereco.data.EconomyList;
import org.fastmcmirror.bartereco.data.PlayerDTO;

public class BarterEconomyAPI {
    public static boolean has(OfflinePlayer player, String type, int amount) {
        try {
            if (!BarterEconomy.sqlData.hasPlayer(player.getUniqueId())) return false;
            PlayerDTO data = BarterEconomy.sqlData.getData(player.getUniqueId());
            if (!data.getData().data.containsKey(type)) return false;
            return data.getData().data.get(type) >= amount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int get(OfflinePlayer player, String type) {
        try {
            if (!BarterEconomy.sqlData.hasPlayer(player.getUniqueId())) return 0;
            PlayerDTO data = BarterEconomy.sqlData.getData(player.getUniqueId());
            if (!data.getData().data.containsKey(type)) return 0;
            return data.getData().data.get(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean take(OfflinePlayer player, String type, int amount) {
        try {
            if (has(player, type, amount)) {
                PlayerDTO data = BarterEconomy.sqlData.getData(player.getUniqueId());
                EconomyList list = data.getData();
                list.data.put(type, list.data.get(type) - amount);
                data.setData(list);
                BarterEconomy.sqlData.createOrUpdate(data);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void give(OfflinePlayer player, String type, int amount) {
        try {
            if (BarterEconomy.sqlData.hasPlayer(player.getUniqueId())) {
                PlayerDTO data = BarterEconomy.sqlData.getData(player.getUniqueId());
                int origin;
                EconomyList list = data.getData();
                if (!data.getData().data.containsKey(type)) {
                    origin = 0;
                } else {
                    origin = list.data.get(type);
                }
                list.data.put(type, origin + amount);
                data.setData(list);
                BarterEconomy.sqlData.createOrUpdate(data);
            } else {
                PlayerDTO data = new PlayerDTO();
                data.setUUID(player.getUniqueId());
                EconomyList list = data.getData();
                list.data.put(type, amount);
                data.setData(list);
                BarterEconomy.sqlData.createOrUpdate(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void set(OfflinePlayer player, String type, int amount) {
        try {
            PlayerDTO data = new PlayerDTO();
            data.setUUID(player.getUniqueId());
            EconomyList list = data.getData();
            list.data.put(type, amount);
            data.setData(list);
            BarterEconomy.sqlData.createOrUpdate(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
