package io.github.thatkawaiisam.modsuite;

import io.github.thatkawaiisam.modsuite.api.ModSuiteAPI;

public class ModSuiteAPIImplementation extends ModSuiteAPI {

    private ModSuitePlugin plugin;

    /**
     * ModSuite API Implementation.
     *
     * @param plugin instance.
     */
    public ModSuiteAPIImplementation(ModSuitePlugin plugin) {
        this.plugin = plugin;
    }
}
