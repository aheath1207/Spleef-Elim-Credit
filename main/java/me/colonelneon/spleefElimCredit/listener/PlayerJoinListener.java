package me.colonelneon.spleefElimCredit.listener;

import me.colonelneon.spleefElimCredit.manager.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {


    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        PlayerManager.playerJoin(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent e) {
        PlayerManager.playerLeave(e.getPlayer());
    }

}
