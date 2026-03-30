package com.dividedby0.healthscaling.config;

import net.minecraft.client.Minecraft;
import java.nio.file.Paths;

public class ConfigManager {
    private static JSON5ConfigManager instance = null;

    public static JSON5ConfigManager getInstance() {
        if (instance == null) {
            try {
                Minecraft mc = Minecraft.getInstance();
                java.nio.file.Path configPath = Paths.get(mc.gameDirectory.toString(), "config");
                instance = new JSON5ConfigManager(configPath);
            } catch (Exception e) {
                System.err.println("Failed to initialize config manager: " + e.getMessage());
                e.printStackTrace();
                try {
                    instance = new JSON5ConfigManager(Paths.get(System.getProperty("java.io.tmpdir")));
                } catch (Exception ex) {
                    System.err.println("Failed to create fallback config manager: " + ex.getMessage());
                }
            }
        }
        return instance;
    }

    public static void reload() {
        instance = null;
        getInstance();
    }
}
