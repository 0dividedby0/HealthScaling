package com.dividedby0.healthscaling;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;

public class HealthScalingHandler {

    private static final String INITIALIZED_KEY = "HealthScaling_HealthInitialized";
    private static final String TOTAL_HEARTS_KEY = "HealthScaling_TotalHearts"; // legacy key
    private static final String XP_HEARTS_KEY = "HealthScaling_XPHearts";
    private static final String BONUS_HEARTS_KEY = "HealthScaling_BonusHearts";

    public static void init() {
        MinecraftForge.EVENT_BUS.register(HealthScalingHandler.class);
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            CompoundTag tag = player.getPersistentData();

            if (!tag.getBoolean(INITIALIZED_KEY)) {
                tag.putBoolean(INITIALIZED_KEY, true);
                tag.putInt(XP_HEARTS_KEY, 1);
                tag.putInt(BONUS_HEARTS_KEY, 0);
            }

            // Migrate from legacy total-heart storage to bonus-only storage.
            if ((!tag.contains(XP_HEARTS_KEY) || !tag.contains(BONUS_HEARTS_KEY)) && tag.contains(TOTAL_HEARTS_KEY)) {
                int legacyTotalHearts = tag.getInt(TOTAL_HEARTS_KEY);
                int xpHearts = Math.min(legacyTotalHearts, Math.max(1, getHeartsFromLevel(player.experienceLevel)));
                int bonusHearts = Math.max(0, legacyTotalHearts - xpHearts);
                tag.putInt(XP_HEARTS_KEY, xpHearts);
                tag.putInt(BONUS_HEARTS_KEY, bonusHearts);
            }

            if (!tag.contains(XP_HEARTS_KEY)) {
                tag.putInt(XP_HEARTS_KEY, Math.max(1, getHeartsFromLevel(player.experienceLevel)));
            }
            if (!tag.contains(BONUS_HEARTS_KEY)) {
                tag.putInt(BONUS_HEARTS_KEY, 0);
            }

            updateFromXP(player);
            applyHealth(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof ServerPlayer player)) return;
        if (event.phase != TickEvent.Phase.END) return;

        updateFromXP(player);
        applyHealth(player);
    }

    private static void updateFromXP(ServerPlayer player) {

        CompoundTag tag = player.getPersistentData();

        int latchedXpHearts = Math.max(1, tag.getInt(XP_HEARTS_KEY));
        int targetHearts = getHeartsFromLevel(player.experienceLevel);

        // XP hearts are latched: they can only go up, never down.
        if (targetHearts > latchedXpHearts) {
            tag.putInt(XP_HEARTS_KEY, targetHearts);
        }
    }

    private static void applyHealth(ServerPlayer player) {

        CompoundTag tag = player.getPersistentData();
        int xpHearts = Math.max(1, tag.getInt(XP_HEARTS_KEY));
        int bonusHearts = Math.max(0, tag.getInt(BONUS_HEARTS_KEY));
        int hearts = xpHearts + bonusHearts;

        double maxHealth = hearts * 2.0;

        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);

        if (player.getHealth() > maxHealth) {
            player.setHealth((float) maxHealth);
        }
    }

    private static int getHeartsFromLevel(int level) {
        var config = com.dividedby0.healthscaling.config.ConfigManager.getInstance();
        if (level >= config.getInt("xpThreshold_9", 200)) return 15;
        if (level >= config.getInt("xpThreshold_8", 150)) return 12;
        if (level >= config.getInt("xpThreshold_7", 100)) return 10;
        if (level >= config.getInt("xpThreshold_6", 75)) return 8;
        if (level >= config.getInt("xpThreshold_5", 50)) return 6;
        if (level >= config.getInt("xpThreshold_4", 40)) return 5;
        if (level >= config.getInt("xpThreshold_3", 30)) return 4;
        if (level >= config.getInt("xpThreshold_2", 20)) return 3;
        if (level >= config.getInt("xpThreshold_1", 10)) return 2;
        return 1;
    }

    public static void addHeart(ServerPlayer player) {
        CompoundTag tag = player.getPersistentData();

        int bonusHearts = Math.max(0, tag.getInt(BONUS_HEARTS_KEY));
        tag.putInt(BONUS_HEARTS_KEY, bonusHearts + 1);
    }
}
