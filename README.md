# 🥔 PotatoFix - Fabric Mod 1.21.10 Tối Ưu Cho Máy Yếu

Mod tối ưu hóa toàn diện hiệu năng Minecraft Java phiên bản **1.21.10**, thiết kế chuyên biệt cho laptop văn phòng, PC đời cũ, card màn hình onboard (Intel HD Graphics).

## 🚀 Hướng Dẫn Tự Biên Dịch (Build .JAR)
Chỉ cần mở Terminal / Command Prompt tại thư mục dự án và chạy:

```bash
# Trên Windows:
gradlew.bat build

# Trên Linux / macOS:
./gradlew build
```

File JAR xuất ra sẽ nằm tại: `build/libs/potatofix-1.0.0.jar`. Copy file này vào thư mục `.minecraft/mods/`.

## ⚡ Cờ JVM (Java Arguments) Khuyên Dùng Cho Máy 2GB - 4GB RAM
Dán đoạn này vào mục **JVM Arguments** trong Launcher (Prism, Modrinth, CurseForge hoặc Vanilla):

```bash
-Xms1536M -Xmx2048M -XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=20 -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -XX:+AlwaysPreTouch -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=40 -XX:G1ReservePercent=10 -XX:G1HeapWastePercent=5 -XX:G1MixedGCCountTarget=4 -XX:InitiatingHeapOccupancyPercent=15 -XX:G1MixedGCLiveThresholdPercent=90 -XX:G1RSetUpdatingPauseTimePercent=5 -XX:SurvivorRatio=32
```

## 🛠️ Combo Mod Khuyên Dùng Cho Bản 1.21.10:
1. **PotatoFix 1.21.10** (Entity Culling + Particle Limiter + Memory Fix)
2. **Sodium** (Render Chunks siêu nhanh qua modern OpenGL)
3. **Lithium** (Tối ưu hóa logic Server & Entity Tick)
4. **FerriteCore** (Nén bộ nhớ RAM)
