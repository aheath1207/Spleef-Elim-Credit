package me.colonelneon.spleefElimCredit;

import me.colonelneon.spleefElimCredit.listener.BlockBreakListener;
import me.colonelneon.spleefElimCredit.listener.PlayerJoinListener;
import me.colonelneon.spleefElimCredit.listener.PlayerMovementListener;
import me.colonelneon.spleefElimCredit.listener.PlayerDeathListener;
import me.colonelneon.spleefElimCredit.manager.CreditMarkerManager;
import me.colonelneon.spleefElimCredit.manager.PlayerManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpleefElimCredit extends JavaPlugin {

    public static Plugin getPlugin() {
        return SpleefElimCredit.getPlugin(SpleefElimCredit.class);
    }

    @Override
    public void onEnable() {
        getLogger().info("SpleefElimCredit enabling...");
        saveResource("config.yml", true);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMovementListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

        PlayerManager.initialise();
        CreditMarkerManager.initialise();

        getLogger().info("SpleefElimCredit enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Cleaning up and disabling...");
        CreditMarkerManager.cleanup();
        PlayerManager.cleanup();
        getLogger().info("SpleefElimCredit disabled and cleaned up!");
    }
}