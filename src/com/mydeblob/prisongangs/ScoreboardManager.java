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
			o.getScore(ChatColor.GREEN + "Owner:").setScore(8);
			o.getScore(g.getOwner()).setScore(7);
			o.getScore(ChatColor.GOLD + "Number of members:").setScore(6);
			o.getScore(String.valueOf(g.getAllPlayers().size())).setScore(5);
			o.getScore(ChatColor.BLUE + "Gang KDR:").setScore(4);
			o.getScore(String.valueOf(gm.getGangKDR(g))).setScore(3);
			o.getScore(ChatColor.LIGHT_PURPLE + "Your KDR:").setScore(2);
			o.getScore(String.valueOf(FileManager.getFileManager().getKdrConfig().getDouble("players." + p.getUniqueId().toString() + ".kdr"))).setScore(1);
		}else if(p.getScoreboard() != null){
			Scoreboard board = p.getScoreboard();  
			Objective o = board.getObjective(DisplaySlot.SIDEBAR);
			if(gm.getGangWithPlayer(p) == null){
				o.setDisplayName(ChatColor.RED + "You have no gang!");
				return;
			}
			Gang g = gm.getGangWithPlayer(p);
			o.setDisplayName(ChatColor.DARK_RED + g.getName());
			o.getScore(ChatColor.GREEN + "Owner:").setScore(8);
			o.getScore(g.getOwner()).setScore(7);
			o.getScore(ChatColor.GOLD + "Number of members:").setScore(6);
			o.getScore(String.valueOf(g.getAllPlayers().size())).setScore(5);
			o.getScore(ChatColor.BLUE + "Gang KDR:").setScore(4);
			o.getScore(String.valueOf(gm.getGangKDR(g))).setScore(3);
			o.getScore(ChatColor.LIGHT_PURPLE + "Your KDR:").setScore(2);
			o.getScore(String.valueOf(FileManager.getFileManager().getKdrConfig().getDouble("players." + p.getUniqueId().toString() + ".kdr"))).setScore(1);
		}
	}
}
