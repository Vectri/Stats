package io.github.vectri.Stats;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Handles economy setup.
 */
public class StatsEconomy {
    public Stats plugin;
    private static Economy economy;
    public static boolean internal = false;
    private static StatsEconomyInternal economyInternal;

    StatsEconomy(Stats plugin) {
        this.plugin = plugin;
        RegisteredServiceProvider<Economy> rsp = null;
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            rsp = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        } else {
            plugin.getLogger().info("Vault not found. Vault is required to use an external economy.");
        }
        if (rsp == null) {
            if (StatsConfig.disableIntegrated) {
                plugin.getLogger().info("No economy plugin was found. The internal economy is disabled. Switching to no economy.");
                return;
            }
            plugin.getLogger().info("No economy plugin was found. Switching to internal economy.");
            internal = true;
            economyInternal = new StatsEconomyInternal(plugin);
            return;
        }
        economy = rsp.getProvider();
    }

    /**
     * Returns a player's balance.
     * @param player The player.
     * @return The player's balance.
     */
    public static double getBalance(Player player) {
        if (!StatsConfig.disableIntegrated)
            return (internal) ?  economyInternal.getBalance(player) : economy.getBalance(player);
        return 0.0;
    }

    /**
     * Returns the name of the currency used by the economy plugin.
     * @return The name of the currency./
     */
    public static String getCurrencyName() {
        if (!StatsConfig.disableIntegrated)
            return (internal) ? StatsConfig.integratedCurrency : economy.currencyNamePlural();
        return null;
    }

    /**
     * Deposits money into an account.
     * @param player The player who owns the account.
     * @param amount The amount to deposit.
     */
    public static void deposit(Player player, double amount) {
        if (internal) {
            economyInternal.deposit(player, amount);
            return;
        }
        economy.depositPlayer(player, amount);
    }

    /**
     * Withdraws money from an account.
     * @param player The player who owns the account.
     * @param amount The amount to withdraw.
     */
    public static void withdraw(Player player, double amount) {
        if (internal) {
            economyInternal.withdraw(player, amount);
            return;
        }
        economy.withdrawPlayer(player, amount);
    }

    /**
     * If the internal economy is being used, saves the .yml
     */
    public void save() {
        if (internal) economyInternal.save();
    }
}
