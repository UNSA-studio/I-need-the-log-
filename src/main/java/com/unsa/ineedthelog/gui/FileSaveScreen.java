package com.unsa.ineedthelog.gui;

import com.unsa.ineedthelog.config.ModConfig;
import com.unsa.ineedthelog.util.LogExporter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSaveScreen extends Screen {
    private final Screen parent;
    private EditBox pathField;
    private String feedbackMessage = null;
    private int feedbackTimer = 0;

    public FileSaveScreen(Screen parent) {
        super(Component.translatable("i_need_the_log.save_screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        String defaultPath = ModConfig.COMMON.exportPath.get();
        this.pathField = new EditBox(this.font, this.width / 2 - 100, this.height / 2 - 20, 200, 20,
                Component.translatable("i_need_the_log.save_screen.path_hint"));
        this.pathField.setMaxLength(256);
        this.pathField.setValue(defaultPath);
        this.addRenderableWidget(this.pathField);

        Button saveButton = Button.builder(
                Component.translatable("i_need_the_log.save_screen.save"),
                button -> {
                    String userPath = this.pathField.getValue().trim();
                    if (userPath.isEmpty()) {
                        feedbackMessage = Component.translatable("i_need_the_log.first_run.error_empty").getString();
                        feedbackTimer = 80;
                        return;
                    }

                    String finalPath = userPath;
                    if (!userPath.endsWith(".log") && !userPath.endsWith(".txt")) {
                        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd_HH:mm"));
                        String fileName = timestamp + "-ERROR-LOG.txt";
                        Path dir = Paths.get(userPath);
                        if (!userPath.endsWith("/") && !userPath.endsWith("\\")) {
                            dir = dir.resolve(fileName);
                        } else {
                            dir = dir.resolve(fileName);
                        }
                        finalPath = dir.toString();
                    }

                    int errorCode = LogExporter.exportLogToFile(finalPath);
                    if (errorCode == 0) {
                        feedbackMessage = Component.translatable("i_need_the_log.save_screen.success", finalPath).getString();
                    } else {
                        // 错误代码映射
                        String errorKey = "i_need_the_log.save_screen.error." + errorCode;
                        feedbackMessage = Component.translatable(errorKey, errorCode).getString();
                        // 如果翻译键不存在，回退到通用错误
                        if (feedbackMessage.equals(errorKey)) {
                            feedbackMessage = Component.translatable("i_need_the_log.save_screen.error.generic", errorCode).getString();
                        }
                    }
                    feedbackTimer = 80;

                    ModConfig.COMMON.exportPath.set(userPath);
                    ModConfig.COMMON_SPEC.save();
                })
                .bounds(this.width / 2 - 50, this.height / 2 + 20, 100, 20)
                .build();
        this.addRenderableWidget(saveButton);

        Button backButton = Button.builder(
                Component.translatable("i_need_the_log.save_screen.back"),
                button -> this.onClose())
                .bounds(this.width / 2 - 50, this.height / 2 + 50, 100, 20)
                .build();
        this.addRenderableWidget(backButton);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, Component.translatable("i_need_the_log.save_screen.instruction"),
                this.width / 2, this.height / 2 - 60, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.pathField.render(guiGraphics, mouseX, mouseY, partialTick);

        if (feedbackMessage != null && feedbackTimer > 0) {
            int color = feedbackMessage.contains(Component.translatable("i_need_the_log.save_screen.success").getString().substring(0, 4)) ? 0x00FF00 : 0xFF5555;
            guiGraphics.drawCenteredString(this.font, feedbackMessage,
                    this.width / 2, this.height / 2 + 80, color);
            feedbackTimer--;
            if (feedbackTimer <= 0) feedbackMessage = null;
        }
    }
}
