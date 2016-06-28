package io.github.vectri.Stats;

import java.util.HashMap;

/**
 * An enum containing ranks and number needed to move up in rank.
 */
public class AttachmentRank {
    private Stats plugin;
    private static HashMap<String, Integer> ranks = new HashMap<>();
    private static ConfigAccessor ranksFile;
    public static int highestRank = 0;

    AttachmentRank(Stats plugin) {
        this.plugin = plugin;
        load();
    }

    /**
     * Loads the accounts.yml into memory.
     */
    private void load() {
        ranksFile = new ConfigAccessor(plugin, "ranks.yml");
        ranksFile.saveDefaultConfig();
        try {
            for(String key : ranksFile.getConfig().getConfigurationSection("ranks").getKeys(false)) {
                int value = ranksFile.getConfig().getInt("ranks." + key);
                ranks.put(key, value);
                if (highestRank < value)
                    highestRank = value;
            }
        } catch (NullPointerException npe) {
            plugin.getLogger().severe("Unable to read ranks.yml! Switching to default values.");
            ranks.put("Strange", 0);
            ranks.put("Unremarkable", 10);
            ranks.put("Scarcely lethal", 25);
            ranks.put("Mildly menacing", 45);
            ranks.put("Somewhat threatening", 70);
            ranks.put("Uncharitable", 100);
            ranks.put("Notably dangerous", 135);
            ranks.put("Sufficiently lethal", 175);
            ranks.put("Truly feared", 225);
            ranks.put("Spectacularly lethal", 275);
            ranks.put("Gore spattered", 400);
            ranks.put("Wicked nasty", 500);
            ranks.put("Positively inhumane", 750);
            ranks.put("Totally ordinary", 999);
            ranks.put("Face melting", 1000);
            ranks.put("Rage inducing", 1500);
            ranks.put("Server clearing", 2500);
            ranks.put("Epic", 5000);
            ranks.put("Legendary", 7000);
            ranks.put("Australian", 7616);
            ranks.put("Hale's own", 8500);
        }
        if (getRank(0) == null) {
            plugin.getLogger().info("Ranks.yml does not have a rank equal 0. Using default value.");
            ranks.put("Strange", 0);
        }
    }

    /**
     * Returns the name given a rank number.
     * @param rank The rank number.
     * @return The name of the rank number.
     */
    public static String getRank(int rank) {
        for (Object object : ranks.keySet()) {
            if (ranks.get(object).equals(rank)) return object.toString();
        }
        return null;
    }
}
