package com.mydeblob;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PrisonGangs extends JavaPlugin{
	public void onEnable(){
		FileManager f = FileManager.getFileManager();
		File config = new File(getDataFolder(), "config.yml");
		if(!config.exists()){
			getLogger().info("[PrisonGangs] No config.yml found! Generating a new one!");
			this.saveDefaultConfig();
		}
		File messages = new File(getDataFolder(), "messages.yml");
		if(!messages.exists()){
			getLogger().info("[PrisonGangs] No messages.yml found! Generating a new one!");
			f.saveDefaultLangConfig();
		}
		f.reloadKdrConfig();
		f.reloadGangConfig();
		Lang.setFile(f.getLangYaml());
		Bukkit.getServer().getPluginManager().registerEvents(new CommandHandler(this), this);
		getCommand("gang").setExecutor(new CommandHandler(this));
		getCommand("kdr").setExecutor(new CommandHandler(this));
		getServer().getPluginManager().registerEvents(new Events(this),  this);
		GangManager.getInstance().setupClans();		
		Converter c = new Converter(this); //Conversion to UUID
		if(toConvert()){
			c.convert();
		}
		getLogger().info("PrisonGangs enabled, made by mydeblob");
	}
	public void onDisable(){
		FileManager f = FileManager.getFileManager();
		f.saveKdrConfig();
		f.saveGangConfig();
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
}
