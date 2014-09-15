package com.mydeblob.prisongangs;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mydeblob.subcommand.Execute;
import com.mydeblob.subcommand.GangCommand;
import com.mydeblob.subcommand.Information;



public class CommandHandler implements CommandExecutor{
	private PrisonGangs plugin;
	public static final GangManager gm = GangManager.getGangManager();
	public static final FileManager f = FileManager.getFileManager();
	public static final Util u = Util.getUtil();
	public CommandHandler(PrisonGangs plugin){
		this.plugin = plugin;
	}

	//We have to have an onCommand for /kdr and /pgupdate due to the way I designed the sub command helper
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) { 
		if(cmd.getName().equalsIgnoreCase("kdr")){
			if(!(sender instanceof Player)){
				sender.sendMessage("This command may only be executed in game!");
				return true;
			}
			Player p = (Player) sender;
			if(p.hasPermission("gangs.kdr") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
				if(args.length < 1){
					p.sendMessage(ChatColor.DARK_RED + "=--" + Lang.TRUNCATED_PREFIX.toString()   + ChatColor.DARK_RED + "--=");
					p.sendMessage(ChatColor.GREEN + "Your KDR: " + ChatColor.BLUE + f.getKdrConfig().getDouble("players." + p.getUniqueId().toString() + ".kdr"));
					p.sendMessage(ChatColor.GREEN + "Your kills: " + ChatColor.BLUE + f.getKdrConfig().getInt("players." + p.getUniqueId().toString() + ".kills"));
					p.sendMessage(ChatColor.GREEN + "Your deaths: " + ChatColor.BLUE + f.getKdrConfig().getInt("players." + p.getUniqueId().toString() + ".deaths"));
					return true;
				}else if(args.length == 1){
					Player t = Bukkit.getServer().getPlayer(args[0]);
					if(!f.getGangConfig().contains("players." + t.getUniqueId().toString())){
						p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_FOUND.toString(p, t));
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "=--" + Lang.TRUNCATED_PREFIX.toString() + ChatColor.DARK_RED + "--=");
					p.sendMessage(ChatColor.GREEN + t.getName() + "'s KDR: " + ChatColor.BLUE + f.getKdrConfig().getDouble("players." + t.getUniqueId().toString() + ".kdr"));
					p.sendMessage(ChatColor.GREEN + t.getName() + "'s kills: " + ChatColor.BLUE + f.getKdrConfig().getInt("players." + t.getUniqueId().toString() + ".kills"));
					p.sendMessage(ChatColor.GREEN + t.getName() + "'s deaths: " + ChatColor.BLUE + f.getKdrConfig().getInt("players." + t.getUniqueId().toString() + ".deaths"));
					return true;
				}
				p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
				return true;
			}else{
				p.sendMessage(Lang.NO_PERMS.toString(p));
				return true;
			}
		}else if(cmd.getName().equalsIgnoreCase("pgupdate")){
			if(sender.hasPermission("gangs.admin") || sender.hasPermission("gangs.update")){
				if(plugin.getConfig().getBoolean("auto-updater")){
					@SuppressWarnings("unused")
					Updater updater = new Updater(plugin, 66577, plugin.getPluginFile(), Updater.UpdateType.NO_VERSION_CHECK, true); // Go straight to downloading, and announce progress to console.
					sender.sendMessage(Lang.PREFIX.toString() + ChatColor.GREEN + "Starting the download of the latest version of PrisonGangs. Check the console for progress on the download. Check the \"update\" folder in your \"plugins\" folder to find the downloaded Jar!");
					return true;
				}else{
					sender.sendMessage(ChatColor.RED + "Please enable auto updating in the PrisonGangs config.yml to use this feature");
					return true;
				}
			}else{
				sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS.toString());
			}
		}
		return false;
	}
	
	public void setupCommands(){
		GangCommand gCmd = new GangCommand(); //GangCommand will automatically take care of permission checking
		plugin.getCommand("gang").setExecutor(gCmd); //Register /gang as the base command; g is defined in the plugin.yml as an alias

		/**
		 * Gang Info Command
		 */
		gCmd.addSubCommand("info", null, "gangs.user", false)
		.setMultiplePermissions(Arrays.asList("gangs.info", "gangs.admin"))
		.setExecutor(new Execute(){
			public void execute(Information info){
				Player p = info.getPlayer();
				if(info.hasArgs()){
					if(info.getArgs().size() > 0){ //If they typed more than /gang info GANG_NAME
						p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString());
						return;
					}else{
						if(gm.getGangByName(info.getArgs().get(0)) == null && !(f.getGangConfig().getStringList("gang-names").contains(info.getArgs().get(0)))){
							p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_NOT_FOUND.toString(Arrays.asList("%s%"), Arrays.asList(p.getName())));
							return;
						}
						Gang g = gm.getGangByName(info.getArgs().get(0));
						p.sendMessage(ChatColor.DARK_RED + "***" + ChatColor.DARK_GREEN + g.getName() + "'s" + ChatColor.BLUE + " Info" + ChatColor.DARK_RED + "***");
						p.sendMessage(ChatColor.GREEN + g.getName() + "'s KDR: " + ChatColor.BLUE + gm.getGangKDR(g));
						p.sendMessage(ChatColor.GREEN + g.getName() + "'s Kills: " + ChatColor.BLUE + gm.getGangKills(g));
						p.sendMessage(ChatColor.GREEN + g.getName() + "'s Deaths: " + ChatColor.BLUE + gm.getGangDeaths(g));
						p.sendMessage(ChatColor.GREEN + g.getName() + "'s Members: " + ChatColor.BLUE + u.getGangPlayerStats(g));
						return;
					}
				}else{ //They just typed /gang info
					if(!gm.isInGang(p)){
						p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(Arrays.asList("%s%"), Arrays.asList(p.getName())));
						return;
					}
					Gang g = info.getGang();
					p.sendMessage(ChatColor.DARK_RED + "***" + ChatColor.DARK_GREEN + g.getName()  + "'s" + ChatColor.BLUE + " Info" + ChatColor.DARK_RED + "***");
					p.sendMessage(ChatColor.GREEN + g.getName() + "'s KDR: " + ChatColor.BLUE + gm.getGangKDR(g));
					p.sendMessage(ChatColor.GREEN + g.getName() + "'s Kills: " + ChatColor.BLUE + gm.getGangKills(g));
					p.sendMessage(ChatColor.GREEN + g.getName() + "'s Deaths: " + ChatColor.BLUE + gm.getGangDeaths(g));
					p.sendMessage(ChatColor.GREEN + g.getName() + "'s Members: " + ChatColor.BLUE + u.getGangPlayerStats(g));
					return;
				}
			}
		});
		
		/**
		 * Gang Create Command
		 */
		gCmd.addSubCommand("create", null, "gangs.user")
		.setMultiplePermissions(Arrays.asList("gangs.create", "gangs.admin"))
		.setExecutor(new Execute(){
			public void execute(Information info){
				Player p = info.getPlayer();
				if(info.getGang() != null){
					p.sendMessage(Lang.PREFIX.toString() + Lang.IN_GANG.toString(Arrays.asList("%s%", "%r%", "%g%"), Arrays.asList(p.getName(), info.getRank().toString(), info.getGangName())));
					return;
				}
				if(gm.getGangByName(info.getArgs().get(0)) != null){
					for(String s:f.getGangConfig().getStringList("gang-names")){
						if(s.equalsIgnoreCase(info.getArgs().get(0))){
							p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_EXISTS.toString(Arrays.asList("%s%", "%g%"), Arrays.asList(p.getName(), info.getGangName())));
							return;
						}
					}
				}
				if((info.getArgs().get(0).length() + 1) > plugin.getConfig().getInt("char-limit")){
					p.sendMessage(Lang.PREFIX.toString() + Lang.CHAR_LIMIT.toString(Arrays.asList("%s%"), Arrays.asList(p.getName())));
					return;
				}
				if(!plugin.getConfig().getStringList("blocked-names").isEmpty()){
					for(String s:plugin.getConfig().getStringList("blocked-names")){
						if(info.getArgs().get(0).equalsIgnoreCase(s)){
							p.sendMessage(Lang.PREFIX.toString() + Lang.BLOCKED_NAME.toString(Arrays.asList("%s%"), Arrays.asList(p.getName())));
							return;
						}
					}
					if (info.getArgs().get(0).contains(".") || info.getArgs().get(0).contains("/") || info.getArgs().get(0).contains("\\") || !info.getArgs().get(0).matches("\\w.*")) {
						p.sendMessage(Lang.PREFIX.toString() + Lang.BLOCKED_NAME.toString(Arrays.asList("%s%"), Arrays.asList(p.getName())));
						return;
					}
				}
				gm.createGang(p, info.getArgs().get(0));
				return;
			}
		});
		
		/**
		 * Gang Promote Command
		 */
		gCmd.addSubCommand("promote", null, "gangs.user")
		.setMultiplePermissions(Arrays.asList("gangs.promote", "gangs.admin"))
		.setMinRank(Rank.TRUSTED)
		.setMininumArgs(2)
		.setExecutor(new Execute(){
			public void execute(Information info){
				Player p = info.getPlayer();
				@SuppressWarnings("deprecation")
				Player target = Bukkit.getServer().getPlayer(info.getArgs().get(0));
				if(target == null){
					p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString()); 
					return;
				}
				if(gm.getGangWithPlayer(target) == null){
					p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_GANG.toString(Arrays.asList("%s%", "%t%"), Arrays.asList(p.getName(), target.getName()))); 
					return;
				}if(gm.getGangWithPlayer(target) != gm.getGangWithPlayer(p)){
					p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_YOUR_GANG.toString(Arrays.asList("%s%", "%t%", "%g%"), Arrays.asList(p.getName(), target.getName(), info.getGangName())));
					return;
				}
				gm.promotePlayer(p, target);
			}
		});
	}

}