package com.mydeblob;


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
	public ArrayList<String> inClanChat = new ArrayList<String>();
	ArrayList<String> inAllyChat = new ArrayList<String>();
	HashMap<Gang, Gang> ally = new HashMap<Gang, Gang>();
	HashMap<String, Gang> invited = new HashMap<String, Gang>();
	private PrisonGangs plugin;
    public static final GangManager t = GangManager.getInstance();
    public static final SettingsManager s = SettingsManager.getInstance();
   
	public CommandHandler(PrisonGangs plugin){
		this.plugin = plugin;
	}
	String prefix = ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + "PrisonGangs" + ChatColor.GRAY + "] ";
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage("This command may only be executed in game!");
			return true;
		}
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("kdr")){
			if(p.hasPermission("gangs.kdr")){
				if(args.length < 1){
					p.sendMessage(ChatColor.DARK_RED + "=--" + ChatColor.DARK_GREEN + "PrisonGangs" + ChatColor.DARK_RED + "--=");
					p.sendMessage(ChatColor.GREEN + "Your KDR: " + ChatColor.BLUE + plugin.getGangConfig().getDouble("players." + p.getName() + ".kdr"));
					p.sendMessage(ChatColor.GREEN + "Your kills: " + ChatColor.BLUE + plugin.getGangConfig().getInt("players." + p.getName() + ".kills"));
					p.sendMessage(ChatColor.GREEN + "Your deaths: " + ChatColor.BLUE + plugin.getGangConfig().getInt("players." + p.getName() + ".deaths"));
					return true;
				}else if(args.length == 1){
					if(!plugin.getGangConfig().contains("players." + args[0])){
						p.sendMessage(prefix + ChatColor.DARK_RED + "Error! The specified player wasn't found! Are you sure you spelled his name correctly?");
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "=--" + ChatColor.DARK_GREEN + "PrisonGangs" + ChatColor.DARK_RED + "--=");
					p.sendMessage(ChatColor.GREEN + args[0] + "'s KDR: " + ChatColor.BLUE + plugin.getGangConfig().getDouble("players." + args[0] + ".kdr"));
					p.sendMessage(ChatColor.GREEN + args[0] + "'s kills: " + ChatColor.BLUE + plugin.getGangConfig().getInt("players." + args[0] + ".kills"));
					p.sendMessage(ChatColor.GREEN + args[0] + "'s deaths: " + ChatColor.BLUE + plugin.getGangConfig().getInt("players." + args[0] + ".deaths"));
					return true;
				}
				return false;
			}
		}
		if(cmd.getName().equalsIgnoreCase("gang")){
			if(args.length < 1){
				if(t.getPlayerClan(p) == null){
					p.sendMessage(prefix + ChatColor.RED + "You are not in a gang! To get information from another gang type /gang info <gangName>");
					return true;
				}
				p.sendMessage(ChatColor.DARK_RED + "***" + ChatColor.DARK_GREEN + t.getPlayerClan(p).getName() + ChatColor.BLUE + " Info" + ChatColor.DARK_RED + "***");
				p.sendMessage(ChatColor.GREEN + "gang KDR: " + ChatColor.BLUE + clanKDR(t.getPlayerClan(p)));
				p.sendMessage(ChatColor.GREEN + "gang Kills: " + ChatColor.BLUE + totalKills(t.getPlayerClan(p)));
				p.sendMessage(ChatColor.GREEN + "gang Deaths: " + ChatColor.BLUE + totalDeaths(t.getPlayerClan(p)));
				p.sendMessage(ChatColor.GREEN + "Members: " + ChatColor.BLUE + getMemberStats(t.getPlayerClan(p)));
				return true;
			}
			if(args[0].equalsIgnoreCase("info")){
				if(args.length == 2){
					if(t.getClan(args[1]) == null && !(s.getClans().getStringList("clannames").contains(args[1]))){
						p.sendMessage(prefix + ChatColor.RED + "Gang not found! Are you sure you typed the gang name correctly?");
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "***" + ChatColor.DARK_GREEN + t.getClan(args[1]).getName() + ChatColor.BLUE + " Info" + ChatColor.DARK_RED + "***");
					p.sendMessage(ChatColor.GREEN + t.getClan(args[1]).getName() + "'s KDR: " + ChatColor.BLUE + clanKDR(t.getClan(args[1])));
					p.sendMessage(ChatColor.GREEN + t.getClan(args[1]).getName() + "'s Kills: " + ChatColor.BLUE + totalKills(t.getClan(args[1])));
					p.sendMessage(ChatColor.GREEN + t.getClan(args[1]).getName() + "'s Deaths: " + ChatColor.BLUE + totalDeaths(t.getClan(args[1])));
					p.sendMessage(ChatColor.GREEN + t.getClan(args[1]).getName() + "'s Members: " + ChatColor.BLUE + getMemberStats(t.getClan(args[1])));
					return true;
				}
				if(t.getPlayerClan(p) == null){
					p.sendMessage(prefix + ChatColor.RED + "You are not in a gang! To get information from another gang type /gang info <gangName>");
					return true;
				}
				p.sendMessage(ChatColor.DARK_RED + "***" + ChatColor.DARK_GREEN + t.getPlayerClan(p).getName() + ChatColor.BLUE + " Info" + ChatColor.DARK_RED + "***");
				p.sendMessage(ChatColor.GREEN + "Gang KDR: " + ChatColor.BLUE + clanKDR(t.getPlayerClan(p))); 
				p.sendMessage(ChatColor.GREEN + "Gang Kills: " + ChatColor.BLUE + totalKills(t.getPlayerClan(p)));
				p.sendMessage(ChatColor.GREEN + "Gang Deaths: " + ChatColor.BLUE + totalDeaths(t.getPlayerClan(p)));
				p.sendMessage(ChatColor.GREEN + "Members: " + ChatColor.BLUE + getMemberStats(t.getPlayerClan(p)));
				return true;
			}
			if(args[0].equalsIgnoreCase("create") && args.length == 2){
				if(p.hasPermission("gangs.create")){
					if(t.getPlayerClan(p) != null){
						p.sendMessage(prefix + ChatColor.RED + "You are already in a gang!");
						return true;
					}
					if(t.getClan(args[1]) == null && s.getClans().getStringList("gang-names").contains(args[1])){
						p.sendMessage(prefix + ChatColor.RED + "That gang already exists!");
						return true;
					}
					if(args[1].length() > plugin.getConfig().getInt("char-length")){
						p.sendMessage(prefix + ChatColor.RED + "Error: That clan name is to long!");
						return true;
					}
					s.getClans().set("gangs." + args[1] + ".members", new ArrayList<String>());
					s.getClans().set("gangs." + args[1] + ".trusted", new ArrayList<String>());
					s.getClans().set("gangs." + args[1] + ".officers", new ArrayList<String>());
					s.getClans().set("gangs." + args[1] + ".leaders", new ArrayList<String>());
					s.getClans().set("gangs." + args[1] + ".description", null);
					List<String> leaders = s.getClans().getStringList("gangs." + args[1] + ".leaders");
					leaders.add(p.getName());
					s.getClans().set("clans." + args[1] + ".leaders", leaders);
					List<String> gangnames = s.getClans().getStringList("gang-names");
					gangnames.add(args[1]);
					s.getClans().set("clannames", gangnames);
					s.saveClans();
					t.setupClans();
					p.sendMessage(prefix + ChatColor.GREEN + "Succesfully created a gang!");
					return true;
				}else{
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				
			}if(args[0].equalsIgnoreCase("create") && args.length != 2){
				p.sendMessage(prefix + ChatColor.RED + "Incorrect usage! Proper usage: /gang create <gangName>");
				return true;
			}if(args[0].equalsIgnoreCase("promote") && args.length == 2){
				if(p.hasPermission("gangs.promote")){
					if(t.getPlayerClan(p) == null){
						p.sendMessage(prefix + ChatColor.RED + "You are not in a gang!");
						return true;
					}
					Player target = (Player) Bukkit.getPlayer(args[1]);
					if(target == null){
						p.sendMessage(prefix + ChatColor.RED + "The specified player isn't online!"); 
						return true;
					}
					if(t.getPlayerClan(target) == null){
						p.sendMessage(prefix + ChatColor.RED + target.getName() + " is not in a gang!"); 
						return true;
					}if(t.getPlayerClan(target) != t.getPlayerClan(p)){
						p.sendMessage(prefix + ChatColor.RED + target.getName() + " is not in your gang!");
						return true;
					}if(t.getPlayerClan(p).getOfficers().contains(p.getName()) || t.getPlayerClan(p).getLeaders().contains(p.getName()) || p.isOp()){
						if(t.getPlayerClan(p).getMembers().contains(target.getName())){
							t.getPlayerClan(p).addTrusted(target);
							t.getPlayerClan(p).removeMember(target);
							p.sendMessage(prefix + ChatColor.GREEN + "Succesfully promoted " + target.getName() + " to trusted!");
							target.sendMessage(prefix + ChatColor.GREEN + "You have been promoted to trusted!");
							t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.BLUE + target.getName() + " has just been promoted to trusted!");
							return true;
						}if(t.getPlayerClan(p).getTrusted().contains(target.getName())){
							t.getPlayerClan(p).addOfficer(target);
							t.getPlayerClan(p).removeTrusted(target);
							p.sendMessage(prefix + ChatColor.GREEN + "Succesfully promoted " + target.getName() + " to a officer!");
							target.sendMessage(prefix + ChatColor.GREEN + "You have been promoted to a officer!");
							t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.BLUE + target.getName() + " has just been promoted to a officer!");
							return true;
						}if(t.getPlayerClan(p).getOfficers().contains(target.getName())){
							t.getPlayerClan(p).addLeader(target);
							t.getPlayerClan(p).removeOfficer(target);
							p.sendMessage(prefix + ChatColor.GREEN + "Succesfully promoted " + target.getName() + " to a leader!");
							target.sendMessage(prefix + ChatColor.GREEN + "You have been promoted to a leader!");
							t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.BLUE + target.getName() + " has just been promoted to a leader!");
							return true;
						}if(t.getPlayerClan(p).getLeaders().contains(target.getName())){
							p.sendMessage(prefix + ChatColor.RED + "That player is already a leader!");
							return true;
						}
					}else{
						p.sendMessage(prefix + ChatColor.RED + "You do not have permission to do this!");
						return true;
					}
				}else{
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("promote") && args.length != 2){
				p.sendMessage(prefix + ChatColor.RED + "Incorrect usage! Proper usage: /gang promote <PlayerName>");
				return true;
			}if(args[0].equalsIgnoreCase("demote") && args.length == 2){
				if(p.hasPermission("gangs.demote")){
					if(t.getPlayerClan(p) == null){
						p.sendMessage(prefix + ChatColor.RED + "You are not in a gang!");
						return true;
					}
					Player target = (Player) Bukkit.getPlayer(args[1]);
					if(target == null){
						p.sendMessage(prefix + ChatColor.RED + "The specified player isn't online!"); 
						return true;
					}
					if(t.getPlayerClan(target) == null){
						p.sendMessage(prefix + ChatColor.RED + target.getName() + " is not in a gang!"); 
						return true;
					}if(t.getPlayerClan(target) != t.getPlayerClan(p)){
						p.sendMessage(prefix + ChatColor.RED + target.getName() + " is not in your gang!");
						return true;
					}if(t.getPlayerClan(p).getOfficers().contains(p.getName()) || t.getPlayerClan(p).getLeaders().contains(p.getName())){
						if(t.getPlayerClan(p).getMembers().contains(target.getName())){
							p.sendMessage(prefix + ChatColor.RED + "That player is already as low as can be!!");
							return true;
						}if(t.getPlayerClan(p).getTrusted().contains(target.getName())){
							t.getPlayerClan(p).addMember(p);
							t.getPlayerClan(p).removeTrusted(target);
							p.sendMessage(prefix + ChatColor.GREEN + "Succesfully demoted " + target.getName() + " to a member!");
							target.sendMessage(prefix + ChatColor.GREEN + "You have been demoted to a member!");
							t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.BLUE + target.getName() + " has just been demoted to a member!");
							return true;
						}if(t.getPlayerClan(p).getOfficers().contains(target.getName())){
							t.getPlayerClan(p).addTrusted(p);
							t.getPlayerClan(p).removeOfficer(target);
							p.sendMessage(prefix + ChatColor.GREEN + "Succesfully demoted " + target.getName() + " to trusted!");
							target.sendMessage(prefix + ChatColor.GREEN + "You have been demoted to trusted!");
							t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.BLUE + target.getName() + " has just been demoted to trusted!");
							return true;
						}if(t.getPlayerClan(p).getLeaders().contains(target.getName())){
							p.sendMessage(prefix + ChatColor.RED + "You can not demote a leader!");
							return true;
						}
					}else{
						p.sendMessage(prefix + ChatColor.RED + "You do not have permission to do this!");
						return true;
					}
				}else{
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("demote") && args.length != 2){
				p.sendMessage(prefix + ChatColor.RED + "Incorrect usage! Proper usage: /gang demote <PlayerName>");
				return true;
			}if(args[0].equalsIgnoreCase("kick") && args.length == 2){
				if(p.hasPermission("gangs.kick")){
					if(t.getPlayerClan(p) == null){
						p.sendMessage(prefix + ChatColor.RED + "You are not in a gang!");
						return true;
					}
					Player target = (Player) Bukkit.getPlayer(args[1]);
					if(target == null){
						p.sendMessage(prefix + ChatColor.RED + "The specified player isn't online!"); 
						return true;
					}
					if(t.getPlayerClan(target) == null){
						p.sendMessage(prefix + ChatColor.RED + target.getName() + " is not in a gang!"); 
						return true;
					}if(t.getPlayerClan(target) != t.getPlayerClan(p)){
						p.sendMessage(prefix + ChatColor.RED + target.getName() + " is not in your gang!");
						return true;
					}
					if(t.getPlayerClan(p).getTrusted().contains(p.getName()) || t.getPlayerClan(p).getOfficers().contains(p.getName()) || t.getPlayerClan(p).getLeaders().contains(p.getName())){
						if(t.getPlayerClan(p).getMembers().contains(target.getName())){
							t.getPlayerClan(p).removeMember(target);
							p.sendMessage(prefix + ChatColor.GREEN + "Succesfully kicked " + target.getName() + " from the gang!");
							target.sendMessage(prefix + ChatColor.GREEN + "You have been kicked from the gang!");
							t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.RED + target.getName() + " has just been kicked from the gang!");
							return true;
						}if(t.getPlayerClan(p).getTrusted().contains(target.getName())){
							t.getPlayerClan(p).removeTrusted(target);
							p.sendMessage(prefix + ChatColor.GREEN + "Succesfully kicked " + target.getName() + " from the gang!");
							target.sendMessage(prefix + ChatColor.GREEN + "You have been kicked from the gang!");
							t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.RED + target.getName() + " has just been kicked from the gang!");
							return true;
						}if(t.getPlayerClan(p).getOfficers().contains(target.getName())){
							t.getPlayerClan(p).removeOfficer(target);
							p.sendMessage(prefix + ChatColor.GREEN + "Succesfully kicked " + target.getName() + " from the gang!");
							target.sendMessage(prefix + ChatColor.GREEN + "You have been kicked from the gang!");
							t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.RED + target.getName() + " has just been kicked from the gang!");
							return true;
						}if(t.getPlayerClan(p).getLeaders().contains(target.getName())){
							t.getPlayerClan(p).removeLeader(target);
							p.sendMessage(prefix + ChatColor.GREEN + "Succesfully kicked " + target.getName() + " from the gang!");
							target.sendMessage(prefix + ChatColor.GREEN + "You have been kicked from the gang!");
							t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.RED + target.getName() + " has just been kicked from the gang!");
							return true;
						}
				}
			}else{
				p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
				}
			}if(args[0].equalsIgnoreCase("kick") && args.length != 2){
				p.sendMessage(prefix + ChatColor.RED + "Incorrect usage! Proper usage: /gang kick <PlayerName>");
				return true;
			}if(args[0].equalsIgnoreCase("leave") && args.length == 1){
				if(p.hasPermission("gangs.leave")){
					if(t.getPlayerClan(p) == null){
						p.sendMessage(prefix + ChatColor.RED + "You are not in a gang!");
						return true;
					}
					if(t.getPlayerClan(p).getMembers().contains(p.getName())){
						t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.RED + p.getName() + " has just left the gang!");
						t.getPlayerClan(p).removeMember(p);
						p.sendMessage(prefix + ChatColor.GREEN + "Succesfully left the gang!");
						return true;
					}if(t.getPlayerClan(p).getTrusted().contains(p.getName())){
						t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.RED + p.getName() + " has just left the gang!");
						t.getPlayerClan(p).removeTrusted(p);
						p.sendMessage(prefix + ChatColor.GREEN + "Succesfully left the gang!");
						return true;
					}if(t.getPlayerClan(p).getOfficers().contains(p.getName())){
						t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.RED + p.getName() + " has just left the gang!");
						t.getPlayerClan(p).removeOfficer(p);
						p.sendMessage(prefix + ChatColor.GREEN + "Succesfully left the gang!");
						return true;
					}if(t.getPlayerClan(p).getLeaders().contains(p.getName())){
						t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.RED + p.getName() + " has just left the gang!");
						Gang gang = t.getPlayerClan(p);
						t.getPlayerClan(p).removeLeader(p);
						if(gang.getLeaders().isEmpty()){
							gang.msg(gang, ChatColor.RED + "The gang has been disbanded due to absence of leaders!!");
							s.getClans().set("clans." + gang.getName(), null);
						    List gangs = s.getClans().getStringList("clannames");
						    gangs.remove(gang.getName());
						    s.getClans().set("clannames", gangs);
						    s.saveClans();
						    t.setupClans();
						    p.sendMessage(ChatColor.RED + "The gang has been disbanded due to absence of leaders!!");
						}
						p.sendMessage(prefix + ChatColor.GREEN + "Succesfully left the gang!");
						return true;
					}
				}else{
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
			}if(args[0].equalsIgnoreCase("leave") && args.length != 1){
				p.sendMessage(prefix + ChatColor.RED + "Incorrect usage! Proper usage: /gang leave");
				return true;
			}if(args[0].equalsIgnoreCase("invite") && args.length == 2){
				if(p.hasPermission("gangs.invite")){
					if(t.getPlayerClan(p) == null){
						p.sendMessage(prefix + ChatColor.RED + "You are not in a gang!");
						return true;
					}
					Player target = (Player) Bukkit.getPlayer(args[1]);
					if(target == null){
						p.sendMessage(prefix + ChatColor.RED + "The specified player isn't online!"); 
						return true;
					}
					if(t.getPlayerClan(p).getMembers().contains(p.getName())){
						p.sendMessage(prefix + ChatColor.RED + "You do not have permission!");
						return true;
					}
					invited.put(target.getName(), t.getPlayerClan(p));
					p.sendMessage(prefix + ChatColor.GREEN + "Succesfully invited " + target.getName() + " to the gang!");
					target.sendMessage(prefix + ChatColor.GREEN + p.getName() + " has invited you to the gang " + t.getPlayerClan(p).getName() + "! Type /gang join " + t.getPlayerClan(p).getName() + " to join!");
					t.getPlayerClan(p).msg(t.getPlayerClan(p), ChatColor.BLUE + target.getName() + " has been invited to the gang!");
					return true;
				}
			}if(args[0].equalsIgnoreCase("invite") && args.length != 2){
				p.sendMessage(prefix + ChatColor.RED + "Incorrect usage! Proper usage: /gang invite <PlayerName>");
				return true;
			}if(args[0].equalsIgnoreCase("join") && args.length == 2){
				if(p.hasPermission("gangs.join")){
					if(t.getPlayerClan(p) != null){
						p.sendMessage(prefix + ChatColor.RED + "You must leave your current gang first! Type /gang leave to leave!");
						return true;
					}
					if(t.getClan(args[1]) == null && !(s.getClans().getStringList("clannames").contains(args[1]))){
						p.sendMessage(prefix + ChatColor.RED + "gang not found! Are you sure you typed the gang name correctly?");
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
//    @EventHandler (priority=EventPriority.HIGH)
//    public void onChat(AsyncPlayerChatEvent e) {
//    	e.getPlayer().sendMessage("[Debugger 2000] You chatted!");
//        if(ingangChat.contains(e.getPlayer().getName())) {
//        	e.getPlayer().sendMessage("Your in gang chat");
//            e.setCancelled(true);
//            gangManager.getInstance().getPlayergang(e.getPlayer()).msg(gangManager.getInstance().getPlayergang(e.getPlayer()), e.getMessage());
//        }
//    }

}
