package io.github.thatkawaiisam.modsuite.modules.connection;

import io.github.thatkawaiisam.modsuite.shared.ServerSwitchPacket;
import io.github.thatkawaiisam.pyrite.packet.PacketContainer;
import io.github.thatkawaiisam.pyrite.packet.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConnectionPacketListener implements PacketContainer {

    private ConnectionModule module;

    /**
     * Connection Packet Listener.
     *
     * @param module instance.
     */
    public ConnectionPacketListener(ConnectionModule module) {
        this.module = module;
    }

    @PacketListener(channels = { "ModSuite" })
    public void onServerSwitch(ServerSwitchPacket packet) {
        // Network Join.
        if (packet.getPreviousServer() == null && packet.getCurrentServer() != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("Network.Receive")) {
                    final String toSend = this.module.getPlugin().getLanguage().getValue("Connection.Join-Network", true)
                            .replace("{server}", packet.getCurrentServer())
                            .replace("{player}", packet.getUsername());
                    player.sendMessage(toSend);
                }
            }
            return;
        }
        // Network Leave.
        if (packet.getPreviousServer() != null && packet.getCurrentServer() == null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("Network.Receive")) {
                    final String toSend = this.module.getPlugin().getLanguage().getValue("Connection.Leave-Network", true)
                            .replace("{server}", packet.getPreviousServer())
                            .replace("{player}", packet.getUsername());
                    player.sendMessage(toSend);
                }
            }
            return;
        }
        // Joining current server.
        if (packet.getCurrentServer().equals(this.module.getPlugin().getServerName())) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("Network.Receive")) {
                    final String toSend = this.module.getPlugin().getLanguage().getValue("Connection.Switch-To", true)
                            .replace("{server}", packet.getPreviousServer())
                            .replace("{player}", packet.getUsername());
                    player.sendMessage(toSend);
                }
            }
            return;
        }
        // Leaving current server.
        if (packet.getPreviousServer().equals(this.module.getPlugin().getServerName())) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("Network.Receive")) {
                    final String toSend = this.module.getPlugin().getLanguage().getValue("Connection.Switch-From", true)
                            .replace("{server}", packet.getCurrentServer())
                            .replace("{player}", packet.getUsername());
                    player.sendMessage(toSend);
                }
            }
            return;
        }
    }

}
