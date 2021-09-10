package me.commandrod.manhunt.listeners;

import me.commandrod.manhunt.game.Game;
import me.commandrod.manhunt.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.List;

public class Events implements Listener {

    private Player bedPlacer;
    private Player bowShooter;
    Game game = new Game();
    Utils utils = new Utils();

    @EventHandler
    public void onShoot(EntityShootBowEvent e){
        if (e.getEntity() instanceof Player){
            bowShooter = (Player) e.getEntity();
        }
    }
    @EventHandler
    public void compassUpdate(PlayerInteractEvent e){
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Player p = e.getPlayer();
            if (p.getItemInHand().equals(Material.COMPASS)){
                p.setCompassTarget(game.getSpeedrunner().getLocation());
            }
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = (Player) e.getEntity();
        if (p == game.getSpeedrunner()){
            game.setGame(false);
            game.setReady(false);
            game.border(p, true);
            Bukkit.broadcastMessage(Utils.color("&cThe speedrunner died! The manhunt is over."));
        }
    }

    // Setup

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (p.hasPermission("manhunt.admin")){
            p.sendMessage(Utils.color("&c&lDue to a bug the speedrunner has to get the last hit on the EnderDragon for it to count!\nThe hit cannot be done by an explosion."));
        }
        if (game.getSpawnLoc() != null){
            p.teleport(game.getSpawnLoc());
        } else {
            game.setSpawnLoc(p.getLocation());
        }
        p.getWorld().getWorldBorder().setCenter(game.getSpawnLoc());
        e.setJoinMessage(Utils.color("&3Welcome to Minecraft Manhunt &b&l" + p.getName() + "&r&3!"));
        Bukkit.setSpawnRadius(0);
        if (!game.isGame()) {
            game.border(p, true);
        }
    }

    @EventHandler
    public void pvpHandler(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            if (!game.isGame()){
                e.setCancelled(true);
            } else {
                e.setCancelled(false);

                // Damage indicator for projectiles
                if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
                    Player playerShot = (Player) e.getEntity();
                    float finalHealth = (float) (playerShot.getHealth() / 2 - e.getDamage() / 2);
                    bowShooter.sendMessage(Utils.color("&b" + playerShot.getName() + " &3is now on &b" + finalHealth + "&c❤&b."));
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        if (!utils.isBlockBypassing(p)){
            if (!game.isGame()){
                if (!p.hasPermission("manhunt.admin")){
                    p.sendMessage(Utils.color("&cBlock interactions are disabled."));
                } else {
                    p.sendMessage(Utils.color("&cBlock interactions are disabled. Admin bypass: /manhunt blockbypass"));
                }
                e.setCancelled(true);
            }
            if (game.isGame()){
                e.setCancelled(false);
            }
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        // Pre-game
        Player p = e.getPlayer();
        if (!utils.isBlockBypassing(p)){
            if (!game.isGame()){
                if (!p.hasPermission("manhunt.admin")){
                    p.sendMessage(Utils.color("&cBlock interactions are disabled."));
                } else {
                    p.sendMessage(Utils.color("&cBlock interactions are disabled. Admin bypass: /manhunt blockbypass"));
                }
                e.setCancelled(true);
            }
            if (game.isGame()){
                e.setCancelled(false);
            }
        }
        // Bed handling
        if (e.getBlock().getType().toString().toLowerCase().contains("bed")){
            bedPlacer = e.getPlayer();
        }
    }

    // End game
    // Normal kill check
    @EventHandler
    public void onFinish(EntityDeathEvent e){
        Player killer = (Player) e.getEntity().getKiller();
        if (e.getEntity().equals(EntityType.ENDER_DRAGON)){
            if (killer == game.getSpeedrunner()){
                game.stopGame();
                Bukkit.broadcastMessage(Utils.color("&3The &bSpeedrunner &3has managed to finish the game! &lGG!"));
            }
        }
    }
}
