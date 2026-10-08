package dev.quiteboring.renderlib;

import dev.quiteboring.renderlib.internal.state.AsymmetricRoundedRectRenderState;
import dev.quiteboring.renderlib.internal.backend.BloomFilter;
import dev.quiteboring.renderlib.internal.backend.Blur;
import dev.quiteboring.renderlib.internal.state.FlatRectRenderState;
import dev.quiteboring.renderlib.internal.state.GradientRectRenderState;
import dev.quiteboring.renderlib.internal.state.GradientTextRenderState;
import dev.quiteboring.renderlib.internal.state.ImageRenderState;
import dev.quiteboring.renderlib.internal.backend.Pipelines;
import dev.quiteboring.renderlib.internal.state.RoundedHeadRenderState;
import dev.quiteboring.renderlib.internal.state.RoundedOutlineRectRenderState;
import dev.quiteboring.renderlib.internal.state.RoundedRectRenderState;
import dev.quiteboring.renderlib.internal.Submit;
import dev.quiteboring.renderlib.internal.state.TextRenderState;
import dev.quiteboring.renderlib.internal.font.GlyphQuad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Static 2D drawing API. Every method takes the current
 * {@link GuiGraphicsExtractor} (e.g. from {@code HudRenderEvent#extractor()}).
 * Colors are packed {@code 0xAARRGGBB} ints (see {@link Colors}).
 */
public final class Render2D {
    private Render2D() {
    }

    // shapes

    public static void roundedRect(GuiGraphicsExtractor extractor,
                                   float x, float y, float width, float height,
                                   float radius, int color) {
        roundedRect(extractor, x, y, width, height, radius, color, null);
    }

    public static void roundedRect(GuiGraphicsExtractor extractor,
                                   float x, float y, float width, float height,
                                   float radius, int color, @Nullable ScreenRectangle scissorArea) {
        if (width <= 0.0f || height <= 0.0f || !Pipelines.ready()) {
            return;
        }
        BloomFilter.submit(extractor,
                RoundedRectRenderState.of(extractor.pose(), x, y, x + width, y + height, radius, color, scissorArea));
    }

    public static void sharpRect(GuiGraphicsExtractor extractor,
                                 float x0, float y0, float x1, float y1, int color) {
        sharpRect(extractor, x0, y0, x1, y1, color, null);
    }

    public static void sharpRect(GuiGraphicsExtractor extractor,
                                 float x0, float y0, float x1, float y1, int color,
                                 @Nullable ScreenRectangle scissorArea) {
        if (x1 - x0 <= 0.0f || y1 - y0 <= 0.0f || !Pipelines.ready()) {
            return;
        }
        BloomFilter.submit(extractor,
                RoundedRectRenderState.of(extractor.pose(), x0, y0, x1, y1, 0.01f, color, scissorArea));
    }

    public static void sharpRectGradient(GuiGraphicsExtractor extractor,
                                         float x, float y, float width, float height,
                                         int colorLeft, int colorRight, @Nullable ScreenRectangle scissorArea) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        BloomFilter.submit(extractor,
                RoundedRectRenderState.of(extractor.pose(), x, y, x + width, y + height,
                        0.01f, colorLeft, colorRight, scissorArea));
    }

    public static void roundedRectGradient(GuiGraphicsExtractor extractor,
                                           float x, float y, float width, float height,
                                           float radius, int colorA, int colorB, boolean vertical,
                                           @Nullable ScreenRectangle scissorArea) {
        if (width <= 0.0f || height <= 0.0f || !Pipelines.ready()) {
            return;
        }
        BloomFilter.submit(extractor,
                vertical
                        ? RoundedRectRenderState.ofVertical(extractor.pose(),
                                x, y, x + width, y + height, radius, colorA, colorB, scissorArea)
                        : RoundedRectRenderState.of(extractor.pose(),
                                x, y, x + width, y + height, radius, colorA, colorB, scissorArea));
    }

    public static void roundedOutline(GuiGraphicsExtractor extractor,
                                      float x, float y, float width, float height,
                                      float radius, float thickness, int color,
                                      @Nullable ScreenRectangle scissorArea) {
        if (width <= 0.0f || height <= 0.0f || !Pipelines.ready()) {
            return;
        }
        BloomFilter.submit(extractor,
                RoundedOutlineRectRenderState.of(extractor.pose(),
                        x, y, x + width, y + height, radius, thickness, color, scissorArea));
    }

    public static void roundedHead(GuiGraphicsExtractor extractor, Identifier skin,
                                   float x, float y, float size, float radius, int color,
                                   @Nullable ScreenRectangle scissorArea) {
        if (size <= 0.0f || !Pipelines.ready()) {
            return;
        }
        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(skin);
        if (texture == null) {
            return;
        }
        BloomFilter.submit(extractor,
                RoundedHeadRenderState.of(extractor.pose(), x, y, x + size, y + size,
                        radius, color, texture.getTextureView(), texture.getSampler(), scissorArea));
    }

    public static void dropShadow(GuiGraphicsExtractor extractor, int steps,
                                  float x, float y, float width, float height,
                                  double spread, float radius, @Nullable ScreenRectangle scissorArea) {
        for (float f = 0.0f; f <= steps / 2.0f; f += 0.5f) {
            int alpha = (int) Math.max(0.5, (spread - f * 1.2) / 5.5);
            if (alpha <= 0) {
                continue;
            }
            roundedRect(extractor, x - f / 2.0f, y - f / 2.0f, width + f, height + f,
                    radius, (Math.min(alpha, 255) << 24), scissorArea);
        }
    }

    public static void roundedRectAsym(GuiGraphicsExtractor extractor,
                                       float x, float y, float width, float height,
                                       float radius, boolean roundBottom, int color,
                                       @Nullable ScreenRectangle scissorArea) {
        if (width <= 0.0f || height <= 0.0f || !Pipelines.ready()) {
            return;
        }
        BloomFilter.submit(extractor,
                AsymmetricRoundedRectRenderState.of(extractor.pose(),
                        x, y, x + width, y + height, radius, roundBottom, color, scissorArea));
    }

    public static void roundedRectAsymGradient(GuiGraphicsExtractor extractor,
                                               float x, float y, float width, float height,
                                               float radius, boolean roundBottom, int colorLeft, int colorRight,
                                               @Nullable ScreenRectangle scissorArea) {
        if (width <= 0.0f || height <= 0.0f || !Pipelines.ready()) {
            return;
        }
        BloomFilter.submit(extractor,
                AsymmetricRoundedRectRenderState.of(extractor.pose(),
                        x, y, x + width, y + height, radius, roundBottom, colorLeft, colorRight, scissorArea));
    }

    public static void flatRect(GuiGraphicsExtractor extractor,
                                float x, float y, float width, float height, int color) {
        flatRect(extractor, x, y, width, height, color, null);
    }

    public static void flatRect(GuiGraphicsExtractor extractor,
                                float x, float y, float width, float height, int color,
                                @Nullable ScreenRectangle scissorArea) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        int x0 = Math.round(x);
        int y0 = Math.round(y);
        int x1 = x0 + Math.max(1, Math.round(width));
        int y1 = y0 + Math.max(1, Math.round(height));
        BloomFilter.submit(extractor,
                FlatRectRenderState.of(extractor.pose(), x0, y0, x1, y1, color, scissorArea));
    }

    public static void flatRectGradient(GuiGraphicsExtractor extractor,
                                        float x, float y, float width, float height,
                                        int colorLeft, int colorRight, @Nullable ScreenRectangle scissorArea) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        int x0 = Math.round(x);
        int y0 = Math.round(y);
        int x1 = x0 + Math.max(1, Math.round(width));
        int y1 = y0 + Math.max(1, Math.round(height));
        BloomFilter.submit(extractor,
                GradientRectRenderState.of(extractor.pose(), x0, y0, x1, y1, colorLeft, colorRight, scissorArea));
    }

    // images

    public static void image(GuiGraphicsExtractor extractor, RenderImage image,
                             float x, float y, float width, float height) {
        image(extractor, image, x, y, width, height, 0xFFFFFFFF);
    }

    public static void image(GuiGraphicsExtractor extractor, RenderImage image,
                             float x, float y, float width, float height, int color) {
        image(extractor, image, x, y, width, height, color, color);
    }

    public static void image(GuiGraphicsExtractor extractor, RenderImage image,
                             float x, float y, float width, float height, int colorLeft, int colorRight) {
        image(extractor, image, x, y, width, height, colorLeft, colorRight, null);
    }

    public static void image(GuiGraphicsExtractor extractor, RenderImage image,
                             float x, float y, float width, float height,
                             int colorLeft, int colorRight, @Nullable ScreenRectangle scissorArea) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        RenderImage.Entry entry = image.entryFor(width, height);
        BloomFilter.submit(extractor,
                ImageRenderState.of(extractor.pose(), x, y, x + width, y + height,
                        colorLeft, colorRight, entry.view(), entry.sampler(), scissorArea));
    }

    public static void image(GuiGraphicsExtractor extractor, RenderImage image,
                             float x, float y, float width, float height,
                             @Nullable ScreenRectangle scissorArea) {
        image(extractor, image, x, y, width, height, 0xFFFFFFFF, 0xFFFFFFFF, scissorArea);
    }

    // text (returns drawn width)

    public static float text(GuiGraphicsExtractor extractor, RenderFont font,
                             CharSequence text, float x, float y, float size, int color) {
        return text(extractor, font, text, x, y, size, color, null);
    }

    public static float text(GuiGraphicsExtractor extractor, RenderFont font,
                             CharSequence text, float x, float y, float size, int color,
                             @Nullable ScreenRectangle scissorArea) {
        if (text.length() == 0 || !Pipelines.ready()) {
            return 0.0f;
        }
        GlyphQuad[] glyphs = font.layout(text, x, y, size);
        if (glyphs.length > 0) {
            Submit.element(extractor,
                    TextRenderState.of(extractor.pose(), glyphs, color,
                            font.textureView(size), font.sampler(size), scissorArea));
        }
        return font.stringWidth(text, size);
    }

    public static float textWithShadow(GuiGraphicsExtractor extractor, RenderFont font,
                                       CharSequence text, float x, float y, float size, int color) {
        return textWithShadow(extractor, font, text, x, y, size, color, shadowOffset(size), null);
    }

    public static float textWithShadow(GuiGraphicsExtractor extractor, RenderFont font,
                                       CharSequence text, float x, float y, float size,
                                       int color, float offset,
                                       @Nullable ScreenRectangle scissorArea) {
        if (text.length() == 0 || !Pipelines.ready()) {
            return 0.0f;
        }
        int shadow = ((color & 0xFCFCFC) >> 2) | (color & 0xFF000000);
        text(extractor, font, text, x + offset, y + offset, size, shadow, scissorArea);
        return text(extractor, font, text, x, y, size, color, scissorArea);
    }

    public static float gradientText(GuiGraphicsExtractor extractor, RenderFont font,
                                     CharSequence text, float x, float y, float size,
                                     int colorLeft, int colorRight) {
        return gradientText(extractor, font, text, x, y, size, colorLeft, colorRight, null);
    }

    public static float gradientText(GuiGraphicsExtractor extractor, RenderFont font,
                                     CharSequence text, float x, float y, float size,
                                     int colorLeft, int colorRight, @Nullable ScreenRectangle scissorArea) {
        if (text.length() == 0 || !Pipelines.ready()) {
            return 0.0f;
        }
        GlyphQuad[] glyphs = font.layout(text, x, y, size);
        if (glyphs.length > 0) {
            Submit.element(extractor,
                    GradientTextRenderState.of(extractor.pose(), glyphs, colorLeft, colorRight,
                            font.textureView(size), font.sampler(size), scissorArea));
        }
        return font.stringWidth(text, size);
    }

    // blur glass / bloom (see RenderConfig for toggles)

    public static void glass(GuiGraphicsExtractor extractor, BlurLayer layer,
                             float x, float y, float width, float height, float cornerRadius,
                             int tintArgb, float alpha, @Nullable ScreenRectangle scissorArea) {
        Blur.drawGlass(extractor, layer, x, y, width, height, cornerRadius, tintArgb, alpha, scissorArea);
    }

    public static void glassGradient(GuiGraphicsExtractor extractor, BlurLayer layer,
                                     float x, float y, float width, float height,
                                     float cornerRadius, int topArgb, int bottomArgb,
                                     boolean vertical, float alpha,
                                     @Nullable ScreenRectangle scissorArea) {
        Blur.drawGlassGradient(extractor, layer, x, y, width, height,
                cornerRadius, topArgb, bottomArgb, vertical, alpha, scissorArea);
    }

    public static void glassFlat(GuiGraphicsExtractor extractor, BlurLayer layer,
                                 float x, float y, float width, float height,
                                 int tintArgb, float alpha, @Nullable ScreenRectangle scissorArea) {
        Blur.drawGlassFlat(extractor, layer, x, y, width, height, tintArgb, alpha, scissorArea);
    }

    public static void blurredRound(GuiGraphicsExtractor extractor, BlurLayer layer,
                                    float x, float y, float width, float height, float cornerRadius) {
        Blur.drawBlurredRound(extractor, layer, x, y, width, height, cornerRadius);
    }

    public static void blurredBoxes(GuiGraphicsExtractor extractor, BlurLayer layer,
                                    List<float[]> boxes, float cornerRadius, float alpha) {
        Blur.drawBlurredRects(extractor, layer, boxes, cornerRadius, alpha);
    }

    private static float shadowOffset(float size) {
        return Math.max(0.5f, size * 0.125f);
    }
}
