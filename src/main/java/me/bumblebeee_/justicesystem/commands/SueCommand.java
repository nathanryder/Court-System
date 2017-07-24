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

public class SueCommand implements CommandExecutor {

    Messages msgs = new Messages();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sue")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command");
                return false;
            }
            Player p = (Player) sender;

            if (!p.hasPermission("court.sue")) {
                p.sendMessage(msgs.getMessage("noPermissions"));
                return false;
            }

            if (!(args.length > 1)) {
                p.sendMessage(msgs.getMessage("invalidArguments").replace("%usage%", "/sue <player> <amount>"));
                return false;
            }

            if (CourtManager.getSuing().containsKey(p.getUniqueId())) {
                p.sendMessage(msgs.getMessage("alreadySuing"));
                return false;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                p.sendMessage(msgs.getMessage("notANumber").replace("%amount%", args[1]));
                return false;
            }

            Player t = Bukkit.getServer().getPlayer(args[0]);
            if (t == null) {
                p.sendMessage(msgs.getMessage("failedToFindPlayer").replace("%name%", args[0]));
                return false;
            }

            CourtManager.getSuing().put(p.getUniqueId(), t.getUniqueId());
            CourtManager.getAmounts().put(p.getUniqueId(), amount);
            p.sendMessage(msgs.getMessage("suedPlayer").replace("%name%", t.getName()).replace("%amount%", String.valueOf(amount)));
            t.sendMessage(msgs.getMessage("suedByPlayer").replace("%name%", p.getName()).replace("%amount%", String.valueOf(amount)));
            return true;
        }
        return false;
    }
}