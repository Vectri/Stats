package io.github.vectri.Stats;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * A paper item used as an attachable to a weapon or tool.
 */
public class ItemAttachable extends ItemStack {
    private ItemStack item;
    private ItemMeta meta;

    ItemAttachable(Attachment attachment, int quantity) {
        this.item = new ItemStack(Material.PAPER);
        this.meta = item.getItemMeta();
        meta.setLore(Arrays.asList(ChatColor.GREEN + "Right-click when held to apply"));
        meta.setDisplayName("Attachment: " + attachment.getName());
        item.setItemMeta(meta);
        item.setAmount(quantity);
    }

    /**
     * Return an ItemStack of the attachable.
     * @return A paper representing the attachable.
     */
    public ItemStack getItemStack() {
        return item;
    }

    /**
     * Begin application of an attachment.
     * @param player The player to begin application for.
     */
    public static void apply(Player player) {
        ItemStack itemInHand = player.getItemInHand();
        ItemMeta meta = itemInHand.getItemMeta();
        String itemName = itemInHand.getItemMeta().getDisplayName();
        if (itemName == null) {
            return;
        }
        if (!meta.getDisplayName().contains("Attachment")) {
            return;
        }
        String lore = "";
        if (meta.hasLore()) {
            lore = itemInHand.getItemMeta().getLore().get(0);
        }
        if (!lore.equals(ChatColor.GREEN + "Right-click when held to apply")) {    // Is this item an attachment?
            return;
        }
        ItemStack playerData = AttachmentApplicationData.get(player);
        if (playerData != null) {   // Is the player applying an attachment already?
            player.sendMessage(ChatColor.RED + "Another part is currently in the application process!");
            return;
        }
        new AttachmentApplicationData(player, itemInHand);  // Add the paper to a HashMap.
        player.getInventory().removeItem(itemInHand);
        String attachmentString = itemName.split(":")[1].trim();  // Attachment: [attachment].
        Attachment attachment = Attachment.match(attachmentString); // Convert the attachment lore to an attachment object.
        String appliances = Attachment.appliance(attachment);  // Retrieve a list of appliances for the attachment.
        player.sendMessage(attachment.getName() + " selected. Applicable item(s): " + ChatColor.GREEN + appliances + ChatColor.RESET + "\nSwitch and hold out an " +
                "applicable item, then type /statistics apply to add the attachment " + attachment.getName().toLowerCase() + " " +
                "to the item.\n" + ChatColor.GRAY + "Type /statistics cancel to cancel the application process");
    }
}
