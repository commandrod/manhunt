package me.commandrod.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.commandrod.manhunt.Utils.*;

public class Manhunt implements CommandExecutor {

    public static Player speedrunner;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("manhunt")) {
                if (p.hasPermission("manhunt.admin")) {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("help")) {
                            p.sendMessage(Utils.color("&3========== &bManhunt &3==========" +
                                    "\n&b - &3/manhunt help - &bShows this menu." +
                                    "\n&b - &3/manhunt speedrunner [player] - &bSets the speedrunner for the game" +
                                    "\n&b - &3/manhunt forceend - &bStops the game in case of a bug." +
                                    "\n&b - &3/manhunt start - &bStarts the game." +
                                    //"\n&b - &3/manhunt reloadconfig - &bReloads the config file." +
                                    "\n&b - &3/manhunt blockbypass - &bAdmin bypass to block interactions pre-game."
                                    ));
                            //"\n&b - &3/manhunt border <number> - &bChanges the border size."));
                        } else if (args[0].equalsIgnoreCase("forceend") && ready) {
                            border(p, true);
                            pvp = false;
                            ready = false;
                            game = false;
                            Bukkit.broadcastMessage(Utils.color("&cThe game has been force stopped."));
                            getSpeedrunner().getWorld().getWorldBorder().setCenter(getSpeedrunner().getLocation());
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.teleport(getSpeedrunner());
                            }
                        } else if (!ready && args[0].equalsIgnoreCase("forceend")) {
                            p.sendMessage(Utils.color("&cThe game hasn't been set up yet."));
                        } else if (args[0].equalsIgnoreCase("start") && ready) {
                            //p.sendMessage(Utils.color("&3The game is starting..."));
                            // (Coming Soon) timer, sounds, and make the hunters unable to move for x amount of seconds (configable)
                            Bukkit.broadcastMessage(Utils.color("&3The manhunt has started!"));
                            border(p, false);
                            pvp = true;
                            ready = true;
                            game = true;
                        } else if (!ready && args[0].equalsIgnoreCase("reloadconfig") || args[0].equalsIgnoreCase("rlconfig")) {
                            // Problem with the config, will be fixed.
                            //plugin.reloadConfig();
                            p.sendMessage(Utils.color("&cCurrently disabled."));
                        } else if (args[0].equalsIgnoreCase("setspawn")) {
                            spawnLoc = p.getLocation();
                            p.getWorld().getWorldBorder().setCenter(spawnLoc);
                            p.sendMessage(Utils.color("&3Changed spawn location."));
                        } else if (!game && args[0].equalsIgnoreCase("blockbypass")) {
                            if (!blockBypass) {
                                p.sendMessage(Utils.color("&3Block bypassing is currently &aenabled&3."));
                                blockBypass = true;
                            } else if (blockBypass) {
                                p.sendMessage(Utils.color("&3Block bypassing is currently &cdisabled&3."));
                                blockBypass = false;
                            }
                        } else if (game) {
                            p.sendMessage(Utils.color("&cThe game has already started!"));
                        } else if (!ready) {
                            p.sendMessage(Utils.color("&cThe game hasn't been set up yet."));
                        } else {
                            p.sendMessage(Utils.color("&cWrong usage! /manhunt help"));
                        }
                    } else if (args.length == 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (args[0].equalsIgnoreCase("speedrunner") || args[0].equalsIgnoreCase("sr")) {
                            if (target != null && !ready) {
                                if (Bukkit.getOnlinePlayers().size() > 1) {
                                    // Game is ready to start
                                    ready = true;
                                    pvp = false;
                                    speedrunner = target;
                                    // Hunters handling
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        // Remove all players - Bug fix
                                        hunters.remove(players);
                                        // Add all players
                                        hunters.add(players);
                                        // Remove  from all players
                                        hunters.remove(speedrunner);

                                        players.getInventory().clear();
                                    }
                                    // Check if hunters are valid, (more than 1 hunter)
                                    for (int i = 0; i < hunters.size(); i++) {
                                        // Hunters setup (Compasses, Messages, etc)
                                        hunters.get(i).getInventory().addItem(Compass());
                                        hunters.get(i).setCompassTarget(speedrunner.getLocation());
                                        hunters.get(i).sendMessage(Utils.color("&3You've been assigned as a &bHunter&3.\nPlease wait for the game to begin."));
                                        speedrunner.sendMessage(Utils.color("&3You've been assigned as a &bSpeedrunner&3.\nPlease wait for the game to begin."));
                                        hunters.get(i).sendMessage(Utils.color("&3Target has been set to " + "&b" + speedrunner.getName() + "&3."));
                                        Bukkit.broadcastMessage(Utils.color("&3There are a total of &b" + hunters.size() + " &3hunters."));
                                    }
                                } else {
                                    p.sendMessage(Utils.color("&cNot enough players to start."));
                                }
                                // If hunters are less or equal to 0 (somehow lol)
                            } else if (target == null) {
                                p.sendMessage(Utils.color("&cThe player &l" + args[1] + " &r&cis not online!"));
                            }
                        }
                        // Adding soon
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
            }
        } else {
            sender.sendMessage("This command can be executed only by a player.");
        }
        return true;
    }
}