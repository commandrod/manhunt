package me.commandrod.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Config
        this.saveDefaultConfig();

        // Command & Listeners handling
        this.getCommand("manhunt").setExecutor(new Manhunt());
        Bukkit.getPluginManager().registerEvents(new Events(), this);
    }
}
