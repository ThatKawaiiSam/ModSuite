package io.github.thatkawaiisam.modsuite.modules.staffchat;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Syntax;
import io.github.thatkawaiisam.artus.bukkit.BukkitCommand;
import io.github.thatkawaiisam.utils.MessageUtility;
import org.bukkit.entity.Player;

public class StaffChatCommand extends BukkitCommand<StaffChatModule> {

    /**
     * Staff Chat Command.
     *
     * @param module instance.
     */
    public StaffChatCommand(StaffChatModule module) {
        super(module);
    }

    @Syntax("<message>")
    @CommandAlias("staffchat|sc")
    public void onStaffChat(Player player, String message) {
        // Check for permission.
        if (!player.hasPermission(getModule().getSendPermission())) {
            player.sendMessage(MessageUtility.formatMessage("&cNo permission!"));
            return;
        }

        // Send message packet.
        this.getModule().sendMessage(player, message);
    }

}
