package me.bumblebeee_.justicesystem;

import lombok.Getter;
import me.bumblebeee_.justicesystem.managers.CourtManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Court {

    Messages msgs = new Messages();

    private CourtManager manager = JusticeSystem.getCourtManager();
    private @Getter Player player;
    private @Getter Player target;
    private @Getter double amount;
    private @Getter Player judge;
    private @Getter boolean isGuilty = false;
    private @Getter boolean inProgress = false;

    public Court(Player playerSueing, Player targetSued, double amountSued) {
        this.player = playerSueing;
        this.target = targetSued;
        this.amount = amountSued;
        this.judge = manager.getJudge();
    }

    public void startSession() {
        if (manager.getJudgeLocation() == null || manager.getJuryLocation() == null || manager.getCourtLocation() == null || manager.getCriminalLocation() == null) {
            manager.getJudge().sendMessage(ChatColor.RED + "Not all spawns are set!");
            return;
        }

        inProgress = true;
        CourtManager.getLocations().put(judge, judge.getLocation());
        CourtManager.getLocations().put(player, player.getLocation());
        CourtManager.getLocations().put(target, target.getLocation());

        judge.teleport(manager.getJudgeLocation());
        for (Player t : CourtManager.getJury()) {
            if (t == null)
                continue;

            CourtManager.getLocations().put(t, t.getLocation());
            CourtManager.getPeopleInCourt().put(t.getUniqueId(), this);
            t.teleport(manager.getJuryLocation());
        }
        if (target != null)
            target.teleport(manager.getCriminalLocation());
        player.teleport(manager.getCourtLocation());

        CourtManager.getPeopleInCourt().put(judge.getUniqueId(), this);
        CourtManager.getPeopleInCourt().put(player.getUniqueId(), this);
        CourtManager.getPeopleInCourt().put(target.getUniqueId(), this);

        Bukkit.getServer().broadcastMessage(msgs.getMessage("courtStartedBroadcast"));
    }

    public void endSession() {
        inProgress = false;
        String send = msgs.getMessage("courtEndedBroadcast").replace("%name%", target.getName());
        send = send.replace("%status_color%", isGuilty() ? "§c" : "§a");
        send = send.replace("%status%", isGuilty ? "guilty" : "innocent");
        Bukkit.getServer().broadcastMessage(send);

        List<UUID> remove = new ArrayList<>();
        for (UUID uuid : CourtManager.getPeopleInCourt().keySet()) {
            if (CourtManager.getPeopleInCourt().get(uuid).equals(this))
                remove.add(uuid);
        }
        CourtManager.getPeopleInCourt().keySet().removeAll(remove);
        for (Player p : CourtManager.getLocations().keySet()) {
            if (p == null)
                continue;
            p.teleport(CourtManager.getLocations().get(p));
        }

        if (isGuilty)
            rewardPlayer(amount);

        CourtManager.getPeopleInCourt().clear();
        CourtManager.getLocations().clear();
        CourtManager.getSuing().remove(player.getUniqueId());
        CourtManager.getAmounts().remove(player.getUniqueId());
    }

    public void setGuilty(boolean guilty) {
        this.isGuilty = guilty;
    }

    public void rewardPlayer(double amount) {
        Economy eco = JusticeSystem.getEconomy();
        eco.withdrawPlayer(target, amount);
        eco.depositPlayer(player, amount);

        target.sendMessage(msgs.getMessage("lostMoney").replace("%amount%", String.valueOf(amount)));
        player.sendMessage(msgs.getMessage("gotMoney").replace("%amount%", String.valueOf(amount)));
    }

}
