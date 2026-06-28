package com.example.lowestfps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Config {
    public static boolean hudEnabled = true;
    public static String hudPosition = "Top-Left"; // Options: Top-Left, Top-Right, Bottom-Left, Bottom-Right, Custom
    public static int customX = 10;
    public static int customY = 10;
    public static int textColor = 0xFF0000; // Defaults to bright Red
    public static float textScale = 1.0f;
    public static float windowSizeSeconds = 2.0f;
    public static boolean showCurrentFps = true;
    public static boolean showWindowLowFps = true;
    public static boolean showAbsoluteLowFps = true;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;

    public static void init() {
        configFile = new File(Minecraft.getInstance().gameDirectory, "config/lowestfps.json");
        load();
    }

    public static void load() {
        if (configFile == null || !configFile.exists()) {
            save();
            return;
        }
        try (FileReader reader = new FileReader(configFile)) {
            ConfigData data = GSON.fromJson(reader, ConfigData.class);
            if (data != null) {
                hudEnabled = data.hudEnabled;
                hudPosition = data.hudPosition;
                customX = data.customX;
                customY = data.customY;
                textColor = data.textColor;
                textScale = data.textScale;
                windowSizeSeconds = data.windowSizeSeconds;
                showCurrentFps = data.showCurrentFps;
                showWindowLowFps = data.showWindowLowFps;
                showAbsoluteLowFps = data.showAbsoluteLowFps;
            }
        } catch (Exception e) {
            LowestFpsMod.LOGGER.error("Failed to load config", e);
        }
    }

    public static void save() {
        if (configFile == null) return;
        try {
            configFile.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(configFile)) {
                ConfigData data = new ConfigData();
                data.hudEnabled = hudEnabled;
                data.hudPosition = hudPosition;
                data.customX = customX;
                data.customY = customY;
                data.textColor = textColor;
                data.textScale = textScale;
                data.windowSizeSeconds = windowSizeSeconds;
                data.showCurrentFps = showCurrentFps;
                data.showWindowLowFps = showWindowLowFps;
                data.showAbsoluteLowFps = showAbsoluteLowFps;
                GSON.toJson(data, writer);
            }
        } catch (Exception e) {
            LowestFpsMod.LOGGER.error("Failed to save config", e);
        }
    }

    private static class ConfigData {
        boolean hudEnabled;
        String hudPosition;
        int customX;
        int customY;
        int textColor;
        float textScale;
        float windowSizeSeconds;
        boolean showCurrentFps;
        boolean showWindowLowFps;
        boolean showAbsoluteLowFps;
    }
}
