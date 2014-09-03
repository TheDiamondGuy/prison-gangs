package com.mydeblob.prisongangs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener{
	private static GangManager gm = GangManager.getGangManager();
	private static FileManager f = FileManager.getFileManager();
	private PrisonGangs plugin;
	public Events(PrisonGangs plugin){
		this.plugin = plugin;
	}
	
	/**
	 * Replace the {gang} tag in chat with the players gang name
	 */
	@EventHandler(priority = EventPriority.HIGH) //High so PrisonGangs gets to it last (So it doesn't interfere with other chat options)
	public void onChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		if(gm.getGangWithPlayer(p) != null){
			Gang g = gm.getGangWithPlayer(p);
			String pGang = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("format")).replaceAll("%g%", g.getName());
			String f = e.getFormat().replaceAll("\\{gang\\}", pGang).replaceAll("\\{GANG\\}", pGang);
			e.setFormat(f);
		}else{
			String f = e.getFormat().replaceAll("\\{gang\\}", "").replaceAll("\\{GANG\\}", "");
			e.setFormat(f);
		}
	}
	
	/**
	 * Gang chat
	 */
	@EventHandler
	public void  onGangChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		if(gm.getGangWithPlayer(p) == null){
			return;
		}
		Gang g = gm.getGangWithPlayer(p);
		if(!gm.isInGangChat(p)){
			return;
		}
		String format = ChatColor.DARK_GRAY + gm.getPlayerRank(p).toText() + " " + plugin.getConfig().getString("seperator") + " " +  p.getName() + ": " + ChatColor.BLUE + e.getMessage();
		e.setCancelled(true);
		gm.messageGang(g, format);
	}
	

	/**
	 * Setting base information for the kdr.yml, adding scoreboard, and adding the join message
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if(!p.hasPlayedBefore() || !f.getKdrConfig().contains("players." + p.getUniqueId().toString())){
			f.getKdrConfig().set("players." + p.getUniqueId().toString() + ".kills", 0);
			f.getKdrConfig().set("players." + p.getUniqueId().toString() + ".deaths", 0);
			f.getKdrConfig().set("players." + p.getUniqueId().toString() + ".kdr", 0);
		}
		p.sendMessage(ChatColor.BLUE + "This server is running PrisonGangs by mydeblob!");
	} 
	
	/**
	 * When a player dies edit the killers and victims kdr appropiatly
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		Player victim = e.getEntity();
		int victimDeaths = f.getKdrConfig().getInt("players." + victim.getUniqueId().toString() + ".deaths");
		victimDeaths++;
		f.getKdrConfig().set("players." + victim.getUniqueId().toString() + ".deaths", victimDeaths);
		double dVictimDeaths = (double) f.getKdrConfig().getInt("players." + victim.getUniqueId().toString() + ".deaths");
		double victimKills = (double) f.getKdrConfig().getInt("players." + victim.getUniqueId().toString() + ".kills");
		if(f.getKdrConfig().getInt("players." + victim.getUniqueId().toString() + ".kills") == 0 || f.getKdrConfig().getDouble("players." + victim.getUniqueId().toString() + ".kdr") < 0.000){
			f.getKdrConfig().set("players." + victim.getUniqueId().toString() + ".kdr", 0.0);
		}else{
			f.getKdrConfig().set("players." + victim.getUniqueId().toString() + ".kdr", victimKills/dVictimDeaths);
		}
		if(victim.getKiller() instanceof Player){
			Player killer = (Player) e.getEntity().getKiller();
			int killerKills = f.getKdrConfig().getInt("players." + killer.getUniqueId().toString() + ".kills");
			killerKills++;
			f.getKdrConfig().set("players." + killer.getUniqueId().toString() + ".kills", killerKills);
			double killerDeaths = (double) f.getKdrConfig().getInt("players." + killer.getUniqueId().toString() + ".deaths");
			double dKillerKills = (double) f.getKdrConfig().getInt("players." + killer.getUniqueId().toString() + ".kills");
			if(f.getKdrConfig().getInt("players." + killer.getUniqueId().toString() + ".deaths") == 0){
				double k = (double) killerKills;
				f.getKdrConfig().set("players." + killer.getUniqueId().toString() + ".kdr", k);
			}else{
				f.getKdrConfig().set("players." + killer.getUniqueId().toString() + ".kdr", dKillerKills/killerDeaths);
			}
		}
	}

	/**
	 * Don't allow gang members to hurt each other
	 */
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if(plugin.getConfig().getBoolean("friendly-fire")) return;
		if(event.getEntity() instanceof Player){
			Player target = (Player) event.getEntity();
			Player damager = null;
			if(event.getDamager() instanceof Player){
				damager = (Player) event.getDamager();
			}else if(event.getDamager() instanceof Projectile){
				Projectile proj = (Projectile) event.getDamager();
				if(proj.getShooter() instanceof Player){
					damager = (Player) proj.getShooter();
				}
			}
			if(damager == null){
				return;
			}
			if(gm.getGangWithPlayer(target) == null){
				return;
			}if(gm.getGangWithPlayer(damager) == null){
				return;
			}if(gm.getGangWithPlayer(target) == gm.getGangWithPlayer(damager)){
				event.setCancelled(true);
			}
		}
	}
	
}
