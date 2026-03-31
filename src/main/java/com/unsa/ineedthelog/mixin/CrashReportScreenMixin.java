package com.unsa.ineedthelog.mixin;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;

@OnlyIn(Dist.CLIENT)
@Mixin(targets = "net.minecraft.client.gui.screens.CrashReportScreen")
public abstract class CrashReportScreenMixin {
}
