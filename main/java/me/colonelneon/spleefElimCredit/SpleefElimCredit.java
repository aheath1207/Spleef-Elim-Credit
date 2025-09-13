package me.colonelneon.spleefElimCredit;

import me.colonelneon.spleefElimCredit.listener.BlockBreakListener;
import me.colonelneon.spleefElimCredit.listener.CreditMarkerListener;
import me.colonelneon.spleefElimCredit.listener.PlayerJoinListener;
import me.colonelneon.spleefElimCredit.manager.CreditMarkerManager;
import me.colonelneon.spleefElimCredit.manager.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class SpleefElimCredit extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new CreditMarkerListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        PlayerManager.initialise();
        CreditMarkerManager.initialise();

        new BukkitRunnable() {
            @Override
            public void run() {
                CreditMarkerManager.checkPlayerCollisions();
            }
        }.runTaskTimer(this, 0, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
