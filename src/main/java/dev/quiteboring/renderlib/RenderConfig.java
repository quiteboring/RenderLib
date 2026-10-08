package dev.quiteboring.renderlib;

import java.util.EnumMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

/**
 * Global switches for RenderLib's post-processing (background blur + bloom glow).
 * Everything defaults to on so the first glass/blur call just works.
 */
public final class RenderConfig {
    private static volatile boolean blur = true;
    private static volatile boolean bloom = true;
    private static volatile int blurRadius = 7;
    private static volatile int bloomRadius = 7;
    private static volatile BlurStyle bloomStyle = BlurStyle.GAUSSIAN;

    private static final Map<BlurLayer, BlurStyle> LAYERS = new EnumMap<>(BlurLayer.class);

    static {
        for (BlurLayer layer : BlurLayer.VALUES) {
            LAYERS.put(layer, BlurStyle.GAUSSIAN);
        }
    }

    private RenderConfig() {
    }

    public static boolean isBlur() {
        return blur;
    }

    public static void setBlur(boolean blur) {
        RenderConfig.blur = blur;
    }

    public static boolean isBloom() {
        return blur && bloom;
    }

    public static void setBloom(boolean bloom) {
        RenderConfig.bloom = bloom;
    }

    public static int getBlurRadius() {
        return blurRadius;
    }

    public static void setBlurRadius(int radius) {
        RenderConfig.blurRadius = Math.max(1, Math.min(20, radius));
    }

    public static int getBloomRadius() {
        return bloomRadius;
    }

    public static void setBloomRadius(int radius) {
        RenderConfig.bloomRadius = Math.max(1, Math.min(20, radius));
    }

    public static BlurStyle getBloomStyle() {
        return bloomStyle;
    }

    public static void setBloomStyle(BlurStyle style) {
        RenderConfig.bloomStyle = style;
    }

    /** Blur style for a layer, or null to disable blur for it. */
    @Nullable
    public static BlurStyle getStyle(BlurLayer layer) {
        return LAYERS.get(layer);
    }

    public static void setStyle(BlurLayer layer, @Nullable BlurStyle style) {
        if (style == null) {
            LAYERS.remove(layer);
        } else {
            LAYERS.put(layer, style);
        }
    }
}
