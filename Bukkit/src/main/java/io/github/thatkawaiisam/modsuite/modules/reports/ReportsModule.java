package io.github.thatkawaiisam.modsuite.modules.reports;

import io.github.thatkawaiisam.artus.bukkit.BukkitModule;
import io.github.thatkawaiisam.modsuite.ModSuitePlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import lombok.Getter;

@Getter
public class ReportsModule extends BukkitModule<ModSuitePlugin> {

    private ReportPacketListener packetListener;
    private String receivePermission, sendPermission;

    /**
     * Reports Module.
     *
     * @param plugin instance.
     */
    public ReportsModule(ModSuitePlugin plugin) {
        super(plugin, "reports");
        this.getOptions().setGenerateConfiguration(true);
    }

    @Override
    public void onEnable() {
        // Permission Values.
        Configuration c = this.getConfiguration().getImplementation();
        this.receivePermission = c.getString("Receive-Permission");
        this.sendPermission = c.getString("Send-Permission");

        // Packet Listener.
        this.packetListener = new ReportPacketListener(this);
        this.getPlugin().getPyrite().registerContainer(this.packetListener);

        // Commands.
        this.addCommand(new ReportCommand(this));
    }

    @Override
    public void onDisable() {
        this.getPlugin().getPyrite().unregisterContainer(this.packetListener);
    }

    /**
     * Broadcast connection message over the message queue.
     *
     * @param reporter of issue.
     * @param reported player.
     * @param reason to warrant a report.
     */
    public void sendMessage(Player reporter, Player reported, String reason) {
        this.getPlugin().getPyrite().sendPacket(
                new ReportPacket(
                        reporter.getName(),
                        reported.getName(),
                        reason,
                        getPlugin().getServerName()
                ),
                "ModSuite"
        );
    }

}
