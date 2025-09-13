package me.colonelneon.spleefElimCredit.manager;

import me.colonelneon.spleefElimCredit.SpleefElimCredit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CreditMarkerManager {
    private static HashMap<Marker, Player> markerHashMap;
    int count = 0;

    public static void initialise() {
        markerHashMap = new HashMap<>();
    }

    public static void createMarker(Player player, Location markerLocation) {
        Marker newMarker = spawnMarker(markerLocation);
        markerHashMap.put(newMarker, player);
    }
    public static boolean isPlayerTouchingMarker(Entity marker, Entity player) {
        if(marker == null || player == null) return false;
        if (marker.equals(player)) return false;

        if (!marker.getWorld().equals(player.getWorld())) return false;

        return marker.getBoundingBox().overlaps(player.getBoundingBox());
    }

    public static Marker spawnMarker(Location location) {
        Marker marker = (Marker) location.getWorld().spawn(location, EntityType.MARKER.getEntityClass());

        return marker;
    }

    public static List<Marker> getMarkerEntitiesAll(){

        List<Marker> markers = markerHashMap.keySet().stream().toList();
        return markers;
    }

    public static void checkPlayerCollisions() {

        for (int i = 0; i < PlayerManager.currentPlayers.size(); i++) {
            Player currentPlayer = (Player) PlayerManager.currentPlayers.get(i);
            for (int e = 0; e < markerHashMap.size(); e++) {
                Marker currentMarker = getMarkerEntitiesAll().get(e);
                if (isPlayerTouchingMarker(currentMarker, currentPlayer)) {
                    Bukkit.getLogger().warning("WOAHHH");
                } else Bukkit.getLogger().info("Not currently touching");
            }
        }
    }
}
