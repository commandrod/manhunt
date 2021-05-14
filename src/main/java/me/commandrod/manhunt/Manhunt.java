package me.commandrod.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Manhunt implements CommandExecutor {

    private static Player speedrunner;
    public static Player getSpeedrunner(){
        return speedrunner;
    }

    public static ItemStack Compass() {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(Utils.color("&3Manhunt Compass"));
        lore.add(Utils.color("&3Currently pointing towards &b" + getSpeedrunner().getName()));
        lore.add(Utils.color("&b" + getSpeedrunner().getName() + " &3is currently in &b" + getSpeedrunner().getWorld().getName()));
        lore.add(Utils.color("&3Right click to update the location!"));
        meta.setLore(lore);
        compass.setItemMeta(meta);
        return compass;
    }

    // Hunters handling
    public static ArrayList<Player> hunters = new ArrayList<Player>();
    public static boolean game;
    public static boolean ready;
    public static boolean pvp;
    public static Location spawnLoc;
    public static void border(Player p, boolean toggle){
        if (toggle){
            p.getWorld().getWorldBorder().setSize(11);
        }
        if (!toggle){
            p.getWorld().getWorldBorder().setSize(4000);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        String cmdName = "manhunt";

        if (cmd.getName().equals(cmdName) && sender instanceof Player) {
            if (p.hasPermission(cmdName + "admin")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("help")) {
                        p.sendMessage(Utils.color("&3========== &bManhunt &3==========" +
                                "\n&b - &3/manhunt help - &bShows this menu." +
                                "\n&b - &3/manhunt speedrunner [player] - &bSets the speedrunner for the game" +
                                "\n&b - &3/manhunt forceend - &bStops the game in case of a bug." +
                                "\n&b - &3/manhunt start - &bStarts the game." +
                                "\n&b - &3/manhunt reloadconfig - &bReloads the config file."));
                        //"\n&b - &3/manhunt border <number> - &bChanges the border size."));
                    } else if (args[0].equalsIgnoreCase("forceend") && ready) {
                        border(p, true);
                        pvp = false;
                        ready = false;
                        game = false;
                        Bukkit.broadcastMessage(Utils.color("&cThe game has been force stopped."));
                        speedrunner.getWorld().getWorldBorder().setCenter(speedrunner.getLocation());
                        for (Player players : Bukkit.getOnlinePlayers()){
                            players.teleport(speedrunner);
                        }
                    } else if (!ready && args[0].equalsIgnoreCase("forceend")){
                        p.sendMessage(Utils.color("&cThe game hasn't been set up yet."));
                    } else if (args[0].equalsIgnoreCase("start") && ready){
                        Bukkit.broadcastMessage(Utils.color("&3The manhunt has started!"));
                        border(p, false);
                        pvp = true;
                        ready = true;
                        game = true;
                    } else if (!ready && args[0].equalsIgnoreCase("reloadconfig") || args[0].equalsIgnoreCase("rlconfig")) {
                        //plugin.reloadConfig();
                        p.sendMessage(Utils.color("&cCurrently disabled."));
                    } else if (args[0].equalsIgnoreCase("setspawn")) {
                        spawnLoc = p.getLocation();
                        p.getWorld().getWorldBorder().setCenter(spawnLoc);
                        p.sendMessage(Utils.color("&3Changed spawn location."));
                    } else if (!ready){
                        p.sendMessage(Utils.color("&cThe game hasn't been set up yet."));
                    } else {
                        p.sendMessage(Utils.color("&cWrong usage! /manhunt help"));
                    }
                } else if (args.length == 2){
                    Player target = Bukkit.getPlayer(args[1]);
                    if (args[0].equalsIgnoreCase("speedrunner") || args[0].equalsIgnoreCase("sr")) {
                        if (target != null && !ready){
                            // Game is ready to start
                            ready = true;
                            pvp = false;
                            speedrunner = target;
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                // Remove players that were in the list before - Bug fix
                                hunters.remove(players);
                                hunters.add(players);
                                // Removes the speedrunner from the total players
                                hunters.remove(speedrunner);

                                players.getInventory().clear();
                            }
                            for (int i = 0; i < hunters.size(); i++) {
                                // Hunters setup (compass, messages)
                                hunters.get(i).getInventory().addItem(Compass());
                                hunters.get(i).setCompassTarget(speedrunner.getLocation());
                                hunters.get(i).sendMessage(Utils.color("&3You've been assigned as a &bHunter&3.\nPlease wait for the game to begin."));
                                speedrunner.sendMessage(Utils.color("&3You've been assigned as a &bSpeedrunner&3.\nPlease wait for the game to begin."));
                                Bukkit.broadcastMessage(Utils.color("&3There are a total of &b" + hunters.size() + " &3hunters."));
                                hunters.get(i).sendMessage(Utils.color("&3Target has been set to " + "&b" + speedrunner.getName() + "&3."));
                            }
                            // If hunters are less or equal to 0 (somehow lol)
                            if (hunters.size() <= 0) {
                                p.sendMessage(Utils.color("&cNot enough players to start."));
                            }
                        } else if (target == null){
                            p.sendMessage(Utils.color("&cThe player &l" + args[1] + " &r&cis not online!"));
                        }
                    }
                    /*if (args[0].equalsIgnoreCase("border") && game == true || ready){
                        if (StringUtils.isNumeric(args[1])){
                            p.getWorld().getWorldBorder().setSize(plugin.getConfig().getInt("game.border-size"));
                            Bukkit.broadcastMessage("&3The border has been set to &b" + plugin.getConfig().getInt("game.border-size") + "&3.");
                        } else {
                            p.sendMessage(Utils.color("&cThe game hasn't been set up yet."));
                        }
                    } else if (game != true || ready != true){
                        p.sendMessage(Utils.color("&cThe game haven't been set up yet."));
                    }*/
                } else {
                    p.sendMessage(Utils.color("&cWrong usage! /manhunt help"));
                }
            } else {
                p.sendMessage(Utils.color("&cInsufficient permissions."));
            }
        } else {
            System.out.println("[" + cmdName + "] An error has occured.");
            return true;
        }
        return true;
    }
}