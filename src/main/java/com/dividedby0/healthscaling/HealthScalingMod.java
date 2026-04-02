package com.dividedby0.healthscaling;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.client.ConfigScreenHandler;
import com.dividedby0.healthscaling.config.ConfigManager;
import com.dividedby0.healthscaling.item.ModItems;

@Mod(HealthScalingMod.MODID)
public class HealthScalingMod {
    public static final String MODID = "healthscaling";

    @SuppressWarnings("removal")
    public HealthScalingMod() {
        // Initialize JSON5 config manager
        ConfigManager.getInstance();

        // Register config screen factory for the mods menu config button
        ModLoadingContext.get().registerExtensionPoint(
            ConfigScreenHandler.ConfigScreenFactory.class,
            () -> new ConfigScreenHandler.ConfigScreenFactory(
                (minecraft, screen) -> new SimpleConfigScreen(screen, ConfigManager.getInstance())
            )
        );

        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        HealthScalingHandler.init();
    }
}
