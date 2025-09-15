package me.colonelneon.spleefElimCredit.listener;

import me.colonelneon.spleefElimCredit.manager.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR) // MONITOR priority just to look at the outcome of the event, rather than staring at it constantly
    // Player attack event handling - checks to make sure it wasn't a zombie or something :D
    public void onPlayerDamageByEntity(EntityDamageByEntityEvent event) {
        // Check if both the damager and victim are players
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) { // This is where I learnt that "instanceof" exists - ABSOLUTE GODSEND
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            // Only record the attack if the event wasn't cancelled
            if (!event.isCancelled()) {
                PlayerManager.handlePlayerAttack(attacker, victim);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    // Death event handling
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // Handles elim credit
        PlayerManager.handlePlayerDeath(player);
    }
}