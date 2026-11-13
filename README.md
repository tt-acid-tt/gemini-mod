# WetMod — Minecraft 1.21.11 / Fabric

This is a 1.21.11 port of the supplied menu/keybind source. It uses Fabric Loom 1.14.10, official Mojang mappings, Fabric Loader 0.18.1, Fabric API 0.141.1+1.21.11, and Java 21.

## Controls

- Press **Up Arrow** to open the settings screen.
- Click the toggle to change the stored `wetFloorEnabled` flag.
- Press **Escape** or **Up Arrow** to close the screen.

## Build

Run `gradle build` with Gradle 9.2.1 and Java 21. The remapped mod JAR is written to `build/libs/`.

The supplied `wet_blur.fsh` is preserved as a resource. The supplied source did not include a registered Minecraft render pipeline/post-effect that loads this shader, so the toggle currently changes the flag and UI only; it does not create live world reflections. That rendering integration still needs to be implemented separately.
