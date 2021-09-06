package io.github.thatkawaiisam.modsuite.modules.reports;

import io.github.thatkawaiisam.pyrite.packet.PacketContainer;
import io.github.thatkawaiisam.pyrite.packet.PacketListener;
import io.github.thatkawaiisam.utils.MessageUtility;
import org.bukkit.Bukkit;

public class ReportPacketListener implements PacketContainer {

    private ReportsModule module;

    /**
     * Report Packet Listener.
     *
     * @param module instance.
     */
    public ReportPacketListener(ReportsModule module) {
        this.module = module;
    }

    @PacketListener(channels = { "ModSuite" })
    public void onReport(ReportPacket packet) {
        // Sending to all players with permission.
        final String toSend = this.module.getPlugin().getLanguage().getValue("Report.Format", true)
                .replace("{server}", packet.getServer())
                .replace("{reporter}", packet.getReporter())
                .replace("{reported}", packet.getReported())
                .replace("{reason}", packet.getReason());

        // Sending to all players with permission.
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(this.module.getReceivePermission()))
                .forEach(player -> player.sendMessage(MessageUtility.formatMessage(toSend)));
    }
}
