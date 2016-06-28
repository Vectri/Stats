package io.github.vectri.Stats;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/**
 * A class to tie in-game events and attachment data.
 */
public class StatsListener implements Listener {
    private Stats plugin;

    StatsListener(Stats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (StatsEconomy.internal) StatsEconomyInternal.checkAccount(event.getPlayer());
    }

    @EventHandler
    public void onPlayerBreath(PlayerBreathEvent event) {
        HashMap<Player, Integer> breathData = event.getBreathData();
        HashMap<Player, Integer> cachedBreathData = event.getCachedBreathData();

        for (Player player : breathData.keySet()) {
            for (Player cachedPlayer : cachedBreathData.keySet()) {
                if (breathData.get(player) < cachedBreathData.get(cachedPlayer)) {  // If the player's breath depleted, they are holding their breath.
                    ItemStack helmet = player.getInventory().getHelmet();
                    if (helmet == null)
                        continue;
                    ItemMeta meta = helmet.getItemMeta();
                    StatsHandler.add(player, helmet, Attachment.BREATH_HELD, 1);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.hasItem() && !(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            return;
        ItemStack itemInHand = player.getItemInHand();
        if (itemInHand.getType().equals(Material.PAPER)) {
            ItemAttachable.apply(player);   // Start attachment application.
            return;
        }
        if (!event.hasBlock())
            return;
        Material clickedBlock = event.getClickedBlock().getType();
        if (!(clickedBlock.equals(Material.DIRT) || clickedBlock.equals(Material.GRASS)))
            return;
        for (Material material : AttachmentCategories.hoes) {
            if (material.equals(itemInHand.getType()))
                StatsHandler.add(player, Attachment.FARMLAND_TILLED, 1);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();
        if (itemInHand.getType().equals(Material.AIR))
            return;
        StatsHandler.add(player, Attachment.BLOCKS_BROKEN, 1);
        Material block = event.getBlock().getType();
        if (block.equals(Material.DIAMOND_ORE) && !(itemInHand.getEnchantments().containsKey(Enchantment.SILK_TOUCH))) {
            StatsHandler.add(player, Attachment.DIAMONDS_BROKEN, 1);
        } else if (block.equals(Material.LEAVES) || block.equals(Material.LEAVES_2)) {
            StatsHandler.add(player, Attachment.LEAVES_BROKEN, 1);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        HumanEntity killer = entity.getKiller();
        if (!(killer instanceof Player))
            return;
        ItemStack itemInHand = killer.getItemInHand();
        String entityName = entity.getName();
        if (entityName.equals("Skeleton") && ((Skeleton) entity).getSkeletonType().equals(Skeleton.SkeletonType.WITHER)) {
            entityName = "Wither" + entityName; // WitherSkeleton.
        } else if (entityName.equals("Horse")) {
            Variant horseType = ((Horse) entity).getVariant();
            if (horseType.equals(Variant.DONKEY)) {
                entityName = "Donkey";
            } else if (horseType.equals(Variant.MULE)) {
                entityName = "Mule";
            }
        } else if (entityName.equals("Zombie") && ((Zombie) entity).isVillager()) {
            entityName = "Zombie" + entityName; // ZombieVillager.
        } else if (entityName.equals("Guardian") && ((Guardian) entity).isElder()) {
            entityName = "Elder" + entityName;  // ElderGuardian.
        } else if (entityName.equals("Snowman")) {
            entityName = "SnowGolem";
        } else if (entityName.equals("Mooshroom")) {
            entityName = "MushroomCow";
        }
        entityName = entityName.replaceAll("\\s", "");  // Get rid of spaces in the entity name.
        Attachment attachment = Attachment.matchClosest(entityName);    // The entity name is not 1:1 with the attachment.
        // Increment an attachment if the weapon has it.
        Player player = (Player) killer;
        StatsHandler.add(player, attachment, 1);
        StatsHandler.add(player, Attachment.KILLS, 1);
        StatsHandler.addAirborneEnemyKills(player, entity);
        StatsHandler.addKillsWhileAirborne(player);
        StatsHandler.addLowHealthKills(player);
        StatsHandler.addCriticalKills(player);
        if (!itemInHand.getType().equals(Material.BOW))
            return;
        StatsHandler.addLongDistanceKills(player, entity);
        EntityDamageEvent damageCause = entity.getLastDamageCause();
        if (!(damageCause instanceof EntityDamageByEntityEvent))
            return;
        Entity damage = ((EntityDamageByEntityEvent) damageCause).getDamager();
        if (!(damage instanceof Arrow))
            return;
        if (!((Arrow) damage).isCritical())
            return;
        StatsHandler.add(player, Attachment.CRITICAL_KILLS, 1); // No need to check for the conditions included in StatsHandler.addCriticalKills(...).
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (!(event.getIgnitingEntity() instanceof Player))
            return;
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();
        if (!event.getCause().equals(BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) && (!itemInHand.getType().equals(Material.FLINT_AND_STEEL)))
            return;
        StatsHandler.add(player, Attachment.FIRES_STARTED, 1);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if (entity instanceof Player) {
            Player player = ((Player) entity);
            if (player.isBlocking()) {
                ItemStack itemInHand = player.getItemInHand();
                StatsHandler.add(player, Attachment.BLOCKED_ATTACKS, 1);
            }
        }
        if (damager instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack itemInHand = player.getItemInHand();
            int amount = (int) event.getDamage();
            StatsHandler.add(player, Attachment.DAMAGE_GIVEN, amount);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        if (event.getCause().equals(DamageCause.FIRE_TICK))
            return;
        Player player = (Player) event.getEntity();
        ItemStack[] Armor = player.getInventory().getArmorContents();
        int amount = (int) event.getDamage();
        for (ItemStack armor : Armor) {
            if (armor.getType().equals(Material.AIR))
                continue;
            if (event.getCause().equals(DamageCause.FALL)) {
                if (Armor[3] != null)
                    StatsHandler.add(player, armor, Attachment.FALL_DAMAGE_TAKEN, amount);
                continue;
            }
            StatsHandler.add(player, armor, Attachment.DAMAGE_TAKEN, amount);
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        final Player player = (Player) event.getEntity();
        new BukkitRunnable() {  // Schedule a new task to run AFTER the fire stops.
            @Override
            public void run() {
/*                if (player.getFireTicks() != 0) // If the player is still burning, don't do anything.
                    return;*/
                if (player.isDead())    // If the player died after the fire stopped.
                    return;
                if (player.getHealth() == player.getMaxHealth())    // If the player was on fire but did not take damage.
                    return;
                ItemStack[] Armor = player.getInventory().getArmorContents();
                for (ItemStack armor : Armor) {
                    if (armor.getType().equals(Material.AIR))
                        continue;
                    StatsHandler.add(player, armor, Attachment.FIRES_SURVIVED, 1);
                }
            }
        }.runTaskLater(this.plugin, event.getDuration() * 20);    // The duration of the fire ticks in seconds.
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();
        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH))
            StatsHandler.add(player, Attachment.FISH_CAUGHT, 1);
        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY))
            StatsHandler.add(player, Attachment.ENTITIES_HOOKED, 1);
    }

    @EventHandler
    public void onPlayerShearSheap(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        StatsHandler.add(player, Attachment.SHEEP_SHEARED, 1);
    }
}
