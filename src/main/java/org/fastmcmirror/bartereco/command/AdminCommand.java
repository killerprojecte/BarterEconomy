package org.fastmcmirror.bartereco.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.fastmcmirror.bartereco.BarterEconomy;
import org.fastmcmirror.bartereco.data.EconomyList;
import org.fastmcmirror.bartereco.data.PlayerDTO;
import org.fastmcmirror.bartereco.utils.Color;

public class AdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (!sender.isOp()){
                sendHelp(sender);
                return false;
            }
            if (args.length==1){
                if (args[0].equals("reload")){
                    BarterEconomy.sqlData.close();
                    BarterEconomy.instance.reloadConfig();
                    BarterEconomy.instance.loadSQL();
                    BarterEconomy.instance.loadMessage();
                    sender.sendMessage(Color.color(BarterEconomy.message.getString("reload")));
                } else {
                    sendHelp(sender);
                }
            } else if (args.length==2){
                if (args[0].equals("lookall")){
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                    if (BarterEconomy.sqlData.hasPlayer(player.getUniqueId())){
                        sender.sendMessage(Color.color(BarterEconomy.message.getString("lookall").replace("%player%",player.getName())));
                        PlayerDTO data = BarterEconomy.sqlData.getData(player.getUniqueId());
                        for (String type : data.getData().data.keySet()){
                            sender.sendMessage(Color.color(BarterEconomy.message.getString("lookall-info").replace("%type%",type)
                                    .replace("%amount%",String.valueOf(data.getData().data.get(type)))));
                        }
                    } else {
                        sender.sendMessage(Color.color(BarterEconomy.message.getString("notfound")));
                    }
                } else {
                    sendHelp(sender);
                }
            } else if (args.length==3){
                if (args[0].equals("look")){
                    String type = args[1];
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
                    if (BarterEconomy.sqlData.hasPlayer(player.getUniqueId())){
                        int amount = 0;
                        PlayerDTO data = BarterEconomy.sqlData.getData(player.getUniqueId());
                        if (data.getData().data.containsKey(type)){
                            amount = data.getData().data.get(type);
                        }
                        sender.sendMessage(Color.color(BarterEconomy.message.getString("look")
                                .replace("%player%",player.getName())
                                .replace("%type%",type)
                                .replace("%amount%",String.valueOf(amount))));
                    } else {
                        sender.sendMessage(Color.color(BarterEconomy.message.getString("notfound")));
                    }
                } else {
                    sendHelp(sender);
                }
            } else if (args.length==4){
                if (args[0].equals("take")){
                    String type = args[1];
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
                    int amount = Integer.parseInt(args[3]);
                    if (BarterEconomy.sqlData.hasPlayer(player.getUniqueId())){
                        PlayerDTO data = BarterEconomy.sqlData.getData(player.getUniqueId());
                        if (!data.getData().data.containsKey(type)){
                            sender.sendMessage(Color.color(BarterEconomy.message.getString("take-nothave")
                                    .replace("%player%",player.getName())
                                    .replace("%type%",type)));
                            return false;
                        }
                        if (data.getData().data.get(type)<amount){
                            sender.sendMessage(Color.color(BarterEconomy.message.getString("notenough")));
                            return false;
                        }
                        EconomyList list = data.getData();
                        list.data.put(type,list.data.get(type)-amount);
                        data.setData(list);
                        BarterEconomy.sqlData.createOrUpdate(data);
                        sender.sendMessage(Color.color(BarterEconomy.message.getString("take")
                                .replace("%player%",player.getName())
                                .replace("%type%",type)
                                .replace("%amount%",String.valueOf(amount))));
                    } else {
                        sender.sendMessage(Color.color(BarterEconomy.message.getString("notfound")));
                    }
                } else if (args[0].equals("set")){
                    String type = args[1];
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
                    int amount = Integer.parseInt(args[3]);
                    if (BarterEconomy.sqlData.hasPlayer(player.getUniqueId())){
                        PlayerDTO data = new PlayerDTO();
                        data.setUUID(player.getUniqueId());
                        EconomyList list = data.getData();
                        list.data.put(type,amount);
                        data.setData(list);
                        BarterEconomy.sqlData.createOrUpdate(data);
                        sender.sendMessage(Color.color(BarterEconomy.message.getString("set")
                                .replace("%player%",player.getName())
                                .replace("%type%",type)
                                .replace("%amount%",String.valueOf(amount))));
                    } else {
                        sender.sendMessage(Color.color(BarterEconomy.message.getString("notfound")));
                    }
                } else if (args[0].equals("give")){
                    String type = args[1];
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
                    int amount = Integer.parseInt(args[3]);
                    if (BarterEconomy.sqlData.hasPlayer(player.getUniqueId())){
                        PlayerDTO data = BarterEconomy.sqlData.getData(player.getUniqueId());
                        int origin;
                        EconomyList list = data.getData();
                        if (!data.getData().data.containsKey(type)){
                            origin = 0;
                        } else {
                            origin = list.data.get(type);
                        }
                        list.data.put(type,origin+amount);
                        data.setData(list);
                        BarterEconomy.sqlData.createOrUpdate(data);
                        sender.sendMessage(Color.color(BarterEconomy.message.getString("give")
                                .replace("%player%",player.getName())
                                .replace("%type%",type)
                                .replace("%amount%",String.valueOf(amount))));
                    } else {
                        sender.sendMessage(Color.color(BarterEconomy.message.getString("notfound")));
                    }
                } else {
                    sendHelp(sender);
                }
            } else {
                sendHelp(sender);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private static void sendHelp(CommandSender sender){
        sender.sendMessage(Color.color("&6&lBarterEconomy &7by FlyProject"));
        if (sender.isOp()){
            sender.sendMessage(Color.color("&e/beco look <type> <player>"));
            sender.sendMessage(Color.color("&e/beco lookall <player>"));
            sender.sendMessage(Color.color("&e/beco take <type> <player> <amount>"));
            sender.sendMessage(Color.color("&e/beco set <type> <player> <amount>"));
            sender.sendMessage(Color.color("&e/beco give <type> <player> <amount>"));
            sender.sendMessage(Color.color("&e/beco reload"));
        }
    }
}
