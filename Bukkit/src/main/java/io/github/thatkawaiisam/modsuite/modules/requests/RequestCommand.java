package io.github.thatkawaiisam.modsuite.modules.requests;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Syntax;
import io.github.thatkawaiisam.artus.bukkit.BukkitCommand;
import io.github.thatkawaiisam.utils.MessageUtility;
import org.bukkit.entity.Player;

public class RequestCommand extends BukkitCommand<RequestModule> {

    /**
     * Request Command.
     *
     * @param module instance.
     */
    public RequestCommand(RequestModule module) {
        super(module);
    }

    @Syntax("<message>")
    @CommandAlias("request|helpop")
    public void onRequest(Player player, String message) {
        // Check permission.
        if (!player.hasPermission(getModule().getSendPermission())) {
            player.sendMessage(MessageUtility.formatMessage("&cNo permission!"));
            return;
        }
        // Send Message Packet.
        this.getModule().sendMessage(player, message);
    }

}
