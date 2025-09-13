package me.colonelneon.spleefElimCredit.listener;

import me.colonelneon.spleefElimCredit.manager.CreditMarkerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBreakListener implements Listener {
    CreditMarkerManager creditMarkerManager = new CreditMarkerManager();

    private Location blockEventBreakLocation;
    private Player elimCreditPlayer;

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        blockEventBreakLocation = e.getBlock().getLocation();
        elimCreditPlayer = e.getPlayer();
        creditMarkerManager.createMarker(elimCreditPlayer, blockEventBreakLocation);
    }

    public Location getBlockEventBreakLocation() {
        return blockEventBreakLocation;
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockBreakEvent e) {
        Location eventLocation = e.getBlock().getLocation();
        if(e.getBlock().getLocation().getBlockX() == 0) {
            CreditMarkerManager.spawnMarker(eventLocation);
            Bukkit.getLogger().info("Marker placed at: " + eventLocation.toString());
        }
    }
}
