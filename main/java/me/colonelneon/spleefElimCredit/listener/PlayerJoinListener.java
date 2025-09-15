package me.colonelneon.spleefElimCredit.listener;

import me.colonelneon.spleefElimCredit.manager.CreditMarkerManager;
import me.colonelneon.spleefElimCredit.manager.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    // Player join handling - Initialises players (When it works)
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        PlayerManager.playerJoin(e.getPlayer());
    }

    @EventHandler
    // Player leaving handling - cleans up players from HashMaps, Lists, ect...
    public void onPlayerLeaveEvent(PlayerQuitEvent e) {
        PlayerManager.playerLeave(e.getPlayer());
        // Clean up marker data for the leaving player
        CreditMarkerManager.cleanupPlayer(e.getPlayer());
    }
}