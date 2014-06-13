package com.mydeblob.prisongangs;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
					p.sendMessage(ChatColor.GREEN + "Your KDR: " + ChatColor.BLUE + f.getGangConfig().getDouble("players." + p.getUniqueId().toString() + ".kdr"));
					p.sendMessage(ChatColor.GREEN + "Your kills: " + ChatColor.BLUE + f.getGangConfig().getInt("players." + p.getUniqueId().toString() + ".kills"));
					p.sendMessage(ChatColor.GREEN + "Your deaths: " + ChatColor.BLUE + f.getGangConfig().getInt("players." + p.getUniqueId().toString() + ".deaths"));
					return true;
				}else if(args.length == 1){
					Player t = Bukkit.getPlayerExact(args[0]);
					if(!f.getGangConfig().contains("players." + t.getUniqueId().toString())){
						p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_FOUND.toString(p, t));
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "=--" + Lang.TRUNCATED_PREFIX.toString() + ChatColor.DARK_RED + "--=");
					p.sendMessage(ChatColor.GREEN + t.getName() + "'s KDR: " + ChatColor.BLUE + f.getGangConfig().getDouble("players." + t.getUniqueId().toString() + ".kdr"));
					p.sendMessage(ChatColor.GREEN + t.getName() + "'s kills: " + ChatColor.BLUE + f.getGangConfig().getInt("players." + t.getUniqueId().toString() + ".kills"));
					p.sendMessage(ChatColor.GREEN + t.getName() + "'s deaths: " + ChatColor.BLUE + f.getGangConfig().getInt("players." + t.getUniqueId().toString() + ".deaths"));
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
					p.sendMessage(ChatColor.DARK_RED + "***" + ChatColor.DARK_GREEN + g.getName() + ChatColor.BLUE + "'s Info" + ChatColor.DARK_RED + "***");
					p.sendMessage(ChatColor.GREEN + g.getName() + "'s KDR: " + ChatColor.BLUE + clanKDR(g));
					p.sendMessage(ChatColor.GREEN + g.getName() + "'s Kills: " + ChatColor.BLUE + totalKills(g));
					p.sendMessage(ChatColor.GREEN + g.getName() + "'s Deaths: " + ChatColor.BLUE + totalDeaths(g));
					p.sendMessage(ChatColor.GREEN + g.getName() + "'s Members: " + ChatColor.BLUE + getMemberStats(g));
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
						p.sendMessage(ChatColor.DARK_RED + "***" + ChatColor.DARK_GREEN + g.getName() + ChatColor.BLUE + " Info" + ChatColor.DARK_RED + "***");
						p.sendMessage(ChatColor.GREEN + g.getName() + "'s KDR: " + ChatColor.BLUE + clanKDR(g));
						p.sendMessage(ChatColor.GREEN + g.getName() + "'s Kills: " + ChatColor.BLUE + totalKills(g));
						p.sendMessage(ChatColor.GREEN + g.getName() + "'s Deaths: " + ChatColor.BLUE + totalDeaths(g));
						p.sendMessage(ChatColor.GREEN + g.getName() + "'s Members: " + ChatColor.BLUE + getMemberStats(g));
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
						p.sendMessage(Lang.PREFIX.toString() + Lang.IN_GANG.toString(p, Gang.getPlayerRank(p.getName(), gm.getGangWithPlayer(p)), gm.getGangWithPlayer(p)));
						return true;
					}
					if(gm.getGangByName(args[1]) != null && f.getGangConfig().getStringList("gang-names").contains(args[1])){
						p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_EXISTS.toString(p, gm.getGangByName(args[1])));
						return true;
					}
					if((args[1].length() + 1) > plugin.getConfig().getInt("char-limit")){
						p.sendMessage(Lang.PREFIX.toString() + Lang.CHAR_LIMIT.toString(p));
						return true;
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
					if(gm.getGangWithPlayer(p) != null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.IN_GANG.toString(p, Gang.getPlayerRank(p.getName(), gm.getGangWithPlayer(p)), gm.getGangWithPlayer(p)));
						return true;
					}
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null){
						p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString(p, target)); 
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
					Player target = (Player) Bukkit.getPlayer(args[1]);
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
		}if(args[0].equalsIgnoreCase("kick") && args.length == 2){
			if(p.hasPermission("gangs.kick") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
				if(gm.getGangWithPlayer(p) == null){
					p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
					return true;
				}
				Player target = (Player) Bukkit.getPlayer(args[1]);
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
		}if(args[0].equalsIgnoreCase("leave") && args.length == 1){
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
		}if(args[0].equalsIgnoreCase("leave") && args.length != 1){
			p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
			return true;
		}if(args[0].equalsIgnoreCase("invite") && args.length == 2){
			if(p.hasPermission("gangs.invite") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
				if(gm.getGangWithPlayer(p) == null){
					p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
					return true;
				}
				Player target = (Player) Bukkit.getPlayer(args[1]);
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
		}if(args[0].equalsIgnoreCase("invite") && args.length != 2){
			p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
			return true;
		}if(args[0].equalsIgnoreCase("join") && args.length == 2){
			if(p.hasPermission("gangs.join") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
				if(gm.getGangWithPlayer(p) != null){
					p.sendMessage(Lang.PREFIX.toString() + Lang.IN_GANG.toString(p, Gang.getPlayerRank(sender.getName(), gm.getGangWithPlayer(p)), gm.getGangWithPlayer(p)));
					return true;
				}
				if(gm.getGangByName(args[1]) == null && !(f.getGangConfig().getStringList("gang-names").contains(args[1]))){
					p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_NOT_FOUND.toString(p));
					return true;
				}
				if(gm.isInvited(p)){
					if(gm.gangsMatchInvited(p, args[1])){
						gm.removeInvitation(p);
						gm.getGangByName(args[1]).addMember(p);
						p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_JOIN.toString(p, Gang.getPlayerRank(sender.getName(), gm.getGangWithPlayer(p)), gm.getGangWithPlayer(p)));
						gm.messageGang(gm.getGangByName(args[1]), Lang.SUCCESS_JOIN.toString(p, Gang.getPlayerRank(sender.getName(), gm.getGangWithPlayer(p)), gm.getGangWithPlayer(p)));
						return true;
					}else{
						p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_NOT_FOUND.toString(p));
						return true;
					}
				}else{
					p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_INVITED.toString(p));
					return true;
				}
				if(invited.containsKey(p.getName()) && invited.get(p.getName()).equals(t.getClan(args[1])) || p.isOp() || p.hasPermission("gangs.admin")){
					invited.remove(p.getName());
					t.getClan(args[1]).addMember(p);
					p.sendMessage(prefix + ChatColor.GREEN + "Welcome to the gang!");
					t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.BLUE + p.getName() + " has just joined the gang!");
					return true;
				}else{
					p.sendMessage(prefix + ChatColor.RED + "You have not been invited to this gang!");
					return true;
				}
			}else{
				p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
		}if(args[0].equalsIgnoreCase("join") && args.length != 2){
			p.sendMessage(prefix + ChatColor.RED + "Incorrect usage! Proper usage: /gang join <gangName>");
			return true;
		}if(args[0].equalsIgnoreCase("uninvite") && args.length == 2){
			if(p.hasPermission("gangs.uninvite")){
				if(t.getPlayerClan(p) == null){
					p.sendMessage(prefix + ChatColor.RED + "You are not in a gang!");
					return true;
				}
				if(t.getPlayerClan(p).getMembers().contains(p.getName())){
					p.sendMessage(prefix + ChatColor.RED + "You do not have permission!");
					return true;
				}
				Player target = (Player) Bukkit.getPlayer(args[1]);
				if(!invited.containsKey(target.getName())){
					p.sendMessage(prefix + ChatColor.RED + "That player was never invited to your gang!");
					return true;
				}
				invited.remove(target.getName());
				p.sendMessage(prefix + ChatColor.GREEN + "Succesfully un-invited " + target.getName() + " from your gang!");
				return true;
			}
		}if(args[0].equalsIgnoreCase("uninvite") && args.length != 2){
			p.sendMessage(prefix + ChatColor.RED + "Incorrect usage! Proper usage: /gang uninvite <PlayerName>");
			return true;
		}if(args[0].equalsIgnoreCase("disband") && args.length == 1){
			if(p.hasPermission("gangs.disband")){
				if(t.getPlayerClan(p) == null){
					p.sendMessage(prefix + ChatColor.RED + "You are not in a gang!");
					return true;
				}
				if(t.getPlayerClan(p).getLeaders().contains(p.getName())){
					t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.RED + "The gang has been disbanded!");
					s.getClans().set("clans." + t.getPlayerClan(p).getName(), null);
					List gangs = s.getClans().getStringList("clannames");
					gangs.remove(t.getPlayerClan(p).getName());
					s.getClans().set("clannames", gangs);
					s.saveClans();
					t.setupClans();
					p.sendMessage(prefix + ChatColor.GREEN + "Succesfully disbanded the gang!");
					return true;
				}else{
					p.sendMessage(prefix + ChatColor.RED + "Only leaders can disband gangs!");
					return true;
				}
			}
		}if(args[0].equalsIgnoreCase("disband") && args.length != 1){
			p.sendMessage(prefix + ChatColor.RED + "Incorrect usage! Proper usage: /gang disband");
			return true;
		}if(args[0].equalsIgnoreCase("c") && args.length == 1){
			if(p.hasPermission("gangs.chat")){
				if(t.getPlayerClan(p) == null){
					p.sendMessage(prefix + ChatColor.RED + "You are not in a gang!");
					return true;
				}
				if(inClanChat.contains(p.getName())){
					inClanChat.remove(p.getName());
					inAllyChat.add(p.getName());
					p.sendMessage(prefix + ChatColor.GREEN + "Now in the ally chat channel");
					return true;
				}if(inAllyChat.contains(p.getName())){
					inAllyChat.remove(p.getName());
					p.sendMessage(prefix + ChatColor.GREEN + "Now in the public chat channel");
					return true;
				}
				inClanChat.add(p.getName());
				p.sendMessage(prefix + ChatColor.GREEN + "Now in the gang chat channel!");
				return true;
			}else{
				p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			}
		}if(args.length == 2 && args[0].equalsIgnoreCase("c") && args[1].equalsIgnoreCase("a")){
			if(p.hasPermission("gangs.chat")){
				if(t.getPlayerClan(p) == null){
					p.sendMessage(prefix + ChatColor.RED + "You are not in a gang!");
					return true;
				}
				inAllyChat.add(p.getName());
				p.sendMessage(prefix + ChatColor.GREEN + "Now in the ally chat channel!");
				return true;
			}else{
				p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
		}if(args[0].equalsIgnoreCase("c") && args.length != 1){
			p.sendMessage(prefix + ChatColor.RED + "Unkown chat channel! Proper chat channels: /gang c (gang chat) and /gang c a (ally chat) and /gang c p (public chat)");
			return true;
		}if(args[0].equalsIgnoreCase("ally") && args.length == 2){
			if(p.hasPermission("gangs.ally")){
				if(t.getPlayerClan(p) == null){
					p.sendMessage(prefix + ChatColor.RED + "You are not in a gang!");
					return true;
				}
				if(t.getClan(args[1]) == null && !(s.getClans().getStringList("clannames").contains(args[1]))){
					p.sendMessage(prefix + ChatColor.RED + "gang not found! Are you sure you typed the gang name correctly?");
					return true;
				}
				if(t.getPlayerClan(p).getMembers().contains(p.getName())){
					p.sendMessage(prefix + ChatColor.RED + "You do not have permission!");
					return true;
				}
				if(t.getPlayerClan(p).getTrusted().contains(p.getName())){
					p.sendMessage(prefix + ChatColor.RED + "You do not have permission!");
					return true;
				}
				if(ally.containsKey(t.getPlayerClan(p)) && ally.get(t.getPlayerClan(p)).getName().equalsIgnoreCase(args[1])){
					List ally = s.getClans().getStringList("clans." + t.getPlayerClan(p).getName() + ".allies");
					ally.add(args[1]);
					s.getClans().set("clans." + t.getPlayerClan(p).getName() + ".allies", ally);
					s.saveClans();
					t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.GREEN + "Your gang is now allies with " + ChatColor.BLUE + args[1] + ChatColor.GREEN + "!");
					t.getClan(args[1]).msg(t.getClan(args[1]), ChatColor.GREEN + "Your gang is now allies with " + ChatColor.BLUE + t.getPlayerClan(p).getName() + ChatColor.GREEN + "!");
					return true;
				}
				ally.put(t.getPlayerClan(p), t.getClan(args[1]));
				for(String s: t.getClan(args[1]).getLeaders()){
					Player plr = Bukkit.getPlayer(s);
					plr.sendMessage(prefix + ChatColor.BLUE + t.getPlayerClan(p).getName() + ChatColor.GREEN + " has requested to become an ally with your gang! To accept type /gang ally " + t.getPlayerClan(p).getName());
				}
				for(String s: t.getClan(args[1]).getOfficers()){
					Player plr = Bukkit.getPlayer(s);
					plr.sendMessage(prefix + ChatColor.BLUE + t.getPlayerClan(p).getName() + ChatColor.GREEN + " has requested to become an ally with your gang! To accept type /gang ally " + t.getPlayerClan(p).getName());
				}
				p.sendMessage(prefix + ChatColor.GREEN + "Succesfully asked " + ChatColor.BLUE + args[1] + ChatColor.GREEN + " to become an ally with your gang!");
				return true;
			}else{
				p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
		}if(args[0].equalsIgnoreCase("ally") && args.length != 2){
			p.sendMessage(prefix + ChatColor.RED + "Incorrect usage! Proper usage: /gang ally <gangName>");
			return true;
		}if(args[0].equalsIgnoreCase("help") && args.length == 1){
			p.sendMessage(ChatColor.DARK_RED + "--=" + ChatColor.DARK_GREEN + "PrisonGangs " + ChatColor.BLUE + "Help" + ChatColor.DARK_RED + "=--");
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
		}if(args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("2") && args.length == 2){
			p.sendMessage(ChatColor.DARK_RED + "--=" + ChatColor.DARK_GREEN + "Templar" + ChatColor.AQUA + " gangs" + ChatColor.BLUE + "Help" + ChatColor.DARK_RED + "=--");
			p.sendMessage(ChatColor.RED + "c is in alias for gang!");
			p.sendMessage(ChatColor.GREEN + "/gang invite <PlayerName>");
			p.sendMessage(ChatColor.YELLOW + "    Invites a player to the gang");
			p.sendMessage(ChatColor.GREEN + "/gang uninvite <PlayerName>");
			p.sendMessage(ChatColor.YELLOW + "    Uninvites a player to the gang");
			p.sendMessage(ChatColor.GREEN + "/gang kick <PlayerName>");
			p.sendMessage(ChatColor.YELLOW + "    Kicks a player from the gang");
			p.sendMessage(ChatColor.GREEN + "/gang promote <PlayerName>");
			p.sendMessage(ChatColor.BLUE + "Type " + ChatColor.RED + "/gang help 3 " + ChatColor.BLUE + "to read the second page!");
			return true;
		}if(args[0].equalsIgnoreCase("help") && args[1].equalsIgnoreCase("3")  && args.length == 2){
			p.sendMessage(ChatColor.DARK_RED + "--=" + ChatColor.DARK_GREEN + "Templar" + ChatColor.AQUA + " gangs" + ChatColor.BLUE + "Help" + ChatColor.DARK_RED + "=--");
			p.sendMessage(ChatColor.RED + "c is in alias for gang!");
			p.sendMessage(ChatColor.YELLOW + "    Promotes a player in the gang");
			p.sendMessage(ChatColor.GREEN + "/gang disband");
			p.sendMessage(ChatColor.YELLOW + "    Disbands a gang");
			p.sendMessage(ChatColor.GREEN + "/gang ally <gangName>");
			p.sendMessage(ChatColor.YELLOW + "    Requests an allyship from another gang");
			p.sendMessage(ChatColor.GREEN + "/gang c");
			p.sendMessage(ChatColor.YELLOW + "    Switches between between gang chat and public chat");
			p.sendMessage(ChatColor.GREEN + "/gang c a");
			p.sendMessage(ChatColor.YELLOW + "Switches between ally chat and public chat");
			return true;
		}
	}else if(cmd.getName().equalsIgnoreCase("pgupdate")){
		if(sender.hasPermission("gangs.admin") || sender.hasPermission("gangs.update")){
			if(plugin.getConfig().getBoolean("auto-updater")){
				@SuppressWarnings("unused")
				Updater updater = new Updater(plugin, 66577, plugin.getPluginFile(), Updater.UpdateType.NO_VERSION_CHECK, true); // Go straight to downloading, and announce progress to console.
				sender.sendMessage(Lang.PREFIX.toString() + ChatColor.GREEN + "Starting the download of the latest version of GuardOverseer. Check console for progress on the download. Reload after is has downloaded!");
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
public double clanKDR(Gang c){
	int membersKills = 0;
	int membersDeaths = 0;
	int trustedKills = 0;
	int trustedDeaths = 0;
	int officersKills = 0;
	int officersDeaths = 0;
	int leadersKills = 0;
	int leadersDeaths = 0;
	int totalKills = 0;
	int totalDeaths = 0;
	double KDR = 0;
	for(String s : c.getMembers()){
		membersKills += plugin.getGangConfig().getInt("players." + s + ".kills");
		membersDeaths += plugin.getGangConfig().getInt("players." + s + ".deaths");
	}
	for(String s : c.getTrusted()){
		trustedKills += plugin.getGangConfig().getInt("players." + s + ".kills");
		trustedDeaths += plugin.getGangConfig().getInt("players." + s + ".deaths");
	}
	for(String s : c.getOfficers()){
		officersKills += plugin.getGangConfig().getInt("players." + s + ".kills");
		officersDeaths += plugin.getGangConfig().getInt("players." + s + ".deaths");
	}
	for(String s : c.getLeaders()){
		leadersKills += plugin.getGangConfig().getInt("players." + s + ".kills");
		leadersDeaths += plugin.getGangConfig().getInt("players." + s + ".deaths");
	}
	if(totalDeaths == 0){
		KDR = totalKills;
		return KDR;
	}else{
		totalKills = membersKills + trustedKills + officersKills + leadersKills;
		totalDeaths = membersDeaths + trustedDeaths + officersDeaths + leadersDeaths;
		KDR = totalKills/totalDeaths;
		return KDR;
	}
}
public int totalKills(Gang c){
	int membersKills = 0;
	int trustedKills = 0;
	int officersKills = 0;
	int leadersKills = 0;
	int totalKills = 0;
	for(String s : c.getMembers()){
		membersKills += plugin.getGangConfig().getInt("players." + s + ".kills");
	}
	for(String s : c.getTrusted()){
		trustedKills += plugin.getGangConfig().getInt("players." + s + ".kills");
	}
	for(String s : c.getOfficers()){
		officersKills += plugin.getGangConfig().getInt("players." + s + ".kills");
	}
	for(String s : c.getLeaders()){
		leadersKills += plugin.getGangConfig().getInt("players." + s + ".kills");
	}
	totalKills = membersKills + trustedKills + officersKills + leadersKills;
	return totalKills;
}
public int totalDeaths(Gang c){
	int membersDeaths = 0;
	int trustedDeaths = 0;
	int officersDeaths = 0;
	int leadersDeaths = 0;
	int totalDeaths = 0;
	for(String s : c.getMembers()){
		membersDeaths += plugin.getGangConfig().getInt("players." + s + ".deaths");
	}
	for(String s : c.getTrusted()){
		trustedDeaths += plugin.getGangConfig().getInt("players." + s + ".death");
	}
	for(String s : c.getOfficers()){
		officersDeaths += plugin.getGangConfig().getInt("players." + s + ".death");
	}
	for(String s : c.getLeaders()){
		leadersDeaths += plugin.getGangConfig().getInt("players." + s + ".death");
	}
	totalDeaths = membersDeaths + trustedDeaths + officersDeaths + leadersDeaths;
	return totalDeaths;
}
public ArrayList<String> getMemberStats(Gang c){
	ArrayList<String> memberData = new ArrayList<String>();
	for(String s : c.getMembers()){
		Player p = Bukkit.getServer().getPlayer(s);
		String status = null;
		if(!(p == null)){
			status = "Online";
		}else if(p == null){
			status = "Offline";
		}
		String name = "Member " + plugin.getConfig().getString("seperator") + " " +  s + " - " + status;
		memberData.add(name);
	}
	for(String s : c.getTrusted()){
		Player p = Bukkit.getServer().getPlayer(s);
		String status = null;
		if(!(p == null)){
			status = "Online";
		}else if(p == null){
			status = "Offline";
		}
		String name = "Trusted " + plugin.getConfig().getString("seperator") + " " +  s + " - " + status;
		memberData.add(name);
	}
	for(String s : c.getOfficers()){
		Player p = Bukkit.getServer().getPlayer(s);
		String status = null;
		if(!(p == null)){
			status = "Online";
		}else if(p == null){
			status = "Offline";
		}
		String name = "Officer " + plugin.getConfig().getString("seperator") + " " +  s + " - " + status;
		memberData.add(name);
	}
	for(String s : c.getLeaders()){
		Player p = Bukkit.getServer().getPlayer(s);
		String status = null;
		if(!(p == null)){
			status = "Online";
		}else if(p == null){
			status = "Offline";
		}
		String name = "Leader " + plugin.getConfig().getString("seperator") + " " +  s + " - " + status;
		memberData.add(name);
	}
	return memberData;
}

}
