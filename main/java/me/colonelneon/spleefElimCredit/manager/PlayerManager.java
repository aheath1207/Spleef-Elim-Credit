package me.colonelneon.spleefElimCredit.manager;

import me.colonelneon.spleefElimCredit.SpleefElimCredit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerManager {
    public static List<Player> currentPlayers;
    public static HashMap<Player, PlayerHitData> playerLastHitByHashMap;

    // Subclass to handle player hit data - it uses timestamps and cba to figure out how to do it without creating a subclass
    public static class PlayerHitData {
        private final Player attacker;
        private final long timestamp;

        // This stuff is very self-explanatory

        // This is the class function - idk what it does but i've learnt to just use them
        public PlayerHitData(Player attacker, long timestamp) {
            this.attacker = attacker;
            this.timestamp = timestamp;
        }

        // Returns the attacker
        public Player getAttacker() {
            return attacker;
        }
        // Returns the timestamp - this is used to set priorities between the marker triggers and hit triggers
        public long getTimestamp() {
            return timestamp;
        }

        // Checks whether the actions are within the timelimits or not
        public boolean isWithinTimeLimit(long currentTime, long timeLimitMs) {
            return (currentTime - timestamp) <= timeLimitMs;
        }
    }

    // Initialisations!
    public static void initialise() {
        currentPlayers = new ArrayList<>();
        playerLastHitByHashMap = new HashMap<>();
    }

    // Cleanups!
    public static void cleanup() {
        currentPlayers.clear();
        playerLastHitByHashMap.clear();
    }

    // Handles player joining, initialises new players and sends a confirmation to the console
    public static void playerJoin(Player player) {
        currentPlayers.add(player);
        Bukkit.getLogger().info("Player: " + player.getName() + " was initialised successfully.");
    }


    // Handles players leaving, cleans up players as they leave the server, sends a confirmation to the console. - depending on if they were initialised properly and will return a warning if not
    public static void playerLeave(Player player) {
        if (currentPlayers.contains(player)) {
            currentPlayers.remove(player);
            // Clean up hit data for the leaving player
            playerLastHitByHashMap.remove(player);
            Bukkit.getLogger().info("Player: " + player.getName() + " was cleared successfully.");
        } else {
            Bukkit.getLogger().warning("Player: " + player.getName() + " cannot be cleared, as they were never initialised.");
        }
    }

    // Player death handling - Uses a priority system (It's just some if statements one-after-another) to check who deserves kill credit
    public static void handlePlayerDeath(Player player) {
        long currentTime = System.currentTimeMillis();
        UUID lastMarkerTriggered = CreditMarkerManager.getLastMarkerTriggered(player);
        PlayerHitData lastHitData = playerLastHitByHashMap.get(player);


        // Default values for the player that gets credit, and the reason for the credit
        Player creditPlayer = null;
        String creditReason = "unknown";

        // Default values for markers
        Player markerOwner = null;
        long markerAge = Long.MAX_VALUE;

        // Checks the last marker that was triggered by a player, checks the owner and age of the marker - for the priority stuff
        if (lastMarkerTriggered != null) {
            markerOwner = CreditMarkerManager.getMarkerOwner(lastMarkerTriggered);
            markerAge = currentTime - CreditMarkerManager.getMarkerCreationTime(lastMarkerTriggered);
        }

        // Priority number one, checked first and will award credit if the marker is less than 5 seconds old (This avoids kills being awarded to people that hit players into holes that other players have made as the victim player is falling)
        if (markerOwner != null && markerAge <= 5000) { // 5 seconds (YES I need this conversion so I dont get confused)
            creditPlayer = markerOwner;
            creditReason = "death_by_marker";
        }
        // Priority number two, checks if the victim player was hit by another player within the last 10 seconds - i'm hoping that it takes less than 10 seconds for a player to fall to their death...
        else if (lastHitData != null && lastHitData.isWithinTimeLimit(currentTime, 10000)) { // 10 seconds (AGAIN I need these conversions please don't come after me)
            creditPlayer = lastHitData.getAttacker();
            creditReason = "death_by_attack";
        }
        // Priority number three, fallback elim credit
        else if (markerOwner != null) {
            creditPlayer = markerOwner;
            creditReason = "death_by_marker";
        }

        // Gives elim credit!
        if (creditPlayer != null) {
//        if (creditPlayer != null && creditPlayer != player) {
            awardEliminationCredit(creditPlayer, player, creditReason);
            Bukkit.getLogger().info("Elim credit given");
        } else {
            Bukkit.getLogger().warning("No elimination credit could be determined for " + player.getName() + "'s death");
        }

        // Cleanup time! - removes the dead player from the playerLastHitByHashMap so that the credit player doesn't get credit if they die without any other conditions being met after respawn
        playerLastHitByHashMap.remove(player);
        CreditMarkerManager.playerDeath(player);

    }

    // Handles all player attacks - Event triggered
    public static void handlePlayerAttack(Player attacker, Player victim) {
        long currentTime = System.currentTimeMillis();
        PlayerHitData hitData = new PlayerHitData(attacker, currentTime);

        // Update or add the hit data
        playerLastHitByHashMap.put(victim, hitData);

        Bukkit.getLogger().info(attacker.getName() + " attacked " + victim.getName() + " at " + currentTime);
    }

    // This no joke just sends a message to everyone in chat announcing deaths and showing elim credit - its chilly
    private static void awardEliminationCredit(Player creditPlayer, Player eliminatedPlayer, String reason) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            switch (reason) {
                case "unknown":
                    String unk = String.format(SpleefElimCredit.getPlugin().getConfig().getString("messages.death_unknown"), eliminatedPlayer.getName());
                    player.sendMessage(unk);
                case "death_by_hit":
                    String dbh = String.format(SpleefElimCredit.getPlugin().getConfig().getString("messages.death_by_hit_credit"), eliminatedPlayer.getName(), creditPlayer.getName());
                    player.sendMessage(dbh);
                case "death_by_marker":
                    String dbm = String.format(SpleefElimCredit.getPlugin().getConfig().getString("messages.death_by_marker_credit"), eliminatedPlayer.getName(), creditPlayer.getName());
                    player.sendMessage(dbm);
            }
            String message = String.format(SpleefElimCredit.getPlugin().getConfig().getString("messages.death_by_hit_credit"), eliminatedPlayer.getName(), creditPlayer.getName());
            player.sendMessage(message);
        }
    }
}