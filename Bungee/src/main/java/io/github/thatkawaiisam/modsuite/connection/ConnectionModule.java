package io.github.thatkawaiisam.modsuite.connection;

import io.github.thatkawaiisam.artus.bungee.BungeeModule;
import io.github.thatkawaiisam.modsuite.ModSuitePlugin;

public class ConnectionModule extends BungeeModule<ModSuitePlugin> {

    /**
     * Connection Module.
     *
     * @param plugin instance.
     */
    public ConnectionModule(ModSuitePlugin plugin) {
        super(plugin, "cache");
    }

    @Override
    public void onEnable() {
        addListener(new ConnectionListeners(this));
    }

    @Override
    public void onDisable() {

    }
}
