package io.github.thatkawaiisam.modsuite;

import io.github.thatkawaiisam.artus.bungee.BungeePlugin;
import io.github.thatkawaiisam.modsuite.connection.ConnectionModule;
import io.github.thatkawaiisam.modsuite.redis.RedisModule;
import io.github.thatkawaiisam.pyrite.Pyrite;

public class ModSuitePlugin extends BungeePlugin {

    @Override
    public void onEnable() {
        // Add all modules.
        this.getModuleFactory().addModule(new RedisModule(this));
        this.getModuleFactory().addModule(new ConnectionModule(this));

        // Enable modules.
        this.getModuleFactory().enableModules();
    }

    @Override
    public void onDisable() {
        // Disable modules.
        this.getModuleFactory().disableModules();
    }

    /**
     * Get ModSuite Pyrite from Redis Module.
     *
     * @return Pyrite instance.
     */
    public Pyrite getPyrite() {
        return this.getModuleFactory().<RedisModule>getModule("redis").getPyrite();
    }
}
