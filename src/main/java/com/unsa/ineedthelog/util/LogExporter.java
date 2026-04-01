package com.unsa.ineedthelog.util;

import net.minecraft.client.Minecraft;
import net.neoforged.fml.ModList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LogExporter {
    /**
     * 导出日志到指定路径
     * @param destination 目标路径（文件或目录）
     * @return 错误代码：0表示成功，非0表示错误，详见错误代码表
     */
    public static int exportLogToFile(String destination) {
        try {
            Path logFile = Minecraft.getInstance().gameDirectory.toPath().resolve("logs/latest.log");
            if (!Files.exists(logFile)) return 1; // 001

            String logContent = Files.readString(logFile);
            String deviceInfo = getDeviceInfo();
            String finalContent = deviceInfo + "\n" + logContent;

            Path targetPath = Paths.get(destination);
            if (!targetPath.isAbsolute()) {
                targetPath = Minecraft.getInstance().gameDirectory.toPath().resolve(destination);
            }
            // 创建父目录
            Path parent = targetPath.getParent();
            if (parent != null && !Files.exists(parent)) {
                try {
                    Files.createDirectories(parent);
                } catch (IOException e) {
                    e.printStackTrace();
                    return 3; // 003
                }
            }
            Files.writeString(targetPath, finalContent);
            return 0; // 成功
        } catch (IOException e) {
            e.printStackTrace();
            // 判断读取还是写入错误
            if (e.getMessage() != null && e.getMessage().contains("latest.log")) {
                return 2; // 002
            }
            return 9; // 999
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
