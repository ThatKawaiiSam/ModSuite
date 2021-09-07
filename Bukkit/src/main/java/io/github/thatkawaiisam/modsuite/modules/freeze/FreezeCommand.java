package io.github.thatkawaiisam.modsuite.modules.freeze;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Syntax;
import io.github.thatkawaiisam.artus.bukkit.BukkitCommand;
import io.github.thatkawaiisam.modsuite.modules.freeze.packet.impl.FreezePlayerPacket;
import io.github.thatkawaiisam.modsuite.modules.freeze.packet.impl.FreezeQuitPacket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FreezeCommand extends BukkitCommand<FreezeModule> {

    /**
     * The freeze command for the freeze module
     *
     * @param module the module instance.
     */
    public FreezeCommand(FreezeModule module) {
        super(module);
    }

    @Syntax("<player>")
    @CommandAlias("freeze|ss")
    public void onFreezeCommand(Player player, Player target) {
        //Checks if the player has the permission
        if (!player.hasPermission(this.getModule().getFreezePermission())){
            player.sendMessage(ChatColor.RED + "No permission");
            return;
        }

        //Checks if the player is the target
        if (player.getUniqueId().equals(target.getUniqueId())){
            player.sendMessage(this.getModule().getPlugin().getLanguage().getValue("Freeze.Cannot-Freeze-Yourself", true));
            return;
        }

        //Checks if the target is op to prevent others from freezing operators
        if (target.isOp()){
            player.sendMessage(this.getModule().getPlugin().getLanguage().getValue("Freeze.Cannot-Freeze-Them", true));
            return;
        }

        //Only allow operators to freeze other staff members that can freeze, but dont allow staff to freeze eachother.
        if (!player.isOp() && target.hasPermission(this.getModule().getFreezePermission())){
            player.sendMessage(this.getModule().getPlugin().getLanguage().getValue("Freeze.Cannot-Freeze-Them", true));
            return;
        }

        //Checks if the user is already frozen
        if (this.getModule().isFrozen(target.getUniqueId())){
            //If so unfreeze the target
            this.getModule().unfreeze(target.getUniqueId());
            player.sendMessage(this.getModule().getPlugin().getLanguage().getValue("Freeze.On-Unfreeze", true));
        } else {
            //If not freeze the target
            this.getModule().freeze(target);
            player.sendMessage(this.getModule().getPlugin().getLanguage().getValue("Freeze.On-Freeze", true));

            if (this.getModule().isBroadcastThroughRedis()){
                this.getModule().getPlugin().getPyrite().sendPacket(
                        new FreezePlayerPacket(player.getName(), target.getName()), "ModSuite");
            } else {
                String message = this.getModule().getPlugin().getLanguage().getValue("Freeze.On-Freeze-Broadcast", true)
                        .replace("{player}", player.getName())
                        .replace("{target}", target.getName());

                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (online.hasPermission(this.getModule().getFreezePermission())) {
                        online.sendMessage(message);
                    }
                }
            }
        }
    }
}
