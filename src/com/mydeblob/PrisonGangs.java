package com.mydeblob;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PrisonGangs extends JavaPlugin{
	private FileConfiguration kdrConfig = null;
    private File kdrFile = null;
	public void onEnable(){
		File config = new File(getDataFolder(), "config.yml");
		if(!config.exists()){
			getLogger().info("[PrisonGangs] No config.yml found! Generating a new one!");
			this.saveDefaultConfig();
		}
		getLogger().info("PrisonGangs enabled, made by mydeblob");
		reloadGangConfig();
		Bukkit.getServer().getPluginManager().registerEvents(new CommandHandler(this), this);
		getCommand("gang").setExecutor(new CommandHandler(this));
		getCommand("kdr").setExecutor(new CommandHandler(this));
		getServer().getPluginManager().registerEvents(new Events(this),  this);
		SettingsManager.getInstance().setup(this);
		GangManager.getInstance().setupClans();
		FileConfiguration pluginyml = YamlConfiguration.loadConfiguration(getResource("plugin.yml"));
		Converter c = new Converter(this);
		if(toConvert()){
			c.convert();
		}
		for(String cmd : pluginyml.getConfigurationSection("commands").getKeys(false)){
		  getCommand(cmd).setPermissionMessage(ChatColor.RED + "You don't have permission!");
		}
	}
	public void onDisable(){
		saveGangConfig();
	}
	
	public boolean toConvert(){
		if(getConfig().getConfigurationSection("players") != null){
			for(String k:getConfig().getConfigurationSection("players").getKeys(false)){
				try{
					Player p = Bukkit.getPlayer(k);
					if(p == null){
						return false;
					}else{
						return true;
					}
				}catch(Exception e){
					Bukkit.getServer().getLogger().log(Level.WARNING, "Failed to check if a UUID conversion is needed");
				}
			}
		}
		return false;
	}
	public void reloadGangConfig() {
        if (gangFile == null) {
        gangFile = new File(getDataFolder(), "kdr.yml");
        }
        gangConfig = YamlConfiguration.loadConfiguration(gangFile);
        InputStream defConfigStream = this.getResource("kdr.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            gangConfig.setDefaults(defConfig);
        }
    }
 
    public FileConfiguration getGangConfig() {
        if (gangConfig == null) {
            this.reloadGangConfig();
        }
        return gangConfig;
    }
 
    public void saveGangConfig() {
        if (gangConfig == null || gangFile == null) {
        return;
        }
        try {
            getGangConfig().save(gangFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + gangFile, ex);
        }
    }
}
