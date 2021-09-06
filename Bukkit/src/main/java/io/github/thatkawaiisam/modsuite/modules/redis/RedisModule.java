package io.github.thatkawaiisam.modsuite.modules.redis;

import io.github.thatkawaiisam.artus.bukkit.BukkitModule;
import io.github.thatkawaiisam.artus.module.ModuleLoadLevel;
import io.github.thatkawaiisam.modsuite.ModSuitePlugin;
import io.github.thatkawaiisam.pyrite.Pyrite;
import io.github.thatkawaiisam.pyrite.PyriteCredentials;
import org.bukkit.configuration.Configuration;

import lombok.Getter;

@Getter
public class RedisModule extends BukkitModule<ModSuitePlugin> {

    private Pyrite pyrite;
    private String serverName;

    /**
     * Redis Module.
     *
     * @param plugin instance.
     */
    public RedisModule(ModSuitePlugin plugin) {
        super(plugin, "redis");
        this.getOptions().setGenerateConfiguration(true);
        this.getOptions().setLoadLevel(ModuleLoadLevel.HIGH);
    }

    @Override
    public String getFileName() {
        return "redis";
    }

    @Override
    public void onEnable() {
        Configuration c = this.getConfiguration().getImplementation();
        this.serverName = c.getString("Server-Name");
        this.pyrite = new Pyrite(
            new PyriteCredentials(
                c.getString("Credentials.IP"),
                c.getString("Credentials.Password"),
                c.getInt("Credentials.Port")
            )
        );
    }

    @Override
    public void onDisable() {
        this.pyrite.close();
    }
}
