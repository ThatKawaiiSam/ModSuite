package io.github.thatkawaiisam.modsuite.modules.freeze;

import io.github.thatkawaiisam.artus.bukkit.BukkitListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FreezeListener extends BukkitListener<FreezeModule> {

    public FreezeListener(FreezeModule module) {
        super(module);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (this.getModule().isFrozen(event.getPlayer().getUniqueId())) {
            this.getModule().unfreeze(event.getPlayer().getUniqueId());

            String message = this.getModule().getPlugin().getLanguage().getValue("Freeze.On-Quit", true)
                    .replace("%player%", event.getPlayer().getName());

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(this.getModule().freezePermission)) {
                    player.sendMessage(message);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.getModule().isFrozen(event.getPlayer().getUniqueId())) {
            FrozenPlayer frozenPlayer = this.getModule().frozenPlayersMap.get(event.getPlayer().getUniqueId());
            Location to = event.getTo();
            boolean hasMoved = frozenPlayer.location[0] != to.getBlockX() || frozenPlayer.location[1] != to.getBlockY() || frozenPlayer.location[2] != to.getBlockZ();
            if (hasMoved) {
                to.setX(frozenPlayer.location[0]);
                to.setY(frozenPlayer.location[1]);
                to.setZ(frozenPlayer.location[2]);
                event.setTo(to);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (this.getModule().isFrozen(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player target = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();

            if (this.getModule().isFrozen(target.getUniqueId())) {
                event.setCancelled(true);
                attacker.sendMessage(this.getModule().getPlugin().getLanguage().getValue("Freeze.On-Damage", true));
            }
        }
    }
}
