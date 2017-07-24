package me.bumblebeee_.justicesystem.listeners;

import me.bumblebeee_.justicesystem.JusticeSystem;
import me.bumblebeee_.justicesystem.managers.CourtManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        CourtManager cm = JusticeSystem.getCourtManager();
        Player p = e.getPlayer();

        if (CourtManager.getLocations().containsKey(p)) {
            p.teleport(CourtManager.getLocations().get(p));
            CourtManager.getLocations().remove(p);
        }

        if (CourtManager.getJury().contains(p)) {
            CourtManager.getJury().remove(p);
        }
        CourtManager.getPeopleInCourt().remove(p.getUniqueId());
    }

}