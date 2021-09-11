package me.commandrod.manhunt.tabcomplete;

import me.commandrod.manhunt.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    List<String> completeList = new ArrayList<>();

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("manhunt")) {
            if (sender.hasPermission("manhunt.admin")) {
                if (args.length >= 1){
                    final String[] oneArg = { "speedrunner", "forceend", "start", "help", "setspawn", "blockbypass", "reloadconfig" };
                    for (String obj : oneArg){
                        completeList.add(obj);
                     }
                }
                if (args.length >= 2){
                    for (Player players : Bukkit.getOnlinePlayers()){
                        completeList.add(players.getName());
                    }
                }
                return completeList;
            }
        }
        return null;
    }
}
