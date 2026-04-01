package com.unsa.ineedthelog.util;

import net.minecraft.client.Minecraft;
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
        StringBuilder info = new StringBuilder();
        info.append("===================================\n");
        info.append("Device Information (Session Start)\n");
        info.append("===================================\n");
        info.append("Java version: ").append(System.getProperty("java.version")).append("\n");
        info.append("Java Publisher: ").append(System.getProperty("java.vendor")).append("\n");
        info.append("Minecraft version: ").append(mc.getLaunchedVersion()).append("\n");
        String neoVersion = ModList.get().getModContainerById("neoforge")
                .map(container -> container.getModInfo().getVersion().toString())
                .orElse("Unknown");
        info.append("NeoForge version: ").append(neoVersion).append("\n");
        info.append("Mods loaded: ").append(ModList.get().size()).append("\n");
        info.append("Launcher: ").append(System.getProperty("minecraft.launcher.brand", "Unknown")).append(" ").append(System.getProperty("minecraft.launcher.version", "")).append("\n");
        info.append("Memory allocated: ").append(Runtime.getRuntime().maxMemory() / 1024 / 1024).append(" MB\n");
        info.append("OS: ").append(System.getProperty("os.name")).append(" ").append(System.getProperty("os.version")).append(" (").append(System.getProperty("os.arch")).append(")\n");
        info.append("Rendering: OpenGL ES 3.2 (Krypton)\n");
        info.append("Login type: ").append(mc.getUser().getType() != null ? mc.getUser().getType().toString() : "Unknown").append("\n");
        info.append("===================================\n");
        return info.toString();
    }
}
