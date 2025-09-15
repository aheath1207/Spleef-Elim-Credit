package me.colonelneon.spleefElimCredit.listener;

import me.colonelneon.spleefElimCredit.manager.CreditMarkerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovementListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Only check if the player actually moved (not just head rotation)
        if (event.getFrom().getX() != event.getTo().getX() ||
                event.getFrom().getY() != event.getTo().getY() ||
                event.getFrom().getZ() != event.getTo().getZ()) {

            Player player = event.getPlayer();
            CreditMarkerManager.checkPlayerCollision(player);
        }
    }
}