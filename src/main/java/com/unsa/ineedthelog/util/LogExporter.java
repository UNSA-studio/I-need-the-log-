package com.unsa.ineedthelog.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageManager;
import net.neoforged.fml.ModList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LogExporter {
    public static boolean exportLogToFile(String destination) {
        try {
            Path logFile = Minecraft.getInstance().gameDirectory.toPath().resolve("logs/latest.log");
            if (!Files.exists(logFile)) return false;

            String logContent = Files.readString(logFile);
            String deviceInfo = getDeviceInfo();
            String finalContent = deviceInfo + "\n" + logContent;

            Path targetPath = Paths.get(destination);
            if (!targetPath.isAbsolute()) {
                targetPath = Minecraft.getInstance().gameDirectory.toPath().resolve(destination);
            }
            Files.createDirectories(targetPath.getParent());
            Files.writeString(targetPath, finalContent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getDeviceInfo() {
        Minecraft mc = Minecraft.getInstance();
        LanguageManager lm = mc.getLanguageManager();
        boolean isChinese = lm.getSelected().getLanguage().startsWith("zh");
        StringBuilder info = new StringBuilder();
        info.append("===================================\n");
        info.append(isChinese ? "设备信息（本次会话）" : "Device Information (Session Start)").append("\n");
        info.append("===================================\n");
        info.append(isChinese ? "Java版本: " : "Java version: ").append(System.getProperty("java.version")).append("\n");
        info.append(isChinese ? "Java发行商: " : "Java Publisher: ").append(System.getProperty("java.vendor")).append("\n");
        info.append(isChinese ? "Minecraft版本: " : "Minecraft version: ").append(mc.getLaunchedVersion()).append("\n");
        String neoVersion = ModList.get().getModContainerById("neoforge")
                .map(container -> container.getModInfo().getVersion().toString())
                .orElse("Unknown");
        info.append(isChinese ? "NeoForge版本: " : "NeoForge version: ").append(neoVersion).append("\n");
        info.append(isChinese ? "加载的模组数量: " : "Mods loaded: ").append(ModList.get().size()).append("\n");
        info.append(isChinese ? "启动器: " : "Launcher: ").append(System.getProperty("minecraft.launcher.brand", "Unknown")).append(" ").append(System.getProperty("minecraft.launcher.version", "")).append("\n");
        info.append(isChinese ? "分配内存: " : "Memory allocated: ").append(Runtime.getRuntime().maxMemory() / 1024 / 1024).append(" MB\n");
        info.append(isChinese ? "操作系统: " : "OS: ").append(System.getProperty("os.name")).append(" ").append(System.getProperty("os.version")).append(" (").append(System.getProperty("os.arch")).append(")\n");
        info.append(isChinese ? "渲染方式: " : "Rendering: ").append("OpenGL ES 3.2 (Krypton)").append("\n");
        info.append(isChinese ? "登录方式: " : "Login type: ").append(mc.getUser().getType() != null ? mc.getUser().getType().toString() : "Unknown").append("\n");
        info.append("===================================\n");
        return info.toString();
    }
}
