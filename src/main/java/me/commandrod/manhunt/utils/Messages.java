package me.commandrod.manhunt.utils;

import me.commandrod.manhunt.Main;
import org.bukkit.Bukkit;

public class Messages {

    Main main = new Main();
    public String loadConfigString(String path){
        String out;
        try {
            out = main.plugin().getConfig().getString(path);
        } catch (Exception e){
            e.printStackTrace();
            Bukkit.getLogger().info("Error getting the path " + path + ". Is the line empty?");
            return null;
        }
        return out;
    }


    public String HELP = "&3========== &bManhunt &3==========" +
            "\n&b - &3/manhunt help - &bShows this menu." +
            "\n&b - &3/manhunt speedrunner [player] - &bSets the speedrunner for the game" +
            "\n&b - &3/manhunt forceend - &bStops the game in case of a bug." +
            "\n&b - &3/manhunt start - &bStarts the game." +
            //"\n&b - &3/manhunt reloadconfig - &bReloads the config file." +
            "\n&b - &3/manhunt blockbypass - &bAdmin bypass to block interactions pre-game.";
    public String NO_PERMISSION = loadConfigString("messages.permission");
    public String CONFIG = loadConfigString("messages.config");
    public String INSUFFICIENT_PLAYERS = loadConfigString("messages.insufficient-players");
}