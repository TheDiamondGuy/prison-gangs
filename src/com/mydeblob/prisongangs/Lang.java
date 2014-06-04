package com.mydeblob.prisongangs;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
	PREFIX("prefix", "&7[&2PrisonGangs&7]"),
	TRUNCATED_PREFIX("prefix", "&7[&2PrisonGangs&7]"),
	WRONG_COMMAND("wrong-command", "&cIncorrect command syntax! Type /gang help for help with commands!"),
	NO_PERMS("no-permission", "&cYou do not have permission to use this command!"),
	IN_GANG("in-gang", "&cYou are already in a gang! You must leave your current gang with /gang leave!"),
	PLAYER_NOT_FOUND("player-not-found", "&cError! The specified player wasn't found! Are you sure you spelled his name correctly?"),
	GANG_NOT_FOUND("gang-not-found", "&cGang not found! Are you sure you typed the gang name correctly?"),
	CHAR_LIMIT("char-limit", "&cError: The gang name is to long!"),
	NOT_IN_GANG("not-in-gang", "&cYou are not in a gang! To get information from another gang type /gang info <gangName>"),
	GANG_EXISTS("gang-exists", "&cThat gang already exists!"),
	SUCCESFULLY_CREATED_GANG("created-gang", "&aSuccesfully created a gang!"), 
	PLAYER_NOT_ONLINE("player-not-online", "&cThe specified player isn't online!"),
	SENDER_SUCCESS_PROMOTE("sender-success-promote", "&aYou succesfully promoted %p% to a %r%!"), //%p% = player; %r% = rank
	TARGET_SUCCESS_PROMOTE("target-success-promote", "&aYou have been promoted to %r%!"),
	ALREADY_LEADER("already-leader", "&cThat player is already a leader!"),
	NO_PERMS_PROMOTE("no-perms-promote", "&cYou do not have permisison to promote people! (You must be a Officer+)"),
	NO_PERMS_DEMOTE("no-perms-demote", "&cYou do not have permission to demote people! (You must be a Officer+"),
	NO_PERMS_KICK("no-perms-kick", "&cYou do not have permission to kick people! (You must be a Officer+)"),
	CANT_DEMOTE("cant-demote", "&cYou can't demote a higher ranked player than you!"),
	CANT_PROMOTE("cant-promote", "&cYou can't promote players higher or equal to you in rank!"),
	CANT_PROMOTE_OWNER("cant-promote-owner", "&cYou can't promote people past leader! To pass over gang ownership type /gang setowner <Player Name>!"),
	CANT_KICK("cant-kick", "&cYou can't kick players higher or equal to you in rank!"),
	SENDER_SUCCESS_KICK("sender-success-kick", "&aSuccesfully kicked %p% from the gang!"),
	TARGET_SUCCESS_KICK("target-success-kick", "&cYou have been kicked from the gang %g% by %p%"), //%g% is replaced with gang name
	SUCCESS_KICK("success-kick", "&c%p% &9has just been kicked from the gang!"),
	SUCCESS_PROMOTE("success-kick", "&c%p% &9has just been promoted to %r%!"),
	SUCCESS_DEMOTE("success-kick", "&c%p% &9has just been demoted to %p%!"),
	SENDER_SUCCESS_LEFT("sender-success-left", "&aYou have succesfully left the gang %g%!"),
	SUCCESS_LEFT("success-left", "&c%p% &9has just left the gang!"),
	DISBAND_ABSENCE("disband-of-absence", "&cThe gang had been disbanded due to absence of an owner!"),
	NO_PERMS_INVITE("no-perms-invite", "You do not have permission to invite people! (You must be Trusted+)"),
	SENDER_SUCCESS_INVITE("sender-success-invite", "&aSuccesfully invited %p% to the gang!"),
	TARGET_SUCCESS_INVITE("target-success-invite", "&aYou have been invited to the gang %g% by %p%! Type /gang join %g% to join!"),
	SUCCESS_INVITE("success-invite", "&c%s% &9has invited %p% to the gang!"), //%s% = sender/invited %p% = person invited
	NOT_INVITED("not-invited", "&cYou have not been invited to the gang %g%!"),
	TARGET_SUCCESS_JOIN("target-success-join", "&aSuccesfully joined the gang %g%!"),
	SUCCESS_JOIN("success-join", "&c%p% &9has joined the gang!"),
	NO_PERMS_UNINVITE("no-perms-uninvite", "&cYou do not have permission to un-invite people! (You must be Trusted+)"),
	NEVER_INVITED("never-invited", "&c%p% was never invited to your gang!"),
	SENDER_SUCCESS_UNINVITE("sender-success-uninvite", "&aSuccesfully uninvited %p% from your gang!"),
	SUCCESS_UNINVITE("success-uninvite", "&c%s% has just uninvited %p%!"),
	SUCCESS_DISBAND("success-disband", "&cThe gang has been disbanded!"),
	SENDER_SUCCESS_DISBAND("sender-success-disband", "&aSuccesfully disbanded the gang!"),
	CANT_DISBAND("cant-disband", "&cOnly gang owners can disband the gang!");
	

	private String path;
	private String def;
	private static YamlConfiguration lang;
	Lang(String path, String def) {
		this.path = path;
		this.def = def;
	}

	public static void setFile(YamlConfiguration langConfig) {
		lang = langConfig;
	}

	public String translate() {
		if (this == PREFIX){
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)) + " ";
		}else{
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def));
		}
	}


}

