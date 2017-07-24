package me.bumblebeee_.justicesystem.commands;

import me.bumblebeee_.justicesystem.Court;
import me.bumblebeee_.justicesystem.JusticeSystem;
import me.bumblebeee_.justicesystem.Messages;
import me.bumblebeee_.justicesystem.managers.CourtManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class JoinJuryCommand implements CommandExecutor {

    Messages msgs = new Messages();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("joinjury")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command");
                return false;
            }
            Player p = (Player) sender;

            if (!p.hasPermission("court.jury.join")) {
                p.sendMessage(msgs.getMessage("noPermissions"));
                return false;
            }

            CourtManager cm = JusticeSystem.getCourtManager();
            int maxMembers = JusticeSystem.getInstance().getConfig().getInt("jury-limit");

            if (CourtManager.getJury().size() >= maxMembers) {
                p.sendMessage(msgs.getMessage("juryFull"));
                return false;
            }

            if (CourtManager.getJury().contains(p)) {
                p.sendMessage(msgs.getMessage("alreadyInJury"));
                return false;
            }

            CourtManager.getJury().add(p);
            p.sendMessage(msgs.getMessage("joinedJury"));

            if (cm.getJudge() == null)
                return true;
            if (!CourtManager.getPeopleInCourt().containsKey(cm.getJudge().getUniqueId()))
                return true;

            Court c = cm.getCourt(cm.getJudge().getUniqueId());
            if (!c.isInProgress())
                return true;

            CourtManager.getLocations().put(p, p.getLocation());
            CourtManager.getPeopleInCourt().put(p.getUniqueId(), c);
            p.teleport(cm.getJuryLocation());
            return true;
        }
        return false;
    }
}