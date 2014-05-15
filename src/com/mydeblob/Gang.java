package com.mydeblob;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Gang {
	private SettingsManager settings = SettingsManager.getInstance();

	  private ArrayList<String> members = new ArrayList<String>(); 
	  private ArrayList<String> trusted = new ArrayList<String>(); 
	  private ArrayList<String> officers = new ArrayList<String>(); 
	  private ArrayList<String> leaders = new ArrayList<String>();
	  private String name;

	  @SuppressWarnings("rawtypes") //For "Iterator is a raw type. References to generic type Iterator<E> should be parameterized"
	public Gang(String name)
	  {
	    this.name = name;
	    String str;
	    for (Iterator localIterator = this.settings.getClans().getStringList("gangs." + name + ".members").iterator(); localIterator.hasNext(); this.members.add(str)){
	    	 str = (String)localIterator.next();
	    }
	    for (Iterator localIterator = this.settings.getClans().getStringList("gangs." + name + ".trusted").iterator(); localIterator.hasNext(); this.trusted.add(str)){
	    	 str = (String)localIterator.next();
	    }
	    for (Iterator localIterator = this.settings.getClans().getStringList("gangs." + name + ".officers").iterator(); localIterator.hasNext(); this.officers.add(str)){
	    	 str = (String)localIterator.next();
	    }
	    for (Iterator localIterator = this.settings.getClans().getStringList("gangs." + name + ".leaders").iterator(); localIterator.hasNext(); this.leaders.add(str)){
	    	 str = (String)localIterator.next();
	    }
	  }
	  public String getName()
	  {
	    return this.name;
	  }
	  public void setName(String name) {
	    this.name = name;
	    save();
	  }
	  public ArrayList<String> getMembers() {
	    return this.members;
	  }
	  public void addMember(Player p) {
	    this.members.add(p.getName());
	    save();
	  }
	  public void removeMember(Player p) {
	    this.members.remove(p.getName());
	    save();
	  }
	  public ArrayList<String> getTrusted() {
	    return this.trusted;
	  }
	  public void addTrusted(Player p) {
	    this.trusted.add(p.getName());
	    save();
	  }
	  public void removeTrusted(Player p) {
	    this.trusted.remove(p.getName());
	    save();
	  }
	  public ArrayList<String> getOfficers(){
		  return this.officers;
	  }
	  public void addOfficer(Player p){
		  this.officers.add(p.getName());
		  save();
	  }
	  public void removeOfficer(Player p){
		  this.officers.remove(p.getName());
		  save();
	  }
	  public ArrayList<String> getLeaders(){
		  return this.leaders;
	  }
	  public void addLeader(Player p){
		  this.leaders.add(p.getName());
		  save();
	  }
	  public void removeLeader(Player p){
		  this.leaders.remove(p.getName());
		  save();
	  }
	  @SuppressWarnings("deprecation")
	public void msg(Gang c, String message){
		  if(GangManager.getInstance().getClan(c.getName()) == null) return;
		  for(String p : c.getMembers()){
			 Player player = Bukkit.getServer().getPlayer(p);
			 if(!(player == null)){
				 player.sendMessage(message);
			 }
		  }
		  for(String p : c.getTrusted()){
				 Player player = Bukkit.getServer().getPlayer(p);
				 if(!(player == null)){
					 player.sendMessage(message);
				 }
			  }
		  for(String p : c.getOfficers()){
				 Player player = Bukkit.getServer().getPlayer(p);
				 if(!(player == null)){
					 player.sendMessage(message);
				 }
			  }
		  for(String p : c.getLeaders()){
				 Player player = Bukkit.getServer().getPlayer(p);
				 if(!(player == null)){
					 player.sendMessage(message);
				 }
			  }
	  }
	  private void save() {
	    ConfigurationSection conf = this.settings.getClans().getConfigurationSection("gangs." + this.name);
	    conf.set("members", this.members);
	    conf.set("trusted", this.trusted);
	    conf.set("officers", this.officers);
	    conf.set("leaders", this.leaders);
	    this.settings.saveClans();
	  }
}
