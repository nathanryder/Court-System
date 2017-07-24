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

public class LeaveJuryCommand implements CommandExecutor {

    Messages msgs = new Messages();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("leavejury")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command");
                return false;
            }
            Player p = (Player) sender;

            if (!p.hasPermission("court.jury.leave")) {
                p.sendMessage(msgs.getMessage("noPermissions"));
                return false;
            }
            CourtManager cm = JusticeSystem.getCourtManager();

            if (!CourtManager.getJury().contains(p)) {
                p.sendMessage(msgs.getMessage("notInJury"));
                return false;
            }

            CourtManager.getJury().remove(p);
            p.sendMessage(msgs.getMessage("leftJury"));

            Court c = cm.getCourt(p.getUniqueId());
            if (c == null)
                return true;

            p.teleport(CourtManager.getLocations().get(p));
            CourtManager.getPeopleInCourt().remove(p.getUniqueId());
            CourtManager.getLocations().remove(p);
            return true;
        }
        return false;
    }
}