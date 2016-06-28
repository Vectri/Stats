package io.github.vectri.Stats;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * A class that handles the stats command.
 */
public class StatisticsCommand implements CommandExecutor {
    private Stats plugin;

    StatisticsCommand(Stats plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String arg1 = (args.length != 0) ? args[0].toLowerCase() : null;
        String arg2 = (args.length > 1) ? args[1].toLowerCase() : null;
        if (!(sender instanceof Player)) {
            plugin.getLogger().info("Stats does not work from console. Go in game.");
            return true;
        }
        Player player = (Player) sender;
        switch (args.length) {
            case 0:
                return false;
            case 1: {   // Curly braces are used on the first cases so to prevent naming conflicts.
                switch (arg1) {
                    case "help":
                        String[] message = {ChatColor.GREEN + "stats v" + plugin.getDescription().getVersion() + " by vectri",
                                ChatColor.GRAY + "aliases for statistics: /stats, /s" + ChatColor.RESET,
                                "/statistics help - this page",
                                "/statistics list - a list of attachments",
                                "/statistics create - makes an item count statistics",
                                "/statistics buy <attachment> - buy an attachment",
                                "/statistics info <attachment> - info on an attachment"};
                        if (StatsEconomy.internal) {
                            String[] alternate = Arrays.copyOf(message, message.length + 1);
                            alternate[alternate.length - 1] =  "/statistics balance - check your balance";
                            sender.sendMessage(alternate);
                            return true;
                        }
                        sender.sendMessage(message);
                        break;
                    case "list":
                        sender.sendMessage(Attachment.list());
                        break;
                    case "balance":
                        // !!! ONLY IF THERE IS NO ECONOMY PLUGIN FOUND !!!
                        if (StatsEconomy.internal && !StatsConfig.disableIntegrated) {
                            player.sendMessage("Your balance is: " + StatsEconomy.getBalance(player) + " " + StatsEconomy.getCurrencyName());
                        } else {
                            return false;
                        }
                        break;
                    case "create":
                        ItemStats.create(player);
                        break;
                    case "apply":
                        Attachment.apply(player);
                        break;
                    case "cancel":
                        Attachment.cancel(player);
                        break;
                    case "buy":
                        sender.sendMessage("/statistics buy <attachment>");
                        break;
                    case "info":
                        sender.sendMessage("/statistics info <attachment>");
                        break;
/*                    case "test":
                        StatsHandler.add(player, Attachment.KILLS, 8500);*/
                    default:
                        return false;
                }
                break;
            }
            case 2: {
                Attachment attachment = Attachment.fromString(arg2);
                switch (arg1) {
                    case "buy":
                        Attachment.buy(player, attachment);
                        break;
                    case "info":
                        Attachment.info(player, attachment);
                        break;
                    default:
                        return false;
                }
                break;
            }
            default: {
                String remaningArgs = arg2;
                for (int i = 2; i < args.length; i++) {
                    remaningArgs += " " + args[i]; // Concatenates each additional argument separated by spaces.
                }
                Attachment attachment = Attachment.fromString(remaningArgs);
                switch (arg1) {
                    case "buy":
                        Attachment.buy(player, attachment);
                        break;
                    case "info":
                        Attachment.info(player, attachment);
                        break;
                    default:
                        return false;
                }
            }
        }
        return true;   // Returning false shows the correct usage of the command if desired.
    }
}
