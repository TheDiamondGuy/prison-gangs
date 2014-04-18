package com.mydeblob;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ClansMain extends JavaPlugin{
	private FileConfiguration customConfig = null;
    private File customConfigFile = null;
	public void onEnable(){
		CommandHandler ch = new CommandHandler(this);
		File config = new File(getDataFolder(), "config.yml");
		if(!config.exists()){
			getLogger().info("[PrisonGangs] No config.yml found! Generating a new one!");
			this.saveDefaultConfig();
		}
		getLogger().info("PrisonGangs enabled, made by mydeblob");
		reloadCustomConfig();
		Bukkit.getServer().getPluginManager().registerEvents(new CommandHandler(this), this);
		getCommand("gang").setExecutor(new CommandHandler(this));
		getCommand("kdr").setExecutor(new CommandHandler(this));
		getServer().getPluginManager().registerEvents(new Events(this, ch),  this);
		SettingsManager.getInstance().setup(this);
		ClanManager.getInstance().setupClans();
		FileConfiguration pluginyml = YamlConfiguration.loadConfiguration(getResource("plugin.yml"));
		for(String cmd : pluginyml.getConfigurationSection("commands").getKeys(false)){
		  getCommand(cmd).setPermissionMessage(ChatColor.RED + "You don't have permission!");
		}
	}
	public void onDisable(){
		saveCustomConfig();
	}
	public void reloadCustomConfig() {
        if (customConfigFile == null) {
        customConfigFile = new File(getDataFolder(), "kdr.yml");
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
 
        // Look for defaults in the jar
        InputStream defConfigStream = this.getResource("kdr.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }
 
    //Method from http://wiki.bukkit.org/Configuration_API_Reference
    public FileConfiguration getCustomConfig() {
        if (customConfig == null) {
            this.reloadCustomConfig();
        }
        return customConfig;
    }
 
    //Method from http://wiki.bukkit.org/Configuration_API_Reference
    public void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
        return;
        }
        try {
            getCustomConfig().save(customConfigFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }
}
