package io.github.vectri.Stats;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for adding tab completions to our commands to make them easier to use.
 */
public class StatisticsCommandTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestion = new ArrayList<String>();
        switch (args.length) {
            case 1:
                suggestion.add("help");
                suggestion.add("list");
                suggestion.add("create");
                suggestion.add("apply");
                suggestion.add("cancel");
                suggestion.add("buy");
                suggestion.add("info");
                if (StatsEconomy.internal) suggestion.add("balance");
                break;
            case 2:
                switch (args[0].toLowerCase()) {
                    case "info":
                    case "buy":
                        for (Attachment attachment : Attachment.values()) {
                            suggestion.add(attachment.toString());
                        }
                        break;
                }
        }
        return suggestion;
    }
}
