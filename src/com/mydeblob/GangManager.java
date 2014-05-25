package com.mydeblob;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.entity.Player;

public class GangManager {
	  private static GangManager instance = new GangManager();

	  private FileManager settings = FileManager.getInstance();

	  private ArrayList<Gang> clans = new ArrayList<Gang>();

	  public static GangManager getInstance()
	  {
	    return instance;
	  }

	@SuppressWarnings("rawtypes") //For "Iterator is a raw type. References to generic type Iterator<E> should be parameterized"
	public void setupClans()
	  {
	    this.clans.clear();
	    String name;
	    for (Iterator localIterator = this.settings.getClans().getStringList("gang-names").iterator(); localIterator.hasNext(); this.clans.add(new Gang(name))){
	    	 name = (String)localIterator.next(); 
	    }
	  }

	  public ArrayList<Gang> getClans()
	  {
	    return this.clans;
	  }

	  public Gang getClan(String name) {
	    for (Gang c : getClans()) {
	      if (c.getName().equalsIgnoreCase(name)){
	    	  return c;
	      }
	    }
	    return null;
	  }

	  public Gang getPlayerClan(Player p) {
	    for (Gang c : getClans()) {
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
