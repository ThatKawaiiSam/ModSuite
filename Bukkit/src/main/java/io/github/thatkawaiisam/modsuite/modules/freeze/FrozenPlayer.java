package io.github.thatkawaiisam.modsuite.modules.freeze;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class FrozenPlayer {

    private final UUID uuid;
    private final int previousHunger;
    private final float previousSaturation;
    private final float previousWalkSpeed;
    public final int[] location;

    /**
     * Unfreeze the owner of this object
     */
    public void unfreeze(){
        Player player = this.getPlayer();

        if (player != null){
            player.setFoodLevel(this.previousHunger);
            player.setSaturation(this.previousSaturation);
            player.setWalkSpeed(this.previousWalkSpeed);
        }
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(this.uuid);
    }
}
