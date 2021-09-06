package io.github.thatkawaiisam.modsuite.modules.requests;

import io.github.thatkawaiisam.pyrite.packet.PacketContainer;
import io.github.thatkawaiisam.pyrite.packet.PacketListener;
import io.github.thatkawaiisam.utils.MessageUtility;
import org.bukkit.Bukkit;

public class RequestPacketListener implements PacketContainer {

    private RequestModule module;

    /**
     * Request Packet Listener.
     *
     * @param module instance.
     */
    public RequestPacketListener(RequestModule module) {
        this.module = module;
    }

    @PacketListener(channels = { "ModSuite" })
    public void onRequest(RequestPacket packet) {
        // Constructing String from language file.
        final String toSend = this.module.getPlugin().getLanguage().getValue("Request.Format", true)
                .replace("{server}", packet.getServer())
                .replace("{player}", packet.getUsername())
                .replace("{message}", packet.getMessage());

        // Sending to all players with permission.
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(this.module.getReceivePermission()))
                .forEach(player -> player.sendMessage(MessageUtility.formatMessage(toSend)));
    }
}
