package me.bumblebeee_.justicesystem.managers;

import lombok.Getter;
import me.bumblebeee_.justicesystem.Court;
import me.bumblebeee_.justicesystem.JusticeSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CourtManager {

    private static @Getter Map<UUID, Integer> amounts = new HashMap<>();
    private static @Getter Map<Player, Location> locations = new HashMap<>();
    private static @Getter Map<UUID, Court> peopleInCourt = new HashMap<>();
    private static @Getter List<Player> jury = new ArrayList<>();
                            //Suer, Sueing
    private static @Getter Map<UUID, UUID> suing = new HashMap<>();

    public void setCourtLocation(Location l) {
        YamlConfiguration c = getDataFile();
        c.set("location.world", l.getWorld().getName());
        c.set("location.x", l.getBlockX());
        c.set("location.y", l.getBlockY());
        c.set("location.z", l.getBlockZ());
        c.set("location.pitch", l.getPitch());
        c.set("location.yaw", l.getY());

        saveDataFile(c);
    }

    public void setJudge(UUID uuid) {
        YamlConfiguration c = getDataFile();
        c.set("judge", String.valueOf(uuid));
        saveDataFile(c);
    }

    public void setJuryLocation(Location l) {
        YamlConfiguration c = getDataFile();
        c.set("jury.world", l.getWorld().getName());
        c.set("jury.x", l.getBlockX());
        c.set("jury.y", l.getBlockY());
        c.set("jury.z", l.getBlockZ());
        c.set("jury.pitch", l.getPitch());
        c.set("jury.yaw", l.getY());

        saveDataFile(c);
    }

    public void setCriminalLocation(Location l) {
        YamlConfiguration c = getDataFile();
        c.set("criminal.world", l.getWorld().getName());
        c.set("criminal.x", l.getBlockX());
        c.set("criminal.y", l.getBlockY());
        c.set("criminal.z", l.getBlockZ());
        c.set("criminal.pitch", l.getPitch());
        c.set("criminal.yaw", l.getY());

        saveDataFile(c);
    }

    public void setJudgeLocation(Location l) {
        YamlConfiguration c = getDataFile();
        c.set("judgeloc.world", l.getWorld().getName());
        c.set("judgeloc.x", l.getBlockX());
        c.set("judgeloc.y", l.getBlockY());
        c.set("judgeloc.z", l.getBlockZ());
        c.set("judgeloc.pitch", l.getPitch());
        c.set("judgeloc.yaw", l.getY());

        saveDataFile(c);
    }

    public Location getCourtLocation() {
        YamlConfiguration c = getDataFile();
        if (c.getString("location.world") == null)
            return null;

        World w = Bukkit.getServer().getWorld(c.getString("location.world"));
        int x = c.getInt("location.x");
        int y = c.getInt("location.y");
        int z = c.getInt("location.z");
        float pitch = c.getInt("location.pitch");
        float yaw = c.getInt("location.yaw");

        return new Location(w, x, y, z, pitch, yaw);
    }

    public Location getJudgeLocation() {
        YamlConfiguration c = getDataFile();
        if (c.getString("judgeloc.world") == null)
            return null;

        World w = Bukkit.getServer().getWorld(c.getString("judgeloc.world"));
        int x = c.getInt("judgeloc.x");
        int y = c.getInt("judgeloc.y");
        int z = c.getInt("judgeloc.z");
        float pitch = c.getInt("judgeloc.pitch");
        float yaw = c.getInt("judgeloc.yaw");

        return new Location(w, x, y, z, pitch, yaw);
    }

    public Location getCriminalLocation() {
        YamlConfiguration c = getDataFile();
        if (c.getString("criminal.world") == null)
            return null;

        World w = Bukkit.getServer().getWorld(c.getString("criminal.world"));
        int x = c.getInt("criminal.x");
        int y = c.getInt("criminal.y");
        int z = c.getInt("criminal.z");
        float pitch = c.getInt("criminal.pitch");
        float yaw = c.getInt("criminal.yaw");

        return new Location(w, x, y, z, pitch, yaw);
    }

    public Location getJuryLocation() {
        YamlConfiguration c = getDataFile();
        if (c.getString("location.world") == null)
            return null;

        World w = Bukkit.getServer().getWorld(c.getString("jury.world"));
        int x = c.getInt("jury.x");
        int y = c.getInt("jury.y");
        int z = c.getInt("jury.z");
        float pitch = c.getInt("jury.pitch");
        float yaw = c.getInt("jury.yaw");

        return new Location(w, x, y, z, pitch, yaw);
    }

    public Player getJudge() {
        YamlConfiguration c = getDataFile();
        String uuid = c.getString("judge");
        if (uuid == null)
            return null;

        return Bukkit.getServer().getPlayer(UUID.fromString(uuid));
    }

    public Court getCourt(UUID uuid) {
        return getPeopleInCourt().get(uuid);
    }

    public YamlConfiguration getDataFile() {
        File f = new File(JusticeSystem.getInstance().getDataFolder() + File.separator + "storage.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return YamlConfiguration.loadConfiguration(f);
    }

    public void saveDataFile(YamlConfiguration c) {
        File f = new File(JusticeSystem.getInstance().getDataFolder() + File.separator + "storage.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        YamlConfiguration c = getDataFile();
        for (Player p : getLocations().keySet()) {
            if (p == null)
                continue;

            p.teleport(getLocations().get(p));
        }
        for (UUID uuid : getSuing().keySet()) {
            c.set("suing." + uuid + ".target", String.valueOf(getSuing().get(uuid)));
            c.set("suing." + uuid + ".amount", String.valueOf(getAmounts().get(uuid)));
        }

        getAmounts().clear();
        getLocations().clear();
        getPeopleInCourt().clear();
        saveDataFile(c);
    }

    public void loadData() {
        YamlConfiguration c = getDataFile();
        ConfigurationSection suing = c.getConfigurationSection("suing");
        if (suing == null)
            return;

        for (String uuidStr : suing.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            getAmounts().put(uuid, c.getInt("suing." + uuid + ".amount"));
            getSuing().put(uuid, UUID.fromString(c.getString("suing." + uuid + ".target")));
        }
        c.set("suing", null);
        saveDataFile(c);
    }

}
