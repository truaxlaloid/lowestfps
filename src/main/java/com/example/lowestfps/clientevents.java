package com.example.lowestfps;

import com.example.lowestfps.gui.ClothConfigScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = LowestFpsMod.MODID, value = Dist.CLIENT)
public class ClientEvents {
    public static final KeyMapping OPEN_CONFIG_KEY = new KeyMapping(
            "key.lowestfps.open_config",
            GLFW.GLFW_KEY_L, 
            "key.categories.misc"
    );

    public static final KeyMapping RESET_KEY = new KeyMapping(
            "key.lowestfps.reset",
            GLFW.GLFW_KEY_K, 
            "key.categories.misc"
    );

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.player != null) {
            while (OPEN_CONFIG_KEY.consumeClick()) {
                mc.setScreen(ClothConfigScreen.createScreen(mc.screen));
            }
            while (RESET_KEY.consumeClick()) {
                FrameTimeTracker.reset();
                mc.player.displayClientMessage(net.minecraft.network.chat.Component.literal("Lowest FPS stats reset!"), true);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGuiPre(RenderGuiEvent.Pre event) {
        // Record frame time before rendering HUD, excluding HUD overhead
        FrameTimeTracker.recordFrame();
    }

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || !Config.hudEnabled || mc.level == null) {
            return;
        }

        double maxTimeWindow = FrameTimeTracker.getMaxFrameTimeMs();
        double maxTimeAbsolute = FrameTimeTracker.getAbsoluteMaxFrameTimeMs();

        double currentFps = 1000.0 / FrameTimeTracker.getLastFrameTimeMs();
        double windowLowFps = maxTimeWindow > 0 ? 1000.0 / maxTimeWindow : 0;
        double absoluteLowFps = maxTimeAbsolute > 0 ? 1000.0 / maxTimeAbsolute : 0;

        List<String> lines = new ArrayList<>();
        if (Config.showCurrentFps) {
            lines.add(String.format("FPS: %.1f", currentFps));
        }
        if (Config.showWindowLowFps) {
            lines.add(String.format("Min (%.1fs): %.1f", Config.windowSizeSeconds, windowLowFps));
        }
        if (Config.showAbsoluteLowFps) {
            lines.add(String.format("True Min: %.1f", absoluteLowFps));
        }

        if (lines.isEmpty()) {
            return;
        }

        GuiGraphics graphics = event.getGuiGraphics();
        Font font = mc.font;
        int windowWidth = mc.getWindow().getGuiScaledWidth();
        int windowHeight = mc.getWindow().getGuiScaledHeight();

        int maxTextWidth = 0;
        for (String line : lines) {
            int w = font.width(line);
            if (w > maxTextWidth) {
                maxTextWidth = w;
            }
        }

        graphics.pose().pushPose();
        graphics.pose().scale(Config.textScale, Config.textScale, 1.0f);

        int lineCount = lines.size();
        int fontHeight = font.lineHeight;
        int totalHeight = lineCount * (fontHeight + 2);

        int x = 0;
        int y = 0;
        float invScale = 1.0f / Config.textScale;

        switch (Config.hudPosition) {
            case "Top-Left":
                x = (int) (10 * invScale);
                y = (int) (10 * invScale);
                break;
            case "Top-Right":
                x = (int) ((windowWidth - 10) * invScale - maxTextWidth);
                y = (int) (10 * invScale);
                break;
            case "Bottom-Left":
                x = (int) (10 * invScale);
                y = (int) ((windowHeight - 10) * invScale - totalHeight);
                break;
            case "Bottom-Right":
                x = (int) ((windowWidth - 10) * invScale - maxTextWidth);
                y = (int) ((windowHeight - 10) * invScale - totalHeight);
                break;
            case "Custom":
            default:
                x = (int) (Config.customX * invScale);
                y = (int) (Config.customY * invScale);
                break;
        }

        int color = Config.textColor | 0xFF000000;

        for (int i = 0; i < lineCount; i++) {
            graphics.drawString(font, lines.get(i), x, y + i * (fontHeight + 2), color, true);
        }

        graphics.pose().popPose();
    }
}
