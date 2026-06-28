package com.example.lowestfps;

import com.example.lowestfps.gui.ClothConfigScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(LowestFpsMod.MODID)
public class LowestFpsMod {
    public static final String MODID = "lowestfps";
    public static final Logger LOGGER = LoggerFactory.getLogger("LowestFPS");

    public LowestFpsMod(ModContainer container, IEventBus modBus) {
        Config.init();

        // Register the Cloth Config screen natively to show up on the mod list
        container.registerExtensionPoint(IConfigScreenFactory.class, 
            (mc, parent) -> ClothConfigScreen.createScreen(parent));

        LOGGER.info("Lowest FPS Mod Initialized!");
    }
}
