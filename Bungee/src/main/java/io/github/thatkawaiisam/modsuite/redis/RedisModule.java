package io.github.thatkawaiisam.modsuite.redis;

import io.github.thatkawaiisam.artus.bungee.BungeeModule;
import io.github.thatkawaiisam.artus.module.ModuleLoadLevel;
import io.github.thatkawaiisam.modsuite.ModSuitePlugin;
import io.github.thatkawaiisam.pyrite.Pyrite;
import io.github.thatkawaiisam.pyrite.PyriteCredentials;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;

@Getter
public class RedisModule extends BungeeModule<ModSuitePlugin> {

    private Pyrite pyrite;

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
