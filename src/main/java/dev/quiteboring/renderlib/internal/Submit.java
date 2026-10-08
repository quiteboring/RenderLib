package dev.quiteboring.renderlib.internal;

import dev.quiteboring.renderlib.mixins.GuiGraphicsAccessor;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;

/** Direct submit path for elements that bypass bloom filtering (text). */
public final class Submit {
    private Submit() {
    }

    public static void element(GuiGraphicsExtractor extractor, GuiElementRenderState element) {
        ((GuiGraphicsAccessor) extractor).renderlib$guiRenderState().addGuiElement(element);
    }
}
