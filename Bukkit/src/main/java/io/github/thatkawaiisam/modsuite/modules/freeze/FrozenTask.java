package io.github.thatkawaiisam.modsuite.modules.freeze;

import io.github.thatkawaiisam.utils.MessageUtility;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class FrozenTask extends BukkitRunnable {

    private final FreezeModule freezeModule;

    @Override
    public void run() {
        freezeModule.getFrozenPlayersMap().forEach((uuid, frozenPlayer) -> {
            Player player = frozenPlayer.getPlayer();
            if (player != null){
                for (String string : this.freezeModule.getMessage()){
                    player.sendMessage(MessageUtility.formatMessage(string));
                }
            }
        });
    }
}
