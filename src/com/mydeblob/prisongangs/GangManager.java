package com.cullan.prisongangs;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class GangManager
{
  private static GangManager instance = new GangManager();
  private static HashMap<String, Gang> invited = new HashMap<String, Gang>();
  private static HashMap<Gang, Gang> allied = new HashMap<Gang, Gang>();
  private static HashMap<String, Gang> gangChat = new HashMap<String, Gang>();
  private FileManager f = FileManager.getFileManager();
  
  public static GangManager getGangManager()
  {
    return instance;
  }
  
  public HashMap<String, Gang> getInvited()
  {
    return invited;
  }
  
  public HashMap<Gang, Gang> getAllied()
  {
    return allied;
  }
  
  public Gang getGangByName(String name)
  {
    for (Gang g : Gang.getGangs()) {
      if (g.getName().equalsIgnoreCase(name)) {
        return g;
      }
    }
    return null;
  }
  
  public boolean isInGang(Player p)
  {
    if (getGangWithPlayer(p) == null) {
      return false;
    }
    return true;
  }
  
  public Gang getGangWithPlayer(Player p)
  {
    for (Gang g : Gang.getGangs()) {
      if (g.getAllPlayers().contains(p.getName())) {
        return g;
      }
    }
    return null;
  }
  
  public Gang getGangWithOfflinePlayer(OfflinePlayer p)
  {
    for (Gang g : Gang.getGangs()) {
      if (g.getAllPlayers().contains(p.getName())) {
        return g;
      }
    }
    return null;
  }
  
  public Ranks getPlayerRank(String playerName, Gang g)
  {
    if ((!g.getMembers().isEmpty()) && 
      (g.getMembers().contains(playerName))) {
      return Ranks.MEMBER;
    }
    if ((!g.getTrusted().isEmpty()) && 
      (g.getTrusted().contains(playerName))) {
      return Ranks.TRUSTED;
    }
    if ((!g.getOfficers().isEmpty()) && 
      (g.getOfficers().contains(playerName))) {
      return Ranks.OFFICER;
    }
    if ((!g.getLeaders().isEmpty()) && 
      (g.getLeaders().contains(playerName))) {
      return Ranks.LEADER;
    }
    if (g.getOwner().equals(playerName)) {
      return Ranks.OWNER;
    }
    return null;
  }
  
  public void promotePlayer(Player sender, Player target, Gang gang)
  {
    if ((sender.getName() == target.getName()) && ((!sender.hasPermission("gangs.admin")) || (!sender.isOp()))) {
      sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_PROMOTE_YOURSELF.toString(sender, target, gang));
    }
    if (gang.getOfficers().contains(sender.getName()))
    {
      if (gang.getMembers().contains(target.getName()))
      {
        gang.addTrusted(target);
        gang.removeMember(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        return;
      }
      sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
      return;
    }
    if (gang.getLeaders().contains(sender.getName()))
    {
      if (gang.getMembers().contains(target.getName()))
      {
        gang.addTrusted(target);
        gang.removeMember(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        return;
      }
      if (gang.getTrusted().contains(target.getName()))
      {
        gang.addOfficer(target);
        gang.removeTrusted(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
        messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
        return;
      }
      sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
      return;
    }
    if (gang.getOwner().equals(sender.getName()))
    {
      if (gang.getMembers().contains(target.getName()))
      {
        gang.addTrusted(target);
        gang.removeMember(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        return;
      }
      if (gang.getTrusted().contains(target.getName()))
      {
        gang.addOfficer(target);
        gang.removeTrusted(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
        messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
        return;
      }
      if (gang.getOfficers().contains(target.getName()))
      {
        gang.addLeader(target);
        gang.removeOfficer(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.LEADER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.LEADER));
        messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.LEADER));
        return;
      }
      sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_PROMOTE_OWNER.toString(sender, target, gang, Ranks.LEADER));
      return;
    }
    if ((sender.hasPermission("gang.admin")) || (sender.isOp()))
    {
      if (gang.getMembers().contains(target.getName()))
      {
        gang.addTrusted(target);
        gang.removeMember(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        return;
      }
      if (gang.getTrusted().contains(target.getName()))
      {
        gang.addOfficer(target);
        gang.removeTrusted(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
        messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OFFICER));
        return;
      }
      if (gang.getOfficers().contains(target.getName()))
      {
        gang.addLeader(target);
        gang.removeOfficer(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.LEADER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.LEADER));
        messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.LEADER));
        return;
      }
      if (gang.getLeaders().contains(target.getName()))
      {
        gang.setOwner(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OWNER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OWNER));
        messageGang(gang, Lang.SUCCESS_PROMOTE.toString(sender, target, gang, Ranks.OWNER));
        return;
      }
    }
    if (gang.getMembers().contains(sender.getName()))
    {
      sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_PROMOTE.toString(sender, target, gang, Ranks.MEMBER));
      return;
    }
    sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_PROMOTE.toString(sender, target, gang, Ranks.TRUSTED));
  }
  
  public void messageGang(Gang g, String message)
  {
    for (String s : g.getAllPlayersUUID())
    {
      OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(s));
      if (p.isOnline()) {
        p.getPlayer().sendMessage(message);
      }
    }
  }
  
  public void demotePlayer(Player sender, Player target, Gang gang)
  {
    if (gang.getOfficers().contains(sender.getName()))
    {
      if (gang.getMembers().contains(target.getName()))
      {
        sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE_ANYLOWER.toString(sender, target, gang, Ranks.MEMBER));
        return;
      }
      if (gang.getTrusted().contains(target.getName()))
      {
        gang.removeTrusted(target);
        gang.addMember(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
        messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
        return;
      }
      sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
      return;
    }
    if (gang.getLeaders().contains(sender.getName()))
    {
      if (gang.getOfficers().contains(target.getName()))
      {
        gang.removeOfficer(target);
        gang.addTrusted(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        return;
      }
      if (gang.getTrusted().contains(target.getName()))
      {
        gang.removeTrusted(target);
        gang.addMember(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
        messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
        return;
      }
      if (gang.getMembers().contains(target.getName()))
      {
        sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE_ANYLOWER.toString(sender, target, gang, Ranks.MEMBER));
        return;
      }
      sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE.toString(sender, target, gang, Ranks.LEADER));
      return;
    }
    if (gang.getOwner().equals(sender.getName()))
    {
      if (gang.getLeaders().contains(target.getName()))
      {
        gang.removeLeader(target);
        gang.addOfficer(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
        messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
        return;
      }
      if (gang.getOfficers().contains(target.getName()))
      {
        gang.removeOfficer(target);
        gang.addTrusted(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        return;
      }
      if (gang.getTrusted().contains(target.getName()))
      {
        gang.removeTrusted(target);
        gang.addMember(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
        messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
        return;
      }
      if (gang.getMembers().contains(target.getName()))
      {
        sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE_ANYLOWER.toString(sender, target, gang, Ranks.MEMBER));
        return;
      }
      sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE.toString(sender, target, gang, Ranks.LEADER));
      return;
    }
    if ((sender.hasPermission("gang.admin")) || (sender.isOp()))
    {
      if (gang.getOwner().equals(target.getName()))
      {
        String oldOwner = gang.getOwner();
        gang.setOwner(sender);
        gang.addLeader(Bukkit.getPlayer(oldOwner));
        target.sendMessage(ChatColor.GREEN + "You are now the new owner of the gang.");
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.LEADER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.LEADER));
        messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.LEADER));
        return;
      }
      if (gang.getOfficers().contains(target.getName()))
      {
        gang.removeLeader(target);
        gang.addOfficer(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
        messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.OFFICER));
        return;
      }
      if (gang.getOfficers().contains(target.getName()))
      {
        gang.removeOfficer(target);
        gang.addTrusted(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
        return;
      }
      if (gang.getTrusted().contains(target.getName()))
      {
        gang.removeTrusted(target);
        gang.addMember(target);
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
        messageGang(gang, Lang.SUCCESS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
        return;
      }
      if (gang.getMembers().contains(target.getName()))
      {
        sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE_ANYLOWER.toString(sender, target, gang, Ranks.MEMBER));
        return;
      }
      sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_DEMOTE.toString(sender, target, gang, Ranks.LEADER));
      return;
    }
    if (gang.getMembers().contains(sender.getName()))
    {
      sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_DEMOTE.toString(sender, target, gang, Ranks.MEMBER));
      return;
    }
    sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_DEMOTE.toString(sender, target, gang, Ranks.TRUSTED));
  }
  
  public void kickPlayer(Player sender, Player target, Gang gang)
  {
    if (gang.getOfficers().contains(sender.getName())) {
      if ((gang.getMembers().contains(target.getName())) || (gang.getTrusted().contains(target.getName())))
      {
        Ranks r = getPlayerRank(target.getName(), gang);
        if (r == Ranks.MEMBER)
        {
          gang.removeMember(target);
          target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
          sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
          messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
          return;
        }
        if (r == Ranks.TRUSTED)
        {
          gang.removeTrusted(target);
          target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
          sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
          messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
        }
      }
      else
      {
        sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_KICK.toString(sender, target, gang, getPlayerRank(target.getName(), gang)));
        return;
      }
    }
    if (gang.getLeaders().contains(sender.getName())) {
      if ((!gang.getLeaders().contains(target.getName())) || (!gang.getOwner().equals(target.getName())))
      {
        Ranks r = getPlayerRank(target.getName(), gang);
        if (r == Ranks.MEMBER)
        {
          gang.removeMember(target);
          target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
          sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
          messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
          return;
        }
        if (r == Ranks.TRUSTED)
        {
          gang.removeTrusted(target);
          target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
          sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
          messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
          return;
        }
        if (r == Ranks.OFFICER)
        {
          gang.removeOfficer(target);
          target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
          sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
          messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
        }
      }
      else
      {
        sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_KICK.toString(sender, target, gang, getPlayerRank(target.getName(), gang)));
        return;
      }
    }
    if (gang.getOwner() == sender.getName())
    {
      Ranks r = getPlayerRank(target.getName(), gang);
      if (r == Ranks.MEMBER)
      {
        gang.removeMember(target);
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
        messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
        return;
      }
      if (r == Ranks.TRUSTED)
      {
        gang.removeTrusted(target);
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
        messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
        return;
      }
      if (r == Ranks.OFFICER)
      {
        gang.removeOfficer(target);
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
        messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
        return;
      }
      if (r == Ranks.LEADER)
      {
        gang.removeLeader(target);
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.LEADER));
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.LEADER));
        messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.LEADER));
        return;
      }
    }
    if ((sender.hasPermission("gangs.admin")) || (sender.isOp()))
    {
      Ranks r = getPlayerRank(target.getName(), gang);
      if (r == Ranks.MEMBER)
      {
        gang.removeMember(target);
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
        messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.MEMBER));
        return;
      }
      if (r == Ranks.TRUSTED)
      {
        gang.removeTrusted(target);
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
        messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.TRUSTED));
        return;
      }
      if (r == Ranks.OFFICER)
      {
        gang.removeOfficer(target);
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
        messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.OFFICER));
        return;
      }
      if (r == Ranks.LEADER)
      {
        gang.removeLeader(target);
        target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_KICK.toString(sender, target, gang, Ranks.LEADER));
        sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_KICK.toString(sender, target, gang, Ranks.LEADER));
        messageGang(gang, Lang.SUCCESS_KICK.toString(sender, target, gang, Ranks.LEADER));
        return;
      }
      if (r == Ranks.OWNER)
      {
        Ranks rr = getPlayerRank(sender.getName(), gang);
        gang.setOwner(sender);
        if (rr == Ranks.MEMBER) {
          gang.removeMember(sender);
        } else if (rr == Ranks.TRUSTED) {
          gang.removeTrusted(sender);
        } else if (rr == Ranks.OFFICER) {
          gang.removeOfficer(sender);
        } else if (rr == Ranks.LEADER) {
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
  
  public void leave(Player p, Gang g)
  {
    if (g.getMembers().contains(p.getName())) {
      messageGang(g, Lang.SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
      p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
      g.removeMember(p);
      return;
    }
    if (g.getTrusted().contains(p.getName())) {
      messageGang(g, Lang.SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
      p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
      g.removeTrusted(p);
      return;
    }
    if (g.getOfficers().contains(p.getName())) {
      messageGang(g, Lang.SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
      p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
      g.removeOfficer(p);
      return;
    }
    if (g.getLeaders().contains(p.getName())) {
      messageGang(g, Lang.SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
      p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
      g.removeLeader(p);
      return;
    }
    if (g.getOwner().contains(p.getName())) {
      messageGang(g, Lang.DISBAND_ABSENCE.toString(p, getPlayerRank(p.getName(), g), g));
      p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString(p, getPlayerRank(p.getName(), g), g));
      disbandGang(p, getGangWithPlayer(p).getName());
      return;
    }
  }
  
  public void invitePlayer(Player sender, Player target, Gang gang) {
	    if (gang.getMembers().contains(sender.getName())) {
	      sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_INVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
	      return;
	    }
    if (invited.containsKey(target.getName()))
    {
      sender.sendMessage(Lang.PREFIX.toString() + Lang.ALREADY_INVITED.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
      return;
    }
    invited.put(target.getName(), gang);
    this.f.getGangConfig().set("invited-players", target.getName());
    this.f.getGangConfig().set("invited-players." + target.getName(), gang.getName());
    this.f.saveGangConfig();
    sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_INVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
    target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_INVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
    messageGang(gang, Lang.SUCCESS_INVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
  }
  
  public boolean isInvited(Player p)
  {
    if (invited.containsKey(p.getName())) {
      return true;
    }
    return false;
  }
  
  public void removeInvitation(Player p)
  {
    if (invited.containsKey(p.getName()))
    {
      invited.remove(p.getName());
      this.f.getGangConfig().set("invited-players." + p.getName(), null);
      this.f.saveGangConfig();
    }
  }
  
  public void uninvitePlayer(Player sender, Player target, Gang gang)
  {
    if (gang.getMembers().contains(sender.getName()))
    {
      sender.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_INVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
      return;
    }
    if (isInvited(target))
    {
      removeInvitation(target);
      sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_UNINVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
      target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_UNINVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
      messageGang(gang, Lang.SUCCESS_UNINVITE.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
      return;
    }
    sender.sendMessage(Lang.PREFIX.toString() + Lang.NEVER_INVITED.toString(sender, target, gang, getPlayerRank(sender.getName(), gang)));
  }
  
  public void loadInvites()
  {
    invited.clear();
    if (this.f.getGangConfig().getConfigurationSection("invited-players") != null) {
      for (String s : this.f.getGangConfig().getConfigurationSection("invited-players").getKeys(false)) {
        if (getGangByName(this.f.getGangConfig().getString("invited-players." + s)) != null) {
          invited.put(s, getGangByName(this.f.getGangConfig().getString("invited-players." + s)));
        }
      }
    }
  }
  
  public void allyGang(Player sender, Gang toAlly, Gang requestingToAlly)
  {
    if (!requestingToAlly.getMembers().contains(sender.getName())) {
      requestingToAlly.getTrusted().contains(sender.getName());
    }
  }
  
  public boolean gangsMatchInvited(Player p, String gangName)
  {
    if ((invited.containsKey(p.getName())) && 
      (invited.get(p.getName()) == getGangByName(gangName))) {
      return true;
    }
    return false;
  }
  
  public void createGang(Player owner, String name)
  {
    this.f.getGangConfig().set("gangs." + name + ".members", new HashSet<Object>());
    this.f.getGangConfig().set("gangs." + name + ".trusted", new HashSet<Object>());
    this.f.getGangConfig().set("gangs." + name + ".officers", new HashSet<Object>());
    this.f.getGangConfig().set("gangs." + name + ".leaders", new HashSet<Object>());
    this.f.getGangConfig().set("gangs." + name + ".owner", owner.getUniqueId().toString());
    List<String> gangs = this.f.getGangConfig().getStringList("gang-names");
    gangs.add(name);
    this.f.getGangConfig().set("gang-names", gangs);
    this.f.saveGangConfig();
    Gang g = new Gang(name);
    owner.sendMessage(Lang.PREFIX.toString() + Lang.SUCCESSFULLY_CREATED_GANG.toString(owner, Ranks.OWNER, g));
  }
  
  public void removeGang(Gang g)
  {
    this.f.getKdrConfig().set("gang-names." + g.getName(), null);
    this.f.getGangConfig().set("gangs." + g.getName(), null);
    this.f.getGangConfig().set("gang-names." + g.getName(), null);
    this.f.saveGangConfig();
    this.f.saveKdrConfig();
    Gang.removeGang(g);
  }
  
  public void disbandGang(Player p, String name){
		Gang g = getGangByName(name);
		if(g.getOwner().equalsIgnoreCase(p.getName())){
			String gname = g.getName();
			Ranks r = getPlayerRank(p.getName(), g);
			p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DISBAND.toString(p, r, gname));
			messageGang(g, Lang.SUCCESS_DISBAND.toString(p, r, gname));
			f.getGangConfig().set("gangs." + g.getName(), null);
			List<String> gangs = f.getGangConfig().getStringList("gang-names");
			gangs.remove(g.getName());
			f.getGangConfig().set("gang-names", gangs);
			f.saveGangConfig();
			Gang.removeGang(g);
			return;
		}else{
			p.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS_DISBAND.toString(p, getPlayerRank(p.getName(), getGangByName(name)), getGangByName(name)));
			return;
		}
	}
  
  public void fdisbandGang(Player p, String name) {
	  Gang g = getGangByName(name);
		String gname = g.getName();
		Ranks r = getPlayerRank(p.getName(), g);
		p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DISBAND.toString(p, r, gname));
		messageGang(g, Lang.SUCCESS_DISBAND.toString(p, r, gname));
		f.getGangConfig().set("gangs." + g.getName(), null);
		List<String> gangs = f.getGangConfig().getStringList("gang-names");
		gangs.remove(g.getName());
		f.getGangConfig().set("gang-names", gangs);
		f.saveGangConfig();
		Gang.removeGang(g);
		return;
	  }
  
  public double getGangKDR(Gang g)
  {
    int totalKills = 0;
    int totalDeaths = 0;
    double KDR = 0.0D;
    if (this.f.getKdrConfig().contains("gang-names." + g.getName()))
    {
      totalKills += this.f.getKdrConfig().getInt("gang-names." + g.getName() + ".kills");
      totalDeaths += this.f.getKdrConfig().getInt("gang-names." + g.getName() + ".deaths");
    }
    if (totalDeaths == 0)
    {
      KDR = totalKills;
      return KDR;
    }
    KDR = totalKills / totalDeaths;
    return KDR;
  }
  
  public int getGangKills(Gang g)
  {
    int totalKills = 0;
    totalKills += this.f.getKdrConfig().getInt("gang-names." + g.getName() + ".kills");
    return totalKills;
  }
  
  public int getGangDeaths(Gang g)
  {
    int totalDeaths = 0;
    totalDeaths += this.f.getKdrConfig().getInt("gang-names." + g.getName() + ".deaths");
    return totalDeaths;
  }
  
  public double getPlayerKDR(OfflinePlayer t)
  {
    int totalKills = 0;
    int totalDeaths = 0;
    double KDR = 0.0D;
    if (this.f.getKdrConfig().contains("players." + t.getUniqueId()))
    {
      totalKills += this.f.getKdrConfig().getInt("players." + t.getUniqueId() + ".kills");
      totalDeaths += this.f.getKdrConfig().getInt("players." + t.getUniqueId() + ".deaths");
    }
    if (totalDeaths == 0)
    {
      KDR = totalKills;
      return KDR;
    }
    DecimalFormat df = new DecimalFormat("0.0");
    String kdr = df.format(f.getKdrConfig().get("players." + t.getUniqueId().toString() + ".kdr"));
	return Double.parseDouble(kdr);
  }
  
  public int getPlayerKills(OfflinePlayer t)
  {
    int totalKills = 0;
    totalKills += this.f.getKdrConfig().getInt("players." + t.getUniqueId() + ".kills");
    return totalKills;
  }
  
  public int getPlayerDeaths(OfflinePlayer t)
  {
    int totalDeaths = 0;
    totalDeaths += this.f.getKdrConfig().getInt("players." + t.getUniqueId() + ".deaths");
    return totalDeaths;
  }
  
  public boolean isInGangChat(Player p)
  {
    if (gangChat.containsKey(p.getName())) {
      return true;
    }
    return false;
  }
  
  public void addToGangChat(Player p, Gang g)
  {
    if (!gangChat.containsKey(p.getName())) {
      gangChat.put(p.getName(), g);
    }
  }
  
  public void removeFromGangChat(Player p, Gang g)
  {
    if (gangChat.containsKey(p.getName())) {
      gangChat.remove(p.getName());
    }
  }
}
