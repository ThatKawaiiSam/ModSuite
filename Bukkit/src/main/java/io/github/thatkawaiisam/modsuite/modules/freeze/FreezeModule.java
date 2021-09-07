package io.github.thatkawaiisam.modsuite.modules.freeze;

import io.github.thatkawaiisam.artus.bukkit.BukkitModule;
import io.github.thatkawaiisam.modsuite.ModSuitePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.*;

public class FreezeModule extends BukkitModule<ModSuitePlugin> {

    public HashMap<UUID, FrozenPlayer> frozenPlayersMap;
    public List<String> message;
    public String freezePermission;

    public void freeze(Player player){
        int[] location = {player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()};
        FrozenPlayer frozenPlayer = new FrozenPlayer(player.getUniqueId(), player.getFoodLevel(), player.getSaturation(), player.getWalkSpeed(), location);
        player.setFoodLevel(0);
        player.setSaturation(0);
        player.setWalkSpeed(0);
        frozenPlayersMap.put(player.getUniqueId(), frozenPlayer);
    }

    public void unfreeze(UUID uuid){
        FrozenPlayer frozenPlayer = this.frozenPlayersMap.get(uuid);

        if (frozenPlayer != null){
            frozenPlayer.unfreeze();
            this.frozenPlayersMap.remove(uuid);
        }
    }

    public boolean isFrozen(UUID uuid){
        return this.frozenPlayersMap.containsKey(uuid);
    }

    public FreezeModule(ModSuitePlugin plugin) {
        super(plugin, "freeze");
        this.frozenPlayersMap = new HashMap<>();
        this.getOptions().setGenerateConfiguration(true);
    }

    @Override
    public void onEnable() {
        Configuration c = this.getConfiguration().getImplementation();
        this.freezePermission = c.getString("Freeze-Permission");

        this.message =
                Arrays.asList(this.getPlugin().getLanguage().getValue("Freeze.Freeze-Message", true).split("\n"));

        this.addListener(new FreezeListener(this));
        this.addCommand(new FreezeCommand(this));
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this.getPlugin(), new FrozenTask(this), 20L, 20L);
    }

    @Override
    public void onDisable() {
        this.frozenPlayersMap.forEach((uuid, frozenPlayer) -> frozenPlayer.unfreeze());
    }
}
