package com.mydeblob.prisongangs;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.mydeblob.prisongangs.Updater.ReleaseType;

public class PrisonGangs extends JavaPlugin{
	public static boolean update = false;
	public static String name = "";
	public static ReleaseType type = null;
	public static String version = "";
	public static String link = "";
	public void onEnable(){
		FileManager f = FileManager.getFileManager();
		f.init(this);
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
		f.reloadLangConfig();
		f.reloadKdrConfig();
		f.saveKdrConfig();
		f.reloadGangConfig();
		Lang.setFile(YamlConfiguration.loadConfiguration(FileManager.langFile));
		Bukkit.getServer().getPluginManager().registerEvents(new CommandHandler(this), this);
		getCommand("gang").setExecutor(new CommandHandler(this));
		getCommand("kdr").setExecutor(new CommandHandler(this));
		getCommand("pgupdate").setExecutor(new CommandHandler(this));
		getServer().getPluginManager().registerEvents(new Events(this),  this);
		Gang.loadGangs();
		GangManager.getGangManager().loadInvites();
		if(getConfig().getBoolean("auto-updater")){
			Updater updater = new Updater(this, 66577, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
			update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
			name = updater.getLatestName(); // Get the latest name
			version = updater.getLatestGameVersion(); // Get the latest game version
			type = updater.getLatestType(); // Get the latest file's type
			link = updater.getLatestFileLink(); // Get the latest link
		}
		getLogger().info("PrisonGangs enabled, made by mydeblob");
	}
	public void onDisable(){
		FileManager f = FileManager.getFileManager();
		f.saveKdrConfig();
		f.saveGangConfig();
	}
	
	public File getPluginFile(){
		return this.getFile();
	}
}
