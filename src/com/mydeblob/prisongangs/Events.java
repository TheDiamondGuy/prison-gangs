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
	@EventHandler(priority = EventPriority.HIGH)
	public void onChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		if(gm.getGangWithPlayer(p) != null){
			Gang g = gm.getGangWithPlayer(p);
			String pClan = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("format")).replaceAll("%g%", g.getName());
			String f = e.getFormat().replaceAll("\\{gang\\}", pClan).replaceAll("\\{GANG\\}", pClan);
			e.setFormat(f);
		}else{
			String f = e.getFormat().replaceAll("\\{gang\\}", null).replaceAll("\\{GANG\\}", null);
			e.setFormat(f);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player p = (Player) event.getPlayer();
		if(!p.hasPlayedBefore() || !f.getGangConfig().contains("players." + p.getUniqueId().toString())){
			f.getGangConfig().set("players." + p.getUniqueId().toString() + ".kills", 0);
			f.getGangConfig().set("players." + p.getUniqueId().toString() + ".deaths", 0);
			f.getGangConfig().set("players." + p.getUniqueId().toString() + ".kdr", 0);
		}
		p.sendMessage(ChatColor.BLUE + "This server is running PrisonGangs by mydeblob!");
	} 
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		Player victim = e.getEntity();
		int victimDeaths = f.getGangConfig().getInt("players." + victim.getUniqueId().toString() + ".deaths");
		victimDeaths++;
		f.getGangConfig().set("players." + victim.getUniqueId().toString() + ".deaths", victimDeaths);
		double dVictimDeaths = (double) f.getGangConfig().getInt("players." + victim.getUniqueId().toString() + ".deaths");
		double victimKills = (double) f.getGangConfig().getInt("players." + victim.getUniqueId().toString() + ".kills");
		if(f.getGangConfig().getInt("players." + victim.getUniqueId().toString() + ".kills") == 0 || f.getGangConfig().getDouble("players." + victim.getUniqueId().toString() + ".kdr") < 0.000){
			f.getGangConfig().set("players." + victim.getUniqueId().toString() + ".kdr", 0.0);
		}else{
			f.getGangConfig().set("players." + victim.getUniqueId().toString() + ".kdr", victimKills/dVictimDeaths);
		}
		if(victim.getKiller() instanceof Player){
			Player killer = (Player) e.getEntity().getKiller();
			int killerKills = f.getGangConfig().getInt("players." + killer.getUniqueId().toString() + ".kills");
			killerKills++;
			f.getGangConfig().set("players." + killer.getUniqueId().toString() + ".kills", killerKills);
			double killerDeaths = (double) f.getGangConfig().getInt("players." + killer.getUniqueId().toString() + ".deaths");
			double dKillerKills = (double) f.getGangConfig().getInt("players." + killer.getUniqueId().toString() + ".kills");
			if(f.getGangConfig().getInt("players." + killer.getUniqueId().toString() + ".deaths") == 0){
				double k = (double) killerKills;
				f.getGangConfig().set("players." + killer.getUniqueId().toString() + ".kdr", k);
			}else{
				f.getGangConfig().set("players." + killer.getUniqueId().toString() + ".kdr", dKillerKills/killerDeaths);
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
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
