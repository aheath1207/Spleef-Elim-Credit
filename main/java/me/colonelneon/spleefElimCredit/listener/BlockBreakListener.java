package me.colonelneon.spleefElimCredit.listener;

import me.colonelneon.spleefElimCredit.manager.CreditMarkerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    CreditMarkerManager creditMarkerManager = new CreditMarkerManager();

    @EventHandler
    // Handles block breaking, triggers marker spawning
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Location blockEventBreakLocation = e.getBlock().getLocation().toCenterLocation();
        Player elimCreditPlayer = e.getPlayer();
        creditMarkerManager.createMarker(elimCreditPlayer, blockEventBreakLocation);
    }
}
