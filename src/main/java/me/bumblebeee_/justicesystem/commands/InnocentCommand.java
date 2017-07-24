package me.bumblebeee_.justicesystem.commands;

import me.bumblebeee_.justicesystem.Court;
import me.bumblebeee_.justicesystem.JusticeSystem;
import me.bumblebeee_.justicesystem.Messages;
import me.bumblebeee_.justicesystem.managers.CourtManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InnocentCommand implements CommandExecutor {

    Messages msgs = new Messages();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("innocent")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command");
                return false;
            }
            Player p = (Player) sender;

            if (!p.hasPermission("court.sue")) {
                p.sendMessage(msgs.getMessage("noPermissions"));
                return false;
            }

            CourtManager cm = JusticeSystem.getCourtManager();

            if (!p.getUniqueId().equals(cm.getJudge().getUniqueId())) {
                p.sendMessage(msgs.getMessage("notJudge"));
                return false;
            }

            Court court = cm.getCourt(p.getUniqueId());
            if (court == null) {
                p.sendMessage(msgs.getMessage("notInCourt"));
                return false;
            }

            court.setGuilty(false);
            Bukkit.getServer().broadcastMessage(msgs.getMessage("setInnocent").replace("%name%", court.getTarget().getName()));
            return true;
        }
        return false;
    }
}