package com.mydeblob.prisongangs;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandHandler implements CommandExecutor, Listener{
	private PrisonGangs plugin;
	public static final GangManager gm = GangManager.getGangManager();
	public static final FileManager f = FileManager.getFileManager();
	public CommandHandler(PrisonGangs plugin){
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
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
					Player t = Bukkit.getPlayerExact(args[0]);
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
		}
		if(cmd.getName().equalsIgnoreCase("gang")){
			if(!(sender instanceof Player)){
				sender.sendMessage("This command may only be executed in game!");
				return true;
			}
			Player p = (Player) sender;
			if(args.length < 1){
				if(p.hasPermission("gangs.info") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
					if(!gm.isInGang(p)){
						p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
						return true;
					}
					Gang g = gm.getGangWithPlayer(p);
					p.sendMessage(ChatColor.DARK_RED + "***" + ChatColor.DARK_GREEN + g.getName()  + "'s" + ChatColor.BLUE + " Info" + ChatColor.DARK_RED + "***");
					p.sendMessage(ChatColor.GREEN + g.getName() + "'s KDR: " + ChatColor.BLUE + gm.getGangKDR(g));
					p.sendMessage(ChatColor.GREEN + g.getName() + "'s Kills: " + ChatColor.BLUE + gm.getGangKills(g));
					p.sendMessage(ChatColor.GREEN + g.getName() + "'s Deaths: " + ChatColor.BLUE + gm.getGangDeaths(g));
					p.sendMessage(ChatColor.GREEN + g.getName() + "'s Members: " + ChatColor.BLUE + getGangPlayerStats(g));
					return true;
				}else{
					p.sendMessage(Lang.NO_PERMS.toString(p));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("info")){
				if(p.hasPermission("gangs.info") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
					if(args.length == 2){
						if(gm.getGangByName(args[1]) == null && !(f.getGangConfig().getStringList("gang-names").contains(args[1]))){
							p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_NOT_FOUND.toString(p));
							return true;
						}
						Gang g = gm.getGangByName(args[1]);
						p.sendMessage(ChatColor.DARK_RED + "***" + ChatColor.DARK_GREEN + g.getName() + "'s" + ChatColor.BLUE + " Info" + ChatColor.DARK_RED + "***");
						p.sendMessage(ChatColor.GREEN + g.getName() + "'s KDR: " + ChatColor.BLUE + gm.getGangKDR(g));
						p.sendMessage(ChatColor.GREEN + g.getName() + "'s Kills: " + ChatColor.BLUE + gm.getGangKills(g));
						p.sendMessage(ChatColor.GREEN + g.getName() + "'s Deaths: " + ChatColor.BLUE + gm.getGangDeaths(g));
						p.sendMessage(ChatColor.GREEN + g.getName() + "'s Members: " + ChatColor.BLUE + getGangPlayerStats(g));
						return true;
					}else{
						p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
						return true;
					}
				}else{
					p.sendMessage(Lang.NO_PERMS.toString(p));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("create") && args.length == 2){
				if(p.hasPermission("gangs.create") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
					if(gm.getGangWithPlayer(p) != null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.IN_GANG.toString(p, gm.getPlayerRank(p.getName(), gm.getGangWithPlayer(p)), gm.getGangWithPlayer(p)));
						return true;
					}
					if(gm.getGangByName(args[1]) != null){
						for(String s:f.getGangConfig().getStringList("gang-names")){
							if(s.equalsIgnoreCase(args[1])){
								p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_EXISTS.toString(p, gm.getGangByName(args[1])));
								return true;
							}
						}
					}
					if((args[1].length() + 1) > plugin.getConfig().getInt("char-limit")){
						p.sendMessage(Lang.PREFIX.toString() + Lang.CHAR_LIMIT.toString(p));
						return true;
					}
					if(!plugin.getConfig().getStringList("blocked-names").isEmpty()){
						for(String s:plugin.getConfig().getStringList("blocked-names")){
							if(args[1].contains(s)){
								p.sendMessage(Lang.PREFIX.toString() + Lang.INAPPROPRIATE_NAME.toString(p));
								return true;
							}
						}
					}
					gm.createGang(p, args[1]);
					return true;
				}else{
					p.sendMessage(Lang.NO_PERMS.toString());
					return true;
				}
			}else if(args[0].equalsIgnoreCase("create") && args.length != 2){
				p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
				return true;
			}if(args[0].equalsIgnoreCase("promote") && args.length == 2){
				if(p.hasPermission("gangs.promote") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
					if(gm.getGangWithPlayer(p) == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
						return true;
					}
					Player target = Bukkit.getPlayerExact(args[1]);
					if(target == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString(p)); 
						return true;
					}
					if(gm.getGangWithPlayer(target) == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_GANG.toString(p, target, gm.getGangWithPlayer(p))); 
						return true;
					}if(gm.getGangWithPlayer(target) != gm.getGangWithPlayer(p)){
						p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_YOUR_GANG.toString(p, target, gm.getGangWithPlayer(p)));
						return true;
					}
					gm.promotePlayer(p, target, gm.getGangWithPlayer(p));
					return true;
				}else{
					p.sendMessage(Lang.NO_PERMS.toString(p));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("promote") && args.length != 2){
				p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
				return true;
			}else if(args[0].equalsIgnoreCase("demote") && args.length == 2){
				if(p.hasPermission("gangs.demote") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
					if(gm.getGangWithPlayer(p) == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
						return true;
					}
					Player target = (Player) Bukkit.getPlayerExact(args[1]);
					if(target == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString(p, target)); 
						return true;
					}if(gm.getGangWithPlayer(target) == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_GANG.toString(p, target, gm.getGangWithPlayer(p))); 
						return true;
					}if(gm.getGangWithPlayer(target) != gm.getGangWithPlayer(p)){
						p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_YOUR_GANG.toString(p, target, gm.getGangWithPlayer(p)));
						return true;
					}
					gm.demotePlayer(p, target, gm.getGangWithPlayer(p));
					return true;
				}else{
					p.sendMessage(Lang.NO_PERMS.toString(p));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("demote") && args.length != 2){
				p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
				return true;
			}else if(args[0].equalsIgnoreCase("setowner") && args.length == 2){ 
				if(p.hasPermission("gangs.setowner") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
					if(gm.getGangWithPlayer(p) == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
						return true;
					}
					Player target = (Player) Bukkit.getPlayerExact(args[1]);
					if(target == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString(p, target)); 
						return true;
					}if(gm.getGangWithPlayer(target) == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_GANG.toString(p, target, gm.getGangWithPlayer(p))); 
						return true;
					}if(gm.getGangWithPlayer(target) != gm.getGangWithPlayer(p)){
						p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_YOUR_GANG.toString(p, target, gm.getGangWithPlayer(p)));
						return true;
					}
					if(p.getName().equals(target.getName())){
						p.sendMessage(Lang.PREFIX.toString() + Lang.CANT_TRANSFER_OWNERSHIP_TO_YOURSELF.toString(p));
						return true;
					}
					Gang g = gm.getGangWithPlayer(p);
					if(!g.getOwner().equalsIgnoreCase(p.getName())){
						p.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_SET_OWNERSHIP.toString(p, target, g, gm.getPlayerRank(target.getName(), g)));
						return true;
					}else{
						Ranks rr = gm.getPlayerRank(target.getName(), g);
						String oldOwner = g.getOwner();
						g.setOwner(target);
						g.addLeader(Bukkit.getPlayerExact(oldOwner));
						if(rr == Ranks.MEMBER){
							g.removeMember(target);
						}else if(rr == Ranks.TRUSTED){
							g.removeTrusted(target);
						}else if(rr == Ranks.OFFICER){
							g.removeOfficer(target);
						}else if(rr == Ranks.LEADER){
							g.removeLeader(target);
						}
						p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_SET_OWNERSHIP.toString(p, target, g, gm.getPlayerRank(p.getName(), g)));
						target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_SET_OWNERSHIP.toString(p, target, g, gm.getPlayerRank(target.getName(), g)));
						gm.messageGang(g, Lang.SUCCESS_SET_OWNERSHIP.toString(p, target, g, gm.getPlayerRank(p.getName(), g)));
						return true;
					}
				}else{
					p.sendMessage(Lang.NO_PERMS.toString(p));
					return true;
				}
			}else if (args[0].equalsIgnoreCase("setowner") && args.length != 2){
				p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
				return true;
			}else if(args[0].equalsIgnoreCase("kick") && args.length == 2){
				if(p.hasPermission("gangs.kick") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
					if(gm.getGangWithPlayer(p) == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
						return true;
					}
					Player target = (Player) Bukkit.getPlayerExact(args[1]);
					if(target == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString(p, target)); 
						return true;
					}if(gm.getGangWithPlayer(target) == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_GANG.toString(p, target, gm.getGangWithPlayer(p))); 
						return true;
					}if(gm.getGangWithPlayer(target) != gm.getGangWithPlayer(p)){
						p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_YOUR_GANG.toString(p, target, gm.getGangWithPlayer(p)));
						return true;
					}
					gm.kickPlayer(p, target, gm.getGangWithPlayer(p));
					return true;
				}else{
					p.sendMessage(Lang.NO_PERMS.toString(p));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("kick") && args.length != 2){
				p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
				return true;
			}else if(args[0].equalsIgnoreCase("leave") && args.length == 1){
				if(p.hasPermission("gangs.leave") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
					if(gm.getGangWithPlayer(p) == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
						return true;
					}
					gm.leave(p, gm.getGangWithPlayer(p));
					return true;
				}else{
					p.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS.toString(p));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("leave") && args.length != 1){
				p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
				return true;
			}else if(args[0].equalsIgnoreCase("invite") && args.length == 2){
				if(p.hasPermission("gangs.invite") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
					if(gm.getGangWithPlayer(p) == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
						return true;
					}
					Player target = (Player) Bukkit.getPlayerExact(args[1]);
					if(target == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString(p, target)); 
						return true;
					}
					gm.invitePlayer(p, target, gm.getGangWithPlayer(p));
					return true;
				}else{
					p.sendMessage(Lang.NO_PERMS.toString(p));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("invite") && args.length != 2){
				p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
				return true;
			}else if(args[0].equalsIgnoreCase("join") && args.length == 2){
				if(p.hasPermission("gangs.join") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
					if(gm.getGangWithPlayer(p) != null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.IN_GANG.toString(p, gm.getPlayerRank(sender.getName(), gm.getGangWithPlayer(p)), gm.getGangWithPlayer(p)));
						return true;
					}
					ArrayList<String> names = new ArrayList<String>();
					for(int i=0; i < f.getGangConfig().getStringList("gang-names").size(); i++) {
						  names.add(f.getGangConfig().getStringList("gang-names").get(i).toLowerCase());
						}
					if(gm.getGangByName(args[1]) == null && !(names.contains(args[1].toLowerCase()))){
						p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_NOT_FOUND.toString(p));
						return true;
					}
					if(gm.isInvited(p)){
						if(gm.gangsMatchInvited(p, args[1])){
							gm.removeInvitation(p);
							gm.getGangByName(args[1]).addMember(p);
							p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_JOIN.toString(p, gm.getPlayerRank(p.getName(), gm.getGangWithPlayer(p)), gm.getGangWithPlayer(p)));
							gm.messageGang(gm.getGangByName(args[1]), Lang.SUCCESS_JOIN.toString(p, gm.getPlayerRank(sender.getName(), gm.getGangWithPlayer(p)), gm.getGangWithPlayer(p)));
							return true;
						}else{
							p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_NOT_FOUND.toString(p));
							return true;
						}
					}else{
						p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_INVITED.toString(p, gm.getGangByName(args[1])));
						return true;
					}
				}else{
					p.sendMessage(Lang.NO_PERMS.toString(p));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("join") && args.length != 2){
				p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
				return true;
			}else if(args[0].equalsIgnoreCase("uninvite") && args.length == 2){
				if(p.hasPermission("gangs.uninvite") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
					if(gm.getGangWithPlayer(p) == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
						return true;
					}
					Player target = (Player) Bukkit.getPlayerExact(args[1]);
					gm.uninvitePlayer(p, target, gm.getGangWithPlayer(p));
					return true;
				}else{
					p.sendMessage(Lang.NO_PERMS.toString(p));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("uninvite") && args.length != 2){
				p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
				return true;
			}else if(args[0].equalsIgnoreCase("disband") && args.length == 1){
				if(p.hasPermission("gangs.disband") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
					if(gm.getGangWithPlayer(p) == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
						return true;
					}
					gm.disbandGang(p, gm.getGangWithPlayer(p).getName());
					return true;
				}else{
					p.sendMessage(Lang.NO_PERMS.toString(p));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("disband") && args.length != 1){
				p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
				return true;
			}else if(args[0].equalsIgnoreCase("help") && args.length == 1){
				p.sendMessage(ChatColor.DARK_RED + "--=" + Lang.PREFIX.toString() + ChatColor.BLUE + "Help" + ChatColor.DARK_RED + "=--");
				p.sendMessage(ChatColor.RED + "g is in alias for gang!");
				p.sendMessage(ChatColor.GREEN + "/gang create <gangName>");
				p.sendMessage(ChatColor.YELLOW + "    Creates a gang");
				p.sendMessage(ChatColor.GREEN + "/gang or /gang info <gangInfo>");
				p.sendMessage(ChatColor.YELLOW + "    Displays information about a gang");
				p.sendMessage(ChatColor.GREEN + "/gang leave");
				p.sendMessage(ChatColor.YELLOW + "    Leaves the gang");
				p.sendMessage(ChatColor.GREEN + "/gang join <gangName>");
				p.sendMessage(ChatColor.YELLOW + "    Joins a gang if invited");
				p.sendMessage(ChatColor.BLUE + "Type " + ChatColor.RED + "/gang help 2 " + ChatColor.BLUE + "to read the second page!");
				return true;
			}else if(args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("2") && args.length == 2){
				p.sendMessage(ChatColor.DARK_RED + "--=" + Lang.PREFIX.toString() + ChatColor.BLUE + "Help" + ChatColor.DARK_RED + "=--");
				p.sendMessage(ChatColor.RED + "g is in alias for gang!");
				p.sendMessage(ChatColor.GREEN + "/gang invite <PlayerName>");
				p.sendMessage(ChatColor.YELLOW + "    Invites a player to the gang");
				p.sendMessage(ChatColor.GREEN + "/gang uninvite <PlayerName>");
				p.sendMessage(ChatColor.YELLOW + "    Uninvites a player to the gang");
				p.sendMessage(ChatColor.GREEN + "/gang kick <PlayerName>");
				p.sendMessage(ChatColor.YELLOW + "    Kicks a player from the gang");
				p.sendMessage(ChatColor.GREEN + "/gang promote <PlayerName>");
				p.sendMessage(ChatColor.BLUE + "Type " + ChatColor.RED + "/gang help 3 " + ChatColor.BLUE + "to read the second page!");
				return true;
			}else if(args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("3")  && args.length == 2){
				p.sendMessage(ChatColor.DARK_RED + "--=" +  Lang.PREFIX.toString() +  ChatColor.BLUE + "Help" + ChatColor.DARK_RED + "=--");
				p.sendMessage(ChatColor.RED + "g is in alias for gang!");
				p.sendMessage(ChatColor.YELLOW + "    Promotes a player in the gang");
				p.sendMessage(ChatColor.GREEN + "/gang disband");
				p.sendMessage(ChatColor.YELLOW + "    Disbands a gang");
				return true;
			}else if(args[0].equalsIgnoreCase("help")){
				p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
				return true;
			}
		}else if(cmd.getName().equalsIgnoreCase("pgupdate")){
			if(sender.hasPermission("gangs.admin") || sender.hasPermission("gangs.update")){
				if(plugin.getConfig().getBoolean("auto-updater")){
					@SuppressWarnings("unused")
					Updater updater = new Updater(plugin, 66577, plugin.getPluginFile(), Updater.UpdateType.NO_VERSION_CHECK, true); // Go straight to downloading, and announce progress to console.
					sender.sendMessage(Lang.PREFIX.toString() + ChatColor.GREEN + "Starting the download of the latest version of PrisonGangs. Check console for progress on the download. Reload after is has downloaded!");
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

	/**
	 * Returns the information needed to display all the members of a gang
	 * 
	 * @param g - The gang needing the retrieval of data
	 * @return A List of all the gang members and there current status (Gang rank and online/offline)
	 */
	@SuppressWarnings("deprecation")
	public ArrayList<String> getGangPlayerStats(Gang g){
		ArrayList<String> memberData = new ArrayList<String>();
		for(String s : g.getMembers()){
			Player p = Bukkit.getServer().getPlayerExact(s);
			String status = null;
			if(!(p == null)){
				status = "Online";
			}else if(p == null){
				status = "Offline";
			}
			String name = "Member " + plugin.getConfig().getString("seperator") + " " +  s + " - " + status;
			memberData.add(name);
		}
		for(String s : g.getTrusted()){
			Player p = Bukkit.getServer().getPlayerExact(s);
			String status = null;
			if(!(p == null)){
				status = "Online";
			}else if(p == null){
				status = "Offline";
			}
			String name = "Trusted " + plugin.getConfig().getString("seperator") + " " +  s + " - " + status;
			memberData.add(name);
		}
		for(String s : g.getOfficers()){
			Player p = Bukkit.getServer().getPlayerExact(s);
			String status = null;
			if(!(p == null)){
				status = "Online";
			}else if(p == null){
				status = "Offline";
			}
			String name = "Officer " + plugin.getConfig().getString("seperator") + " " +  s + " - " + status;
			memberData.add(name);
		}
		for(String s : g.getLeaders()){
			Player p = Bukkit.getServer().getPlayerExact(s);
			String status = null;
			if(!(p == null)){
				status = "Online";
			}else if(p == null){
				status = "Offline";
			}
			String name = "Leader " + plugin.getConfig().getString("seperator") + " " +  s + " - " + status;
			memberData.add(name);
		}
		Player p = Bukkit.getServer().getPlayerExact(g.getOwner());
		String status = null;
		if(!(p == null)){
			status = "Online";
		}else if(p == null){
			status = "Offline";
		}
		String name = "Owner " + plugin.getConfig().getString("seperator") + " " +  p.getName() + " - " + status;
		memberData.add(name);
		return memberData;
	}

}
