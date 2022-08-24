package mc.obliviate.util.versiondetection;

public enum ServerVersionController {

    UNKNOWN,
    OUTDATED,
    V1_8,
    V1_9,
    V1_10,
    V1_11,
    V1_12,
    V1_13,
    V1_14,
    V1_15,
    V1_16,
    V1_17,
    V1_18,
    V1_19,
    NEWER;

    private static ServerVersionController serverVersion;

    /**
     *
     * Examples:
     * param = V1_8, server = V1_9 ---> false
     * param = V1_9, server = V1_9 ---> false
     * param = V1_10, server = V1_9 ---> true
     * param = V1_10, server = V1_11 ---> false

     * @param version version to compare with current version
     * @return true if param version is higher than server's current version.
     */
    public static boolean isServerVersionAbove(ServerVersionController version) {
        return getServerVersion().ordinal() > version.ordinal();
    }

    /**
     *
     * Examples:
     * param = V1_8, server = V1_9 ---> false
     * param = V1_9, server = V1_9 ---> true
     * param = V1_10, server = V1_9 ---> true
     * param = V1_10, server = V1_11 ---> false
     *
     * @param version version to compare with current version
     * @return true if param version is equal to server's current version or higher.
     */
    public static boolean isServerVersionAtLeast(ServerVersionController version) {
        return getServerVersion().ordinal() >= version.ordinal();
    }
    /**
     *
     * Examples:
     * param = V1_8, server = V1_9 ---> true
     * param = V1_9, server = V1_9 ---> true
     * param = V1_10, server = V1_9 ---> false
     * param = V1_10, server = V1_11 ---> true

     * @param version version to compare with current version
     * @return true if param version is equal to server's current version or lower.
     */
    public static boolean isServerVersionAtOrBelow(ServerVersionController version) {
        return getServerVersion().ordinal() <= version.ordinal();
    }

    /**
     *
     * Examples:
     * param = V1_8, server = V1_9 ---> true
     * param = V1_9, server = V1_9 ---> false
     * param = V1_10, server = V1_9 ---> false
     * param = V1_10, server = V1_11 ---> true

     * @param version version to compare with current version
     * @return true if param version is lower than server's current version.
     */
    public static boolean isServerVersionBelow(ServerVersionController version) {
        return getServerVersion().ordinal() < version.ordinal();
    }

    /**
     * Call this method after calling {@code ServerVersionController.calculateServerVersion(Bukkit.getBukkitVersion())}
     * @return current version of server
     */
    public static ServerVersionController getServerVersion() {
        if (ServerVersionController.serverVersion == null) throw new IllegalStateException("Server version could not get because its not calculated. Call ServerVersionController.calculateServerVersion() to calculate.");
        return ServerVersionController.serverVersion;
    }

    /**
     *
     * @param bukkitVersionString Bukkit.getBukkitVersion()
     * @return Bukkit Version Enum
     */
    public static void calculateServerVersion(String bukkitVersionString) {
        final String bukkitVersion = bukkitVersionString.split("-")[0].split("\\.")[1];
        try {
            ServerVersionController.serverVersion = ServerVersionController.valueOf("V1_" + bukkitVersion);
        } catch (Exception e) {
            ServerVersionController.serverVersion = UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}

