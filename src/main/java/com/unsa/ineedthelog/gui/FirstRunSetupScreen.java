package com.unsa.ineedthelog.gui;

import com.unsa.ineedthelog.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class FirstRunSetupScreen extends Screen {
    private EditBox pathField;
    private String message = "";
    private int messageTimer = 0;

    public FirstRunSetupScreen() {
        super(Component.translatable("i_need_the_log.first_run.title"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.pathField = new EditBox(this.font, centerX - 150, centerY - 40, 300, 20,
                Component.translatable("i_need_the_log.first_run.path_hint"));
        this.pathField.setMaxLength(256);
        this.pathField.setValue(ModConfig.COMMON.exportPath.get());
        this.addRenderableWidget(this.pathField);

        Button saveButton = Button.builder(
                Component.translatable("i_need_the_log.first_run.save"),
                button -> {
                    String path = this.pathField.getValue().trim();
                    if (path.isEmpty()) {
                        message = Component.translatable("i_need_the_log.first_run.error_empty").getString();
                        messageTimer = 60;
                        return;
                    }
                    ModConfig.COMMON.exportPath.set(path);
                    ModConfig.COMMON.firstRun.set(false);
                    ModConfig.COMMON_SPEC.save();
                    Minecraft.getInstance().setScreen(null);
                })
                .bounds(centerX - 160, centerY + 20, 150, 20)
                .build();
        this.addRenderableWidget(saveButton);

        Button exitButton = Button.builder(
                Component.translatable("i_need_the_log.first_run.exit"),
                button -> Minecraft.getInstance().stop())
                .bounds(centerX + 10, centerY + 20, 150, 20)
                .build();
        this.addRenderableWidget(exitButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int centerX = this.width / 2;
        guiGraphics.drawCenteredString(this.font, Component.translatable("i_need_the_log.first_run.welcome"),
                centerX, this.height / 2 - 80, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, Component.translatable("i_need_the_log.first_run.instruction"),
                centerX, this.height / 2 - 60, 0xAAAAAA);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (messageTimer > 0) {
            guiGraphics.drawCenteredString(this.font, message, centerX, this.height / 2 + 60, 0xFF5555);
            messageTimer--;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
