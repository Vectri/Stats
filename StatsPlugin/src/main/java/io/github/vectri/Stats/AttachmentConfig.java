package io.github.vectri.Stats;

import java.util.HashMap;

/**
 * A class to handle the configuration of the prices of each attachment.
 */
public class AttachmentConfig {
    private Stats plugin;
    private static HashMap<Attachment, Double> attachments = new HashMap<>();
    private static ConfigAccessor attachmentsFile;

    AttachmentConfig(Stats plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        attachmentsFile = new ConfigAccessor(plugin, "attachments.yml");
        attachmentsFile.saveDefaultConfig();
        try {
            for(String key : attachmentsFile.getConfig().getConfigurationSection("attachments").getKeys(false)) {
                Attachment attachment = Attachment.fromString(key);
                attachments.put(attachment, attachmentsFile.getConfig().getDouble("attachments." + attachment));
            }
        } catch (NullPointerException npe) {
            plugin.getLogger().severe("Unable to read attachments.yml, default values will be used.");
        }
        addDefaults();
    }

    /**
     * Adds the remaining un priced attachments to the HashMap to ensure fluidity in the case of an incomplete attachments.yml
     */
    private void addDefaults() {
        for (Attachment attachment : Attachment.values()) {
            if (attachments.containsKey(attachment))
                continue;
            double price = 0;
            price = (Attachment.isDefault(attachment)) ? 100.0 : 30.0;  // Defaults are 100 by default.
            attachments.put(attachment, price);
        }
    }

    /**
     * Returns the price of an attachment, if it exists.
     * @param attachment The attachment.
     * @return The value of the attachment, -1 if it does not exist.
     */
    public static double getPrice(Attachment attachment) {
        if (attachments.containsKey(attachment))
            return attachments.get(attachment);
        return -1;
    }
}
