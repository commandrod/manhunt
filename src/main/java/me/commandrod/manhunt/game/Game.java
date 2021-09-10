package me.commandrod.manhunt.game;

import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private boolean ready;
    private boolean game;
    private Location spawnLoc;
    private List<Player> hunters = new ArrayList<Player>();
    private Player speedrunner;

    public Game() {
    }

    public void border(Player player, boolean toggle) {
        WorldBorder worldBorder = player.getWorld().getWorldBorder();
        if (toggle){
            worldBorder.setSize(10);
        } else {
            worldBorder.setSize(1500);
        }
    }

    public void stopGame(){
        setReady(false);
        setGame(false);
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setGame(boolean game) {
        this.game = game;
    }
    public boolean isGame() {
        return game;
    }

    public boolean isReady() {
        return ready;
    }

    public Location getSpawnLoc() {
        return spawnLoc;
    }

    public void setSpawnLoc(Location newSpawnLoc) {
        this.spawnLoc = newSpawnLoc;
    }

    public List<Player> getHunters() {
        return hunters;
    }

    public void setHunters(List<Player> hunters) {
        this.hunters = hunters;
    }

    public Player getSpeedrunner() {
        return speedrunner;
    }

    public void setSpeedrunner(Player speedrunner){
        this.speedrunner = speedrunner;
    }
}