package io.github.thatkawaiisam.modsuite.api;

public abstract class ModSuiteAPI {

    private static ModSuiteAPI apiInstance;

    /**
     * ModSuite API
     */
    public ModSuiteAPI() {
        apiInstance = this;
        System.out.println("[ModSuite] API has now loaded.");
    }

    /**
     * Get ModSuite API Instance.
     *
     * @return API Instance if loaded.
     */
    public static ModSuiteAPI INSTANCE() {
        if (apiInstance == null) {
            throw new ModSuiteAPIException("API is currently not loaded");
        }
        return apiInstance;
    }

}
