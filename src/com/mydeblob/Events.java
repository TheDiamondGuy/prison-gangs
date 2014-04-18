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
    public static final ClanManager t = ClanManager.getInstance();
	private ClansMain plugin;
	private CommandHandler ch;
	public Events(ClansMain plugin, CommandHandler ch){
		this.plugin = plugin;
		this.ch = ch;
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onChat(AsyncPlayerChatEvent event){
		if(t.getPlayerClan(event.getPlayer()) != null && !ch.inAllyChat.contains(event.getPlayer().getName()) && !ch.inClanChat.contains(event.getPlayer().getName())){
			String pClan = ChatColor.WHITE + "[" + ChatColor.DARK_GRAY + ClanManager.getInstance().getPlayerClan(event.getPlayer()).getName() + ChatColor.WHITE +  "]";
			String f = event.getFormat().replaceAll("\\{clan_name\\}", pClan);
			event.setFormat(f);
		}else{
			if(ch.inAllyChat.contains(event.getPlayer().getName())){
				String f = ChatColor.GRAY + event.getPlayer().getName() + ":" + ChatColor.LIGHT_PURPLE + event.getMessage();
				event.getPlayer().sendMessage("Ally chat with format " + f);
				event.setCancelled(true);
				t.getPlayerClan(event.getPlayer()).msg(t.getPlayerClan(event.getPlayer()), f);
			}if(ch.inClanChat.contains(event.getPlayer().getName())){
				event.setCancelled(true);
				String f = ChatColor.DARK_GRAY + event.getPlayer().getName() + ":" + ChatColor.BLUE + event.getMessage();
				event.getPlayer().sendMessage("Clan chat with format " + f);
				t.getPlayerClan(event.getPlayer()).msg(t.getPlayerClan(event.getPlayer()), f);
			}if(!ch.inClanChat.contains(event.getPlayer().getName()) && !ch.inAllyChat.contains(event.getPlayer().getName())){
				String f = event.getFormat().replaceAll("\\{clan_name\\}", "");
				event.setFormat(f);
			}
		}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player p = (Player) event.getPlayer();
		if(!p.hasPlayedBefore() || !plugin.getCustomConfig().contains("players." + p.getName())){
			plugin.getCustomConfig().set("players." + p.getName() + ".kills", 0);
			plugin.getCustomConfig().set("players." + p.getName() + ".deaths", 0);
			plugin.getCustomConfig().set("players." + p.getName() + ".kdr", 0);
		}
	} 
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		int deaths = plugin.getCustomConfig().getInt("players." + event.getEntity().getName() + ".deaths");
		deaths++;
		plugin.getCustomConfig().set("players." + event.getEntity().getName() + ".deaths", deaths);
		double edeaths = (double) plugin.getCustomConfig().getInt("players." + event.getEntity().getName() + ".deaths");
		double ekills = (double) plugin.getCustomConfig().getInt("players." + event.getEntity().getName() + ".kills");
		if(plugin.getCustomConfig().getInt("players." + event.getEntity().getName() + ".kills") == 0 || plugin.getCustomConfig().getDouble("players." + event.getEntity().getName() + ".kdr") < 0.000){
			plugin.getCustomConfig().set("players." + event.getEntity().getName() + ".kdr", 0.0);
		}else{
			plugin.getCustomConfig().set("players." + event.getEntity().getName() + ".kdr", ekills/edeaths);
		}
		if(event.getEntity().getKiller() instanceof Player){
			Player killer = (Player) event.getEntity().getKiller();
			int kills = plugin.getCustomConfig().getInt("players." + killer.getName() + ".kills");
			kills++;
			plugin.getCustomConfig().set("players." + killer.getName() + ".kills", kills);
			double ddeaths = (double) plugin.getCustomConfig().getInt("players." + event.getEntity().getKiller().getName() + ".deaths");
			double dkills = (double) plugin.getCustomConfig().getInt("players." + event.getEntity().getKiller().getName() + ".kills");
			if(plugin.getCustomConfig().getInt("players." + killer.getName() + ".deaths") == 0){
				double k = (double) kills;
				plugin.getCustomConfig().set("players." + killer.getName() + ".kdr", k);
			}else{
				plugin.getCustomConfig().set("players." + killer.getName() + ".kdr", dkills/ddeaths);
			}
		}
	}
	@EventHandler
	public void playerJoin(PlayerJoinEvent event){
		ClanManager.getInstance().setupClans();
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
			if(ClanManager.getInstance().getPlayerClan(target) == null){
				return;
			}if(ClanManager.getInstance().getPlayerClan(damager) == null){
				return;
			}if(ClanManager.getInstance().getPlayerClan(target).equals(ClanManager.getInstance().getPlayerClan(damager))){
				event.setCancelled(true);
			}
		}
	}
}