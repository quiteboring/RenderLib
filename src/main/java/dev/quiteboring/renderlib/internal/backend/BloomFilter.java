package dev.quiteboring.renderlib.internal.backend;

import dev.quiteboring.renderlib.BlurLayer;
import dev.quiteboring.renderlib.RenderConfig;

import dev.quiteboring.renderlib.mixins.GuiGraphicsAccessor;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public final class BloomFilter {
    private static final Set<GuiElementRenderState> SUPPRESSED =
            Collections.newSetFromMap(new IdentityHashMap<>());

    @Nullable
    private static BlurLayer current;

    private BloomFilter() {
    }

    public static void begin(BlurLayer consumer) {
        current = consumer;
    }

    public static void end() {
        current = null;
    }

    public static void submit(GuiGraphicsExtractor extractor, GuiElementRenderState element) {
        BlurLayer consumer = current;
        if (consumer != null && RenderConfig.getStyle(consumer) == null) {
            SUPPRESSED.add(element);
        }
        ((GuiGraphicsAccessor) extractor).renderlib$guiRenderState().addGuiElement(element);
    }

    public static boolean isSuppressed(GuiElementRenderState element) {
        return !SUPPRESSED.isEmpty() && SUPPRESSED.contains(element);
    }

    public static void endFrame() {
        if (!SUPPRESSED.isEmpty()) {
            SUPPRESSED.clear();
        }
    }
}
