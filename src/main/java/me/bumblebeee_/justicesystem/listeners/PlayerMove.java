package me.bumblebeee_.justicesystem.listeners;

import me.bumblebeee_.justicesystem.Court;
import me.bumblebeee_.justicesystem.JusticeSystem;
import me.bumblebeee_.justicesystem.managers.CourtManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getFrom().getX() == e.getTo().getX() &&
                e.getFrom().getY() == e.getTo().getY() &&
                e.getFrom().getZ() == e.getTo().getZ())
            return;

        Player p = e.getPlayer();

        CourtManager cm = JusticeSystem.getCourtManager();
        Court c = cm.getCourt(p.getUniqueId());

        if (c == null)
            return;

        if (!c.isInProgress())
            return;

        e.setCancelled(true);
    }

}