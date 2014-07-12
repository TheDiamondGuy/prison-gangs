package com.mydeblob.prisongangs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {
	private static GangManager gm = GangManager.getGangManager();
	public static void updateScoreboard(Player p){
		if(p.getScoreboard() == null){
			Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();  
			Objective o = board.registerNewObjective("prisongangs", "dummy");
			o.setDisplaySlot(DisplaySlot.SIDEBAR);
			if(gm.getGangWithPlayer(p) == null){
				o.setDisplayName(ChatColor.RED + "You have no gang!");
				return;
			}
			Gang g = gm.getGangWithPlayer(p);
			o.setDisplayName(ChatColor.DARK_RED + g.getName());
			
		}
	}
}
