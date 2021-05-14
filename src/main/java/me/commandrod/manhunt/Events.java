package me.commandrod.manhunt;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.commandrod.manhunt.Manhunt.*;

public class Events implements Listener {

    public static Player bedPlacer;
    List<Material> bedsList = Arrays.asList(Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.WHITE_BED, Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED, Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED, Material.LIME_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.PURPLE_BED, Material.RED_BED, Material.YELLOW_BED, Material.PINK_BED);

    @EventHandler
    public void compassUpdate(PlayerInteractEvent e){
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Player p = e.getPlayer();
            if (p.getItemInHand().equals(Compass())){
                p.setCompassTarget(getSpeedrunner().getLocation());
            }
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = (Player) e.getEntity();
        if (p.equals(getSpeedrunner())){
            game = false;
            ready = false;
            pvp = false;
            Bukkit.broadcastMessage(Utils.color("&cThe speedrunner died! The manhunt is over."));
            border(p, true);
        }
    }

    // Setup

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (e.getPlayer().hasPermission("manhunt.admin")){
            e.getPlayer().sendMessage(Utils.color("&c&lDue to a bug the speedrunner has to get the last hit on the EnderDragon for it to count!\nThe hit cannot be done by an explosion."));
        }
        if (!(spawnLoc == null)){
            e.getPlayer().teleport(spawnLoc);
        } else {
            spawnLoc = e.getPlayer().getLocation();
        }
        e.getPlayer().getWorld().getWorldBorder().setCenter(spawnLoc);
        //e.setJoinMessage(Utils.color("&3Welcome to Minecraft Manhunt &b&l" + e.getPlayer().getName() + "&r&3!"));
        e.setJoinMessage(Utils.color("&3Welcome to Minecraft Manhunt &b&l" + e.getPlayer().getName() + "&r&3!"));
        Bukkit.setSpawnRadius(0);
        if (!game) {
            border(e.getPlayer(), true);
        }
    }

    @EventHandler
    public void pvpHandler(EntityDamageEvent e){
        if (pvp == false){
            switch (e.getCause()){
                case ENTITY_ATTACK:
                    e.setCancelled(true);
                    break;
                case ENTITY_SWEEP_ATTACK:
                    e.setCancelled(true);
                    break;
                case FALL:
                    e.setCancelled(true);
                    break;
            }
        }
        if (pvp == true){
            e.setCancelled(false);
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        if (!p.hasPermission("manhunt.admin")){
            if (!game){
                e.setCancelled(true);
            }
            if (game){
                e.setCancelled(false);
            }
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        // Pre-game
        Player p = e.getPlayer();
        if (!p.hasPermission("manhunt.admin")) {
            if (!game) {
                e.setCancelled(true);
            }
            if (game){
                e.setCancelled(false);
            }
        }
        // Bed handling
        if (bedsList.contains(e.getBlock())){
            bedPlacer = e.getPlayer();
        }
    }

    // End game
    // Normal kill check
    @EventHandler
    public void onFinish(EntityDeathEvent e){
        Player killer = (Player) e.getEntity().getKiller();
        if (e.getEntity().equals(EntityType.ENDER_DRAGON)){
            if (killer == getSpeedrunner() || e.getEntity().getKiller().equals(bedsList)){
                Bukkit.broadcastMessage(Utils.color("&3The &bSpeedrunner &3has managed to finish the game! &lGG!"));
                pvp = false;
                game = false;
                ready = false;
            }
        }
    }
}
