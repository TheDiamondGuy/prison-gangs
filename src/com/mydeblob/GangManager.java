package com.mydeblob;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.entity.Player;

public class GangManager {
	  private static GangManager instance = new GangManager();

	  private SettingsManager settings = SettingsManager.getInstance();

	  private ArrayList<Clan> clans = new ArrayList<Clan>();

	  public static GangManager getInstance()
	  {
	    return instance;
	  }

	@SuppressWarnings("rawtypes") //For "Iterator is a raw type. References to generic type Iterator<E> should be parameterized"
	public void setupClans()
	  {
	    this.clans.clear();
	    String name;
	    for (Iterator localIterator = this.settings.getClans().getStringList("clannames").iterator(); localIterator.hasNext(); this.clans.add(new Clan(name))){
	    	 name = (String)localIterator.next(); 
	    }
	  }

	  public ArrayList<Clan> getClans()
	  {
	    return this.clans;
	  }

	  public Clan getClan(String name) {
	    for (Clan c : getClans()) {
	      if (c.getName().equalsIgnoreCase(name)){
	    	  return c;
	      }
	    }
	    return null;
	  }

	  public Clan getPlayerClan(Player p) {
	    for (Clan c : getClans()) {
	      if(c.getMembers().contains(p.getName())){
	    	  return c;
	      }if(c.getTrusted().contains(p.getName())){
	    	  return c;
	      }if(c.getOfficers().contains(p.getName())){
	    	  return c;
	      }if(c.getLeaders().contains(p.getName())){
	    	  return c;
	      }
	    }
	    return null;
	  }
}
