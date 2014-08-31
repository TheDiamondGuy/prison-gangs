package com.mydeblob.prisongangs;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.mydeblob.subcommand.Execute;
import com.mydeblob.subcommand.GangCommand;
import com.mydeblob.subcommand.Information;



public class CommandHandler{
	private PrisonGangs plugin;
	public static final GangManager gm = GangManager.getGangManager();
	public static final FileManager f = FileManager.getFileManager();
	public CommandHandler(PrisonGangs plugin){
		this.plugin = plugin;
	}

	public void setupCommands(){
		GangCommand gCmd = new GangCommand(); //GangCommand will automatically take care of permission checking
		GangCommand kdrCmd = new GangCommand();
		plugin.getCommand("gang").setExecutor(gCmd); //Register /gang as the base command; g is defined in the plugin.yml as an alias
		plugin.getCommand("kdr").setExecutor(kdrCmd);
		
		/**
		 * KDR Command
		 */
		kdrCmd.addSubCommand("kdr", null, "gangs.user")
		.setMultiplePermissions(Arrays.asList("gangs.kdr", "gangs.admin"))
		.setExecutor(new Execute(){
			@SuppressWarnings("deprecation")
			public void execute(Information info){
				Player p = info.getPlayer();
				if(info.hasArgs()){
					if(info.getArgs().size() > 0){ //If they typed more than /kdr GANG_NAME
						p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString());
						return;
					}else{
						Player target = Bukkit.getServer().getPlayer(info.getArgs().get(0));
						if(!f.getGangConfig().contains("players." + target.getUniqueId().toString())){
							p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_FOUND.toString(Arrays.asList("%s%", "%t%"), Arrays.asList(p.getName(), info.getArgs().get(0))));
							return;
						}
						p.sendMessage(ChatColor.DARK_RED + "=--" + Lang.TRUNCATED_PREFIX.toString() + ChatColor.DARK_RED + "--=");
						p.sendMessage(ChatColor.GREEN + target.getName() + "'s KDR: " + ChatColor.BLUE + f.getKdrConfig().getDouble("players." + target.getUniqueId().toString() + ".kdr"));
						p.sendMessage(ChatColor.GREEN + target.getName() + "'s kills: " + ChatColor.BLUE + f.getKdrConfig().getInt("players." + target.getUniqueId().toString() + ".kills"));
						p.sendMessage(ChatColor.GREEN + target.getName() + "'s deaths: " + ChatColor.BLUE + f.getKdrConfig().getInt("players." + target.getUniqueId().toString() + ".deaths"));
						return;
					}
				}else{ //They just typed /kdr
					p.sendMessage(ChatColor.DARK_RED + "=--" + Lang.TRUNCATED_PREFIX.toString()   + ChatColor.DARK_RED + "--=");
					p.sendMessage(ChatColor.GREEN + "Your KDR: " + ChatColor.BLUE + f.getKdrConfig().getDouble("players." + p.getUniqueId().toString() + ".kdr"));
					p.sendMessage(ChatColor.GREEN + "Your kills: " + ChatColor.BLUE + f.getKdrConfig().getInt("players." + p.getUniqueId().toString() + ".kills"));
					p.sendMessage(ChatColor.GREEN + "Your deaths: " + ChatColor.BLUE + f.getKdrConfig().getInt("players." + p.getUniqueId().toString() + ".deaths"));
					return;
				}
			}
		});
	}

}