package me.ryan.windchargemodifier;

import me.ryan.windchargemodifier.commands.CommandWindChargeModifier;
import me.ryan.windchargemodifier.config.Config;
import me.ryan.windchargemodifier.events.EntityKnockbackByEntityListener;
import me.ryan.windchargemodifier.events.ProjectileLaunchListener;
import me.ryan.windchargemodifier.utils.Services;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class WindChargeModifier extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        registerServices();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this); // Desregistrar todos eventos para este plugin
    }

    private void registerCommands() {
        getCommand("windchargemodifier").setExecutor(new CommandWindChargeModifier());
    }

    private void registerServices() {
        Services.registerService(Config.class, new Config(), this);
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new EntityKnockbackByEntityListener(), this);
        pluginManager.registerEvents(new ProjectileLaunchListener(), this);
    }

    public static WindChargeModifier getInstance() {
        return getPlugin(WindChargeModifier.class);
    }

}
