package io.github.thatkawaiisam.modsuite.modules.cache;

import io.github.thatkawaiisam.artus.bukkit.BukkitListener;
import io.github.thatkawaiisam.modsuite.modules.connection.ServerSwitchPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CacheListeners extends BukkitListener<CacheModule> {

    /**
     * Cache Listeners.
     *
     * @param module instance.
     */
    public CacheListeners(CacheModule module) {
        super(module);
    }

    @EventHandler
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event) {
        final Player player = Bukkit.getPlayer(event.getUniqueId());

        // Prevent weird race conditions with rejoining.
        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage("You tried to login too quickly.");
            this.getModule().getPlugin().getServer().getScheduler().runTask(getModule().getPlugin(), () -> player.kickPlayer("Duplicate login."));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("ModSuite.Staff")) {
            return;
        }

        // Get Staff Profile.
        StaffProfile profile = this.getModule().getProfile(event.getPlayer().getUniqueId());

        // Create Staff Profile if not found in local cache.
        if (profile == null) {
            profile = new StaffProfile(event.getPlayer().getUniqueId());
            profile.setCurrentServer(getModule().getPlugin().getServerName());
            this.getModule().getProfiles().add(profile);
        }

        // Load and Save Profile to update various cache components across the network.
        this.getModule().loadProfile(profile);
        this.getModule().saveProfile(profile, false);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Check if the player has a StaffProfile or not.
        StaffProfile profile = this.getModule().getProfile(event.getPlayer().getUniqueId());
        if (profile == null) {
            return;
        }

        this.getModule().getPlugin().getPyrite().sendPacket(
                new ServerSwitchPacket(
                        event.getPlayer().getName(),
                        getModule().getPlugin().getServerName(),
                        profile.getPreviousServer()
                ),
                "ModSuite"
        );
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // Check if the player has a StaffProfile or not.
        StaffProfile profile = this.getModule().getProfile(event.getPlayer().getUniqueId());
        if (profile == null) { // Probably didn't have enough time to generate.
            return;
        }

        // Save and remove.
        this.getModule().saveProfile(profile, true);
        this.getModule().getProfiles().remove(profile);

        // Clean up cache.
        new BukkitRunnable() {
            @Override
            public void run() {
                boolean noLongerOnNetwork = getModule().checkDeletionFromCache(profile);
                // TODO: Write a bungeecord implementation so that it can decide when a player actually leaves the netwrok.
                if (noLongerOnNetwork) {
                    getModule().getPlugin().getPyrite().sendPacket(
                            new ServerSwitchPacket(
                                    event.getPlayer().getName(),
                                    profile.getPreviousServer(),
                                    null
                            ),
                            "ModSuite"
                    );
                }
            }
        }.runTaskLater(this.getModule().getPlugin(), 50);
    }
}
