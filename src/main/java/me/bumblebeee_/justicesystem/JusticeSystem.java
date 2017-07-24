package me.bumblebeee_.justicesystem;

import lombok.Getter;
import me.bumblebeee_.justicesystem.commands.*;
import me.bumblebeee_.justicesystem.listeners.PlayerMove;
import me.bumblebeee_.justicesystem.listeners.PlayerQuit;
import me.bumblebeee_.justicesystem.managers.CourtManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class JusticeSystem extends JavaPlugin {

    Messages msgs = new Messages();

    private static @Getter Plugin instance;
    private static @Getter CourtManager courtManager;
    private static @Getter Economy economy = null;
    private static @Getter Map<String, String> commands = new HashMap<>();


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        courtManager = new CourtManager();
        msgs.setup();
        registerCommands();
        registerEvents();
        setupEconomy();
        courtManager.loadData();

        commands.put("/setcourt", "Sets the courts location");
        commands.put("/setjudge <player>", "Sets the judges location");
        commands.put("/joinjury", "Joins the jury");
        commands.put("/leavejury", "Leaves the jury");
        commands.put("/sue <player> <amount>", "Sue a player");
        commands.put("/innocent", "Declare a player as innocent");
        commands.put("/guilty", "Declare a player as guilty");
        commands.put("/court status", "View the current court status");
        commands.put("/court reload", "Reload the plugin");
        commands.put("/court help", "View this page");
        commands.put("/setjury", "Set the jurys location");
        commands.put("/setcriminal", "Set the criminals location");
        commands.put("/court start <player>", "Start a court session");
        commands.put("/court end", "End a court session");
        commands.put("/court remove", "Stop suing a player");
    }

    @Override
    public void onDisable() {
        courtManager.saveData();
    }

    public void registerCommands() {
        Bukkit.getServer().getPluginCommand("court").setExecutor(new CourtCommand());
        Bukkit.getServer().getPluginCommand("guilty").setExecutor(new GuiltyCommand());
        Bukkit.getServer().getPluginCommand("innocent").setExecutor(new InnocentCommand());
        Bukkit.getServer().getPluginCommand("joinjury").setExecutor(new JoinJuryCommand());
        Bukkit.getServer().getPluginCommand("leavejury").setExecutor(new LeaveJuryCommand());
        Bukkit.getServer().getPluginCommand("setcourt").setExecutor(new SetCourtCommand());
        Bukkit.getServer().getPluginCommand("setcriminal").setExecutor(new SetCriminalCommand());
        Bukkit.getServer().getPluginCommand("setjudge").setExecutor(new setJudgeCommand());
        Bukkit.getServer().getPluginCommand("setjury").setExecutor(new SetJuryCommand());
        Bukkit.getServer().getPluginCommand("sue").setExecutor(new SueCommand());
    }

    public void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}
