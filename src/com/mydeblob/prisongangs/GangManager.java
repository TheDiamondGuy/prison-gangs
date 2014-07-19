package com.mydeblob.prisongangs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GangManager {
	private static GangManager instance = new GangManager();
	private static HashMap<String, Gang> invited = new HashMap<String, Gang>();
	private static HashMap<Gang, Gang> allied = new HashMap<Gang, Gang>(); //Gang requestion ally; Gang recieveing request; This is ally requests
	private static HashMap<String, Gang> gangChat = new HashMap<String, Gang>();
	private FileManager f = FileManager.getFileManager();
	public static GangManager getGangManager(){
		return instance;
	}

	/**
	 * Get the invited players and the gang they were invited to
	 * 
	 * @return the invited players and gang
	 */
	public HashMap<String, Gang> getInvited(){
		return invited;
	}
	
	public HashMap<Gang, Gang> getAllied(){ //Not functional
		return allied;
	}
	
	/**
	 * Get a gang object by name
	 * 
	 * @param name - Name of the gang object you are attempting to get
	 * @return NULL if no gang found, otherwise @return gang object
	 */
	public Gang getGangByName(String name){
		for (Gang g: Gang.getGangs()){
			if (g.getName().equalsIgnoreCase(name)){
				return g;
			}
		}
		return null;
	}

	/**
	 * Checks if a player is in a gang
	 * 
	 * @param p - The player you want to check if is in a gang
	 * @return TRUE if the player is in a gang, FALSE otherwise
	 */
	public boolean isInGang(Player p){
		if(getGangWithPlayer(p) == null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Gets the gang object that has a player in it
	 * 
	 * @param p - Player to get the gang object for
	 * @return NULL if the player isn't in a gang, @return a gang object otherwise
	 */
	public Gang getGangWithPlayer(Player p){
		for (Gang g: Gang.getGangs()){
			if(g.getAllPlayers().contains(p.getName())){
				return g;
			}
		}
		return null;
	}
	
	/**
	 * Gets the players rank
	 * 
	 * @throws NPE if player isn't in a gang
	 * @param playerName - Player you want to get the rank
	 * @param g - Players gang
	 * @return enum Ranks (Players rank)
	 */
	public Ranks getPlayerRank(String playerName, Gang g){
		  if(!g.getMembers().isEmpty()){
			  if(g.getMembers().contains(playerName)){
				  return Ranks.MEMBER;
			  }
		  }if(!g.getTrusted().isEmpty()){
			  if(g.getTrusted().contains(playerName)){
				  return Ranks.TRUSTED;
			  }
		  }if(!g.getOfficers().isEmpty()){
			  if(g.getOfficers().contains(playerName)){
				  return Ranks.OFFICER;
			  }
		  }if(!g.getLeaders().isEmpty()){
			 if(g.getLeaders().contains(playerName)){
				 return Ranks.LEADER;
			 }
		  }if(g.getOwner().equals(playerName)){
			  return Ranks.OWNER;
		  }
		  return null;
	  }
	
	/**
	 * Promotes the specified player in the specified gang
	 * 
	 * @throws NPE if the player isn't in the specified gang and/or it doesn't exist
	 * @param sender - The player that sent the command
	 * @param target - The player getting promoted
	 * @param gang - The gang of the players
	 */
	@SuppressWarnings("deprecation") //getPlayerExact is deprecated due to UUID's
	public void promotePlayer(Player sender, Player target, Gang gang){
		if(sender.getName() == target.getName() && (!sender.hasPermission("gangs.admin") || !sender.isOp())){
			sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_PROMOTE_YOURSELF.toString(sender, target, gang));
		}
		if(gang.getOfficers().contains(sender.getName())){
			if(gang.getMembers().contains(target.getName())){
				gang.addTrusted(target);
				gang.removeMember(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				return;
			}else{
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				return;
			}
		}if(gang.getLeaders().contains(sender.getName())){
			if(gang.getMembers().contains(target.getName())){
				gang.addTrusted(target);
				gang.removeMember(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				return;
			}else if(gang.getTrusted().contains(target.getName())){
				gang.addOfficer(target);
				gang.removeTrusted(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
				messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
				return;
			}else{
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
				return;
			}
		}if(gang.getOwner().equals(sender.getName())){
			if(gang.getMembers().contains(target.getName())){
				gang.addTrusted(target);
				gang.removeMember(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				return;
			}else if(gang.getTrusted().contains(target.getName())){
				gang.addOfficer(target);
				gang.removeTrusted(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
				messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
				return;
			}else if(gang.getOfficers().contains(target.getName())){
				gang.addLeader(target);
				gang.removeOfficer(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.LEADER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.LEADER));
				messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.LEADER));
				return;
			}else{
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_PROMOTE_OWNER.toString(sender, target, gang, Ranks.LEADER));
				return;
			}
		}if(sender.hasPermission("gang.admin") || sender.isOp()){
			if(gang.getMembers().contains(target.getName())){
				gang.addTrusted(target);
				gang.removeMember(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				return;
			}else if(gang.getTrusted().contains(target.getName())){
				gang.addOfficer(target);
				gang.removeTrusted(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
				messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
				return;
			}else if(gang.getOfficers().contains(target.getName())){
				gang.addLeader(target);
				gang.removeOfficer(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.LEADER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.LEADER));
				messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.LEADER));
				return;
			}else if(gang.getLeaders().contains(target.getName())){
				String oldOwner = gang.getOwner();
				gang.setOwner(target);
				gang.addLeader(Bukkit.getPlayerExact(oldOwner));
				gang.removeOfficer(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OWNER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OWNER));
				messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OWNER));
				return;
			}
		}
		if(gang.getMembers().contains(sender.getName())){
			sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_PROMOTE.toString(sender, target, gang, Ranks.MEMBER));
			return;
		}else{
			sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
			return;
		}
	}

	/**
	 * Messages all players in a gang the specified message
	 * 
	 * @throws NPE if the gang doesn't exist
	 * @param g - The gang to message
	 * @param message - The message to send
	 */
	public void messageGang(Gang g, String message){
		for(String s:g.getAllPlayersUUID()){
			Player p = Bukkit.getPlayer(UUID.fromString(s));
			if(!p.isOnline()) continue;
			p.sendMessage(message);
		}
	}

	/**
	 * Demote the specified player in the specified gang
	 * 
	 * @throws NPE if the player isn't in the specified gang and/or it doesn't exist
	 * @param sender - The player that sent the command
	 * @param target - The player getting promoted
	 * @param gang - The gang of the players
	 */
	@SuppressWarnings("deprecation")
	public void demotePlayer(Player sender, Player target, Gang gang){
		if(gang.getOfficers().contains(sender.getName())){
			if(gang.getMembers().contains(target.getName())){
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE_ANYLOWER.toString(sender, target, gang, Ranks.MEMBER));
				return;
			}else if(gang.getTrusted().contains(target.getName())){
				gang.removeTrusted(target);
				gang.addMember(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
				messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
				return;
			}else{
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
				return;
			}
		}if(gang.getLeaders().contains(sender.getName())){
			if(gang.getOfficers().contains(target.getName())){
				gang.removeOfficer(target);
				gang.addTrusted(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				return;
			}else if(gang.getTrusted().contains(target.getName())){
				gang.removeTrusted(target);
				gang.addMember(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
				messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
				return;
			}else if(gang.getMembers().contains(target.getName())){
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE_ANYLOWER.toString(sender, target, gang, Ranks.MEMBER));
				return;
			}else{
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE.toString(sender, target, gang, Ranks.LEADER));
				return;
			}
		}if(gang.getOwner().equals(sender.getName())){
			if(gang.getLeaders().contains(target.getName())){
				gang.removeLeader(target);
				gang.addOfficer(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
				messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
				return;
			}else if(gang.getOfficers().contains(target.getName())){
				gang.removeOfficer(target);
				gang.addTrusted(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				return;
			}else if(gang.getTrusted().contains(target.getName())){
				gang.removeTrusted(target);
				gang.addMember(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
				messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
				return;
			}else if(gang.getMembers().contains(target.getName())){
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE_ANYLOWER.toString(sender, target, gang, Ranks.MEMBER));
				return;
			}else{
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE.toString(sender, target, gang, Ranks.LEADER));
				return;
			}
		}if(sender.hasPermission("gang.admin") || sender.isOp()){
			if(gang.getOwner().equals(target.getName())){
				String oldOwner = gang.getOwner();
				gang.setOwner(sender);
				gang.addLeader(Bukkit.getPlayerExact(oldOwner));
				target.sendMessage(ChatColor.GREEN + "You are now the new owner of the gang.");
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.LEADER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.LEADER));
				messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.LEADER));
				return;
			}else if(gang.getOfficers().contains(target.getName())){
				gang.removeLeader(target);
				gang.addOfficer(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
				messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
				return;
			}else if(gang.getOfficers().contains(target.getName())){
				gang.removeOfficer(target);
				gang.addTrusted(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
				return;
			}else if(gang.getTrusted().contains(target.getName())){
				gang.removeTrusted(target);
				gang.addMember(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
				messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
				return;
			}else if(gang.getMembers().contains(target.getName())){
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE_ANYLOWER.toString(sender, target, gang, Ranks.MEMBER));
				return;
			}else{
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE.toString(sender, target, gang, Ranks.LEADER));
				return;
			}
		}
		if(gang.getMembers().contains(sender.getName())){
			sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
			return;
		}else{
			sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
			return;
		}
	}

	/**
	 * Kicks the specified player in the specified gang
	 * 
	 * @throws NPE if the player isn't in the specified gang and/or it doesn't exist
	 * @param sender - The player that sent the command
	 * @param target - The player getting promoted
	 * @param gang - The gang of the players
	 */
	@SuppressWarnings("deprecation") //getPlayerExact is deprecated due to UUID's
	public void kickPlayer(Player sender, Player target, Gang gang){ 
		if(gang.getOfficers().contains(sender.getName())){
			if(gang.getMembers().contains(target.getName()) || gang.getTrusted().contains(target.getName())){
				Ranks r = getPlayerRank(target.getName(), gang);
				if(r == Ranks.MEMBER){
					gang.removeMember(target);
					target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
					sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
					messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
					return;
				}else if(r == Ranks.TRUSTED){
					gang.removeTrusted(target);
					target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
					sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
					messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
					return;
				}
			}else{
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_KICK.toString(sender, target, gang, getPlayerRank(target.getName(), gang)));
				return;
			}
		}if(gang.getLeaders().contains(sender.getName())){
			if(!gang.getLeaders().contains(target.getName()) || !(gang.getOwner().equals(target.getName()))){
				Ranks r = getPlayerRank(target.getName(), gang);
				if(r == Ranks.MEMBER){
					gang.removeMember(target);
					target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
					sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
					messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
					return;
				}else if(r == Ranks.TRUSTED){
					gang.removeTrusted(target);
					target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
					sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
					messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
					return;
				}else if(r == Ranks.OFFICER){
					gang.removeOfficer(target);
					target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
					sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
					messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
					return;
				}
			}else{
				sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_KICK.toString(sender, target, gang, getPlayerRank(target.getName(), gang)));
				return;
			}
		}if(gang.getOwner() == sender.getName()){
			Ranks r = getPlayerRank(target.getName(), gang);
			if(r == Ranks.MEMBER){
				gang.removeMember(target);
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
				messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
				return;
			}else if(r == Ranks.TRUSTED){
				gang.removeTrusted(target);
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
				messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
				return;
			}else if(r == Ranks.OFFICER){
				gang.removeOfficer(target);
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
				messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
				return;
			}else if(r == Ranks.LEADER){
				gang.removeLeader(target);
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.LEADER));
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.LEADER));
				messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.LEADER));
				return;
			}
		}if(sender.hasPermission("gangs.admin") || sender.isOp()){
			Ranks r = getPlayerRank(target.getName(), gang);
			if(r == Ranks.MEMBER){
				gang.removeMember(target);
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
				messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
				return;
			}else if(r == Ranks.TRUSTED){
				gang.removeTrusted(target);
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
				messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
				return;
			}else if(r == Ranks.OFFICER){
				gang.removeOfficer(target);
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
				messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
				return;
			}else if(r == Ranks.LEADER){
				gang.removeLeader(target);
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.LEADER));
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.LEADER));
				messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.LEADER));
				return;
			}else if(r == Ranks.OWNER){
				Ranks rr = getPlayerRank(sender.getName(), gang);
				String oldOwner = gang.getOwner();
				gang.setOwner(sender);
				gang.addLeader(Bukkit.getPlayerExact(oldOwner));
				gang.removeLeader(target);
				if(rr == Ranks.MEMBER){
					gang.removeMember(sender);
				}else if(rr == Ranks.TRUSTED){
					gang.removeTrusted(sender);
				}else if(rr == Ranks.OFFICER){
					gang.removeOfficer(sender);
				}else if(rr == Ranks.LEADER){
					gang.removeLeader(sender);
				}
				target.sendMessage(ChatColor.GREEN + "You are now the new owner of the gang.");
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.OWNER));
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.OWNER));
				messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.OWNER));
				return;
			}
		}
			
	}

	/**
	 * Leaves the specified gang
	 * 
	 * @throws NPE if the player isn't in the specified gang and/or it doesn't exist
	 * @param p - The player that is leaving
	 * @param gang - The gang of the player leaving
	 */
	public void leave(Player p, Gang g){
		if(g.getMembers().contains(p.getName())){
			messageGang(g, Lang.SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
			p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
			g.removeMember(p);
			return;
		}else if(g.getTrusted().contains(p.getName())){
			messageGang(g, Lang.SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
			p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
			g.removeTrusted(p);
			return;
		}else if(g.getOfficers().contains(p.getName())){
			messageGang(g, Lang.SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
			p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
			g.removeTrusted(p);
			return;
		}else if(g.getLeaders().contains(p.getName())){
			messageGang(g, Lang.SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
			p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
			g.removeTrusted(p);
			return;
		}else if(g.getOwner().equals(p.getName())){
			messageGang(g, Lang.DISBAND_ABSENCE.toString(p, getPlayerRank(p.getName(), g), g));
			p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
			removeGang(g);
			return;
		}
	}
	
	/**
	 * Invites the specified player to the specified gang
	 * 
	 * @throws NPE if the player isn't in the specified gang and/or it doesn't exist
	 * @param sender - The player that sent the command
	 * @param target - The player getting invited
	 * @param gang - The gang of the players
	 */
	public void invitePlayer(Player sender, Player target, Gang gang){
		if(gang.getMembers().contains(sender.getName())){
			sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_INVITE.toString(sender, target, gang, getPlayerRank(target.getName(), gang)));
			return;
		}else{
			if(invited.containsKey(target.getName())){
				sender.sendMessage(Lang.PREFIX.toString() + Lang.ALREADY_INVITED.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
				return;
			}
			invited.put(target.getName(), gang);
			f.getGangConfig().set("invited-players", target.getName());
			f.getGangConfig().set("invited-players." + target.getName(), gang.getName());
			f.saveGangConfig();
			sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_INVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
			target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_INVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
			messageGang(gang, Lang.SUCCESS_INVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
			return;
		}
	}
	
	/**
	 * Checks if the specified player is invited to a gang
	 * 
	 * @param p - The player you want to check if he is invited
	 * @return TRUE if he is invited to a gang, FALSE otherwise
	 */
	public boolean isInvited(Player p){
		if(invited.containsKey(p.getName())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Removes the specified players invitations
	 * 
	 * @param p - The player you want to remove his invitations
	 */
	public void removeInvitation(Player p){
		if(invited.containsKey(p.getName())){
			invited.remove(p.getName());
			f.getGangConfig().set("invited-players." + p.getName(), null);
			f.saveGangConfig();
		}
	}
	
	/**
	 * Uninvites the specifed player from the specified gang
	 * 
	 * @param sender - The player that sent the command
	 * @param target - The player to be uninvited
	 * @param gang - The gang to uninvite the player from
	 */
	public void uninvitePlayer(Player sender, Player target, Gang gang){
		if(gang.getMembers().contains(sender.getName())){
			sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_INVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
			return;
		}else{
			if(isInvited(target)){
				removeInvitation(target);
				sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_UNINVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
				target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_UNINVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
				messageGang(gang, Lang.SUCCESS_UNINVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
				return;
			}else{
				sender.sendMessage(Lang.PREFIX.toString() + Lang.NEVER_INVITED.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
				return;
			}
		}
	}
	
	/**
	 * Loads invited on server startup... don't touch this method
	 */
	public void loadInvites(){
		invited.clear();
		if(f.getGangConfig().getConfigurationSection("invited-players") != null){
			for(String s:f.getGangConfig().getConfigurationSection("invited-players").getKeys(false)){
				if(getGangByName(f.getGangConfig().getString("invited-players." + s)) != null){
					invited.put(s, getGangByName(f.getGangConfig().getString("invited-players." + s)));
				}
			}
		}
	}
	
	//Not functional
	public void allyGang(Player sender, Gang toAlly, Gang requestingToAlly){
		if(requestingToAlly.getMembers().contains(sender.getName()) || requestingToAlly.getTrusted().contains(sender.getName())){
			
		}
	}
	
	/**
	 * Checks if the specified gang name matches the players invite
	 * 
	 * @param p - The player that has been invited
	 * @param gangName - The gang name to check
	 * @return TRUE if the names match, FALSE otherwise
	 */
	public boolean gangsMatchInvited(Player p, String gangName){
		if(invited.containsKey(p.getName())){
			if(invited.get(p.getName()) == getGangByName(gangName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Creates a gang
	 * 
	 * @param owner - Owner of the gang (CommandSender)
	 * @param name - Name of the gang
	 */
	public void createGang(Player owner, String name){
		f.getGangConfig().set("gangs." + name + ".members", new ArrayList<String>());
		f.getGangConfig().set("gangs." + name + ".trusted", new ArrayList<String>());
		f.getGangConfig().set("gangs." + name + ".officers", new ArrayList<String>());
		f.getGangConfig().set("gangs." + name + ".leaders", new ArrayList<String>());
		f.getGangConfig().set("gangs." + name + ".owner", owner.getUniqueId().toString());
		List<String> gangs = f.getGangConfig().getStringList("gang-names");
		gangs.add(name);
		f.getGangConfig().set("gang-names", gangs);
		f.saveGangConfig();
		Gang g = new Gang(name);
		owner.sendMessage(Lang.PREFIX.toString() + Lang.SUCCESSFULLY_CREATED_GANG.toString(owner, Ranks.OWNER, g));
	}

	/**
	 * Removes a gang
	 * 
	 * @param g - The gang to remove
	 */
	public void removeGang(Gang g){
		f.getGangConfig().set("gangs." + g.getName(), null);
		List<String> gangs = f.getGangConfig().getStringList("gang-names");
		gangs.remove(g.getName());
		f.getGangConfig().set("gang-names", gangs);
		f.saveGangConfig();
		Gang.removeGang(g);
	}

	/**
	 * Disbands a gang (Used in CommandHandler)
	 * 
	 * @param p - The player sending the command
	 * @param name - The name of the gang
	 */
	public void disbandGang(Player p, String name){
		Gang g = getGangByName(name);
		if(g.getOwner() == p.getName()){
			String gname = g.getName();
			Ranks r = getPlayerRank(p.getName(), g);
			f.getGangConfig().set("gangs." + g.getName(), null);
			List<String> gangs = f.getGangConfig().getStringList("gang-names");
			gangs.remove(g.getName());
			f.getGangConfig().set("gang-names", gangs);
			f.saveGangConfig();
			Gang.removeGang(g);
			messageGang(g, Lang.SUCCESS_DISBAND.toString(p, r, gname));
			p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DISBAND.toString(p, r, gname));
			return;
		}else{
			p.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_DISBAND.toString(p, getPlayerRank(p.getName(), getGangByName(name)), getGangByName(name)));
			return;
		}
	}
	
	/**
	 * Get's the gang KDR for use in /g or /g info
	 * 
	 * @throws NPE for reason unknown (Game breaking)
	 * @param g - Gang to get the kdr
	 * @return The gangs total kdr
	 */
	public double getGangKDR(Gang g){
		int totalKills = 0;
		int totalDeaths = 0;
		double KDR = 0;
		for(String s:g.getAllPlayersUUID()){
			UUID uuid = UUID.fromString(s);
			Player p = Bukkit.getPlayer(uuid);
			if(f.getKdrConfig().contains("players." + p.getUniqueId().toString())){
				totalKills += f.getKdrConfig().getInt("players." + p.getUniqueId().toString() + ".kills");
				totalDeaths += f.getKdrConfig().getInt("players." + p.getUniqueId().toString() + ".deaths");
			}
		}
		if(totalDeaths == 0){
			KDR = totalKills;
			return KDR;
		}else{
			KDR = totalKills/totalDeaths;
			return KDR;
		}
	}
	
	 /**
	  * Get's a gangs total kills (For use in the getGangKDR method)
	  * 
	  * @param g - Gang to get the kills 
	  * @return The specified gangs total kills
	  */
	public int getGangKills(Gang g){
		int totalKills = 0;
		for(String s:g.getAllPlayersUUID()){
			UUID uuid = UUID.fromString(s);
			Player p = Bukkit.getPlayer(uuid);
			totalKills += f.getKdrConfig().getInt("players." + p.getUniqueId().toString() + ".kills");
		}
		return totalKills;
	}
	
	 /**
	  * Get's a gangs total deaths (For use in the getGangKDR method)
	  * 
	  * @param g - Gang to get the deaths
	  * @return The specified gangs total deaths
	  */
	public int getGangDeaths(Gang g){
		int totalDeaths = 0;
		for(String s:g.getAllPlayersUUID()){
			UUID uuid = UUID.fromString(s);
			Player p = Bukkit.getPlayer(uuid);
			totalDeaths += f.getKdrConfig().getInt("players." + p.getUniqueId().toString() + ".deaths");
		}
		return totalDeaths;
	}
	
	/**
	 * Checks if a player is in ally or gang chat
	 * 
	 * @param p - The player to check
	 * @return TRUE if the player is in a gang chat, otherwise FALSE
	 */
	public boolean isInGangChat(Player p){ //Ally chat is not yet
		if(gangChat.containsKey(p.getName())){
			return true;
		}
		return false;
	}
	
	/**
	 * Adds a player to the gang chat hashmap
	 * 
	 * @param p - The player to be added
	 * @param g - The players gang
	 */
	public void addToGangChat(Player p, Gang g){
		if(!gangChat.containsKey(p.getName())){
			gangChat.put(p.getName(), g);
		}
	}
	
	/**
	 * Removes a player from the gang chat hashmap
	 * 
	 * @param p - The player to be removed
	 * @param g - The players gang
	 */
	public void removeFromGangChat(Player p, Gang g){
		if(gangChat.containsKey(p.getName())){
			gangChat.remove(p.getName());
		}
	}
}
