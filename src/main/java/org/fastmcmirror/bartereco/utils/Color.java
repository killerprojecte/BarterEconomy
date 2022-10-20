package org.fastmcmirror.bartereco.utils;

import org.bukkit.ChatColor;

public class Color {
    public static String color(String text){
        return ChatColor.translateAlternateColorCodes('&',text);
    }
}
