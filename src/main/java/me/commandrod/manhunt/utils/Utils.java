package me.commandrod.manhunt.utils;

import me.commandrod.manhunt.Main;
import me.commandrod.manhunt.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    Game game = new Game();
    Main main = new Main();
    public Utils(){}
    private Runnable timer = new Runnable() {
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(main.plugin(), timer, 0, 20);
        @Override
        public void run() {
            int countdown = 11;
            if (countdown > -1){
                for (Player players : Bukkit.getOnlinePlayers()){
                    players.sendTitle(color("&3Starting in " + countdown), color("&bPrepare to run!"), 5, 40 ,5);
                }
            }
            if (countdown == -1){
                for (Player players : Bukkit.getOnlinePlayers()){
                    players.sendTitle(color("&3Good luck!"), color("&bTry to catch the speedrunner!"), 5, 40 ,5);
                }
                Bukkit.getScheduler().cancelTask(id);
            }
            countdown--;
        }
    };
    private Runnable compass = new Runnable() {
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(main.plugin(), compass, 0, 20);
        @Override
        public void run() {
            for (Player players : Bukkit.getOnlinePlayers()){
                updateCompass(players, game.getSpeedrunner());
            }
            if (!game.isGame()){
                Bukkit.getScheduler().cancelTask(id);
            }
        }
    };
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public void updateCompass(Player player, Player speedrunner){
        player.setCompassTarget(speedrunner.getLocation());
        if (loadConfigBoolean("show-world")){
            Compass().getItemMeta().getLore().set(2, Utils.color("&3The speedrunner is in the world &b\"" + speedrunner.getWorld().getName() + "\"&3."));
        }
    }
    private List<Player> blockBypass = new ArrayList<>();
    public List<Player> getBlockBypass() {
        return blockBypass;
    }

    public boolean isBlockBypassing(Player player) {
        return blockBypass.contains(player);
    }

    public ItemStack Compass() {
        List<String> lore = Arrays.asList(" ", Utils.color("&3Why are you looking here? focus on the game!"));
        ItemStack i = new ItemStack(Material.COMPASS);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(Utils.color("&3Speedruning Compass"));
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }

    public void startGame(Player player) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main.plugin(), timer, 0, 20);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main.plugin(), compass, 0, 20);
        Bukkit.broadcastMessage(Utils.color("&3The manhunt has started!"));
        game.border(player, false);
        game.setGame(true);
        game.setReady(true);
    }

    public String HELPMESSAGE = "&3========== &bManhunt &3==========" +
            "\n&b - &3/manhunt help - &bShows this menu." +
            "\n&b - &3/manhunt speedrunner [player] - &bSets the speedrunner for the game" +
            "\n&b - &3/manhunt forceend - &bStops the game in case of a bug." +
            "\n&b - &3/manhunt start - &bStarts the game." +
            //"\n&b - &3/manhunt reloadconfig - &bReloads the config file." +
            "\n&b - &3/manhunt blockbypass - &bAdmin bypass to block interactions pre-game.";

    public Boolean loadConfigBoolean(String path){
        boolean out;
        try {
            out = main.plugin().getConfig().getBoolean(path);
        } catch (Exception e){
            e.printStackTrace();
            Bukkit.getLogger().info("Error getting the path " + path + ". Is the line empty?");
            return null;
        }
        return out;
    }
}