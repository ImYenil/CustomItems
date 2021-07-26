package net.choco.customitems.command;

import net.choco.customitems.Main;
import net.choco.customitems.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomItemCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("customitems.admin")) {
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage("§cCommands: /customitems give <player> <weapon name>");
            sender.sendMessage("§cCommands: /customitems list");
            return true;
        }
        if(args[0].equalsIgnoreCase("list")) {
            if(!sender.hasPermission("customitems.list"))
                return true;

            StringBuilder list = new StringBuilder();
            for(String weapon : Main.getInstance().getItemHandler().getItems()) {
                list.append((list.length() == 0 ? "" : ", ")+ weapon);
            }

            sender.sendMessage("§a[CustomItems] Available custom items: ");
            sender.sendMessage("§a[CustomItems] §7"+list);

            return true;
        }
        if(args[0].equalsIgnoreCase("give")) {
            if (args.length <= 1) {
                sender.sendMessage("§cCommands: /customitems give <player> <item name>");
                return true;
            }
            if (args.length <= 2) {
                sender.sendMessage("§cCommands: /customitems give <player> <weapon name>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                sender.sendMessage("§cTargetted player is offline.");
                return true;
            }

            String item = args[2];
            Item i = Main.getInstance().getItemHandler().getItem(item);
            if(i == null) {
                sender.sendMessage("§cCouldn't find "+item+" item.");
                return true;
            }

            target.getInventory().addItem(i.getItem());
            sender.sendMessage("§cGave 1x "+i+" to "+target.getName());
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new LinkedList<>();
        if (args.length == 1) {
            if (sender.hasPermission("customitems.command.help")) {
                tab.addAll(Arrays.asList("give", "list"));
            }
        }
        if (args.length == 2) {
            if (sender.hasPermission("customitems.command.player")) {
                tab.addAll(Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toList()));
            }
        }
        if (args.length == 3) {
            if (sender.hasPermission("customitems.command.items")) {
                tab.addAll(Main.getInstance().getItemHandler().getItems());
            }
        }
        return tab;
    }
}