package io.github.vectri.Stats;

import org.bukkit.entity.Player;

import java.io.IOError;
import java.util.HashMap;

/**
 * An internal economy if no economy is provided.
 */
public class StatsEconomyInternal {
    private Stats plugin;
    private static ConfigAccessor accountsFile;
    private static HashMap<String, Double> accounts = new HashMap<>();

    StatsEconomyInternal(Stats plugin) {
        this.plugin = plugin;
        load();
        save();
    }

    /**
     * Loads the accounts.yml into memory.
     */
    private void load() {
        accountsFile = new ConfigAccessor(plugin, "accounts.yml");
        try {
            for(String key : accountsFile.getConfig().getConfigurationSection("accounts").getKeys(false)) {
                accounts.put(key, accountsFile.getConfig().getDouble("accounts." + key));
            }
        } catch (NullPointerException e) {
            plugin.getLogger().severe("No data found in accounts.yml, if this is your first time running the integrated economy you may safely ignore this.");
        }
    }

    /**
     * Saves the data contained in the accounts HashMap to the config file.
     */
    public void save() {
        accountsFile.createSection("accounts", accounts);
        accountsFile.saveConfig();
    }

    /**
     * Gets the balance of a player from the HashMap.
     * @param player The player to check.
     * @return The balance of the player.
     */
    public static double getBalance(Player player) {
        return accounts.get(player.getName());
    }

    /**
     * Deposits money into an account.
     * @param player The player who owns the account.
     * @param amount The amount to deposit.
     */
    public static void deposit(Player player, double amount) {
        String playerName = player.getName();
        double balance = accounts.get(playerName);
        accounts.put(player.getName(), balance + amount);
    }

    /**
     * Withdraws money from an account.
     * @param player The player who owns the account.
     * @param amount The amount to withdraw.
     */
    public static void withdraw(Player player, double amount) {
        String playerName = player.getName();
        double balance = accounts.get(playerName);
        double newBalance = balance - amount;
        if (newBalance < 0)
            newBalance = 0;
        accounts.put(player.getName(), newBalance);
    }

    /**
     * Creates a new account for a given player if they do not already have one.
     * @param player The player to create an account for.
     */
    public static void checkAccount(Player player) {
        if (!accounts.containsKey(player.getName())) accounts.put(player.getName(), 100.0);
    }
}
