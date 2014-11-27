package com.cullan.prisongangs;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandHandler
  implements CommandExecutor, Listener
{
  private PrisonGangs plugin;
  public static final GangManager gm = GangManager.getGangManager();
  public static final FileManager f = FileManager.getFileManager();
  public CommandHandler(PrisonGangs plugin)
  {
    this.plugin = plugin;
  }
  
  private transient boolean Admin = false;

  public void enableBypass(boolean PAdmin) {
        this.Admin = PAdmin;
  }

  public boolean isAdminOn() {
        return Admin;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("kdr"))
    {
      if (!(sender instanceof Player))
      {
        sender.sendMessage("This command may only be executed in game!");
        return true;
      }
      Player p = (Player)sender;
      if ((p.hasPermission("gangs.kdr")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user")))
      {
        if (args.length < 1)
        {
          p.sendMessage(ChatColor.DARK_RED + "=--" + Lang.TRUNCATED_PREFIX.toString() + ChatColor.DARK_RED + "--=");
          p.sendMessage(ChatColor.AQUA + p.getName() + "'s KDR: " + ChatColor.RED + gm.getPlayerKDR(p));
          p.sendMessage(ChatColor.AQUA + p.getName() + "'s Kills: " + ChatColor.RED + gm.getPlayerKills(p));
          p.sendMessage(ChatColor.AQUA + p.getName() + "'s Deaths: " + ChatColor.RED + gm.getPlayerDeaths(p));
          return true;
        }
        if (args.length == 1) {
          OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
          if (!f.getKdrConfig().contains("players." + t.getUniqueId().toString())) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_FOUND.toString(p));
            return true;
          }
          p.sendMessage(ChatColor.DARK_RED + "=--" + Lang.TRUNCATED_PREFIX.toString() + ChatColor.DARK_RED + "--=");
          p.sendMessage(ChatColor.AQUA + t.getName() + "'s KDR: " + ChatColor.RED + gm.getPlayerKDR(t));
          p.sendMessage(ChatColor.AQUA + t.getName() + "'s Kills: " + ChatColor.RED + gm.getPlayerKills(t));
          p.sendMessage(ChatColor.AQUA + t.getName() + "'s Deaths: " + ChatColor.RED + gm.getPlayerDeaths(t));
          return true;
        }
        p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
        return true;
      }
      p.sendMessage(Lang.NO_PERMS.toString(p));
      return true;
    }
    //Console Commands
    if (cmd.getName().equalsIgnoreCase("gang") && sender instanceof ConsoleCommandSender) {
    	if (args[0].equalsIgnoreCase("reload")) {
      	  if (args.length != 1) {
      	  sender.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString());
      	  return true;
      	  } try {
          	f.reloadGangConfig();
          	f.reloadKdrConfig();
          	plugin.getLogger().log(Level.INFO, "Successfully reloaded gang config.");
          	return true;
          	} catch (Exception e) {
          	plugin.getLogger().log(Level.SEVERE, "There was an error trying to reload the config!.");
          	return true;
          	}
          }
    	if (args[0].equalsIgnoreCase("save")) {
        	  if (args.length != 1) {
        	  sender.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString());
        	  return true;
        	  } try {
            	f.saveGangConfig();
            	f.saveKdrConfig();
            	plugin.getLogger().log(Level.INFO, "Successfully saved all gangs.");
            	return true;
            	} catch (Exception e) {
            	plugin.getLogger().log(Level.SEVERE, "There was an error trying to save gangs!.");
            	return true;
            	}
            }
    }
    //Player Commands
    if (cmd.getName().equalsIgnoreCase("gang")) {
    	Player p = (Player)sender;
      if (!(sender instanceof Player)) {
        sender.sendMessage("This command may only be executed in game!");
        return true;
      }
      if (args.length < 1) {
        if ((p.hasPermission("gangs.info")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user"))) {
          if (!gm.isInGang(p)) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
            return true;
          }
          Gang g = gm.getGangWithOfflinePlayer(p);
          p.sendMessage(ChatColor.DARK_RED + "***" + ChatColor.DARK_GREEN + g.getName() + "'s" + ChatColor.BLUE + " Info" + ChatColor.DARK_RED + "***");
          p.sendMessage(ChatColor.AQUA + g.getName() + "'s KDR: " + ChatColor.RED + gm.getGangKDR(g));
          p.sendMessage(ChatColor.AQUA + g.getName() + "'s Kills: " + ChatColor.RED + gm.getGangKills(g));
          p.sendMessage(ChatColor.AQUA + g.getName() + "'s Deaths: " + ChatColor.RED + gm.getGangDeaths(g));
          p.sendMessage(ChatColor.AQUA + g.getName() + "'s Members: " + ChatColor.GOLD + getGangPlayerStats(g));
          return true;
        }
        p.sendMessage(Lang.NO_PERMS.toString(p));
        return true;
      }
      if (args[0].equalsIgnoreCase("info")) {
        if ((p.hasPermission("gangs.info")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user"))) {
          if (args.length == 2) {
            if ((gm.getGangByName(args[1]) == null) && (!f.getGangConfig().getStringList("gang-names.").contains(args[1]))) {
              p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_NOT_FOUND.toString(p));
              return true;
            }
            Gang g = gm.getGangByName(args[1]);
            p.sendMessage(ChatColor.DARK_RED + "***" + ChatColor.DARK_GREEN + g.getName() + "'s" + ChatColor.BLUE + " Info" + ChatColor.DARK_RED + "***");
            p.sendMessage(ChatColor.AQUA + g.getName() + "'s KDR: " + ChatColor.RED + gm.getGangKDR(g));
            p.sendMessage(ChatColor.AQUA + g.getName() + "'s Kills: " + ChatColor.RED + gm.getGangKills(g));
            p.sendMessage(ChatColor.AQUA + g.getName() + "'s Deaths: " + ChatColor.RED + gm.getGangDeaths(g));
            p.sendMessage(ChatColor.AQUA + g.getName() + "'s Members: " + ChatColor.GOLD + getGangPlayerStats(g));
            return true;
          }
          p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
          return true;
        }
        p.sendMessage(Lang.NO_PERMS.toString(p));
        return true;
      }
      if (args[0].equalsIgnoreCase("reload")) {
    	  if (!sender.isOp()) {
        		sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
        		return true;
        	}
      	  if (args.length != 1) {
      	  p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString());
      	  return true;
      	  } try {
          	f.reloadGangConfig();
          	f.reloadKdrConfig();
          	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully reloaded gang config."));
          	plugin.getLogger().log(Level.INFO, "Successfully reloaded gang config.");
          	return true;
          	} catch (Exception e) {
          	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere was an error trying to reload the config!"));
          	plugin.getLogger().log(Level.SEVERE, "There was an error trying to reload the config!");
          	return true;
          	}
          }
      //Admin Bypass Support (WIP)
      if (args[0].equalsIgnoreCase("admin")) {
    	  if (!sender.isOp()) {
    		sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
      		return true;
    	  }
    	  if (args.length != 2) {
    	  sender.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
          return true;
    	  }
    	  if (args[1].equalsIgnoreCase("on")) {
    	  sender.sendMessage(ChatColor.RED + "You have enabled gang bypass.");
    	  this.enableBypass(true);
    	  return true;
    	  }
    	  if (args[1].equalsIgnoreCase("off")) {
    	  sender.sendMessage(ChatColor.RED + "You have disabled gang bypass.");
    	  this.enableBypass(false);
    	  return true;
    	  }
      }
      if (args[0].equalsIgnoreCase("save")) {
    	  if (!sender.isOp()) {
        		sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
        		return true;
        	}
      	  if (args.length != 1) {
      	  p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
      	  return true;
      	  }	try {
          	f.saveGangConfig();
          	f.saveKdrConfig();
          	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully saved all gangs."));
          	plugin.getLogger().log(Level.INFO, "Successfully saved all gangs.");
          	return true;
          	} catch (Exception e) {
          	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere was an error trying to save gangs!"));
          	plugin.getLogger().log(Level.SEVERE, "There was an error trying to save gangs!.");
          	return true;
          	}
          }
      if ((args[0].equalsIgnoreCase("create")) && (args.length == 2)) {
        if ((p.hasPermission("gangs.create")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user"))) {
          if (gm.getGangWithOfflinePlayer(p) != null) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.IN_GANG.toString(p, gm.getPlayerRank(p.getName(), gm.getGangWithOfflinePlayer(p)), gm.getGangWithPlayer(p)));
            return true;
          }
          if (gm.getGangByName(args[1]) != null) {
            for (String s : f.getGangConfig().getStringList("gang-names")) {
              if (s.equalsIgnoreCase(args[1])) {
                p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_EXISTS.toString(p, gm.getGangByName(args[1])));
                return true;
              }
            }
          }
          if (args[1].length() + 1 > this.plugin.getConfig().getInt("char-limit")) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.CHAR_LIMIT.toString(p));
            return true;
          }
          if (!this.plugin.getConfig().getStringList("blocked-names").isEmpty()) {
            for (String s : this.plugin.getConfig().getStringList("blocked-names")) {
              if (args[1].equalsIgnoreCase(s)) {
                p.sendMessage(Lang.PREFIX.toString() + Lang.BLOCKED_NAME.toString(p));
                return true;
              }
            }
            if ((args[1].contains(".")) || (args[1].contains("/")) || (args[1].contains("\"")) || (!args[1].matches("\\w.*") || args[1].contains("$"))) {
              p.sendMessage(Lang.PREFIX.toString() + Lang.BLOCKED_NAME.toString(p));
              return true;
            }
          }
          try {
          gm.createGang(p, args[1]);
          return true;
          } catch (Exception e) {
        	  p.sendMessage(Lang.PREFIX.toString() + Lang.BLOCKED_NAME.toString(p));
          }
        }
        p.sendMessage(Lang.NO_PERMS.toString());
        return true;
      }
      if ((args[0].equalsIgnoreCase("create")) && (args.length != 2)) {
        p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("promote")) && (args.length == 2)) {
        if ((p.hasPermission("gangs.promote")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user"))) {
          if (gm.getGangWithPlayer(p) == null) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
            return true;
          }
          Player target = Bukkit.getPlayer(args[1]);
          if (target == null) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString());
            return true;
          }
          if (gm.getGangWithPlayer(target) == null) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_GANG.toString(p, target, gm.getGangWithPlayer(p)));
            return true;
          }
          if (gm.getGangWithPlayer(target) != gm.getGangWithPlayer(p)) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_YOUR_GANG.toString(p, target, gm.getGangWithPlayer(p)));
            return true;
          }
          gm.promotePlayer(p, target, gm.getGangWithPlayer(p));
          return true;
        }
        p.sendMessage(Lang.NO_PERMS.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("promote")) && (args.length != 2)) {
        p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("demote")) && (args.length == 2)) {
        if ((p.hasPermission("gangs.demote")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user"))) {
          if (gm.getGangWithPlayer(p) == null) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
            return true;
          }
          Player target = Bukkit.getPlayer(args[1]);
          if (target == null) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString());
            return true;
          }
          if (gm.getGangWithPlayer(target) == null)
          {
            p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_GANG.toString(p, target, gm.getGangWithPlayer(p)));
            return true;
          }
          if (gm.getGangWithPlayer(target) != gm.getGangWithPlayer(p)) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_YOUR_GANG.toString(p, target, gm.getGangWithPlayer(p)));
            return true;
          }
          gm.demotePlayer(p, target, gm.getGangWithPlayer(p));
          return true;
        }
        p.sendMessage(Lang.NO_PERMS.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("demote")) && (args.length != 2)) {
        p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("setowner")) && (args.length == 2)) {
    	  if(p.hasPermission("gangs.setowner") || p.hasPermission("gangs.admin") || p.hasPermission("gangs.user")){
				if(gm.getGangWithPlayer(p) == null){
					p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
					return true;
				}
				Player target = Bukkit.getServer().getPlayer(args[1]);
				if(target == null){
					p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString()); 
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
					g.addLeader(Bukkit.getServer().getPlayer(oldOwner));
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
      } if ((args[0].equalsIgnoreCase("setowner")) && (args.length != 2))
      {
        p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("kick")) && (args.length == 2))
      {
        if ((p.hasPermission("gangs.kick")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user")))
        {
          if (gm.getGangWithPlayer(p) == null)
          {
            p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
            return true;
          }
          Player target = Bukkit.getPlayer(args[1]);
          if (target == null)
          {
            p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString());
            return true;
          }
          if (gm.getGangWithPlayer(target) == null)
          {
            p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_GANG.toString(p, target, gm.getGangWithPlayer(p)));
            return true;
          }
          if (gm.getGangWithPlayer(target) != gm.getGangWithPlayer(p))
          {
            p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_NOT_IN_YOUR_GANG.toString(p, target, gm.getGangWithPlayer(p)));
            return true;
          }
          gm.kickPlayer(p, target, gm.getGangWithPlayer(p));
          return true;
        }
        p.sendMessage(Lang.NO_PERMS.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("kick")) && (args.length != 2))
      {
        p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("leave")) && (args.length == 1)) {
        if ((p.hasPermission("gangs.leave")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user"))) {
          if (gm.getGangWithPlayer(p) == null) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
            return true;
          } //try {
          Gang g = gm.getGangWithPlayer(p);
          gm.leave(p, g);
          return true;
         // } catch (Exception e) {
        	 // Gang gang = gm.getGangWithPlayer(p);
        	 // gm.fdisbandGang(p, gang.getName());
          	//}
        }
        p.sendMessage(Lang.PREFIX.toString() + Lang.NO_PERMS.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("leave")) && (args.length != 1))
      {
        p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("invite")) && (args.length == 2)) {
        if ((p.hasPermission("gangs.invite")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user"))) {
          if (gm.getGangWithPlayer(p) == null) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
            return true;
          }
          Player target = Bukkit.getPlayer(args[1]);
          if (target == p) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.CANT_INVITE_YOURSELF);
            return true;
          }
          if (target == null) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString());
            return true;
          }
          gm.invitePlayer(p, target, gm.getGangWithPlayer(p));
          return true;
        }
        p.sendMessage(Lang.NO_PERMS.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("join")) && (args.length == 2)) {
        if ((p.hasPermission("gangs.join")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user"))) {
          if (gm.getGangWithPlayer(p) != null) {
            p.sendMessage(Lang.PREFIX.toString() + Lang.IN_GANG.toString(p, gm.getPlayerRank(sender.getName(), gm.getGangWithPlayer(p)), gm.getGangWithPlayer(p)));
            return true;
          }
          Set<String> names = new HashSet<String>();
          for (int i = 0; i < f.getGangConfig().getStringList("gang-names").size(); i++) {
            names.add(((String)f.getGangConfig().getStringList("gang-names").get(i)).toLowerCase());
          }
          if ((gm.getGangByName(args[1]) == null) && (!names.contains(args[1].toLowerCase())))
          {
            p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_NOT_FOUND.toString(p));
            return true;
          }
          if (gm.isInvited(p)) {
            if (gm.gangsMatchInvited(p, args[1])) {
              gm.removeInvitation(p);
              gm.getGangByName(args[1]).addMember(p);
              p.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_JOIN.toString(p, gm.getPlayerRank(p.getName(), gm.getGangWithPlayer(p)), gm.getGangWithPlayer(p)));
              gm.messageGang(gm.getGangByName(args[1]), Lang.SUCCESS_JOIN.toString(p, gm.getPlayerRank(sender.getName(), gm.getGangWithPlayer(p)), gm.getGangWithPlayer(p)));
              return true;
            }
            p.sendMessage(Lang.PREFIX.toString() + Lang.GANG_NOT_FOUND.toString(p));
            return true;
          }
          p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_INVITED.toString(p, gm.getGangByName(args[1])));
          return true;
        }
        p.sendMessage(Lang.NO_PERMS.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("join")) && (args.length != 2))
      {
        p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("uninvite")) || (args[0].equalsIgnoreCase("deinvite")) || (
        (args[0].equalsIgnoreCase("revoke")) && (args.length == 2)))
      {
        if ((p.hasPermission("gangs.uninvite")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user")))
        {
          if (gm.getGangWithPlayer(p) == null)
          {
            p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
            return true;
          }
          Player target = Bukkit.getPlayer(args[1]);
          if (target == null)
          {
            p.sendMessage(Lang.PREFIX.toString() + Lang.PLAYER_NOT_ONLINE.toString());
            return true;
          }
          gm.uninvitePlayer(p, target, gm.getGangWithPlayer(p));
          return true;
        }
        p.sendMessage(Lang.NO_PERMS.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("uninvite")) && (args.length != 2))
      {
        p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("disband")) && (args.length == 1))
      {
        if ((p.hasPermission("gangs.disband")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user")))
        {
          if (gm.getGangWithPlayer(p) == null)
          {
            p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
            return true;
          }
          Gang gang = gm.getGangWithPlayer(p);
          try {
          gm.disbandGang(p, gang.getName());
          return true;
          } catch (Exception e) {
        	  gm.fdisbandGang(p, gang.getName());
        	  return true;
          }
        }
        p.sendMessage(Lang.NO_PERMS.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("disband")) && (args.length != 1))
      {
        p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("c")) && (args.length == 1))
      {
        if ((p.hasPermission("gangs.chat")) || (p.hasPermission("gangs.admin")) || (p.hasPermission("gangs.user")))
        {
          if (gm.getGangWithPlayer(p) == null)
          {
            p.sendMessage(Lang.PREFIX.toString() + Lang.NOT_IN_GANG.toString(p));
            return true;
          }
          Gang g = gm.getGangWithPlayer(p);
          if (gm.isInGangChat(p))
          {
            gm.removeFromGangChat(p, g);
            p.sendMessage(Lang.PREFIX.toString() + Lang.SWITCH_TO_PUBLIC_CHAT.toString(p, g));
            return true;
          }
          gm.addToGangChat(p, g);
          p.sendMessage(Lang.PREFIX.toString() + Lang.SWITCH_TO_GANG_CHAT.toString(p, g));
          return true;
        }
        p.sendMessage(Lang.NO_PERMS.toString(p));
        return true;
      }
      if ((args[0].equalsIgnoreCase("help")) && (args.length == 1))
      {
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
      }
      if ((args[0].equalsIgnoreCase("help")) && (args[1].equalsIgnoreCase("2")) && (args.length == 2))
      {
        p.sendMessage(ChatColor.DARK_RED + "--=" + Lang.PREFIX.toString() + ChatColor.BLUE + "Help" + ChatColor.DARK_RED + "=--");
        p.sendMessage(ChatColor.RED + "g is in alias for gang!");
        p.sendMessage(ChatColor.GREEN + "/gang invite <PlayerName>");
        p.sendMessage(ChatColor.YELLOW + "    Invites a player to the gang");
        p.sendMessage(ChatColor.GREEN + "/gang uninvite,deinvite <PlayerName>");
        p.sendMessage(ChatColor.YELLOW + "    Uninvites a player to the gang");
        p.sendMessage(ChatColor.GREEN + "/gang kick <PlayerName>");
        p.sendMessage(ChatColor.YELLOW + "    Kicks a player from the gang");
        p.sendMessage(ChatColor.BLUE + "Type " + ChatColor.RED + "/gang help 3 " + ChatColor.BLUE + "to read the second page!");
        return true;
      }
      if ((args[0].equalsIgnoreCase("help")) && (args[1].equalsIgnoreCase("3")) && (args.length == 2))
      {
        p.sendMessage(ChatColor.DARK_RED + "--=" + Lang.PREFIX.toString() + ChatColor.BLUE + "Help" + ChatColor.DARK_RED + "=--");
        p.sendMessage(ChatColor.GREEN + "/gang promote <PlayerName>");
        p.sendMessage(ChatColor.RED + "g is in alias for gang!");
        p.sendMessage(ChatColor.YELLOW + "    Promotes a player in the gang");
        p.sendMessage(ChatColor.GREEN + "/gang disband");
        p.sendMessage(ChatColor.YELLOW + "    Disbands a gang");
        p.sendMessage(ChatColor.GREEN + "/gang c, /g c");
        p.sendMessage(ChatColor.YELLOW + "    Switches between gang and public chat");
        return true;
      }
      if (args[0].equalsIgnoreCase("help"))
      {
        p.sendMessage(Lang.PREFIX.toString() + Lang.WRONG_COMMAND.toString(p));
        return true;
      }
    }
	return false;
  }
  
  public Set<String> getGangPlayerStats(Gang g)
  {
    Set<String> memberData = new HashSet<String>();
    for (String s : g.getMembers())
    {
      OfflinePlayer p = Bukkit.getOfflinePlayer(s);
      String status = null;
      if ((p != null) && (p.isOnline())) {
        status = ChatColor.GREEN + "Online" + ChatColor.GOLD;
      } else {
        status = ChatColor.RED + "Offline" + ChatColor.GOLD;
      }
      String name = "Member " + s + " - " + status;
      memberData.add(name);
    }
    for (String s : g.getTrusted())
    {
      OfflinePlayer p = Bukkit.getOfflinePlayer(s);
      String status = null;
      if ((p != null) && (p.isOnline())) {
        status = ChatColor.GREEN + "Online" + ChatColor.GOLD;
      } else {
        status = ChatColor.RED + "Offline" + ChatColor.GOLD;
      }
      String name = "Trusted " + s + " - " + status;
      memberData.add(name);
    }
    for (String s : g.getOfficers())
    {
      OfflinePlayer p = Bukkit.getOfflinePlayer(s);
      String status = null;
      if ((p != null) && (p.isOnline())) {
        status = ChatColor.GREEN + "Online" + ChatColor.GOLD;
      } else {
        status = ChatColor.RED + "Offline" + ChatColor.GOLD;
      }
      String name = "Officer " + s + " - " + status;
      memberData.add(name);
    }
    for (String s : g.getLeaders())
    {
      OfflinePlayer p = Bukkit.getOfflinePlayer(s);
      String status = null;
      if ((p != null) && (p.isOnline())) {
        status = ChatColor.GREEN + "Online" + ChatColor.GOLD;
      } else {
        status = ChatColor.RED + "Offline" + ChatColor.GOLD;
      }
      String name = "Leader " + s + " - " + status;
      memberData.add(name);
    }
    String owner = g.getOwner();
    OfflinePlayer p = Bukkit.getOfflinePlayer(owner);
    String status = null;
    if ((p != null) && (p.isOnline())) {
      status = ChatColor.GREEN + "Online" + ChatColor.GOLD;
    } else {
      status = ChatColor.RED + "Offline" + ChatColor.GOLD;
    }
    String name = "Owner " + owner + " - " + status;
    memberData.add(name);
    return memberData;
  }
}
