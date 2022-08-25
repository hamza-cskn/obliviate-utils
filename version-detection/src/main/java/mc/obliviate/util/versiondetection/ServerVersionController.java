package mc.obliviate.util.versiondetection;

public enum ServerVersionController {

    UNKNOWN,
    OUTDATED,
    V1_5,
    V1_6,
    V1_7,
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
    V1_20,
    V1_21,
    V1_22,
    V1_23,
    V1_24,
    NEWER;

    private static ServerVersionController serverVersion;

    /**
     * Examples:
     * param = V1_8, server = V1_9 ---> false
     * param = V1_9, server = V1_9 ---> false
     * param = V1_10, server = V1_9 ---> true
     * param = V1_10, server = V1_11 ---> false
     *
     * @param version version to compare with current version
     * @return true if param version is higher than server's current version.
     */
    public static boolean isServerVersionAbove(ServerVersionController version) {
        return getServerVersion().ordinal() > version.ordinal();
    }

    /**
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
     * Examples:
     * param = V1_8, server = V1_9 ---> true
     * param = V1_9, server = V1_9 ---> true
     * param = V1_10, server = V1_9 ---> false
     * param = V1_10, server = V1_11 ---> true
     *
     * @param version version to compare with current version
     * @return true if param version is equal to server's current version or lower.
     */
    public static boolean isServerVersionAtOrBelow(ServerVersionController version) {
        return getServerVersion().ordinal() <= version.ordinal();
    }

    /**
     * Examples:
     * param = V1_8, server = V1_9 ---> true
     * param = V1_9, server = V1_9 ---> false
     * param = V1_10, server = V1_9 ---> false
     * param = V1_10, server = V1_11 ---> true
     *
     * @param version version to compare with current version
     * @return true if param version is lower than server's current version.
     */
    public static boolean isServerVersionBelow(ServerVersionController version) {
        return getServerVersion().ordinal() < version.ordinal();
    }

    /**
     * Call this method after calling {@code ServerVersionController.calculateServerVersion(Bukkit.getBukkitVersion())}
     *
     * @return current version of server
     */
    public static ServerVersionController getServerVersion() {
        if (ServerVersionController.serverVersion == null) calculateServerVersion(findServerInstance());
        return ServerVersionController.serverVersion;
    }

    /**
     * @param server Bukkit.getServer()
     * @return Bukkit Version Enum
     */
    public static void calculateServerVersion(final Object server) {
        if (server == null) {
            ServerVersionController.serverVersion = UNKNOWN;
            return;
        }
        final Class<?> serverClazz = server.getClass();
        final String packageName = serverClazz.getPackage().getName();
        try {
            final String version = packageName.substring(packageName.lastIndexOf('.') + 1);
            final String rawVersion = version.split("_")[1];
            ServerVersionController.serverVersion = matchServerVersion(Integer.parseInt(rawVersion));
        } catch (Exception ignore) {
            ServerVersionController.serverVersion = UNKNOWN;
        }
    }

    private static ServerVersionController matchServerVersion(int rawVersion) {
        if (rawVersion < 5) return OUTDATED;
        if (rawVersion > ServerVersionController.values().length + 2) return NEWER;
        return ServerVersionController.valueOf("V1_" + rawVersion);
    }

    private static Object findServerInstance() {
        try {
            return Class.forName("org.bukkit.Bukkit").getMethod("getServer").invoke(null, (Object[]) null);
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public String toString() {
        return name();
    }

}

