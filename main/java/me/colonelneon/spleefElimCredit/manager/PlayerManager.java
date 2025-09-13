package me.colonelneon.spleefElimCredit.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    public static List<Player> currentPlayers;

    public static void initialise() {
        PlayerManager.currentPlayers = new ArrayList<>();
    }

    public static void playerJoin(Player player) {
        PlayerManager.currentPlayers.add(player);
        Bukkit.getLogger().info("Player: " + player.getName() + " was initialised successfully.");
    }

    public static void playerLeave(Player player) {
        if (PlayerManager.currentPlayers.contains(player)) {
            PlayerManager.currentPlayers.remove(player);
            Bukkit.getLogger().info("Player: " + player.getName() + " was cleared successfully.");
        } else {
            Bukkit.getLogger().warning("Player: " + player.getName() + " cannot be cleared, as they were never initialised.");
        }

    }

    public static List<Player> getCurrentPlayers() {
        return PlayerManager.currentPlayers;
    }
}
