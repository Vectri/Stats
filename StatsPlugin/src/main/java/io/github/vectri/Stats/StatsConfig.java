package io.github.vectri.Stats;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * A config for adjusting settings in Stats.
 */
public class StatsConfig {
    private Stats plugin;
    public static boolean disableIntegrated = false;
    public static String integratedCurrency = "Bells";
    public static int announceRankUp = 0;

    StatsConfig(Stats plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        FileConfiguration config = plugin.getConfig();
        plugin.saveDefaultConfig();
        try {
            if (config.getBoolean("economy.disable-integrated")) {
                disableIntegrated = true;
            }
            integratedCurrency = config.getString("economy.currency-name");
            announceRankUp = config.getInt("ranks.announce-rank-up");
            if (announceRankUp < 0 || announceRankUp > 2) {
                plugin.getLogger().severe("announce-rank-up in config.yml is set outside its bounds! Assuming default value.");
                announceRankUp = 0;
            }
        } catch (NullPointerException npe) {
            plugin.getLogger().severe("Unable to read config.yml");
        }
    }

}
