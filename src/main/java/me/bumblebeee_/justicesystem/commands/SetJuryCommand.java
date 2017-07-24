package me.bumblebeee_.justicesystem.commands;

import me.bumblebeee_.justicesystem.JusticeSystem;
import me.bumblebeee_.justicesystem.Messages;
import me.bumblebeee_.justicesystem.managers.CourtManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetJuryCommand implements CommandExecutor {

    Messages msgs = new Messages();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setjury")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return false;
            }
            Player p = (Player) sender;

            if (!p.hasPermission("court.setjury")) {
                p.sendMessage(msgs.getMessage("noPermissions"));
                return false;
            }

            CourtManager cm = JusticeSystem.getCourtManager();

            cm.setJuryLocation(p.getLocation());
            p.sendMessage(msgs.getMessage("setJuryLocation"));
            return true;
        }
        return false;
    }
}