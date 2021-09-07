package io.github.thatkawaiisam.modsuite.modules.freeze;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Syntax;
import io.github.thatkawaiisam.artus.bukkit.BukkitCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FreezeCommand extends BukkitCommand<FreezeModule> {

    public FreezeCommand(FreezeModule module) {
        super(module);
    }

    @Syntax("<player>")
    @CommandAlias("freeze|ss")
    public void onFreezeCommand(Player player, Player target) {
        if (player.getUniqueId().equals(target.getUniqueId())){
            player.sendMessage(ChatColor.RED + "You cannot freeze yourself.");
            return;
        }

        if (target.isOp()){
            player.sendMessage(ChatColor.RED + "You cannot freeze that player");
            return;
        }

        if (!player.isOp() && target.hasPermission("modesuite.freeze")){ //make it so other staff cant just freeze other staff only ops can
            player.sendMessage(ChatColor.RED + "You cannot freeze that player");
            return;
        }

        if (this.getModule().isFrozen(target.getUniqueId())){
            this.getModule().unfreeze(target.getUniqueId());
            player.sendMessage(this.getModule().getPlugin().getLanguage().getValue("Freeze.On-Unfreeze", true));
        } else {
            this.getModule().freeze(target);
            player.sendMessage(this.getModule().getPlugin().getLanguage().getValue("Freeze.On-Freeze", true));
        }

    }

}
