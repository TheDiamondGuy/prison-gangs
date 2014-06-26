package com.mydeblob.prisongangs;

import java.util.logging.Level;

import org.bukkit.Bukkit;

public class Logger {

	
	public static void log(Level l, String msg){
		Bukkit.getServer().getLogger().log(l, msg);
	}
	
	public static void broadcast(String msg){ //For debug
		Bukkit.getServer().broadcastMessage(msg);
	}
}
