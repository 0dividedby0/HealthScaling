package com.dividedby0.healthscaling;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import com.dividedby0.healthscaling.config.JSON5ConfigManager;

public class SimpleConfigScreen extends Screen {
    private final Screen previousScreen;
    private final JSON5ConfigManager configManager;
    private EditBox xpCsvInput;

    public SimpleConfigScreen(Screen previousScreen, JSON5ConfigManager configManager) {
        super(Component.literal("Health Scaling Configuration"));
        this.previousScreen = previousScreen;
        this.configManager = configManager;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int labelX = centerX - 190;
        int inputX = centerX + 10;
        int y = 80;

        // XP heart requirements as a single CSV field
        StringBuilder csv = new StringBuilder();
        for (int i = 1; i <= 9; i++) {
            if (i > 1) csv.append(",");
            csv.append(configManager.getInt("xpThreshold_" + i, 10 * i));
        }
        this.xpCsvInput = new EditBox(this.font, inputX, y, 300, 20,
                Component.literal("XP Heart Thresholds (CSV)"));
        this.xpCsvInput.setValue(csv.toString());
        this.addRenderableWidget(this.xpCsvInput);

        // Save and Back buttons at bottom
        int buttonY = this.height - 40;
        this.addRenderableWidget(Button.builder(Component.literal("Save"), (btn) -> this.save())
                .bounds(centerX - 110, buttonY, 100, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Back"), (btn) -> this.onClose())
                .bounds(centerX + 10, buttonY, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        int labelX = this.width / 2 - 190;
        int y = 60;
        guiGraphics.drawString(this.font, "XP Heart Requirements (CSV, 9 values, 2-15 hearts):", labelX, y, 0xFFCC66);
        guiGraphics.drawString(this.font, "Defaults: 10,20,30,40,50,75,100,150,200", labelX, y + 14, 0x888888);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void save() {
        String[] xpVals = this.xpCsvInput.getValue().split(",");
        for (int i = 1; i <= 9; i++) {
            int value = 10 * i;
            if (xpVals.length >= i) {
                try {
                    value = Integer.parseInt(xpVals[i - 1].trim());
                } catch (NumberFormatException ignore) {
                }
            }
            configManager.setInt("xpThreshold_" + i, value);
        }
        configManager.saveConfig();
        this.onClose();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.previousScreen);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
