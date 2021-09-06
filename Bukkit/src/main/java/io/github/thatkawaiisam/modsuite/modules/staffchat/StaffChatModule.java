package io.github.thatkawaiisam.modsuite.modules.staffchat;

import io.github.thatkawaiisam.artus.bukkit.BukkitModule;
import io.github.thatkawaiisam.modsuite.ModSuitePlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import lombok.Getter;

@Getter
public class StaffChatModule extends BukkitModule<ModSuitePlugin> {

    private StaffChatPacketListener packetListener;
    private String receivePermission, sendPermission;

    /**
     * Staff Chat Module.
     *
     * @param plugin instance.
     */
    public StaffChatModule(ModSuitePlugin plugin) {
        super(plugin, "staffchat");
        this.getOptions().setGenerateConfiguration(true);
    }

    @Override
    public void onEnable() {
        // Configuration.
        Configuration c = this.getConfiguration().getImplementation();
        this.receivePermission = c.getString("Receive-Permission");
        this.sendPermission = c.getString("Send-Permission");

        // Packet Listener.
        this.packetListener = new StaffChatPacketListener(this);
        this.getPlugin().getPyrite().registerContainer(this.packetListener);

        // Command.
        this.addCommand(new StaffChatCommand(this));
    }

    @Override
    public void onDisable() {
        this.getPlugin().getPyrite().unregisterContainer(this.packetListener);
    }

    /**
     * Broadcast staff chat message over the message queue.
     *
     * @param player that sent the message.
     * @param message to be sent.
     */
    public void sendMessage(Player player, String message) {
        this.getPlugin().getPyrite().sendPacket(
                new StaffChatPacket(
                        player.getName(),
                        message,
                        getPlugin().getServerName()
                ),
                "ModSuite"
        );
    }

}
