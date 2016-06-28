package io.github.vectri.Stats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/**
 * A class to handle management of player breath data.
 */
public class PlayerBreathData extends BukkitRunnable {
    private Stats plugin;
    private HashMap<Player, Integer> breathData = new HashMap<Player, Integer>();
    private HashMap<Player, Integer> cachedBreathData = new HashMap<Player, Integer>();

    PlayerBreathData(Stats plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the data contained in the breathData HashMap.
     * @return The data.
     */
    public HashMap<Player, Integer> get() {
        return breathData;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {   // Get each player's current breath.
            int remainingAir = player.getRemainingAir();
            if (breathData.get(player) == null) {
                breathData.put(player, remainingAir);
                break;
            }
            if (breathData.get(player) != remainingAir) breathData.put(player, remainingAir);   // And update any variances in the HashMap's values.
        }
        if (!(breathData.size() == cachedBreathData.size())) { // Make perfectly sure that we are comparing equal data, if not, update it.
            cachedBreathData.putAll(breathData);  // This will resolve itself a second later, so it's OK to not check.
            return;
        }
        // Pass the data to an event before we update the cache.
        Bukkit.getServer().getPluginManager().callEvent(new PlayerBreathEvent(breathData, cachedBreathData));
        // Now that we have updated only breathData and checked the two, updated cachedData.
        cachedBreathData.putAll(breathData);
    }
}
