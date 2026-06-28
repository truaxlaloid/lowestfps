package com.example.lowestfps.gui;

import com.example.lowestfps.Config;
import com.example.lowestfps.FrameTimeTracker;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class ClothConfigScreen {
    public static Screen createScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Lowest FPS Settings"));

        ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enable HUD"), Config.hudEnabled)
                .setDefaultValue(true)
                .setSaveConsumer(val -> Config.hudEnabled = val)
                .build());

        general.addEntry(entryBuilder.startStringDropdownMenu(Component.literal("HUD Position"), Config.hudPosition)
                .setDefaultValue("Top-Left")
                .setSelections(Arrays.asList("Top-Left", "Top-Right", "Bottom-Left", "Bottom-Right", "Custom"))
                .setSaveConsumer(val -> Config.hudPosition = val)
                .build());

        general.addEntry(entryBuilder.startIntField(Component.literal("Custom X (Pixels)"), Config.customX)
                .setDefaultValue(10)
                .setSaveConsumer(val -> Config.customX = val)
                .build());

        general.addEntry(entryBuilder.startIntField(Component.literal("Custom Y (Pixels)"), Config.customY)
                .setDefaultValue(10)
                .setSaveConsumer(val -> Config.customY = val)
                .build());

        general.addEntry(entryBuilder.startColorField(Component.literal("Text Color"), Config.textColor)
                .setDefaultValue(0xFF0000)
                .setSaveConsumer(val -> Config.textColor = val)
                .build());

        general.addEntry(entryBuilder.startFloatField(Component.literal("Text Scale"), Config.textScale)
                .setDefaultValue(1.0f)
                .setMin(0.5f)
                .setMax(3.0f)
                .setSaveConsumer(val -> Config.textScale = val)
                .build());

        general.addEntry(entryBuilder.startFloatField(Component.literal("Sliding Window (Seconds)"), Config.windowSizeSeconds)
                .setDefaultValue(2.0f)
                .setMin(0.5f)
                .setMax(60.0f)
                .setSaveConsumer(val -> Config.windowSizeSeconds = val)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Show Current FPS"), Config.showCurrentFps)
                .setDefaultValue(true)
                .setSaveConsumer(val -> Config.showCurrentFps = val)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Show Window Low FPS"), Config.showWindowLowFps)
                .setDefaultValue(true)
                .setSaveConsumer(val -> Config.showWindowLowFps = val)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Show Absolute Low FPS"), Config.showAbsoluteLowFps)
                .setDefaultValue(true)
                .setSaveConsumer(val -> Config.showAbsoluteLowFps = val)
                .build());

        general.addEntry(entryBuilder.startTextDescription(Component.literal("Configuring changes resets current low-FPS stats."))
                .build());

        builder.setSavingRunnable(() -> {
            Config.save();
            FrameTimeTracker.reset();
        });

        return builder.build();
    }
}
