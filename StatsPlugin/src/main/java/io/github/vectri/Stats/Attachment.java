package io.github.vectri.Stats;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.regex.Pattern;

/**
 * An enum containing attachables.
 */
public enum Attachment implements AttachmentCategories {
    KILLS("Kills", weaponsAndTools, swordsAndBows),  // Default to swords and bows.
    BLOCKS_BROKEN("Blocks broken", weaponsAndTools, pickaxes),  // Default to pickaxes.
    DAMAGE_TAKEN("Damage taken", armor, armor),  // Default to armor.
    FARMLAND_TILLED("Farmland tilled", hoes, hoes), // Default to hoes.
    FIRES_STARTED("Fires started", flintAndSteel, flintAndSteel), // Default to flint and steel.
    SHEEP_SHEARED("Sheep sheared", shears, shears), // Default to shears.
    FISH_CAUGHT("Fish caught", fishingRod, fishingRod), // Default to fishing rods.
    LEAVES_BROKEN("Leaves broken", shears),
    DAMAGE_GIVEN("Damage given", weaponsAndTools),
    FALL_DAMAGE_TAKEN("Fall damage taken", boots),
    BREATH_HELD("Breath held", helmets),
    BLOCKED_ATTACKS("Blocked attacks", swords),   // !!! WILL BE REMOVED IN 1.9 !!!
    FIRES_SURVIVED("Fires survived", armor),
    LONG_DISTANCE_KILLS("Long distance kills", bows),
    KILLS_WHILE_AIRBORNE("Kills while airborne", weaponsAndTools),
    AIRBORNE_OPPONENTS_KILLED("Airborne opponents killed", weaponsAndTools),
    DIAMONDS_BROKEN("Diamonds broken", pickaxes),
    LOW_HEALTH_KILLS("Low health kills", swords),
    CRITICAL_KILLS("Critical kills", weaponsAndTools),
    ENTITIES_HOOKED("Entities hooked", fishingRod),
    // <mob> killed.
    BATS_KILLED("Bats killed", weaponsAndTools),
    CHICKENS_KILLED("Chickens killed", weaponsAndTools),
    COWS_KILLED("Cows killed", weaponsAndTools),
    MUSHROOMCOWS_KILLED("Mushroom Cows killed", weaponsAndTools),
    PIGS_KILLED("Pigs killed", weaponsAndTools),
    RABBITS_KILLED("Rabbits killed", weaponsAndTools),
    SHEEP_KILLED("Sheep killed", weaponsAndTools),
    SQUIDS_KILLED("Squid killed", weaponsAndTools),
    VILLAGERS_KILLED("Villagers killed", weaponsAndTools),
    CAVESPIDERS_KILLED("Cave spiders killed", weaponsAndTools),
    ENDERMANS_KILLED("Endermen killed", weaponsAndTools), // Incorrectly named so it does not need to be renamed in StatsListener.
    SPIDERS_KILLED("Spiders killed", weaponsAndTools),
    ZOMBIEPIGMANS_KILLED("Zombie pigmen killed", weaponsAndTools),
    BLAZES_KILLED("Blazes killed", weaponsAndTools),
    CREEPERS_KILLED("Creepers killed", weaponsAndTools),
    ELDERGUARDIANS_KILLED("Elder guardians killed", weaponsAndTools),
    ENDERMITES_KILLED("Endermites killed", weaponsAndTools),
    GHASTS_KILLED("Ghasts killed", weaponsAndTools),
    GUARDIANS_KILLED("Guardians killed", weaponsAndTools),
    MAGMACUBES_KILLED("Magma cubes killed", weaponsAndTools),
    SILVERFISHS_KILLED("Silverfish killed", weaponsAndTools),
    SKELETONS_KILLED("Skeletons killed", weaponsAndTools),
    SLIMES_KILLED("Slimes killed", weaponsAndTools),
    WITCHS_KILLED("Witches killed", weaponsAndTools),
    WITHERSKELETONS_KILLED("Wither skeletons killed", weaponsAndTools),
    ZOMBIES_KILLED("Zombies killed", weaponsAndTools),
    ZOMBIEVILLAGERS_KILLED("Zombie villagers killed", weaponsAndTools),
    DONKEYS_KILLED("Donkeys killed", weaponsAndTools),
    HORSES_KILLED("Horses killed", weaponsAndTools),
    MULES_KILLED("Mules killed", weaponsAndTools),
    OCELOTS_KILLED("Ocelots killed", weaponsAndTools),
    WOLFS_KILLED("Wolves killed", weaponsAndTools),
    IRONGOLEMS_KILLED("Iron golems killed", weaponsAndTools),
    SNOWGOLEMS_KILLED("Snow golems killed", weaponsAndTools),
    ENDERDRAGONS_KILLED("Ender dragons killed", weaponsAndTools),
    WITHERS_KILLED("Withers killed", weaponsAndTools);

    private final String name;
    private final Material[] category;
    private final Material[] defaults;

    Attachment(final String name, final Material[] category) {
        this.name = name;
        this.category = category;
        this.defaults = null;
    }

    Attachment(final String name, final Material[] category, final Material[] defaultItems) {
        this.name = name;
        this.category = category;
        this.defaults = defaultItems;
    }

    /**
     * Returns the name of an attachable.
     * @return Name of attachable.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the category of an attachable.
     * @return The category of attachable, returns null if no categories match.
     */
    public Material[] getCategory() {
        return category;
    }

    /**
     * Retuns which materials use a given attachment as their default attachment.
     * @return An array of Materials.
     */
    public Material[] getDefaults() {
        return defaults;
    }

    /**
     * Convert from a string to an attachment.
     * @param attachment The string to convert from.
     * @return The matching attachment if there is one, null if there is not.
     */
    public static Attachment fromString(String attachment) {
        try {
            return Attachment.valueOf(attachment.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * A list of all attachments.
     * @return A list of attachments.
     */
    public static String list() {
        String list = "";
        int currentAttachment = 0;
        int listLength = Attachment.values().length - 1;
        for (Attachment attachment : Attachment.values()) {
            list += ChatColor.GREEN + " " + attachment.name + ChatColor.RESET;
            if (currentAttachment < listLength) {
                list += ",";
                currentAttachment++;
            } else {
                list += "."; // Add a period at the end of the list.
            }
        }
        String message = "List of strange parts (" + ChatColor.GREEN + (listLength)
                + ChatColor.RESET + "):" + list;
        return message;
    }

    /**
     * A list of appliances given an attachment.
     * @param attachment The attachment to list appliances for.
     * @return A list of appliances, null if the attachment was not valid.
     */
    public static String appliance(Attachment attachment) {
        Material[] category = attachment.category;
        if (category == swords) return "Swords";
        if (category == weaponsAndTools) return "Weapons and tools";
        if (category == armor) return "Armor";
        if (category == shears) return "Shears";
        if (category == hoes) return "Hoes";
        if (category == flintAndSteel) return "Flint and steel";
        if (category == boots) return "Boots";
        if (category == helmets) return "Helmets";
        if (category == bows) return "Bows";
        if (category == pickaxes) return "Pickaxes";
        if (category == axes) return "Axes";
        if (category == spades) return "Spades";
        if (category == fishingRod) return "Fishing rods";
        return null;
    }

    /**
     * Checks whether a given item can receive a given attachment.
     * @param item The item to evaluate.
     * @param attachment The attachment to evaluate.
     * @return Returns true if the two are compatible, false if not.
     */
    public static boolean isApplicable(ItemStack item, Attachment attachment) {
        Material type = item.getType();
        for (Material material : attachment.category) {
            if (material.equals(type)) return true;
        }
        return false;
    }

    /**
     * Checks an item to see if it contains the given attachment.
     * @param item The item to check.
     * @param attachment The attachment to search for.
     * @return Returns true if the attachment was found on the item, false if not.
     */
    public static boolean hasAttachment(ItemStack item, Attachment attachment) {
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            if (lore.toString().contains(attachment.name))
                return true;
        }
        return false;
    }

    /**
     * Checks to see if a list of attachments contains the given attachment.
     * @param attachments A list of attachments.
     * @param attachment An attachment to check for in the list.
     * @return The number of the element where the attachment was found in the list of attachments, 0 if it was not found.
     */
    public static int hasAttachment(Attachment[] attachments, Attachment attachment) {
        for (int i = 0; i < attachments.length; i++) {
            if (attachment == attachments[i]) return i;
        }
        return 0;
    }

    /**
     * Match a part given the name of the attachment.
     * @param name The name of the attachment to match.
     * @return The matched attachment, null if nothing matches.
     */
    public static Attachment match(String name) {
        for (Attachment attachment : Attachment.values()) {
            if (attachment.name.equals(name)) return attachment;
        }
        return null;
    }

    /**
     * Match a part given vague name of the attachment.
     * @param name The vague name of the attachment to match.
     * @return The matched attachment, null if nothing matches.
     */
    public static Attachment matchClosest(String name) {
        name = name.toUpperCase();
        for (Attachment attachment : Attachment.values()) {
            if (Pattern.matches(name + ".+", attachment.toString())) return attachment;
        }
        return null;
    }

    /**
     * Checks and returns the attachment that goes with the item provided if it is a default item.
     * @param item The item to evaluate.
     * @return The attachment that is applicable to the item if any, null if the given item is not a default item.
     */
    public static Attachment matchDefault(ItemStack item) {
        for (Attachment attachment : Attachment.values()) {
            if (attachment.defaults == null)
                continue;
            for (Material material : attachment.defaults) {
                if (item.getType().equals(material))
                    return attachment;
            }
        }
/*        for (Material[] material : defaults) {
            for (Material m : material) {
                if (!item.getType().equals(m))
                    continue;
                if (material.equals(swords) || material.equals(bows))
                    return KILLS;
                if (material.equals(pickaxes) | material.equals(axes) | material.equals(spades))
                    return BLOCKS_BROKEN;
                if (material.equals(armor))
                    return DAMAGE_TAKEN;
                if (material.equals(shears))
                    return LEAVES_BROKEN;
                if (material.equals(hoes))
                    return FARMLAND_TILLED;
                if (material.equals(flintAndSteel))
                    return FIRES_STARTED;
                if (material.equals(fishingRod))
                    return FISH_CAUGHT;
            }
        }*/
        return null;
    }

    /**
     * Quickly check whether an attachment is a default attachment.
     * @param attachment The attachment to check
     * @return True or false.
     */
    public static boolean isDefault(Attachment attachment) {
        if (attachment.defaults != null)
            return true;
        return false;
    }

    /**
     * Checks the amount of attachments on a given item.
     * @param item The item to check.
     * @return The number of attachments on the item.
     */
    public static int amount(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore())
            return meta.getLore().size();
        return 0;
    }

    /**
     * Returns a list of the attachments on a given item, if any.
     * @param item The item.
     * @return The list of attachments on the item, null if none are found.
     */
    public static Attachment[] getAttachments(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            Attachment[] attachments = new Attachment[lore.size()]; // Every object in the array must be an Attachment.
            for (int i = 0; i < lore.size(); i++) {
                String attachment = ChatColor.stripColor(lore.get(i).split(":")[0].trim()); // The color code is left in the string, we don't want that.
                attachments[i] = match(attachment);
            }
            return attachments;
        }
        return null;
    }

    /**
     * Apply an attachment.
     * @param player The player to give the attachment to.
     */
    public static void apply(Player player) {
        ItemStack playerData = AttachmentApplicationData.get(player);
        if (playerData == null) {   // If the player is applying an attachment.
            player.sendMessage(ChatColor.RED + "This command can only be used when applying an attachment");
            return;
        }
        String attachmentString = playerData.getItemMeta().getDisplayName().split(":")[1].trim();  // Attachment: [attachment].
        Attachment attachment = Attachment.match(attachmentString); // Convert the attachment lore to an attachment object.
        ItemStack itemInHand = player.getItemInHand();
        Material itemInHandType = itemInHand.getType();
        if (itemInHandType.equals(Material.AIR)) {
            player.sendMessage(ChatColor.RED + attachment.name + " is not applicable to air.");
            return;
        }
        if (Attachment.matchDefault(itemInHand) == null && !itemInHand.getItemMeta().getLore().get(0).contains(ChatColor.GOLD + "")) {
            player.sendMessage(ChatColor.RED + "This item does not count statistics and can not receive attachments. /statistics create");
            return;
        }
/*        Attachment[] attachments = Attachment.getAttachments(itemInHand);
        if (!(attachments[1].equals(KILLS) || attachments[1].equals(BLOCKS_BROKEN) || attachments[1].equals(DAMAGE_TAKEN) ||    // Check for a default attachment.
                attachments[1].equals(LEAVES_BROKEN) || attachments[1].equals(FARMLAND_TILLED) || attachments[1].equals(FIRES_STARTED))) {
            player.sendMessage(ChatColor.RED + "This item does not count statistics and can not receive attachments. /statistics create");
            return;
        }*/
        boolean isApplicable = Attachment.isApplicable(itemInHand, attachment);
        if (!isApplicable) {
            player.sendMessage(ChatColor.RED + attachment.getName() + " is not applicable to " +
                    itemInHandType.toString().toLowerCase().replace("_", " "));
            return;
        }
        if (Attachment.hasAttachment(itemInHand, attachment)) {
            player.sendMessage(ChatColor.RED + "You may not apply the same part twice on a single weapon.");
            return;
        }
        if (Attachment.amount(itemInHand) > 6) {    // Limit 5 extra attachments per item.
            player.sendMessage(ChatColor.RED + "You have the maximum amount of attachments on your " +
                    itemInHand.getType().toString().toLowerCase().replace("_", " "));
            return;
        }
        ItemStack statisticItem = new ItemStats(itemInHand, attachment).getItemStack(); // Add the attachment to the item.
        player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount() - 1);
        player.getInventory().setItemInHand(statisticItem);
        player.sendMessage(ChatColor.GREEN + "The attachment " + attachment.getName().toLowerCase() + " " +
                "was applied to your " + itemInHand.getType().toString().toLowerCase().replace("_", " ") + "!");
        AttachmentApplicationData.remove(player);   // The application process is finished.
    }

    /**
     * Cancel a attachment application process.
     * @param player The player to cancel the application process for.
     */
    public static void cancel(Player player) {
        ItemStack playerData = AttachmentApplicationData.get(player);
        if (playerData == null) {
            player.sendMessage(ChatColor.RED + "This command can only be used when applying an attachment");
            return;
        }
        playerData.setAmount(1);
        player.getInventory().addItem(playerData);
        player.sendMessage(ChatColor.GRAY + playerData.getItemMeta().getDisplayName().split(":")[1].trim() + " was " +
                "returned to your inventory");
        AttachmentApplicationData.remove(player);   // The application was cancelled, give the player back their item.
    }

    /**
     * Buy an attachment.
     * @param player The player to give the attachment to.
     * @param attachment The attachment to buy.
     */
    public static void buy(Player player, Attachment attachment) {
        // Implement balance check.
        // ...
        if (attachment == null) {   // Was the argument the user provided a valid attachment?
            player.sendMessage(ChatColor.RED + "The specified attachment is not a valid attachment! /statistics list");
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
            player.sendMessage(ChatColor.RED + "You need " + costWithCurrency + " to buy " + attachment.name.toLowerCase() + ".");
            return;
        }
/*        if (attachment.equals(KILLS) || attachment.equals(BLOCKS_BROKEN) || attachment.equals(DAMAGE_TAKEN) || // Disallow the purchase of default attachments.
                attachment.equals(LEAVES_BROKEN) || attachment.equals(FARMLAND_TILLED) || attachment.equals(FIRES_STARTED)) {
            player.sendMessage(ChatColor.RED + "Default attachments may only be created with /statistics create.");
            return;
        }*/
        ItemStack attachable = new ItemAttachable(attachment, 1).getItemStack();
        player.getInventory().addItem(attachable);
        if (!StatsConfig.disableIntegrated) {
            StatsEconomy.withdraw(player, 30.00);
            player.sendMessage(ChatColor.GREEN + attachment.name + " was added to your inventory. Your new balance is " + moneyWithCurrency + ".");
            return;
        }
        player.sendMessage(ChatColor.GREEN + attachment.name + " was added to your inventory.");
    }

    public static void info(Player player, Attachment attachment) {
        if (attachment == null) {
            player.sendMessage(ChatColor.RED + "The specified attachment is not a valid attachment! /statistics list");
            return;
        }
        String info = Attachment.appliance(attachment);
        player.sendMessage(ChatColor.GREEN + attachment.getName() + ChatColor.RESET + " - Applicable item(s): " + ChatColor.GREEN + info + ChatColor.RESET + ", " +
                "Cost: " + ChatColor.GREEN + AttachmentConfig.getPrice(attachment) + " " + StatsEconomy.getCurrencyName());
    }
}
