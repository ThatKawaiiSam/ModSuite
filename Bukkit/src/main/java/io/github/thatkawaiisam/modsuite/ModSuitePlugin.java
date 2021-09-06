package io.github.thatkawaiisam.modsuite;

import io.github.thatkawaiisam.artus.bukkit.BukkitPlugin;
import io.github.thatkawaiisam.artus.bukkit.language.BukkitLanguageModule;
import io.github.thatkawaiisam.modsuite.modules.cache.CacheModule;
import io.github.thatkawaiisam.modsuite.modules.connection.ConnectionModule;
import io.github.thatkawaiisam.modsuite.modules.redis.RedisModule;
import io.github.thatkawaiisam.modsuite.modules.reports.ReportsModule;
import io.github.thatkawaiisam.modsuite.modules.requests.RequestModule;
import io.github.thatkawaiisam.modsuite.modules.staffchat.StaffChatModule;
import io.github.thatkawaiisam.pyrite.Pyrite;

import lombok.Getter;

@Getter
public class ModSuitePlugin extends BukkitPlugin {

    @Override
    public void onEnable() {
        // Add all modules.
        this.getModuleFactory().addModule(new BukkitLanguageModule<>(this));
        this.getModuleFactory().addModule(new RedisModule(this));
        this.getModuleFactory().addModule(new CacheModule(this));
        this.getModuleFactory().addModule(new ReportsModule(this));
        this.getModuleFactory().addModule(new RequestModule(this));
        this.getModuleFactory().addModule(new StaffChatModule(this));
        this.getModuleFactory().addModule(new ConnectionModule(this));

        // Enable Modules.
        this.getModuleFactory().enableModules();

        // Initialise API.
        new ModSuiteAPIImplementation(this);
    }

    @Override
    public void onDisable() {
        // Disable Modules.
        this.getModuleFactory().disableModules();
    }

    /**
     * Get Language Module.
     *
     * @return module instance.
     */
    public BukkitLanguageModule<ModSuitePlugin> getLanguage() {
        return this.getModuleFactory().getModule("lang");
    }

    /**
     * Get ModSuite Pyrite from Redis Module.
     *
     * @return Pyrite instance.
     */
    public Pyrite getPyrite() {
        return this.getModuleFactory().<RedisModule>getModule("redis").getPyrite();
    }

    /**
     * Get Server Name.
     * TODO: Better implementation - grab Hydra Server Name.
     *
     * @return name of server.
     */
    public String getServerName() {
        return this.getModuleFactory().<RedisModule>getModule("redis").getServerName();
    }

}
