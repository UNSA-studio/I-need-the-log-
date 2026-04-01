package com.unsa.ineedthelog;

import com.unsa.ineedthelog.config.ModConfig;
import com.unsa.ineedthelog.gui.FirstRunSetupScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Ineedthelog.MOD_ID)
public class Ineedthelog {
    public static final String MOD_ID = "i_need_the_log";
    private static final Logger LOGGER = LoggerFactory.getLogger(Ineedthelog.class);
    private static boolean firstRunHandled = false;

    public Ineedthelog(ModContainer container, IEventBus modBus) {
        ModConfig.register(container);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modBus.addListener(this::clientSetup);
            NeoForge.EVENT_BUS.register(this);
        }
        LOGGER.info("I need the log! mod loaded.");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // 无需额外操作，等待 ClientTickEvent
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        if (!firstRunHandled && ModConfig.COMMON.firstRun.get()) {
            firstRunHandled = true;
            LOGGER.info("Showing first run setup screen (tick)");
            Minecraft.getInstance().execute(() -> {
                Minecraft.getInstance().setScreen(new FirstRunSetupScreen());
            });
        }
    }
}
