package com.mydeblob.prisongangs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GangManager {
	  private static GangManager instance = new GangManager();
	  private FileManager f = FileManager.getFileManager();
	  public static GangManager getGangManager(){
	    return instance;
	  }

	  public Gang getGangByName(String name){
	    for (Gang g: Gang.getGangs()){
	      if (g.getName().equalsIgnoreCase(name)){
	    	  return g;
	      }
	    }
	    return null;
	  }

	  public Gang getGangWithPlayer(Player p){
	    for (Gang g: Gang.getGangs()){
	      if(g.getAllPlayers().contains(p.getName())){
	    	  return g;
	      }
	    }
	    return null;
	  }
	  @SuppressWarnings("deprecation") //getPlayerExact is deprecated due to UUID's
	  public void promotePlayer(Player sender, Player target, Gang g){
		  Gang gang = getGangWithPlayer(sender);
		  if(gang.getOfficers().contains(sender.getName()) || sender.hasPermission("gangs.admin") || sender.isOp()){
			  if(gang.getMembers().contains(target.getName())){
				  gang.addTrusted(target);
				  gang.removeMember(target);
				  sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "trusted"));
				  target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "trusted"));
				  messageGang(g, Lang.SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "trusted"));
				  return;
			  }else{
				  sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_PROMOTE_OFFICER.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "trusted"));
				  return;
			  }
		  }if(gang.getLeaders().contains(sender.getName())){
			  if(gang.getTrusted().contains(target.getName())){
					gang.addOfficer(target);
					gang.removeTrusted(target);
					sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "officer"));
					target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "officer"));
					messageGang(g, Lang.SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "officer"));
					return;
				}else{
					sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_PROMOTE_LEADER.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "officer"));
					return;
				}
		  }if(gang.getOwner().equalsIgnoreCase(sender.getName())){
			if(gang.getOfficers().contains(target.getName())){
					gang.addLeader(target);
					gang.removeOfficer(target);
					sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "leader"));
					target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "leader"));
					messageGang(g, Lang.SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "leader"));
					return;
				}else{
					sender.sendMessage(Lang.PREFIX.toString() + Lang.CANT_PROMOTE_OWNER.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "leader"));
					return;
				}
		  }if(sender.hasPermission("gang.admin") || sender.isOp()){
			  if(gang.getMembers().contains(target.getName())){
				  gang.addTrusted(target);
				  gang.removeMember(target);
				  sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "trusted"));
				  target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "trusted"));
				  messageGang(g, Lang.SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "trusted"));
				  return;
			  }if(gang.getTrusted().contains(target.getName())){
					gang.addOfficer(target);
					gang.removeTrusted(target);
					sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "officer"));
					target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "officer"));
					messageGang(g, Lang.SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "officer"));
					return;
				}if(gang.getOfficers().contains(target.getName())){
					gang.addLeader(target);
					gang.removeOfficer(target);
					sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "leader"));
					target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "leader"));
					messageGang(g, Lang.SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "leader"));
					return;
				}if(gang.getLeaders().contains(target.getName())){
					String oldOwner = gang.getOwner();
					gang.setOwner(target);
					gang.addLeader(Bukkit.getPlayerExact(oldOwner));
					gang.removeOfficer(target);
					sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "owner"));
					target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "owner"));
					messageGang(g, Lang.SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "owner"));
					return;
				}
		  }
	  }
	  
	@SuppressWarnings("deprecation") //getPlayerExact is deprecated due to UUID's
	public void messageGang(Gang g, String message){
		  for(String s:g.getAllPlayers()){
			  Player p = Bukkit.getPlayerExact(s);
			  p.sendMessage(message);
		  }
	  }
	  
	  public void demotePlayer(Player p, Gang g){
		  
	  }
	  
	  public void kickPlayer(Player p, Gang g){
		  
	  }
	  
	  public void leave(Player p, Gang g){
		  if(g.getMembers().contains(p.getName())){
			  	messageGang(g, Lang.SUCCESS_LEFT.toString().replaceAll("%s%", p.getName()).replaceAll("%r%", "member").replaceAll("%g%", g.getName()));
			  	p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString().replaceAll("%s%", p.getName()).replaceAll("%r%", "member").replaceAll("%g%", g.getName()));
				g.removeMember(p);
				return;
			}if(g.getTrusted().contains(p.getName())){
			  	messageGang(g, Lang.SUCCESS_LEFT.toString().replaceAll("%s%", p.getName()).replaceAll("%r%", "trusted").replaceAll("%g%", g.getName()));
			  	p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString().replaceAll("%s%", p.getName()).replaceAll("%r%", "trusted").replaceAll("%g%", g.getName()));
				g.removeTrusted(p);
				return;
			}if(g.getOfficers().contains(p.getName())){
			  	messageGang(g, Lang.SUCCESS_LEFT.toString().replaceAll("%s%", p.getName()).replaceAll("%r%", "officer").replaceAll("%g%", g.getName()));
			  	p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString().replaceAll("%s%", p.getName()).replaceAll("%r%", "officer").replaceAll("%g%", g.getName()));
				g.removeTrusted(p);
				return;
			}if(g.getLeaders().contains(p.getName())){
			  	messageGang(g, Lang.SUCCESS_LEFT.toString().replaceAll("%s%", p.getName()).replaceAll("%r%", "leader").replaceAll("%g%", g.getName()));
			  	p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString().replaceAll("%s%", p.getName()).replaceAll("%r%", "leader").replaceAll("%g%", g.getName()));
				g.removeTrusted(p);
				return;
			}if(g.getOwner().equalsIgnoreCase(p.getName())){
				messageGang(g, Lang.DISBAND_ABSENCE.toString().replaceAll("%s%", p.getName()).replaceAll("%r%", "owner").replaceAll("%g%", g.getName()));
				removeGang(g.getName());
			  	p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_LEFT.toString().replaceAll("%s%", p.getName()).replaceAll("%r%", "owner").replaceAll("%g%", g.getName()));
				return;
			}
	  }
	  
	  public void createGang(Player owner, String name){
		    f.getGangConfig().set("gangs." + name + ".members", new ArrayList<String>());
			f.getGangConfig().set("gangs." + name + ".trusted", new ArrayList<String>());
			f.getGangConfig().set("gangs." + name + ".officers", new ArrayList<String>());
			f.getGangConfig().set("gangs." + name + ".leaders", new ArrayList<String>());
			f.getGangConfig().set("gangs." + name + ".owner", owner.getName());
			List<String> gangs = f.getGangConfig().getStringList("gang-names");
			gangs.add(name);
			f.getGangConfig().set("gang-names", gangs);
			f.saveGangConfig();
			@SuppressWarnings("unused")
			Gang g = new Gang(name);
			owner.sendMessage(Lang.PREFIX.toString() + Lang.SUCCESFULLY_CREATED_GANG.toString().replaceAll("%s%", owner.getName()).replaceAll("%g%", name));
	  }
	  
	  public void removeGang(String name){
		  	Gang g = getGangByName(name);
			f.getGangConfig().set("gangs." + g.getName(), null);
		    List<String> gangs = f.getGangConfig().getStringList("gang-names");
		    gangs.remove(g.getName());
		    f.getGangConfig().set("gang-names", gangs);
		    f.saveGangConfig();
		    Gang.removeGang(g);
	  }
	  
	  public void disbandGang(Player p, String name){
		  	Gang g = getGangByName(name);
		  	String gname = g.getName();
			f.getGangConfig().set("gangs." + g.getName(), null);
		    List<String> gangs = f.getGangConfig().getStringList("gang-names");
		    gangs.remove(g.getName());
		    f.getGangConfig().set("gang-names", gangs);
		    f.saveGangConfig();
		    Gang.removeGang(g);
		  	messageGang(g, Lang.SUCCESS_DISBAND.toString().replaceAll("%s%", p.getName()).replaceAll("%g%", gname));
		    p.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_DISBAND.toString().replaceAll("%s%", p.getName()).replaceAll("%g%", gname));
	  }
}
