package com.mydeblob;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class SettingsManager {
	  static SettingsManager instance = new SettingsManager();
	  Plugin p;
	  FileConfiguration clans;
	  File clanfile;

	  public static SettingsManager getInstance()
	  {
	    return instance;
	  }

	  public void setup(Plugin p)
	  {
	    this.p = p;

	    if (!p.getDataFolder().exists()) p.getDataFolder().mkdir();

	    this.clanfile = new File(p.getDataFolder(), "gangs.yml");
	    this.clans = YamlConfiguration.loadConfiguration(this.clanfile);

	    if (!this.clanfile.exists())
	      try {
	        this.clanfile.createNewFile();
	      }
	      catch (IOException e) {
	        Bukkit.getServer().getLogger().info("Could not create the gangs file!");
	      }
	  }

	  public FileConfiguration getClans()
	  {
	    return this.clans;
	  }

	  public void saveClans() {
	    try {
	      this.clans.save(this.clanfile);
	    }
	    catch (IOException e) {
	      Bukkit.getServer().getLogger().info("Could not save gangs file!");
	    }
	  }

	  public PluginDescriptionFile getDescription() {
	    return this.p.getDescription();
	  }
}
