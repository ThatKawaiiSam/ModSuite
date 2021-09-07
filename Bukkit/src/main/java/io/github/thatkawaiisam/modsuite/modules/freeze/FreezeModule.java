package io.github.thatkawaiisam.modsuite.modules.freeze;

import io.github.thatkawaiisam.artus.bukkit.BukkitModule;
import io.github.thatkawaiisam.modsuite.ModSuitePlugin;
import io.github.thatkawaiisam.modsuite.modules.freeze.packet.FreezePacketListener;
import io.github.thatkawaiisam.modsuite.modules.reports.ReportPacketListener;
import lombok.Getter;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
public class FreezeModule extends BukkitModule<ModSuitePlugin> {

    private HashMap<UUID, FrozenPlayer> frozenPlayersMap;
    private List<String> message;
    private String freezePermission;
    private boolean broadcastThroughRedis;
    private FreezePacketListener packetListener;

    /**
     * Freeze Module
     *
     * @param plugin the plugin instance.
     */
    public FreezeModule(ModSuitePlugin plugin) {
        super(plugin, "freeze");
        this.frozenPlayersMap = new HashMap<>();
        this.getOptions().setGenerateConfiguration(true);
    }

    @Override
    public void onEnable() {
        //Configurations
        Configuration c = this.getConfiguration().getImplementation();
        this.freezePermission = c.getString("Freeze-Permission");
        this.broadcastThroughRedis = c.getBoolean("Freeze-Broadcast-Redis");

        this.message =
                Arrays.asList(this.getPlugin().getLanguage().getValue("Freeze.Freeze-Message", true).split("\n"));

        //Listeners
        this.addListener(new FreezeListener(this));

        //Commands
        this.addCommand(new FreezeCommand(this));

        //Tasks
        this.getPlugin().getServer().getScheduler().runTaskTimerAsynchronously(this.getPlugin(), new FrozenTask(this), 20L, 20L);

        //Packet listener
        if (this.broadcastThroughRedis) {
            this.packetListener = new FreezePacketListener(this);
            this.getPlugin().getPyrite().registerContainer(this.packetListener);
        }

    }

    @Override
    public void onDisable() {
        this.frozenPlayersMap.forEach((uuid, frozenPlayer) -> frozenPlayer.unfreeze());
    }

    /**
     * Freezes the target player
     *
     * @param player the player instance of the player to freeze.
     */
    public void freeze(Player player){
        int[] location = {player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()};
        FrozenPlayer frozenPlayer = new FrozenPlayer(player.getUniqueId(), player.getFoodLevel(), player.getSaturation(), player.getWalkSpeed(), location);
        player.setFoodLevel(0);
        player.setSaturation(0);
        player.setWalkSpeed(0);
        this.frozenPlayersMap.put(player.getUniqueId(), frozenPlayer);
    }

    /**
     * Unfreezes the target player
     *
     * @param uuid the uuid of the player to unfreeze
     */
    public void unfreeze(UUID uuid){
        FrozenPlayer frozenPlayer = this.frozenPlayersMap.get(uuid);

        if (frozenPlayer != null){
            frozenPlayer.unfreeze();
            this.frozenPlayersMap.remove(uuid);
        }
    }

    /**
     * Checks if the player is frozen
     *
     * @param uuid the uuid of the player
     * @return a boolean which is true if player is frozen or false otherwise
     */
    public boolean isFrozen(UUID uuid){
        return this.frozenPlayersMap.containsKey(uuid);
    }

}
