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

import java.util.Map;
import java.util.UUID;

public class CourtCommand implements CommandExecutor {

    Messages msgs = new Messages();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("court")) {
            if (!(args.length > 0)) {
                sender.sendMessage(msgs.getMessage("invalidArguments").replace("%usage%", "/court help"));
                return false;
            }

            if (args[0].equalsIgnoreCase("help")) {
                for (String cmds : JusticeSystem.getCommands().keySet()) {
                    String desc = JusticeSystem.getCommands().get(cmds);
                    sender.sendMessage(msgs.getMessage("helpCommand").replace("%command%", cmds).replace("%description%", desc));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("court.reload")) {
                    sender.sendMessage(msgs.getMessage("reloadSuccess"));
                    return false;
                }

                JusticeSystem.getInstance().reloadConfig();
                sender.sendMessage(msgs.getMessage("reloadSuccess"));
                return true;
            } else if (args[0].equalsIgnoreCase("status")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                    return false;
                }
                Player p = (Player) sender;

                if (!p.hasPermission("court.status")) {
                    p.sendMessage(msgs.getMessage("noPermissions"));
                    return false;
                }

                CourtManager cm = JusticeSystem.getCourtManager();
                Court c = cm.getCourt(p.getUniqueId());

                if (c == null) {
                    p.sendMessage(msgs.getMessage("notInCourt"));
                    return false;
                }

                boolean courtStatus = c.isGuilty();
                String send = msgs.getMessage("courtStatus").replace("%name%", c.getTarget().getName());
                send = send.replace("%status_color%", courtStatus ? "§c" : "§a");
                send = send.replace("%status%", courtStatus ? "guilty" : "innocent");
                p.sendMessage(send);
                return true;
            } else if (args[0].equalsIgnoreCase("start")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                    return false;
                }
                Player p = (Player) sender;
                CourtManager cm = JusticeSystem.getCourtManager();

                if (!p.hasPermission("court.start")) {
                    p.sendMessage(msgs.getMessage("noPermissions"));
                    return false;
                }

                if (!(args.length > 1)) {
                    p.sendMessage(msgs.getMessage("invalidArguments").replace("%usage%", "/court start <player>"));
                    return false;
                }

                if (cm.getJudge() == null) {
                    p.sendMessage(msgs.getMessage("notJudge"));
                    return false;
                }
                if (!p.getUniqueId().equals(cm.getJudge().getUniqueId())) {
                    p.sendMessage(msgs.getMessage("notJudge"));
                    return false;
                }

                Map<UUID, UUID> suing = CourtManager.getSuing();
                Player suer = Bukkit.getServer().getPlayer(args[1]);
                if (suer == null) {
                    p.sendMessage(msgs.getMessage("failedToFindPlayer").replace("%name%", args[1]));
                    return false;
                }

                if (!suing.containsKey(suer.getUniqueId())) {
                    p.sendMessage(msgs.getMessage("notSuingAnyone").replace("%name%", args[1]));
                    return false;
                }
                Player target = Bukkit.getServer().getPlayer(suing.get(suer.getUniqueId()));

                Court c = new Court(suer, target, CourtManager.getAmounts().get(suer.getUniqueId()));
                c.startSession();
                return true;
            } else if (args[0].equalsIgnoreCase("end")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                    return false;
                }
                Player p = (Player) sender;
                CourtManager cm = JusticeSystem.getCourtManager();

                if (!p.hasPermission("court.end")) {
                    p.sendMessage(msgs.getMessage("noPermissions"));
                    return false;
                }

                if (cm.getJudge() == null) {
                    p.sendMessage(msgs.getMessage("notJudge"));
                    return false;
                }
                if (!p.getUniqueId().equals(cm.getJudge().getUniqueId())) {
                    p.sendMessage(msgs.getMessage("notJudge"));
                    return false;
                }

                Court c = CourtManager.getPeopleInCourt().get(p.getUniqueId());
                if (c == null) {
                    p.sendMessage(msgs.getMessage("notInCourt"));
                    return false;
                }

                c.endSession();
                return true;
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                    return false;
                }
                Player p = (Player) sender;

                if (!p.hasPermission("court.remove")) {
                    p.sendMessage(msgs.getMessage("noPermissions"));
                    return false;
                }

                if (!CourtManager.getSuing().containsKey(p.getUniqueId())) {
                    p.sendMessage(msgs.getMessage("notSuingAnyone"));
                    return false;
                }

                Player target = Bukkit.getServer().getPlayer(CourtManager.getSuing().get(p.getUniqueId()));
                if (target != null)
                    target.sendMessage(msgs.getMessage("caseDroppedAgainst").replace("%name%", p.getName()));

                p.sendMessage(msgs.getMessage("youDroppedCase"));
                CourtManager.getSuing().remove(p.getUniqueId());
                CourtManager.getAmounts().remove(p.getUniqueId());
                return false;
            } else {
                sender.sendMessage(msgs.getMessage("invalidArguments").replace("%usage%", "/court help"));
            }

            return true;
        }
        return false;
    }
}