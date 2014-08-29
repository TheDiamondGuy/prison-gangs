package com.mydeblob.prisongangs;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public enum Lang {
	PREFIX("prefix", "&7[&2PrisonGangs&7]"),
	TRUNCATED_PREFIX("prefix", "&7[&2PrisonGangs&7]"),
	WRONG_COMMAND("wrong-command", "&cIncorrect command syntax! Type /gang help for help with commands!"),
	CHAR_LIMIT("char-limit", "&cError: The gang name is to long!"),
	BLOCKED_NAME("blocked-name", "&cError: The gang name has a blocked word and/or character in it!"),
	PLAYER_NOT_FOUND("player-not-found", "&cError! The specified player wasn't found! Are you sure you spelled his name correctly?"),
	PLAYER_NOT_ONLINE("player-not-online", "&cThe specified player isn't online!"),
	NO_PERMS("no-perms", "&cYou do not have permission to use this command!"),
	GANG_NOT_FOUND("gang-not-found", "&cGang not found! Are you sure you typed the gang name correctly?"),
	NOT_IN_GANG("not-in-gang", "&cYou are not in a gang! To get information from another gang type /gang info <gangName>. To get help with gangs, type /gang help"),
	IN_GANG("in-gang", "&cYou are already in a gang! You must leave your current gang with /gang leave!"),
	GANG_EXISTS("gang-exists", "&cThat gang already exists!"),
	SWITCH_TO_GANG_CHAT("switch-to-gang-chat", "&aYou successfully switched to gang chat!"),
	SWITCH_TO_PUBLIC_CHAT("switch-to-public-chat", "&aYou successfully switch to public chat!"),
	TARGET_NOT_IN_GANG("target-not-in-gang", "&c%t% is not in a gang!"),
	TARGET_NOT_IN_YOUR_GANG("target-not-in-your-gang", "&c%t% is not in your gang!"),
	SUCCESSFULLY_CREATED_GANG("created-gang", "&aSuccessfully created a gang with the name of %g%!"), 
	CANT_INVITE_YOURSELF("cant-invite-yourself", "&cYou can't invite yourself!"),
	SENDER_SUCCESS_INVITE("sender-success-invite", "&aSuccessfully invited %t% to the gang!"),
	TARGET_SUCCESS_INVITE("target-success-invite", "&aYou have been invited to the gang %g% by %s%! Type /gang join %g% to join!"),
	SUCCESS_INVITE("success-invite", "&c%s% &9has invited &c%t% &9to the gang!"), 
	ALREADY_INVITED("already-invited", "&cYou have already invited %t% to the gang!"),
	NO_PERMS_INVITE("no-perms-invite", "&cYou do not have permission to invite people! (You must be Trusted+)"),
	CANT_UNINVITE_YOURSELF("cant-uninvite-yourself", "&cYou can't uninvite yourself!"),
	SENDER_SUCCESS_UNINVITE("sender-success-uninvite", "&aSuccessfully uninvited %t% from your gang!"),
	TARGET_SUCCESS_UNINVITE("target-success-uninvite", "&cYou have been uninvited from %g%!"),
	SUCCESS_UNINVITE("success-uninvite", "&c%s% &9has just uninvited &c%t%!"),
	NEVER_INVITED("never-invited", "&c%t% was never invited to your gang!"),
	NO_PERMS_UNINVITE("no-perms-uninvite", "&cYou do not have permission to un-invite people! (You must be Trusted+)"),
	TARGET_SUCCESS_JOIN("player-success-join", "&aSuccessfully joined the gang %g%!"),
	SUCCESS_JOIN("success-join", "&c%s% &9has joined the gang!"),
	NOT_INVITED("not-invited", "&cYou have not been invited to the gang %g%!"),
	SENDER_SUCCESS_LEFT("player-success-left", "&aYou have Successfully left the gang %g%!"),
	SUCCESS_LEFT("success-left", "&c%s% &9has just left the gang!"),
	DISBAND_ABSENCE("disband-of-absence", "&cThe gang had been disbanded due to the absence of an owner!"),
	CANT_KICK_YOURSELF("cant-kick-yourself", "&cYou can't kick yourself! Type /leave to leave the gang!"),
	SENDER_SUCCESS_KICK("sender-success-kick", "&aSuccessfully kicked %t% from the gang!"),
	TARGET_SUCCESS_KICK("target-success-kick", "&cYou have been kicked from the gang %g% by %s%"), 
	SUCCESS_KICK("success-kick", "&c%t% &9has just been kicked from the gang!"),
	CANT_KICK("cant-kick", "&cYou can't kick players higher or equal to you in rank!"),
	NO_PERMS_KICK("no-perms-kick", "&cYou do not have permission to kick people! (You must be a Officer+)"),
	CANT_PROMOTE_YOURSELF("cant-promote-yourself", "&cYou can't promote yourself!"),
	SENDER_SUCCESS_PROMOTE("sender-success-promote", "&aYou Successfully promoted %t% to a %r%!"), 
	TARGET_SUCCESS_PROMOTE("target-success-promote", "&aYou have been promoted to %r%!"),
	SUCCESS_PROMOTE("success-promote", "&c%t% &9has just been promoted to &c%r%!"),
	CANT_PROMOTE("cant-promote", "&cYou can't promote players higher or equal to you in rank!"),
	CANT_PROMOTE_OWNER("cant-promote-owner", "&cYou can't promote people past leader! To pass over gang ownership type /gang setowner <Player Name>!"),
	NO_PERMS_PROMOTE("no-perms-promote", "&cYou do not have permission to promote people! (You must be a Officer+)"),
	CANT_DEMOTE_YOURSELF("cant-demote-yourself", "&cYou can't demote yourself!"),
	SENDER_SUCCESS_DEMOTE("sender-success-demote", "&aYou Successfully demoted %t% to %r%!"),
	TARGET_SUCCESS_DEMOTE("target-success-demote", "&cYou have just been demoted to %r% by &9%s%"),
	SUCCESS_DEMOTE("success-demote", "&c%t% &9has just been demoted to &c%r%!"),
	CANT_DEMOTE("cant-demote", "&cYou can't demote a higher ranked player than you!"),
	CANT_DEMOTE_ANYLOWER("cant-demote-anylower", "&cYou can't demote %t% any lower then he already is (Member)!"),
	NO_PERMS_DEMOTE("no-perms-demote", "&cYou do not have permission to demote people! (You must be a Officer+)"),
	CANT_TRANSFER_OWNERSHIP_TO_YOURSELF("cant-transfer-ownership-to-yourself", "&cYou can't transfer ownership to yourself!"),
	SENDER_SUCCESS_SET_OWNERSHIP("sender-success-set-ownership", "&aYou have successfully transfered the gang ownership to %t%!"),
	TARGET_SUCCESS_SET_OWNERSHIP("target-success-set-ownership", "&a%s% has just made you owner of the gang!"),
	SUCCESS_SET_OWNERSHIP("success-set-ownership", "&c%s% &9just transfered gang ownership to &c%t%!"),
	NO_PERMS_SET_OWNERSHIP("no-perms-set-ownership", "&cYou do not have permission to transfer gang ownership! (You must be the Owner!)"),
	SENDER_SUCCESS_DISBAND("sender-success-disband", "&aSuccessfully disbanded the gang!"),
	SUCCESS_DISBAND("success-disband", "&cThe gang has been disbanded!"),
	NO_PERMS_DISBAND("no-perms-disband", "&cOnly gang owners can disband the gang!");
	

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
	public String toString() {
		if (this == PREFIX){
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)) + " ";
		}else{
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def));
		}
	}
	public String toString(Player sender) {
		if (this == PREFIX){
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)) + " ";
		}else{
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)).replaceAll("%s%", sender.getName());
		}
	}
	public String toString(Player sender, Player target){
		if (this == PREFIX){
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)) + " ";
		}else{
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)).replaceAll("%s%", sender.getName()).replaceAll("%t%", target.getName());
		}
	}
	public String toString(Player sender, Rank r, Gang g) {
		if (this == PREFIX){
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)) + " ";
		}else{
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)).replaceAll("%s%", sender.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", r.toText());
		}
	}
	public String toString(Player sender, Rank r, String gName) {
		if (this == PREFIX){
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)) + " ";
		}else{
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)).replaceAll("%s%", sender.getName()).replaceAll("%g%", gName).replaceAll("%r%", r.toText());
		}
	}
	public String toString(Player sender, Gang g) {
		if (this == PREFIX){
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)) + " ";
		}else{
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)).replaceAll("%s%", sender.getName()).replaceAll("%g%", g.getName());
		}
	}
	public String toString(Player sender, Player target, Gang g){
		if (this == PREFIX){
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)) + " ";
		}else{
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)).replaceAll("%s%", sender.getName()).replaceAll("%t%", target.getName()).replaceAll("%g%", g.getName());
		}
	}
	public String toString(Player sender, Player target, Gang g, Rank r){
		if (this == PREFIX){
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)) + " ";
		}else{
			return ChatColor.translateAlternateColorCodes('&', lang.getString(this.path, def)).replaceAll("%s%", sender.getName()).replaceAll("%t%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", r.toText());
		}
	}

}

