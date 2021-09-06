package io.github.thatkawaiisam.modsuite.modules.connection;

import io.github.thatkawaiisam.artus.bukkit.BukkitModule;
import io.github.thatkawaiisam.modsuite.ModSuitePlugin;

public class ConnectionModule extends BukkitModule<ModSuitePlugin> {

    private ConnectionPacketListener packetListener;

    /**
     * Connection Module.
     *
     * @param plugin instance.
     */
    public ConnectionModule(ModSuitePlugin plugin) {
        super(plugin, "connection");
        this.getOptions().setGenerateConfiguration(false);
    }

    @Override
    public void onEnable() {
        this.packetListener = new ConnectionPacketListener(this);
        this.getPlugin().getPyrite().registerContainer(this.packetListener);
    }

    @Override
    public void onDisable() {
        this.getPlugin().getPyrite().unregisterContainer(this.packetListener);
    }

}
