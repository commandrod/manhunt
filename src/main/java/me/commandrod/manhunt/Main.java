package me.commandrod.manhunt;

import me.commandrod.manhunt.commands.Manhunt;
import me.commandrod.manhunt.listeners.Events;
import me.commandrod.manhunt.tabcomplete.TabComplete;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private Main instance;
    public Main plugin() { return instance; }
    public Main(){}

    @Override
    public void onEnable() {
        instance = this;
        // Config
        this.saveDefaultConfig();

        // Command & Listeners handling
        this.getCommand("manhunt").setExecutor(new Manhunt());
        this.getCommand("manhunt").setTabCompleter(new TabComplete());
        Bukkit.getPluginManager().registerEvents(new Events(), this);
    }
}