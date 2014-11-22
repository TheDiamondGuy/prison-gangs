package com.cullan.prisongangs;

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

public class Events
  implements Listener
{
  private static GangManager gm = GangManager.getGangManager();
  private static FileManager f = FileManager.getFileManager();
  private PrisonGangs plugin;
  
  public Events(PrisonGangs plugin)
  {
    this.plugin = plugin;
  }
  
  @EventHandler(priority=EventPriority.HIGH)
  public void onChat(AsyncPlayerChatEvent e)
  {
    Player p = e.getPlayer();
    if (gm.getGangWithPlayer(p) != null)
    {
      Gang g = gm.getGangWithPlayer(p);
      try {
      String pGang = ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("format")).replaceAll("%g%", g.getName().replace("$", "s"));
      String f = e.getFormat().replaceAll("\\{gang\\}", pGang).replaceAll("\\{GANG\\}", pGang);
      e.setFormat(f);
      }
      catch (Exception e2) {}
    }
    else
    {
      String f = e.getFormat().replaceAll("\\{gang\\}", "").replaceAll("\\{GANG\\}", "");
      e.setFormat(f);
    }
  }
  
  @EventHandler
  public void onGangChat(AsyncPlayerChatEvent e)
  {
    Player p = e.getPlayer();
    if (gm.getGangWithPlayer(p) == null) {
      return;
    }
    Gang g = gm.getGangWithPlayer(p);
    if (!gm.isInGangChat(p)) {
      return;
    }
    String format = ChatColor.DARK_GRAY + "[" + g.getName() + "] " + ChatColor.GRAY + "[" + gm.getPlayerRank(p.getName(), g).toText() + "] " + ChatColor.GREEN + p.getName() + ": " + ChatColor.GRAY + e.getMessage();
    e.setCancelled(true);
    gm.messageGang(g, format);
  }
  
  @EventHandler
  public void onJoin(PlayerJoinEvent e)
  {
    Player p = e.getPlayer();
    Gang g = gm.getGangWithPlayer(p);
    if ((gm.getGangWithPlayer(p) != null) && 
      (g.getName().contains("\""))) {
      gm.disbandGang(p, g.getName());
    }
    if ((!p.hasPlayedBefore()) || (!f.getKdrConfig().contains("players." + p.getUniqueId().toString())))
    {
      f.getKdrConfig().set("players." + p.getUniqueId().toString() + ".kills", Integer.valueOf(0));
      f.getKdrConfig().set("players." + p.getUniqueId().toString() + ".deaths", Integer.valueOf(0));
      f.getKdrConfig().set("players." + p.getUniqueId().toString() + ".kdr", Integer.valueOf(0));
    }
    if ((gm.getGangWithPlayer(p) != null) && (
      (!p.hasPlayedBefore()) || (!f.getKdrConfig().contains("gang-names." + g.getName()))))
    {
      f.getKdrConfig().set("gang-names." + g.getName() + ".kills", Integer.valueOf(0));
      f.getKdrConfig().set("gang-names." + g.getName().toString() + ".deaths", Integer.valueOf(0));
      f.getKdrConfig().set("gang-names." + g.getName() + ".kdr", Integer.valueOf(0));
    }
  }
  
  @EventHandler
  public void onGangDeath(PlayerDeathEvent e)
  {
    Player victim = e.getEntity();
    if (((victim.getKiller() instanceof Player)) && 
      (gm.getGangWithPlayer(victim) != null))
    {
      Gang v = gm.getGangWithPlayer(victim);
      int victimDeaths = f.getKdrConfig().getInt("gang-names." + v.getName() + ".deaths");
      victimDeaths++;
      f.getKdrConfig().set("gang-names." + v.getName() + ".deaths", Integer.valueOf(victimDeaths));
      double dVictimDeaths = f.getKdrConfig().getInt("gang-names." + v.getName() + ".deaths");
      double victimKills = f.getKdrConfig().getInt("gang-names." + v.getName() + ".kills");
      if ((f.getKdrConfig().getInt("gang-names." + v.getName() + ".kills") == 0) || (f.getKdrConfig().getDouble("gang-names." + v.getName() + ".kdr") < 0.0D)) {
        f.getKdrConfig().set("gang-names." + v.getName() + ".kdr", Double.valueOf(0.0D));
      } else {
        f.getKdrConfig().set("gang-names." + v.getName() + ".kdr", Double.valueOf(victimKills / dVictimDeaths));
      }
    }
    if ((victim.getKiller() instanceof Player))
    {
      Player killer = e.getEntity().getKiller();
      if (gm.getGangWithPlayer(killer) != null)
      {
        Gang k = gm.getGangWithPlayer(killer);
        int killerKills = f.getKdrConfig().getInt("gang-names." + k.getName() + ".kills");
        killerKills++;
        f.getKdrConfig().set("gang-names." + k.getName() + ".kills", Integer.valueOf(killerKills));
        double killerDeaths = f.getKdrConfig().getInt("gang-names." + k.getName() + ".deaths");
        double dKillerKills = f.getKdrConfig().getInt("gang-names." + k.getName() + ".kills");
        if (f.getKdrConfig().getInt("gang-names." + k.getName() + ".deaths") == 0)
        {
          double kills = killerKills;
          f.getKdrConfig().set("gang-names." + k.getName() + ".kdr", Double.valueOf(kills));
        }
        else
        {
          f.getKdrConfig().set("gang-names." + k.getName() + ".kdr", Double.valueOf(dKillerKills / killerDeaths));
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e)
  {
    Player victim = e.getEntity();
    if ((victim.getKiller() instanceof Player))
    {
      int victimDeaths = f.getKdrConfig().getInt("players." + victim.getUniqueId().toString() + ".deaths");
      victimDeaths++;
      f.getKdrConfig().set("players." + victim.getUniqueId().toString() + ".deaths", Integer.valueOf(victimDeaths));
      double dVictimDeaths = f.getKdrConfig().getInt("players." + victim.getUniqueId().toString() + ".deaths");
      double victimKills = f.getKdrConfig().getInt("players." + victim.getUniqueId().toString() + ".kills");
      if ((f.getKdrConfig().getInt("players." + victim.getUniqueId().toString() + ".kills") == 0) || (f.getKdrConfig().getDouble("players." + victim.getUniqueId().toString() + ".kdr") < 0.0D)) {
        f.getKdrConfig().set("players." + victim.getUniqueId().toString() + ".kdr", Double.valueOf(0.0D));
      } else {
        f.getKdrConfig().set("players." + victim.getUniqueId().toString() + ".kdr", Double.valueOf(victimKills / dVictimDeaths));
      }
    }
    if ((victim.getKiller() instanceof Player))
    {
      Player killer = e.getEntity().getKiller();
      int killerKills = f.getKdrConfig().getInt("players." + killer.getUniqueId().toString() + ".kills");
      killerKills++;
      f.getKdrConfig().set("players." + killer.getUniqueId().toString() + ".kills", Integer.valueOf(killerKills));
      double killerDeaths = f.getKdrConfig().getInt("players." + killer.getUniqueId().toString() + ".deaths");
      double dKillerKills = f.getKdrConfig().getInt("players." + killer.getUniqueId().toString() + ".kills");
      if (f.getKdrConfig().getInt("players." + killer.getUniqueId().toString() + ".deaths") == 0)
      {
        double k = killerKills;
        f.getKdrConfig().set("players." + killer.getUniqueId().toString() + ".kdr", Double.valueOf(k));
      }
      else
      {
        f.getKdrConfig().set("players." + killer.getUniqueId().toString() + ".kdr", Double.valueOf(dKillerKills / killerDeaths));
      }
    }
  }
  
  @EventHandler
  public void onDamage(EntityDamageByEntityEvent event)
  {
    if (this.plugin.getConfig().getBoolean("friendly-fire")) {
      return;
    }
    if ((event.getEntity() instanceof Player))
    {
      Player target = (Player)event.getEntity();
      Player damager = null;
      if ((event.getDamager() instanceof Player))
      {
        damager = (Player)event.getDamager();
      }
      else if ((event.getDamager() instanceof Projectile))
      {
        Projectile proj = (Projectile)event.getDamager();
        if ((proj.getShooter() instanceof Player)) {
          damager = (Player)proj.getShooter();
        }
      }
      if (damager == null) {
        return;
      }
      if (gm.getGangWithPlayer(target) == null) {
        return;
      }
      if (gm.getGangWithPlayer(damager) == null) {
        return;
      }
      if (gm.getGangWithPlayer(target) == gm.getGangWithPlayer(damager)) {
        event.setCancelled(true);
      }
    }
  }
}
