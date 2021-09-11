package me.commandrod.manhunt.commands;

import me.commandrod.manhunt.game.Game;
import me.commandrod.manhunt.utils.Messages;
import me.commandrod.manhunt.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Manhunt implements CommandExecutor {

    Game game = new Game();
    Utils utils = new Utils();
    Messages msgs = new Messages();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("manhunt")) {
                if (!p.hasPermission("manhunt.admin")) {
                    p.sendMessage(Utils.color("&Insufficient permissions."));
                    return true;
                }
                if (!game.isReady()) {
                    p.sendMessage(Utils.color("&cThe game hasn't been set up yet."));
                    return true;
                }
                if (args.length == 1) {
                    switch (args[0]) {
                        case "help":
                            p.sendMessage(Utils.color(msgs.HELP));
                            break;
                        case "forceend":
                            Bukkit.broadcastMessage(Utils.color("&cThe game has been force stopped."));
                            game.stopGame();
                            game.getSpeedrunner().getWorld().getWorldBorder().setCenter(game.getSpeedrunner().getLocation());
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.teleport(game.getSpeedrunner());
                            }
                            break;
                        case "start":
                            utils.startGame(p);
                            break;
                        case "setspawn":
                            game.setSpawnLoc(p.getLocation());
                            p.getWorld().getWorldBorder().setCenter(game.getSpawnLoc());
                            p.sendMessage(Utils.color("&3Changed spawn location."));
                            break;
                        case "blockbypass":
                            if (!utils.isBlockBypassing(p)) {
                                p.sendMessage(Utils.color("&3Block bypassing is currently &aenabled&3."));
                                utils.getBlockBypass().add(p);
                            } else {
                                p.sendMessage(Utils.color("&3Block bypassing is currently &cdisabled&3."));
                                utils.getBlockBypass().remove(p);
                            }
                            break;
                        case "reloadconfig":
                            p.sendMessage(Utils.color("&cCurrently disabled."));
                            break;
                    }
                } else if (args.length >= 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (args[0].equalsIgnoreCase("speedrunner") || args[0].equalsIgnoreCase("sr")) {
                        if (target == null) {
                            p.sendMessage(Utils.color("&cThe player &l" + args[1] + " &r&cis not online!"));
                            return true;
                        }
                        if (game.isGame()) {
                            p.sendMessage(Utils.color("&cThere is currently a game running!\n" +
                                    "If you're sure this is an issue, execute the following command:\n" +
                                    "/manhunt forceend"));
                            return true;
                        }
                        if (Bukkit.getOnlinePlayers().size() <= 1) {
                            p.sendMessage(Utils.color("&cThere are not enough players online!"));
                            return true;
                        }
                        // Game is ready to start
                        game.setReady(true);
                        game.setGame(false);
                        game.setSpeedrunner(target);
                        // Hunters handling
                        // Remove all players - Bug fix
                        game.getHunters().clear();
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            // Add all players
                            game.getHunters().add(players);
                            // Remove  from all players
                            game.getHunters().remove(game.getSpeedrunner());
                            players.getInventory().clear();
                        }
                        for (Player hunter : game.getHunters()) {
                            // Hunters setup (Compasses, Messages, etc)
                            hunter.getInventory().addItem(utils.Compass());
                            hunter.setCompassTarget(game.getSpeedrunner().getLocation());
                            hunter.sendMessage(Utils.color("&3You've been assigned as a &bHunter&3.\nPlease wait for the game to begin."));
                            hunter.sendMessage(Utils.color("&3Target has been set to " + "&b" + game.getSpeedrunner().getName() + "&3."));
                        }
                        game.getSpeedrunner().sendMessage(Utils.color("&3You've been assigned as a &bSpeedrunner&3.\nPlease wait for the game to begin."));
                        Bukkit.broadcastMessage(Utils.color("&3There are a total of &b" + game.getHunters().size() + " &3hunters."));
                    }
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
        }
        return true;
    }
}