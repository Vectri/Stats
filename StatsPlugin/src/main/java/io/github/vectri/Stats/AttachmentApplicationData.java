package io.github.vectri.Stats;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * A class to handle management of attachment application data.
 */
public class AttachmentApplicationData {
    private static HashMap<Player, ItemStack> storage = new HashMap<Player, ItemStack>();   // There can only be one! HashMap instance.

    /**
     * Add player name and attachment data to a HashMap.
     * @param player The player.
     * @param item The attachment.
     */
    AttachmentApplicationData(Player player, ItemStack item) {
        storage.put(player, item);
    }

    /**
     * Get data for a specified player.
     * @param player
     * @return
     */
    public static ItemStack get(Player player) {
        return storage.get(player);
    }

    /**
     * Remove data for a specified player.
     * @param player
     */
    public static void remove(Player player) {
        storage.remove(player);
    }
}
