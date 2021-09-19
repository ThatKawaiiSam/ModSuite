package io.github.thatkawaiisam.modsuite.connection;

import io.github.thatkawaiisam.artus.bungee.BungeeListener;
import io.github.thatkawaiisam.modsuite.shared.ServerSwitchPacket;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.event.EventHandler;

public class ConnectionListeners extends BungeeListener<ConnectionModule> {

    /**
     * Connection Listeners.
     *
     * @param module instance.
     */
    public ConnectionListeners(ConnectionModule module) {
        super(module);
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent event) {
        if (!event.getPlayer().hasPermission("ModSuite.Staff")) {
            return;
        }

        String from = null;
        if (event.getFrom() !=  null) {
            from = event.getFrom().getName();
        }

        this.getModule().getPlugin().getPyrite().sendPacket(
                new ServerSwitchPacket(
                        event.getPlayer().getName(),
                        event.getPlayer().getServer().getInfo().getName(),
                        from
                ),
                "ModSuite"
        );
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        if (event.getPlayer().getServer() == null) {
            return;
        }

        if (!event.getPlayer().hasPermission("ModSuite.Staff")) {
            return;
        }

        this.getModule().getPlugin().getPyrite().sendPacket(
                new ServerSwitchPacket(
                        event.getPlayer().getName(),
                        null,
                        event.getPlayer().getServer().getInfo().getName()
                ),
                "ModSuite"
        );
    }

}
