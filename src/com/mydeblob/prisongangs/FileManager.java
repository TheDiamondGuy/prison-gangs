package com.mydeblob.prisongangs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FileManager {
	  private Plugin p;
	  static FileManager instance = new FileManager();
	  private static FileConfiguration gangConfig;
	  private static File gangFile;
	  private static FileConfiguration kdrConfig = null;
	  private static File kdrFile = null;
	  private static FileConfiguration langConfig = null;
	  public static File langFile = null;
	  public static FileManager getFileManager(){
	    return instance;
	  }
	  public void init(Plugin p){
		  this.p = p;
	  }
	  /*
	   --------------------KDR Configuration Implementation--------------------
	   */
	  public void reloadKdrConfig(){
	        if (kdrFile == null) {
	        kdrFile = new File(p.getDataFolder(), "kdr.yml");
	        }
	        kdrConfig = YamlConfiguration.loadConfiguration(kdrFile);
	        InputStream defConfigStream = p.getResource("kdr.yml");
	        if (defConfigStream != null) {
	            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	            kdrConfig.setDefaults(defConfig);
	        }
	    }
	 
	    public FileConfiguration getKdrConfig() {
	        if (kdrConfig == null) {
	            this.reloadKdrConfig();
	        }
	        return kdrConfig;
	    }
	 
	    public void saveKdrConfig() {
	        if (kdrConfig == null || kdrFile == null) {
	        return;
	        }
	        try {
	            getKdrConfig().save(kdrFile);
	        } catch (IOException ex) {
	            p.getLogger().log(Level.SEVERE, "Could not save config to " + kdrFile, ex);
	        }
	    }
	    
	    /*
		   --------------------Language Configuration Implementation--------------------
		   */
	    public void reloadLangConfig() {
	        if (langFile == null) {
	        langFile = new File(p.getDataFolder(), "messages.yml");
	        }
	        langConfig = YamlConfiguration.loadConfiguration(langFile);
	        InputStream defConfigStream = p.getResource("messages.yml");
	        if (defConfigStream != null) {
	            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	            langConfig.setDefaults(defConfig);
	        }
	    }
	    
	    public FileConfiguration getLangConfig() {
	        if (langConfig == null) {
	            this.reloadLangConfig();
	        }
	        return langConfig;
	    }
	 
	    public void saveLangConfig() {
	        if (langConfig == null || langFile == null) {
	        return;
	        }
	        try {
	            getLangConfig().save(langFile);
	        } catch (IOException ex) {
	            p.getLogger().log(Level.SEVERE, "Could not save config to " + langFile, ex);
	        }
	    }
	    
	    
	    public void saveDefaultLangConfig() {
	        if (langFile == null) {
	            langFile = new File(p.getDataFolder(), "messages.yml");
	        }
	        if (!langFile.exists()) {            
	             p.saveResource("messages.yml", false);
	         }
	    }
	    
	    /*
		   --------------------Gang Configuration Implementation--------------------
		   */
	    public void reloadGangConfig(){
	        if (gangFile == null) {
	        gangFile = new File(p.getDataFolder(), "gangs.yml");
	        }
	        gangConfig = YamlConfiguration.loadConfiguration(gangFile);
	        InputStream defConfigStream = p.getResource("gangs.yml");
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
	            p.getLogger().log(Level.SEVERE, "Could not save config to " + gangFile, ex);
	        }
	    }
}
