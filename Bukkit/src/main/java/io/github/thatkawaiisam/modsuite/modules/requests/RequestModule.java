package io.github.thatkawaiisam.modsuite.modules.requests;

import io.github.thatkawaiisam.artus.bukkit.BukkitModule;
import io.github.thatkawaiisam.modsuite.ModSuitePlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import lombok.Getter;

@Getter
public class RequestModule extends BukkitModule<ModSuitePlugin> {

    private RequestPacketListener packetListener;
    private String receivePermission, sendPermission;

    /**
     * Request Module.
     *
     * @param plugin instance.
     */
    public RequestModule(ModSuitePlugin plugin) {
        super(plugin, "request");
        this.getOptions().setGenerateConfiguration(true);
    }

    @Override
    public void onEnable() {
        Configuration c = this.getConfiguration().getImplementation();
        this.receivePermission = c.getString("Receive-Permission");
        this.sendPermission = c.getString("Send-Permission");

        // Packet Listener.
        this.packetListener = new RequestPacketListener(this);
        this.getPlugin().getPyrite().registerContainer(packetListener);

        // Commands.
        this.addCommand(new RequestCommand(this));
    }

    @Override
    public void onDisable() {
        this.getPlugin().getPyrite().unregisterContainer(packetListener);
    }

    /**
     * Broadcast request message over the message queue.
     *
     * @param player that is sending the message.
     * @param message to send.
     */
    public void sendMessage(Player player, String message) {
        this.getPlugin().getPyrite().sendPacket(
                new RequestPacket(
                        player.getName(),
                        message,
                        getPlugin().getServerName()
                ),
                "ModSuite"
        );
    }

}
