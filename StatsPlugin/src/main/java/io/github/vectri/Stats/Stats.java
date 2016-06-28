package io.github.vectri.Stats;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginChannelDirection;
import org.bukkit.scheduler.BukkitTask;


/**
 * The main class.
 */
public class Stats extends JavaPlugin {
    private BukkitTask pbd;
    private StatsEconomy economy;

    @Override
    public void onEnable() {
        // Command.
        PluginCommand statsCommand = this.getCommand("statistics");
        statsCommand.setExecutor(new StatisticsCommand(this));
        // Listener.
        statsCommand.setTabCompleter(new StatisticsCommandTabComplete());
        this.getServer().getPluginManager().registerEvents(new StatsListener(this), this);
        pbd = new PlayerBreathData(this).runTaskTimer(this, 0, 30);
        // Economy.
        economy = new StatsEconomy(this);
        // Config files.
        new AttachmentRank(this);
        new AttachmentConfig(this);
        new StatsConfig(this);
    }

    @Override
    public void onDisable() {
        pbd.cancel();
        if (economy.internal) economy.save();
    }
}