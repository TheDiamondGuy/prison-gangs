package com.cullan.prisongangs;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PrisonGangs
  extends JavaPlugin
{

  public final Events pl = new Events(this);
  
  public void onEnable()
  {
    FileManager f = FileManager.getFileManager();
    f.init(this);
    File config = new File(getDataFolder(), "config.yml");
    if (!config.exists())
    {
      getLogger().info("[PrisonGangs] No config.yml found! Generating a new one!");
      saveDefaultConfig();
    }
    File messages = new File(getDataFolder(), "messages.yml");
    if (!messages.exists())
    {
      getLogger().info("[PrisonGangs] No messages.yml found! Generating a new one!");
      f.saveDefaultLangConfig();
    }
    f.reloadLangConfig();
    f.reloadKdrConfig();
    f.saveKdrConfig();
    f.reloadGangConfig();
    f.saveGangConfig();
    Lang.setFile(YamlConfiguration.loadConfiguration(FileManager.langFile));
    Bukkit.getServer().getPluginManager().registerEvents(new CommandHandler(this), this);
    getCommand("gang").setExecutor(new CommandHandler(this));
    getCommand("kdr").setExecutor(new CommandHandler(this));
    getServer().getPluginManager().registerEvents(pl, this);
    Gang.loadGangs();
    GangManager.getGangManager().loadInvites();
    getLogger().info("[PrisonGangs] PrisonGangs enabled, edited by CullanP");
  }
  
  public void onDisable()
  {
    FileManager f = FileManager.getFileManager();
    f.saveKdrConfig();
    f.saveGangConfig();
    getLogger().info("[PrisonGangs] PrisonGangs disabled, edited by CullanP");
  }
  
  public File getPluginFile()
  {
    return getFile();
  }
}
