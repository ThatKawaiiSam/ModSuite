package io.github.thatkawaiisam.modsuite.modules.freeze.packet;

import io.github.thatkawaiisam.modsuite.modules.freeze.FreezeModule;
import io.github.thatkawaiisam.modsuite.modules.freeze.packet.impl.FreezePlayerPacket;
import io.github.thatkawaiisam.modsuite.modules.freeze.packet.impl.FreezeQuitPacket;
import io.github.thatkawaiisam.pyrite.packet.PacketContainer;
import io.github.thatkawaiisam.pyrite.packet.PacketListener;
import io.github.thatkawaiisam.utils.MessageUtility;
import org.bukkit.Bukkit;

public class FreezePacketListener implements PacketContainer {

    private FreezeModule module;

    /**
     * Freeze Packet Listener.
     *
     * @param module instance.
     */
    public FreezePacketListener(FreezeModule module) {
        this.module = module;
    }

    @PacketListener(channels = { "ModSuite" })
    public void onFreeze(FreezePlayerPacket packet) {
        final String toSend = this.module.getPlugin().getLanguage().getValue("Freeze.On-Freeze-Broadcast", true)
                .replace("{player}", packet.getPlayer())
                .replace("{target}", packet.getTarget());

        // Sending to all players with permission.
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(this.module.getFreezePermission()))
                .forEach(player -> player.sendMessage(MessageUtility.formatMessage(toSend)));
    }

    @PacketListener(channels = { "ModSuite" })
    public void onFreezeQuit(FreezeQuitPacket packet) {
        final String toSend = this.module.getPlugin().getLanguage().getValue("Freeze.On-Quit", true)
                .replace("{player}", packet.getPlayer());

        // Sending to all players with permission.
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(this.module.getFreezePermission()))
                .forEach(player -> player.sendMessage(MessageUtility.formatMessage(toSend)));
    }

}
