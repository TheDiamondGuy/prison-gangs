package com.mydeblob;

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
    public static final GangManager t = GangManager.getInstance();
	private PrisonGangs plugin;
	public Events(PrisonGangs plugin){
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onChat(AsyncPlayerChatEvent event){
		if(t.getPlayerClan(event.getPlayer()) != null){
			String pClan = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("format")).replaceAll("%g%", GangManager.getInstance().getPlayerClan(event.getPlayer()).getName());
			String f = event.getFormat().replaceAll("\\{clan_name\\}", pClan);
			event.setFormat(f);
		}else{
				String f = event.getFormat().replaceAll("\\{clan_name\\}", null);
				event.setFormat(f);
		}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player p = (Player) event.getPlayer();
		if(!p.hasPlayedBefore() || !plugin.getGangConfig().contains("players." + p.getUniqueId().toString())){
			plugin.getGangConfig().set("players." + p.getUniqueId().toString() + ".kills", 0);
			plugin.getGangConfig().set("players." + p.getUniqueId().toString() + ".deaths", 0);
			plugin.getGangConfig().set("players." + p.getUniqueId().toString() + ".kdr", 0);
		}
	} 
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		int deaths = plugin.getGangConfig().getInt("players." + event.getEntity().getName() + ".deaths");
		deaths++;
		plugin.getGangConfig().set("players." + event.getEntity().getName() + ".deaths", deaths);
		double edeaths = (double) plugin.getGangConfig().getInt("players." + event.getEntity().getName() + ".deaths");
		double ekills = (double) plugin.getGangConfig().getInt("players." + event.getEntity().getName() + ".kills");
		if(plugin.getGangConfig().getInt("players." + event.getEntity().getName() + ".kills") == 0 || plugin.getGangConfig().getDouble("players." + event.getEntity().getName() + ".kdr") < 0.000){
			plugin.getGangConfig().set("players." + event.getEntity().getName() + ".kdr", 0.0);
		}else{
			plugin.getGangConfig().set("players." + event.getEntity().getName() + ".kdr", ekills/edeaths);
		}
		if(event.getEntity().getKiller() instanceof Player){
			Player killer = (Player) event.getEntity().getKiller();
			int kills = plugin.getGangConfig().getInt("players." + killer.getName() + ".kills");
			kills++;
			plugin.getGangConfig().set("players." + killer.getName() + ".kills", kills);
			double ddeaths = (double) plugin.getGangConfig().getInt("players." + event.getEntity().getKiller().getName() + ".deaths");
			double dkills = (double) plugin.getGangConfig().getInt("players." + event.getEntity().getKiller().getName() + ".kills");
			if(plugin.getGangConfig().getInt("players." + killer.getName() + ".deaths") == 0){
				double k = (double) kills;
				plugin.getGangConfig().set("players." + killer.getName() + ".kdr", k);
			}else{
				plugin.getGangConfig().set("players." + killer.getName() + ".kdr", dkills/ddeaths);
			}
		}
	}
	@EventHandler
	public void playerJoin(PlayerJoinEvent event){
		GangManager.getInstance().setupClans();
	}
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			Player target = (Player) event.getEntity();
			Player damager = null;
			if(event.getDamager() instanceof Player){
				damager = (Player) event.getDamager();
			}if(event.getDamager() instanceof Projectile){
				Projectile proj = (Projectile) event.getDamager();
				if(proj.getShooter() instanceof Player){
					damager = (Player) proj.getShooter();
				}
			}
			if(GangManager.getInstance().getPlayerClan(target) == null){
				return;
			}if(GangManager.getInstance().getPlayerClan(damager) == null){
				return;
			}if(GangManager.getInstance().getPlayerClan(target).equals(GangManager.getInstance().getPlayerClan(damager))){
				event.setCancelled(true);
			}
		}
	}
}
