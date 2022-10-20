package org.fastmcmirror.bartereco.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.fastmcmirror.bartereco.BarterEconomy;
import org.fastmcmirror.bartereco.api.BarterEconomyAPI;

public class PlaceholderHook extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "beco";
    }

    @Override
    public String getAuthor() {
        return "FlyProject";
    }

    @Override
    public String getVersion() {
        return BarterEconomy.instance.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.startsWith("look_")) {
            params = params.substring(5);
            return String.valueOf(BarterEconomyAPI.get(player, params));
        } else if (params.startsWith("has_")) {
            String[] args = params.substring(4).split("_");
            return String.valueOf(BarterEconomyAPI.has(player, args[0], Integer.parseInt(args[1])));
        }
        return "";
    }
}
