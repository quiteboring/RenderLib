package dev.quiteboring.renderlib.event;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public record HudRenderEvent(GuiGraphicsExtractor extractor, float partialTick) {

    public int width() {
        return extractor.guiWidth();
    }

    public int height() {
        return extractor.guiHeight();
    }

}
