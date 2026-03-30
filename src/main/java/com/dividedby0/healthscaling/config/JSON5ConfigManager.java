package com.dividedby0.healthscaling.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JSON5ConfigManager {
    private static final String CONFIG_FILENAME = "healthscaling.json5";
    private final Path configPath;
    private final Map<String, Object> configData;

    public static class ConfigEntry {
        public String key;
        public Object value;
        public Object minValue;
        public Object maxValue;
        public String description;
        public String type; // "int", "string", "boolean"

        public ConfigEntry(String key, Object value, String type, String description) {
            this.key = key;
            this.value = value;
            this.type = type;
            this.description = description;
        }
    }

    private final Map<String, ConfigEntry> configMetadata;

    public JSON5ConfigManager(Path configDir) {
        this.configPath = configDir.resolve(CONFIG_FILENAME);
        this.configData = new HashMap<>();
        this.configMetadata = new HashMap<>();
        initializeMetadata();
        loadConfig();
    }

    private void initializeMetadata() {
        configMetadata.put("xpThreshold_1", new ConfigEntry(
                "xpThreshold_1", 10, "int", "XP level required for 2 hearts"));
        configMetadata.put("xpThreshold_2", new ConfigEntry(
                "xpThreshold_2", 20, "int", "XP level required for 3 hearts"));
        configMetadata.put("xpThreshold_3", new ConfigEntry(
                "xpThreshold_3", 30, "int", "XP level required for 4 hearts"));
        configMetadata.put("xpThreshold_4", new ConfigEntry(
                "xpThreshold_4", 40, "int", "XP level required for 5 hearts"));
        configMetadata.put("xpThreshold_5", new ConfigEntry(
                "xpThreshold_5", 50, "int", "XP level required for 6 hearts"));
        configMetadata.put("xpThreshold_6", new ConfigEntry(
                "xpThreshold_6", 75, "int", "XP level required for 8 hearts"));
        configMetadata.put("xpThreshold_7", new ConfigEntry(
                "xpThreshold_7", 100, "int", "XP level required for 10 hearts"));
        configMetadata.put("xpThreshold_8", new ConfigEntry(
                "xpThreshold_8", 150, "int", "XP level required for 12 hearts"));
        configMetadata.put("xpThreshold_9", new ConfigEntry(
                "xpThreshold_9", 200, "int", "XP level required for 15 hearts"));
    }

    private void loadConfig() {
        createDefaults();

        if (Files.exists(configPath)) {
            try {
                String content = Files.readString(configPath);
                parseJSON5(content);
            } catch (IOException e) {
                System.err.println("Error reading config file: " + e.getMessage());
            }
        } else {
            saveConfig();
        }
    }

    private void parseJSON5(String json) {
        // Remove line and block comments
        json = json.replaceAll("//.*?\\n", "\n");
        json = json.replaceAll("/\\*.*?\\*/", "");

        Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*([^,}]+)");
        var matcher = pattern.matcher(json);

        while (matcher.find()) {
            String key = matcher.group(1);
            String valueStr = matcher.group(2).trim();

            Object value = parseValue(valueStr);
            if (value != null && configMetadata.containsKey(key)) {
                configData.put(key, value);
                configMetadata.get(key).value = value;
            }
        }
    }

    private Object parseValue(String valueStr) {
        valueStr = valueStr.replaceAll("[,\\s]+$", "");

        if (valueStr.equalsIgnoreCase("true")) return true;
        if (valueStr.equalsIgnoreCase("false")) return false;
        if (valueStr.startsWith("\"") && valueStr.endsWith("\"")) {
            return valueStr.substring(1, valueStr.length() - 1);
        }

        try {
            if (valueStr.contains(".")) {
                return Double.parseDouble(valueStr);
            } else {
                return Integer.parseInt(valueStr);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void createDefaults() {
        for (String key : configMetadata.keySet()) {
            configData.put(key, configMetadata.get(key).value);
        }
    }

    public void saveConfig() {
        try {
            Files.createDirectories(configPath.getParent());
            StringBuilder json = new StringBuilder("{\n");

            boolean first = true;
            for (String key : configData.keySet()) {
                if (!first) json.append(",\n");
                first = false;

                ConfigEntry entry = configMetadata.get(key);
                json.append("  // ").append(entry.description).append("\n");
                json.append("  \"").append(key).append("\": ");

                Object value = configData.get(key);
                if (value instanceof String) {
                    json.append("\"").append(value).append("\"");
                } else {
                    json.append(value);
                }
            }

            json.append("\n}\n");
            Files.writeString(configPath, json.toString());
        } catch (IOException e) {
            System.err.println("Error saving config file: " + e.getMessage());
        }
    }

    public int getInt(String key, int defaultValue) {
        try {
            Object val = configData.get(key);
            if (val instanceof Integer) return (Integer) val;
            if (val instanceof Number) return ((Number) val).intValue();
            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public void setInt(String key, int value) {
        configData.put(key, value);
        if (configMetadata.containsKey(key)) {
            configMetadata.get(key).value = value;
        }
    }

    public String getString(String key, String defaultValue) {
        try {
            Object val = configData.get(key);
            if (val instanceof String) return (String) val;
            if (val != null) return val.toString();
            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public void setString(String key, String value) {
        configData.put(key, value);
        if (configMetadata.containsKey(key)) {
            configMetadata.get(key).value = value;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        try {
            Object val = configData.get(key);
            if (val instanceof Boolean) return (Boolean) val;
            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public void setBoolean(String key, boolean value) {
        configData.put(key, value);
        if (configMetadata.containsKey(key)) {
            configMetadata.get(key).value = value;
        }
    }

    public String[] getAllKeys() {
        return configData.keySet().toArray(new String[0]);
    }

    public Map<String, ConfigEntry> getConfigMetadata() {
        return configMetadata;
    }

    public Map<String, Object> getAllConfig() {
        return new HashMap<>(configData);
    }
}
