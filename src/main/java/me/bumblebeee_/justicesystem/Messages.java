package me.bumblebeee_.justicesystem;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Messages {

    public static File f;
    public static YamlConfiguration c;

    public void setup() {
        f = new File(JusticeSystem.getInstance().getDataFolder() + File.separator + "messages.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            c = YamlConfiguration.loadConfiguration(f);

            createMessage("noPermissions", "&cYou do not have the required permissions");
            createMessage("invalidArguments", "&cInvalid arguments! Correct usage: %usage%");
            createMessage("failedToFindPlayer", "&cCould not find a player called %name%");
            createMessage("setJudge", "&aSuccessfully set %name% as the judge");
            createMessage("setCourtLocation", "&aSuccessfully set court location!");
            createMessage("juryFull", "&cThe jury is full!");
            createMessage("joinedJury", "&aYou have joined the jury!");
            createMessage("leftJury", "&aYou have left the jury!");
            createMessage("notInJury", "&cYou are not in the jury!");
            createMessage("suedPlayer", "&aYou have sued %name% for %amount%");
            createMessage("suedByPlayer", "&aYou are being sued by %name% for %amount%");
            createMessage("notANumber", "&c%amount% is not a number");
            createMessage("notJudge", "&cYou are not the judge!");
            createMessage("notInCourt", "&cYou are not in court!");
            createMessage("setGuilty", "&c%name% is guilty!");
            createMessage("setInnocent", "&a%name% is innocent!");
            createMessage("reloadSuccess", "&aSuccessfully reloaded config!");
            createMessage("courtStatus", "&6Currently prosecuting %name% who is currently %status_color%%status%");
            createMessage("setJuryLocation", "&aSuccessfully set jury location!");
            createMessage("setCriminalLocation", "&aSuccessfully set criminal location!");
            createMessage("setJudgeLocation", "&aSuccessfully set judge location!");
            createMessage("notSuingAnyone", "&c%name% is not suing anyone!");
            createMessage("alreadySuing", "&cYou can only sue one person!");
            createMessage("courtStartedBroadcast", "&6[Court] &aA Court session has been started!");
            createMessage("courtEndedBroadcast", "&6[Court] %name% is %status_color%%status%");
            createMessage("lostMoney", "&cYou lost %amount% after being sued");
            createMessage("gotMoney", "&aYou recieved %amount% after winning your case");
            createMessage("caseDroppedAgainst", "&cYou are no longer being sued by %name%");
            createMessage("youDroppedCase", "&cYou are no longer suing anyone");
            createMessage("helpCommand", "&6%command%&f: %description%");
            createMessage("alreadyInJury", "&cYou are already in the jury");

            try {
                c.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            c = YamlConfiguration.loadConfiguration(f);
        }
    }

    public void createMessage(String key, String value) {
        c.set(key, value);
    }

    public String getMessage(String key) {
        String msg = c.getString(key);
        if (msg == null) {
            JusticeSystem.getInstance().getLogger().warning(ChatColor.RED + "Failed to find message with key " + key);
            JusticeSystem.getInstance().getLogger().warning(ChatColor.RED + "Deleting the messages.yml file or adding the key will fix this");
            return ChatColor.translateAlternateColorCodes('&', "&cFailed to find message! Please report this to a server admin.");
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}