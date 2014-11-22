package com.cullan.prisongangs;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Gang
{
  private static FileManager f = FileManager.getFileManager();
  private ArrayList<String> members = new ArrayList<String>();
  private ArrayList<String> trusted = new ArrayList<String>();
  private ArrayList<String> officers = new ArrayList<String>();
  private ArrayList<String> leaders = new ArrayList<String>();
  private ArrayList<String> membersUuid = new ArrayList<String>();
  private ArrayList<String> trustedUuid = new ArrayList<String>();
  private ArrayList<String> officersUuid = new ArrayList<String>();
  private ArrayList<String> leadersUuid = new ArrayList<String>();
  private String ownersUuid;
  private static ArrayList<Gang> gangs = new ArrayList<Gang>();
  private String owner;
  private String name;
  
  public Gang(String name)
  {
    this.name = name;
    for (String s : f.getGangConfig().getStringList("gangs." + name + ".members")) {
      if (s != null)
      {
        this.membersUuid.add(s);
        this.members.add(Bukkit.getOfflinePlayer(UUID.fromString(s)).getName());
      }
    }
    for (String s : f.getGangConfig().getStringList("gangs." + name + ".trusted")) {
      if (s != null)
      {
        this.trustedUuid.add(s);
        this.trusted.add(Bukkit.getOfflinePlayer(UUID.fromString(s)).getName());
      }
    }
    for (String s : f.getGangConfig().getStringList("gangs." + name + ".officers")) {
      if (s != null)
      {
        this.officersUuid.add(s);
        this.officers.add(Bukkit.getOfflinePlayer(UUID.fromString(s)).getName());
      }
    }
    for (String s : f.getGangConfig().getStringList("gangs." + name + ".leaders")) {
      if (s != null)
      {
        this.leadersUuid.add(s);
        this.leaders.add(Bukkit.getOfflinePlayer(UUID.fromString(s)).getName());
      }
    }
    this.owner = Bukkit.getOfflinePlayer(UUID.fromString(f.getGangConfig().getString("gangs." + name + ".owner"))).getName();
    this.ownersUuid = f.getGangConfig().getString("gangs." + name + ".owner");
    gangs.add(this);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public ArrayList<String> getMembers()
  {
    return this.members;
  }
  
  public void addMember(Player p)
  {
    this.membersUuid.add(p.getUniqueId().toString());
    this.members.add(p.getName());
    f.getGangConfig().set("gangs." + this.name + ".members", this.membersUuid);
    f.saveGangConfig();
  }
  
  public void removeMember(OfflinePlayer target)
  {
    this.membersUuid.remove(target.getUniqueId().toString());
    this.members.remove(target.getName());
    f.getGangConfig().set("gangs." + this.name + ".members", this.membersUuid);
    f.saveGangConfig();
  }
  
  public ArrayList<String> getTrusted()
  {
    return this.trusted;
  }
  
  public void addTrusted(Player p)
  {
    this.trustedUuid.add(p.getUniqueId().toString());
    this.trusted.add(p.getName());
    f.getGangConfig().set("gangs." + this.name + ".trusted", this.trustedUuid);
    f.saveGangConfig();
  }
  
  public void removeTrusted(OfflinePlayer target)
  {
    this.trustedUuid.remove(target.getUniqueId().toString());
    this.trusted.remove(target.getName());
    f.getGangConfig().set("gangs." + this.name + ".trusted", this.trustedUuid);
    f.saveGangConfig();
  }
  
  public ArrayList<String> getOfficers()
  {
    return this.officers;
  }
  
  public void addOfficer(Player p)
  {
    this.officersUuid.add(p.getUniqueId().toString());
    this.officers.add(p.getName());
    f.getGangConfig().set("gangs." + this.name + ".officers", this.officersUuid);
    f.saveGangConfig();
  }
  
  public void removeOfficer(OfflinePlayer target)
  {
    this.officersUuid.remove(target.getUniqueId().toString());
    this.officers.remove(target.getName());
    f.getGangConfig().set("gangs." + this.name + ".officers", this.officersUuid);
    f.saveGangConfig();
  }
  
  public ArrayList<String> getLeaders()
  {
    return this.leaders;
  }
  
  public void addLeader(Player p){
	  this.leadersUuid.add(p.getUniqueId().toString());
	  this.leaders.add(p.getName());
	  f.getGangConfig().set("gangs." + this.name + ".leaders", leadersUuid);
	  f.saveGangConfig();
  }
  
  public void removeLeader(Player p){
	  this.leadersUuid.remove(p.getUniqueId().toString());
	  this.leaders.remove(p.getName());
	  f.getGangConfig().set("gangs." + this.name + ".leaders", leadersUuid);
	  f.saveGangConfig();
  }
  
  public String getOwner(){
	  return this.owner;
  }
  
  public void setOwner(Player p) {
	  this.owner = p.getName();
	  this.ownersUuid = p.getUniqueId().toString();
	  f.getGangConfig().set("gangs." + this.name + ".owner", p.getUniqueId().toString());
	  f.saveGangConfig();
  }
  
  public void setLeader(Player p)
  {
    this.leadersUuid.add(p.getUniqueId().toString());
    this.leaders.add(p.getName());
    f.getGangConfig().set("gangs." + this.name + ".leaders", p.getUniqueId().toString());
    f.saveGangConfig();
  }
  
  public ArrayList<String> getAllPlayers()
  {
    ArrayList<String> allPlayers = new ArrayList<String>();
    for (String s : getMembers()) {
      allPlayers.add(s);
    }
    for (String s : getTrusted()) {
      allPlayers.add(s);
    }
    for (String s : getOfficers()) {
      allPlayers.add(s);
    }
    for (String s : getLeaders()) {
      allPlayers.add(s);
    }
    allPlayers.add(getOwner());
    return allPlayers;
  }
  
  public ArrayList<String> getAllPlayersUUID()
  {
    ArrayList<String> allPlayers = new ArrayList<String>();
    for (String s : this.membersUuid) {
      allPlayers.add(s);
    }
    for (String s : this.trustedUuid) {
      allPlayers.add(s);
    }
    for (String s : this.officersUuid) {
      allPlayers.add(s);
    }
    for (String s : this.leadersUuid) {
      allPlayers.add(s);
    }
    allPlayers.add(this.ownersUuid);
    return allPlayers;
  }
  
  public void clearPlayers()
  {
    this.members.clear();
    this.membersUuid.clear();
    this.trusted.clear();
    this.trustedUuid.clear();
    this.officers.clear();
    this.officersUuid.clear();
    this.leaders.clear();
    this.leadersUuid.clear();
  }
  
  public static void loadGangs()
  {
    gangs.clear();
    for (String s : f.getGangConfig().getStringList("gang-names")) {
      gangs.add(new Gang(s));
    }
  }
  
  public static ArrayList<Gang> getGangs()
  {
    return gangs;
  }
  
  public static void removeGang(Gang g)
  {
    g.clearPlayers();
    if (gangs.contains(g)) {
      gangs.remove(g);
    }
  }
}
