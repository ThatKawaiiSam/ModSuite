package io.github.thatkawaiisam.modsuite.modules.staffchat;

import io.github.thatkawaiisam.pyrite.packet.PacketContainer;
import io.github.thatkawaiisam.pyrite.packet.PacketListener;
import io.github.thatkawaiisam.utils.MessageUtility;
import org.bukkit.Bukkit;

public class StaffChatPacketListener implements PacketContainer {

    private StaffChatModule module;

    /**
     * Staff Chat Packet Listener.
     *
     * @param module instance.
     */
    public StaffChatPacketListener(StaffChatModule module) {
        this.module = module;
    }

    @PacketListener(channels = { "ModSuite" })
    public void onStaffChat(StaffChatPacket packet) {
        // Constructing String from language file.
        final String toSend = this.module.getPlugin().getLanguage().getValue("Staff-Chat.Format", true)
                .replace("{server}", packet.getServer())
                .replace("{player}", packet.getUsername())
                .replace("{message}", packet.getMessage());

        // Sending to all players with permission.
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(this.module.getReceivePermission()))
                .forEach(player -> player.sendMessage(MessageUtility.formatMessage(toSend)));
    }
}
