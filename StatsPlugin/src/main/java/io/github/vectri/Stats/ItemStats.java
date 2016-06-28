package io.github.vectri.Stats;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * An item with a statistics attachment on it.
 */
public class ItemStats {
    private ItemStack item;
    private ItemMeta meta;

    ItemStats(ItemStack item, Attachment attachment) {
        List<String> lore = new ArrayList<String>();
        this.item = item;
        this.meta = item.getItemMeta();
        ChatColor attachmentColor = ChatColor.WHITE;
        if (meta.hasLore()) {   // If the item has lore on already, retrieve it.
            lore = meta.getLore();
            String ls = lore.get(1);
            if (ls.contains(ChatColor.WHITE + ""))  // If the base attachment is already on the item.
                attachmentColor = ChatColor.GRAY;
        } else {
            lore.add(ChatColor.GOLD + AttachmentRank.getRank(0));
        }
        lore.add(attachmentColor + attachment.getName() + ": 0");
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Return an item with the applied attachment.
     * @return The item.
     */
    public ItemStack getItemStack() {
        return item;
    }

    /**
     * Create a statistic tracking item.
     * @param player The player to give the item to.
     */
    public static void create(Player player) {
        // Implement balance check.
        // ...
        ItemStack itemInHand = player.getItemInHand();
        if (itemInHand.getType().equals(Material.AIR)) {
            player.sendMessage(ChatColor.RED + "Air can not count statistics.");
            return;
        }
        ItemMeta meta = itemInHand.getItemMeta();
        if (meta.hasLore()) {
            if (meta.getLore().get(0).contains(ChatColor.GOLD + "")) {
                player.sendMessage(ChatColor.RED + "That item already tracks statistics.");
                return;
            }
        }
        String itemInHandName = itemInHand.getType().toString().toLowerCase().replace("_", " ");
        Attachment attachment = Attachment.matchDefault(itemInHand);
        if (attachment == null) {
            player.sendMessage(ChatColor.RED + itemInHandName.substring(0, 1).toUpperCase() + itemInHandName.substring(1) + " can not count statistics.");
            return;
        }
        double cost = AttachmentConfig.getPrice(attachment);
        if (StatsConfig.disableIntegrated)
            cost = 0.0;
        if (cost == -1) {
            player.sendMessage(ChatColor.RED + "Something went horribly wrong. Try your purchase again.");
            return;
        }
        double money = StatsEconomy.getBalance(player) - cost;
        String currency = " " + StatsEconomy.getCurrencyName();
        String costWithCurrency = Math.abs(cost) + currency;
        String moneyWithCurrency = money + currency;
        if (money < 0) {
            player.sendMessage(ChatColor.RED + "You need " + costWithCurrency + " to create a statistics tracker on your " + itemInHandName + ".");
            return;
        }
        ItemStack statisticItem = new ItemStats(itemInHand, attachment).getItemStack();  // Create the item.
        player.getInventory().remove(itemInHand);
        player.getInventory().setItemInHand(statisticItem);
        if (!StatsConfig.disableIntegrated) {
            StatsEconomy.withdraw(player, 100.00);
            player.sendMessage(ChatColor.GREEN + "Your new balance is " + moneyWithCurrency + ". Your " + itemInHandName + " is now tracking " +
                    "statistics.  You may apply more attachments later with /statistics buy <attachment>");
        }
        player.sendMessage(ChatColor.GREEN + "Your " + itemInHandName + " is now tracking " +
                "statistics.  You may apply more attachments later with /statistics buy <attachment>");
    }
}
