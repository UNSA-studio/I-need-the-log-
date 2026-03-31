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

@Mod(Ineedthelog.MOD_ID)
public class Ineedthelog {
    public static final String MOD_ID = "i_need_the_log";

    public Ineedthelog(ModContainer container, IEventBus modBus) {
        // 注册配置
        ModConfig.register(container);
        // 注册事件监听
        modBus.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            if (ModConfig.COMMON.firstRun.get()) {
                Minecraft.getInstance().setScreen(new FirstRunSetupScreen());
            }
        });
    }
}
