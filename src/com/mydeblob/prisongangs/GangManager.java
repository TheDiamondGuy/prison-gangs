package com.mydeblob.prisongangs;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GangManager {
	  private static GangManager instance = new GangManager();
	  private FileManager f = FileManager.getFileManager();
	  private ArrayList<Gang> gangs = new ArrayList<Gang>();
	  public static GangManager getGangManager(){
	    return instance;
	  }

	public void loadGangs(){
	    this.gangs.clear();
	    for(String s:f.getGangConfig().getStringList("gang-names")){
	    	this.gangs.add(new Gang(s));
	    }
	  }

	  public ArrayList<Gang> getGangs()
	  {
	    return this.gangs;
	  }

	  public Gang getGangByName(String name){
	    for (Gang g: getGangs()){
	      if (g.getName().equalsIgnoreCase(name)){
	    	  return g;
	      }
	    }
	    return null;
	  }

	  public Gang getGangWithPlayer(Player p){
	    for (Gang g:getGangs()){
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
					sender.sendMessage(Lang.PREFIX.toString() + Lang.SENDER_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "leader"));
					target.sendMessage(Lang.PREFIX.toString() + Lang.TARGET_SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "leader"));
					messageGang(g, Lang.SUCCESS_PROMOTE.toString().replaceAll("%s%", sender.getName()).replaceAll("%p%", target.getName()).replaceAll("%g%", g.getName()).replaceAll("%r%", "leader"));
					return;
				}
		  }
	  }
	  //it isn't reaaadyyy
	  
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
		  
	  }
	  
	  public void createGang(Player owner, String name){
		  
	  }
	  
	  public void disbandGang(String name){
		  
	  }
}
