package com.example.wetmod.gui;

import com.example.wetmod.WetModClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class WetMenuScreen extends Screen {
    private float animationProgress = 0.0f;
    private boolean closing = false;
    private float toggleAnim = WetModClient.wetFloorEnabled ? 1.0f : 0.0f;

    public WetMenuScreen() {
        super(Component.literal("Wet Floor Settings"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (closing) {
            animationProgress -= delta * 0.18f;
            if (animationProgress <= 0.0f) {
                this.minecraft.setScreen(null);
                return;
            }
        } else if (animationProgress < 1.0f) {
            animationProgress = Math.min(1.0f, animationProgress + delta * 0.18f);
        }

        float targetToggle = WetModClient.wetFloorEnabled ? 1.0f : 0.0f;
        toggleAnim += (targetToggle - toggleAnim) * 0.25f;

        int alpha = (int) (animationProgress * 190);
        graphics.fill(0, 0, this.width, this.height, (alpha << 24) | 0x080810);

        int panelWidth = 340;
        int panelHeight = 230;
        int panelX = (this.width - panelWidth) / 2;
        int panelY = (this.height - panelHeight) / 2;

        graphics.pose().pushMatrix();
        graphics.pose().translate(this.width / 2.0f, this.height / 2.0f);
        graphics.pose().scale(animationProgress, animationProgress);
        graphics.pose().translate(-this.width / 2.0f, -this.height / 2.0f);

        drawRoundedRect(graphics, panelX, panelY, panelWidth, panelHeight, 18, 0xEE14141E);
        drawRoundedRectOutline(graphics, panelX, panelY, panelWidth, panelHeight, 18, 1, 0x55FFFFFF);

        graphics.drawCenteredString(this.font, Component.literal("✦ WET FLOOR SHADERS ✦"),
                this.width / 2, panelY + 22, 0xFFFFFF);
        graphics.drawCenteredString(this.font, Component.literal("Realtime water puddles & reflection engine"),
                this.width / 2, panelY + 42, 0x8888A0);

        int toggleX = panelX + 30;
        int toggleY = panelY + 95;
        int toggleW = panelWidth - 60;
        int toggleH = 50;

        drawRoundedRect(graphics, toggleX, toggleY, toggleW, toggleH, 12, 0x33FFFFFF);
        graphics.drawString(this.font, Component.literal("Wetfloor Reflections"),
                toggleX + 16, toggleY + 18, 0xFFFFFF, true);

        int switchX = toggleX + toggleW - 70;
        int switchY = toggleY + 14;
        int switchW = 54;

        int trackColor = interpolateColor(0xFF444455, 0xFF2563EB, toggleAnim);
        drawRoundedRect(graphics, switchX, switchY, switchW, 22, 11, trackColor);

        float knobX = switchX + 3 + (toggleAnim * (switchW - 6 - 16));
        drawRoundedRect(graphics, (int) knobX, switchY + 3, 16, 16, 8, 0xFFFFFFFF);

        graphics.pose().popMatrix();
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return super.mouseClicked(event, doubleClick);
        }

        int panelWidth = 340;
        int panelHeight = 230;
        int panelX = (this.width - panelWidth) / 2;
        int panelY = (this.height - panelHeight) / 2;
        int toggleX = panelX + 30;
        int toggleY = panelY + 95;
        int toggleW = panelWidth - 60;
        int toggleH = 50;

        if (event.x() >= toggleX && event.x() <= toggleX + toggleW
                && event.y() >= toggleY && event.y() <= toggleY + toggleH) {
            WetModClient.wetFloorEnabled = !WetModClient.wetFloorEnabled;
            if (this.minecraft.player != null) {
                String state = WetModClient.wetFloorEnabled ? "ON" : "OFF";
                this.minecraft.player.displayClientMessage(
                        Component.literal("WetFloor Shaders: " + state), true
                );
            }
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == GLFW.GLFW_KEY_ESCAPE || event.key() == GLFW.GLFW_KEY_UP) {
            closing = true;
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public void onClose() {
        closing = true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    private void drawRoundedRect(GuiGraphics graphics, int x, int y, int width,
                                 int height, int radius, int color) {
        graphics.fill(x + radius, y, x + width - radius, y + height, color);
        graphics.fill(x, y + radius, x + radius, y + height - radius, color);
        graphics.fill(x + width - radius, y + radius, x + width, y + height - radius, color);
        graphics.fill(x + radius, y + radius, x + width - radius, y + height - radius, color);
        fillCorner(graphics, x + radius, y + radius, radius, color, 1);
        fillCorner(graphics, x + width - radius, y + radius, radius, color, 2);
        fillCorner(graphics, x + radius, y + height - radius, radius, color, 3);
        fillCorner(graphics, x + width - radius, y + height - radius, radius, color, 4);
    }

    private void fillCorner(GuiGraphics graphics, int centerX, int centerY,
                            int radius, int color, int quadrant) {
        for (int dy = 0; dy <= radius; dy++) {
            for (int dx = 0; dx <= radius; dx++) {
                if (dx * dx + dy * dy <= radius * radius) {
                    int px;
                    int py;
                    if (quadrant == 1) { px = centerX - dx; py = centerY - dy; }
                    else if (quadrant == 2) { px = centerX + dx; py = centerY - dy; }
                    else if (quadrant == 3) { px = centerX - dx; py = centerY + dy; }
                    else { px = centerX + dx; py = centerY + dy; }
                    graphics.fill(px, py, px + 1, py + 1, color);
                }
            }
        }
    }

    private void drawRoundedRectOutline(GuiGraphics graphics, int x, int y,
                                        int width, int height, int radius,
                                        int thickness, int color) {
        graphics.fill(x + radius, y, x + width - radius, y + thickness, color);
        graphics.fill(x + radius, y + height - thickness, x + width - radius, y + height, color);
        graphics.fill(x, y + radius, x + thickness, y + height - radius, color);
        graphics.fill(x + width - thickness, y + radius, x + width, y + height - radius, color);
    }

    private int interpolateColor(int color1, int color2, float fraction) {
        int a1 = (color1 >>> 24) & 0xFF, r1 = (color1 >>> 16) & 0xFF;
        int g1 = (color1 >>> 8) & 0xFF, b1 = color1 & 0xFF;
        int a2 = (color2 >>> 24) & 0xFF, r2 = (color2 >>> 16) & 0xFF;
        int g2 = (color2 >>> 8) & 0xFF, b2 = color2 & 0xFF;
        int a = (int) (a1 + (a2 - a1) * fraction);
        int r = (int) (r1 + (r2 - r1) * fraction);
        int g = (int) (g1 + (g2 - g1) * fraction);
        int b = (int) (b1 + (b2 - b1) * fraction);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
