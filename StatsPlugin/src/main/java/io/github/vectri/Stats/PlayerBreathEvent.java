package io.github.vectri.Stats;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

/**
 * A class to handle when a player is under water and losing breath.
 */
public class PlayerBreathEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private HashMap<Player, Integer> breathData = new HashMap<Player, Integer>();
    private HashMap<Player, Integer> cachedBreathData = new HashMap<Player, Integer>();

    PlayerBreathEvent(HashMap<Player, Integer> breathData, HashMap<Player, Integer> cachedBreathData) {
        this.breathData = breathData;
        this.cachedBreathData = cachedBreathData;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public HashMap<Player, Integer> getBreathData() {
        return breathData;
    }

    public HashMap<Player, Integer> getCachedBreathData() {
        return cachedBreathData;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
