package com.mydeblob;

import java.util.logging.Level;

import org.bukkit.Bukkit;

public class Converter {
	private PrisonGangs plugin;
	public Converter(PrisonGangs plugin){
		this.plugin = plugin;
	}
	public void convert(){
		if(plugin.getConfig().getConfigurationSection("players") != null){
			for(String k:plugin.getConfig().getConfigurationSection("players").getKeys(false)){
				try{
					String uuid = Bukkit.getPlayer(k).getUniqueId().toString();
					plugin.getGangConfig().set("players." + k, uuid);
					Bukkit.getServer().getLogger().info("Succesfully converted the user " + k);
				}catch(Exception e){
					Bukkit.getServer().getLogger().log(Level.WARNING, "Failed to convert player " + k);
				}
			}
		}
	}
}
