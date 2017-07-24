package me.bumblebeee_.justicesystem.commands;

import me.bumblebeee_.justicesystem.JusticeSystem;
import me.bumblebeee_.justicesystem.Messages;
import me.bumblebeee_.justicesystem.managers.CourtManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setJudgeCommand implements CommandExecutor {

    Messages msgs = new Messages();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setJudge")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command");
                return false;
            }
            Player p = (Player) sender;

            if (!p.hasPermission("court.setjudge")) {
                p.sendMessage(msgs.getMessage("noPermissions"));
                return false;
            }

            if (!(args.length > 0)) {
                CourtManager cm = JusticeSystem.getCourtManager();

                cm.setJudgeLocation(p.getLocation());
                p.sendMessage(msgs.getMessage("setJudgeLocation"));
                return true;
            }

            Player t = Bukkit.getServer().getPlayer(args[0]);
            if (t == null) {
                p.sendMessage(msgs.getMessage("failedToFindPlayer").replace("%name%", args[0]));
                return false;
            }

            CourtManager cm = JusticeSystem.getCourtManager();

            cm.setJudge(t.getUniqueId());
            p.sendMessage(msgs.getMessage("setJudge").replace("%name%", args[0]));
            return false;
        }
        return false;
    }
}