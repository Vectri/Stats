package io.github.vectri.Stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * A class that handles the numbers behind attachments.
 */
public class StatsHandler {
    /**
     * Increases any given attachment on a given item if it has the attachment by one.
     * @param player The player to apply the change for.
     * @param attachment The attachment to increment by one.
     * @param amount The amount to increase the attachment by.
     */
    private StatsHandler(Player player, Attachment attachment, int amount) {
        this(player, player.getItemInHand(), attachment, amount);
    }

    /**
     * Increases any given attachment on a given item if it has the attachment by one.
     * @param player The player to apply the change for
     * @param item A given item to increment, specifically armor or something that can not be held in the player's hand.
     * @param attachment The attachment to increment by one.
     * @param amount The amount to increase the attachment by.
     */
    private StatsHandler(Player player, ItemStack item, Attachment attachment, int amount) {
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore())
            return;
        Attachment[] attachments = Attachment.getAttachments(item);   // Get an array of the player's held item's attachments.
        int attachmentLineNumber = Attachment.hasAttachment(attachments, attachment);
        if (attachmentLineNumber == 0)
            return;
        List<String> lore = meta.getLore();
        String[] ls = lore.get(attachmentLineNumber).split(":");
        int newAmount = Integer.parseInt(ls[1].trim()) + amount;   // The amount of specified attachment.
        if (attachmentLineNumber == 1) {    // Is the default part being increased?
            String rank = AttachmentRank.getRank(newAmount);
            if (rank != null) {
                lore.set(0, ChatColor.GOLD + rank);
                int announceRankUp = StatsConfig.announceRankUp;
                if (announceRankUp == 0 || (announceRankUp == 1 && AttachmentRank.highestRank == newAmount)) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (player == p) continue;
                        p.sendMessage(player.getName() + "\'s " + item.getType().toString().toLowerCase().replace("_", " ") +
                                " has reached a new kill rank: " + ChatColor.GOLD + rank + ChatColor.RESET + "!");
                    }
                }
                player.sendMessage("Your " + item.getType().toString().toLowerCase().replace("_", " ") +
                        " has reached a new kill rank: " + ChatColor.GOLD + rank + ChatColor.RESET + "!");
                if (StatsEconomy.internal) {
                    double moneyEarned = newAmount / 10;
                    StatsEconomy.deposit(player, moneyEarned);
                    player.sendMessage(ChatColor.GREEN + "" + moneyEarned + " " + StatsEconomy.getCurrencyName() +
                            " has been added to your account for ranking up.");
                }
            }
        }
        lore.set(attachmentLineNumber, ls[0].trim() + ": " + Integer.toString(newAmount));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Increases a given attachment on a given item by one if it has the attachment.
     * @param player The player to apply the change for.
     * @param attachment The attachment to modify.
     * @param amount The amount to increase the given attachment by.
     */
    public static void add(Player player, Attachment attachment, int amount) {
        new StatsHandler(player, attachment, amount);
    }

    /**
     * Increases a given attachment on a given item by one if it has the attachment.
     * @param player The player to apply the change for.
     * @param item A given item to increment, specifically armor or something that can not be held in the player's hand.
     * @param attachment The attachment to modify.
     * @param amount The amount to increase the given attachment by.
     */
    public static void add(Player player, ItemStack item, Attachment attachment, int amount) {
        new StatsHandler(player, item, attachment, amount);
    }

    /**
     * Increases the LONG_DISTANCE_KILLS attachment on a given item if it has the attachment by one.
     * @param player The player to apply the change for.
     * @param entity The entity involved in the event, like a player.
     */
    public static void addLongDistanceKills(Player player, LivingEntity entity) {
        int distanceX = Math.abs(player.getLocation().getBlockX() - entity.getLocation().getBlockX());
        int distanceY = Math.abs(player.getLocation().getBlockY() - entity.getLocation().getBlockY());
        int distanceZ = Math.abs(player.getLocation().getBlockZ() - entity.getLocation().getBlockZ());
        if (distanceX >= 25 || distanceY >= 25 || distanceZ >= 25)
            new StatsHandler(player, Attachment.LONG_DISTANCE_KILLS, 1);
    }

    /**
     * Increases the AIRBORNE_OPPONENTS_KILLED attachment on a given item if it has the attachment by one.
     * @param player The player to apply the change for.
     * @param entity The entity involved in the event, like a player.
     */
    public static void addAirborneEnemyKills(Player player, LivingEntity entity) {
        if (entity.getFallDistance() > 0 && !entity.isOnGround() && !entity.isInsideVehicle())
            new StatsHandler(player, Attachment.AIRBORNE_OPPONENTS_KILLED, 1);
    }

    /**
     * Increases the KILLS_WHILE_AIRBORNE attachment on a given item if it has the attachment by one.
     * @param player The player that killed an entity.
     */
    public static void addKillsWhileAirborne(Player player) {
        Entity entity = (Entity) player;
        if (player.getFallDistance() > 0 && !entity.isOnGround() && !player.isInsideVehicle())
            new StatsHandler(player, Attachment.KILLS_WHILE_AIRBORNE, 1);
    }

    /**
     * Increases the LOW_HEALTH_KILLS attachment on a given item if it has the attachment by one.
     * @param player The player that killed an entity.
     */
    public static void addLowHealthKills(Player player) {
        if (player.getHealth() <= 2)
            new StatsHandler(player, Attachment.LOW_HEALTH_KILLS, 1);
    }

    /**
     * Increases the DAMAGE_GIVEN attachment on a given item if it has the attachment by one.
     * @param player The player that killed an entity.
     */
    public static void addCriticalKills(Player player) {
        Entity entity = (Entity) player;
        if (player.getFallDistance() > 0 && !entity.isOnGround() &&
                !player.hasPotionEffect(PotionEffectType.BLINDNESS) && !player.isInsideVehicle())
            new StatsHandler(player, Attachment.CRITICAL_KILLS, 1);
    }
}