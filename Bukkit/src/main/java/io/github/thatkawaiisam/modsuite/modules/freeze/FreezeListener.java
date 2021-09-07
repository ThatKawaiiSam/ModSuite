package io.github.thatkawaiisam.modsuite.modules.freeze;

import io.github.thatkawaiisam.artus.bukkit.BukkitListener;
import io.github.thatkawaiisam.modsuite.modules.freeze.packet.impl.FreezePlayerPacket;
import io.github.thatkawaiisam.modsuite.modules.freeze.packet.impl.FreezeQuitPacket;
import io.github.thatkawaiisam.modsuite.modules.reports.ReportPacket;
import io.github.thatkawaiisam.modsuite.modules.reports.ReportPacketListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FreezeListener extends BukkitListener<FreezeModule> {

    /**
     * The freeze listener for the freeze module
     *
     * @param module the module instance
     */
    public FreezeListener(FreezeModule module) {
        super(module);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (this.getModule().isFrozen(event.getPlayer().getUniqueId())) {
            this.getModule().unfreeze(event.getPlayer().getUniqueId());

            if (!this.getModule().isBroadcastThroughRedis()) {
                String message = this.getModule().getPlugin().getLanguage().getValue("Freeze.On-Quit", true)
                        .replace("{player}", event.getPlayer().getName());

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission(this.getModule().getFreezePermission())) {
                        player.sendMessage(message);
                    }
                }
            } else {
                this.getModule().getPlugin().getPyrite().sendPacket(
                        new FreezeQuitPacket(event.getPlayer().getName()), "ModSuite");
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.getModule().isFrozen(event.getPlayer().getUniqueId())) {
            FrozenPlayer frozenPlayer = this.getModule().getFrozenPlayersMap().get(event.getPlayer().getUniqueId());
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
