package com.mydeblob.prisongangs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;

public class CrackShotDepend implements Listener{

	private static Plugin plugin;
	public CrackShotDepend(Plugin p){
		plugin = p;
	}
	GangManager gm = GangManager.getGangManager();
	
	@EventHandler
	public void onDamage(WeaponDamageEntityEvent e){
		if(plugin.getConfig().getBoolean("friendly-fire")) return;
		if(e.getVictim() instanceof Player){
			Player target = (Player) e.getVictim();
			Player damager = null;
			if(e.getPlayer() instanceof Player){
				damager = (Player) e.getPlayer();
			}
			if(damager == null){
				return;
			}
			if(gm.getGangWithPlayer(target) == null){
				return;
			}if(gm.getGangWithPlayer(damager) == null){
				return;
			}if(gm.getGangWithPlayer(target) == gm.getGangWithPlayer(damager)){
				e.setCancelled(true);
			}
		}
		
	}
}
