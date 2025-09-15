package me.colonelneon.spleefElimCredit.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CreditMarkerManager {
    private static HashMap<UUID, Player> markerHashMap;
    private static HashMap<UUID, Player> markerTriggerHashMap;
    private static List<UUID> markerList;
    private static HashMap<Player, UUID> playerLastMarkerTriggered;
    private static HashMap<UUID, Long> markerCreationTimes;

    // checks last triggered marker for each player to avoid spam - it was annoying me so I fixed it :D
    private static HashMap<Player, UUID> lastTriggeredMarker;

    // Initialisation!
    public static void initialise() {
        markerHashMap = new HashMap<>();
        markerTriggerHashMap = new HashMap<>();
        markerList = new ArrayList<>();
        lastTriggeredMarker = new HashMap<>();
        playerLastMarkerTriggered = new HashMap<>();
        markerCreationTimes = new HashMap<>();
    }

    // Cleanup stuff
    public static void cleanup() {
        // This annoyed me to figure out for some reason - Removes removes all marker entities that are in markerList
        for (UUID markerUUID : markerList) {
            Entity entity = Bukkit.getServer().getEntity(markerUUID);
            if (entity != null) {
                entity.remove();
            }
        }
        // Clears all HashMaps, Lists, ect...
        markerList.clear();
        markerHashMap.clear();
        markerTriggerHashMap.clear();
        lastTriggeredMarker.clear();
        playerLastMarkerTriggered.clear();
        markerCreationTimes.clear();
    }

    // Player death stuff - clears them from basically everything so that elim credit resets
    public static void playerDeath(Player player) {
        playerLastMarkerTriggered.remove(player);
        lastTriggeredMarker.remove(player);
    }

    // Creates markers - triggered when blocks are broken
    public static void createMarker(Player player, Location markerLocation) {
        Entity marker = markerLocation.getWorld().spawn(markerLocation, EntityType.MARKER.getEntityClass());

        UUID markerUUID = marker.getUniqueId();
        long creationTime = System.currentTimeMillis();

        markerHashMap.put(markerUUID, player);
        markerCreationTimes.put(markerUUID, creationTime);
        markerList.add(markerUUID);
    }

    // A check to see if the player is touching markers - ALSO marker entities SUCK because their bounding boxes are 0.5 blocks big so I had to expand them >:(
    public static boolean isPlayerTouchingMarker(Entity marker, Entity player) {
        if(marker == null || player == null) return false;
        if (marker.equals(player)) return false;

        if (!marker.getWorld().equals(player.getWorld())) return false;
        BoundingBox markerBoundingBox = marker.getBoundingBox().expand(0.5, 0.5, 0.5);
        return markerBoundingBox.overlaps(player.getBoundingBox());
    }

    // Returns the UUID of the closest marker to the player, triggered when the player is touching a marker - avoids multiple markers being marked as "the credit marker"
    public static UUID getClosestTouchingMarker(Player player) {
        UUID closestMarkerUUID = null;
        double closestDistance = Double.MAX_VALUE;
        Location playerLocation = player.getLocation();

        for (UUID markerUUID : markerList) {
            Entity markerEntity = Bukkit.getServer().getEntity(markerUUID);

            if (markerEntity != null && isPlayerTouchingMarker(markerEntity, player)) {
                // Calculate distance from player to marker
                double distance = playerLocation.distance(markerEntity.getLocation());

                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestMarkerUUID = markerUUID;
                }
            }
        }

        return closestMarkerUUID;
    }

    // Checks player collision with markers, event-triggered
    public static void checkPlayerCollision(Player player) {
        // Get the closest marker to the player - that they are touching
        UUID closestMarkerUUID = getClosestTouchingMarker(player);
        UUID lastTriggered = lastTriggeredMarker.get(player);

        // Triggered if the closest marker isn't the same as the last triggered marker - basically just updates it if you enter another marker without leaving the first one beforehand
        if (closestMarkerUUID != null && !Objects.equals(closestMarkerUUID, lastTriggered)) {
            Player markerOwner = markerHashMap.get(closestMarkerUUID);
            if (markerOwner != null) {
                playerLastMarkerTriggered.remove(player);
                collisionTrigger(closestMarkerUUID, player);
                lastTriggeredMarker.put(player, closestMarkerUUID);
                playerLastMarkerTriggered.put(player, closestMarkerUUID);
            }
        } else if (closestMarkerUUID == null && lastTriggered != null) {
            lastTriggeredMarker.remove(player);
        }
    }
    // Collision trigger - adds the player that is currently touching the marker and the marker that they are touching to markerTriggerHashMap (To store the current elim credit)
    public static void collisionTrigger(UUID uuid, Player player) {
        markerTriggerHashMap.put(uuid, player);
    }

    // Returns the UUID of the last marker that was triggered by the player - to give elim credit
    public static UUID getLastMarkerTriggered(Player player) {
        return playerLastMarkerTriggered.get(player);
    }

    // Player cleanup stuff - removes players from HashMaps, Lists, ect...
    public static void cleanupPlayer(Player player) {
        lastTriggeredMarker.remove(player);
        playerLastMarkerTriggered.remove(player);

        // Remove player from markerTriggerHashMap
        markerTriggerHashMap.entrySet().removeIf(entry -> entry.getValue().equals(player));
    }

    // Returns the owner of a marker (Who broke the block that caused it to be spawned - to give elim credit)
    public static Player getMarkerOwner(UUID markerUUID) {
        return markerHashMap.get(markerUUID);
    }

    // Returns the time that the marker was created - to prioritise elim credits
    public static long getMarkerCreationTime(UUID markerUUID) {
        Long creationTime = markerCreationTimes.get(markerUUID);
        return creationTime != null ? creationTime : 0;
    }
}