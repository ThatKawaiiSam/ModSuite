package io.github.thatkawaiisam.modsuite.modules.cache;

import io.github.thatkawaiisam.artus.bukkit.BukkitModule;
import io.github.thatkawaiisam.artus.module.ModuleLoadLevel;
import io.github.thatkawaiisam.modsuite.ModSuitePlugin;

import java.util.*;
import lombok.Getter;

public class CacheModule extends BukkitModule<ModSuitePlugin> {

    @Getter private Set<StaffProfile> profiles = new LinkedHashSet<>();

    /**
     * Cache Module.
     *
     * @param plugin instance.
     */
    public CacheModule(ModSuitePlugin plugin) {
        super(plugin, "cache");
        this.getOptions().setLoadLevel(ModuleLoadLevel.NORMAL);
    }

    @Override
    public void onEnable() {
        this.addListener(new CacheListeners(this));
    }

    @Override
    public void onDisable() {
        this.profiles.clear();
    }

    /**
     * Get's all of the Staff Profiles on the current server instance,
     *
     * @return list of Staff Profiles.
     */
    public Set<StaffProfile> getCurrentServerProfiles() {
        Set<StaffProfile> toReturn = new LinkedHashSet<>();
        for (StaffProfile profile : this.profiles) {
            if (profile.isOnCurrentServer()) {
                toReturn.add(profile);
            }
        }
        return toReturn;
    }

    /**
     * Get a player's StaffProfile.
     *
     * @param uuid of target player.
     * @return StaffProfile if it exists.
     */
    public StaffProfile getProfile(UUID uuid) {
        for (StaffProfile profile : this.profiles) {
            if (profile.getUuid().equals(uuid)) {
                return profile;
            }
        }
        return null;
    }

    /**
     * Load StaffProfile from cache.
     *
     * @param profile to load.
     */
    public void loadProfile(StaffProfile profile) {
        getPlugin().getPyrite().runRedisCommand(jedis -> {
            // Check that they actually exist as a value.
            if (!jedis.exists("ModSuite:" + profile.getUuid())) {
                return jedis;
            }
            // Grab of the profile information and apply if it exists.
            Map<String, String> data = jedis.hgetAll("ModSuite:" + profile.getUuid());
            if (data.containsKey("joinedTime")) {
                profile.setJoinedTime(Long.parseLong(data.get("joinedTime")));
            }
            if (data.containsKey("previousServer")) {
                profile.setPreviousServer(data.get("previousServer"));
            }
            // Ensure that the key TTL is now permanent.
            jedis.persist("ModSuite:" + profile.getUuid());
            return jedis;
        });
    }

    /**
     * Save Profile to Cache.
     *
     * @param profile to save.
     * @param quit if user has quit.
     */
    public void saveProfile(StaffProfile profile, boolean quit) {
        getPlugin().getPyrite().runRedisCommand(jedis -> {
            Map<String, String> data = new HashMap<>();

            // Constructing profile data.
            data.put("currentServer", profile.getCurrentServer());
            data.put("joinedTime", profile.getJoinedTime() + "");

            // If the profile has quit, be sure to state that this current instance is their previous server in the event
            // that the data is used or referenced else where.
            if (quit) {
                profile.setPreviousServer(profile.getCurrentServer());
                data.put("previousServer", profile.getPreviousServer());
            }

            jedis.hmset("ModSuite:" + profile.getUuid(), data);

            // If the profile has quit the current instance, add a TTL of 2 seconds in order for them to join the next server.
            // Note: There should be a more elegant system of doing this. Potentially through a separate bungee plugin?
            if (quit) {
                jedis.expire("ModSuite:" + profile.getUuid(), 3);
            }
            return jedis;
        });
    }

    /**
     * Checks if profile has been deleted from redis cache.
     *
     * @param profile to check.
     * @return if profile still exists.
     */
    public boolean checkDeletionFromCache(StaffProfile profile) {
        return this.getPlugin().getPyrite().<Boolean>runRedisCommand(jedis -> !jedis.exists("ModSuite:" + profile.getUuid()));
    }

}
