package com.mydeblob.prisongangs;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Gang {
	private static FileManager f = FileManager.getFileManager();

	  private ArrayList<String> members = new ArrayList<String>(); 
	  private ArrayList<String> trusted = new ArrayList<String>(); 
	  private ArrayList<String> officers = new ArrayList<String>(); 
	  private ArrayList<String> leaders = new ArrayList<String>();
	  private static ArrayList<Gang> gangs = new ArrayList<Gang>();
	  private String owner = null;
	  private String name;

	public Gang(String name){
	    this.name = name;
	    for(String s:f.getGangConfig().getStringList("gangs." + name + ".members")){
	    	members.add(s);
	    }
	    for(String s:f.getGangConfig().getStringList("gangs." + name + ".trusted")){
	    	trusted.add(s);
	    }
	    for(String s:f.getGangConfig().getStringList("gangs." + name + ".officers")){
	    	officers.add(s);
	    }
	    for(String s:f.getGangConfig().getStringList("gangs." + name + ".leaders")){
	    	leaders.add(s);
	    }
	    owner = f.getGangConfig().getString("gangs." + name + ".owner");
	    gangs.add(this);
	  }
	  public String getName(){
	    return this.name;
	  }
	  public ArrayList<String> getMembers(){
	    return this.members;
	  }
	  public void addMember(Player p){
	    this.members.add(p.getName());
	    f.getGangConfig().set("gangs." + this.name + ".members", members);
	    f.saveGangConfig();
	  }
	  public void removeMember(Player p){
	    this.members.remove(p.getName());
	    f.getGangConfig().set("gangs." + this.name + ".members", members);
	    f.saveGangConfig();
	  }
	  public ArrayList<String> getTrusted(){
	    return this.trusted;
	  }
	  public void addTrusted(Player p) {
	    this.trusted.add(p.getName());
	    f.getGangConfig().set("gangs." + this.name + ".trusted", trusted);
	    f.saveGangConfig();
	  }
	  public void removeTrusted(Player p) {
	    this.trusted.remove(p.getName());
	    f.getGangConfig().set("gangs." + this.name + ".trusted", trusted);
	    f.saveGangConfig();
	  }
	  public ArrayList<String> getOfficers(){
		  return this.officers;
	  }
	  public void addOfficer(Player p){
		  this.officers.add(p.getName());
		  f.getGangConfig().set("gangs." + this.name + ".officers", officers);
		  f.saveGangConfig();
	  }
	  public void removeOfficer(Player p){
		  this.officers.remove(p.getName());
		  f.getGangConfig().set("gangs." + this.name + ".officers", officers);
		  f.saveGangConfig();
	  }
	  public ArrayList<String> getLeaders(){
		  return this.leaders;
	  }
	  public void addLeader(Player p){
		  this.leaders.add(p.getName());
		  f.getGangConfig().set("gangs." + this.name + ".leaders", leaders);
		  f.saveGangConfig();
	  }
	  public void removeLeader(Player p){
		  this.leaders.remove(p.getName());
		  f.getGangConfig().set("gangs." + this.name + ".leaders", leaders);
		  f.saveGangConfig();
	  }
	  public String getOwner(){
		  return this.owner;
	  }
	  public void setOwner(Player p){
		  this.owner = p.getName();
		  f.getGangConfig().set("gangs." + this.name + ".owner", owner);
		  f.saveGangConfig();
	  }
	  
	  public ArrayList<String> getAllPlayers(){
		  ArrayList<String> allPlayers = new ArrayList<String>();
		  for(String s:getMembers()){
			  allPlayers.add(s);
		  }
		  for(String s:getTrusted()){
			  allPlayers.add(s);
		  }
		  for(String s:getOfficers()){
			  allPlayers.add(s);
		  }
		  for(String s:getLeaders()){
			  allPlayers.add(s);
		  }
		  allPlayers.add(getOwner());
		  return allPlayers;
	  }
		public static void loadGangs(){
		    Gang.gangs.clear();
		    for(String s:f.getGangConfig().getStringList("gang-names")){
		    	Gang.gangs.add(new Gang(s));
		    }
		  }

		  public static ArrayList<Gang> getGangs(){
		    return Gang.gangs;
		  }
	  public static void removeGang(Gang g){
		  if(Gang.gangs.contains(g)){
			  Gang.gangs.remove(g);
		  }
	  }
	  
	  public Enum<Ranks> getPlayerRank(String playerName, Gang g){
		  if(g.getMembers().contains(playerName)){
			  return Ranks.MEMBER;
		  }else if(g.getTrusted().contains(playerName)){
			  return Ranks.TRUSTED;
		  }else if(g.getOfficers().contains(playerName)){
			  return Ranks.OFFICER;
		  }else if(g.getLeaders().contains(playerName)){
			  return Ranks.LEADER;
		  }else if(g.getOwner().equalsIgnoreCase(playerName)){
			  return Ranks.OWNER;
		  }else{
			  return null;
		  }
	  }
}
