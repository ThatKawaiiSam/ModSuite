package io.github.thatkawaiisam.modsuite.modules.cache;

import io.github.thatkawaiisam.utils.CachedInventory;
import io.github.thatkawaiisam.utils.UUIDUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StaffProfile {

    private final UUID uuid;

    private long joinedTime = System.currentTimeMillis();
    private String currentServer = null;
    private String previousServer = null;
    private String name = "Unknown";

    /**
     * Staff Profile
     *
     * @param uuid of player.
     */
    public StaffProfile(UUID uuid) {
        this.uuid = uuid;
        // Fetches offline name in the event that the staff profile
        // has come from another networked server.
        UUIDUtility.getName(uuid).whenComplete((uuidPair, throwable) -> {
            this.name = uuidPair.getName();
        });
    }

    /**
     * Check if a Staff Profile is on current instance.
     *
     * @return whether this profile's UUID is online according to bukkit.
     */
    public boolean isOnCurrentServer() {
        Player player = Bukkit.getPlayer(this.uuid);
        return player != null && player.isOnline();
    }

}
