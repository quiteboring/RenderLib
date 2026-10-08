package dev.quiteboring.renderlib;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
//? if >=26.3 {
import com.mojang.renderpearl.api.textures.FilterMode;
//?} else {
/*import com.mojang.blaze3d.textures.FilterMode;
*///?}
//? if >=26.3 {
import com.mojang.renderpearl.api.textures.GpuSampler;
//?} else {
/*import com.mojang.blaze3d.textures.GpuSampler;
*///?}
//? if >=26.3 {
import com.mojang.renderpearl.api.textures.GpuTextureView;
//?} else {
/*import com.mojang.blaze3d.textures.GpuTextureView;
*///?}
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class RenderImage {
    private final BufferedImage source;
    private final Map<Long, Entry> scaled = new HashMap<>();

    private RenderImage(BufferedImage source) {
        this.source = source;
    }

    public static RenderImage fromImage(BufferedImage source) {
        return new RenderImage(source);
    }

    public static RenderImage fromResource(String fileName) {
        return fromModAssets("renderlib", fileName);
    }

    /** Loads {@code assets/<namespace>/textures/<fileName>} from any mod on the classpath. */
    public static RenderImage fromModAssets(String namespace, String fileName) {
        String path = "/assets/" + namespace + "/textures/" + fileName;
        try (InputStream in = RenderImage.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("Missing image resource: " + path);
            }
            return new RenderImage(ImageIO.read(in));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load image resource: " + path, e);
        }
    }

    static float pixelRatio() {
        return Math.max(1, Minecraft.getInstance().getWindow().getGuiScale());
    }

    public Entry entryFor(float guiWidth, float guiHeight) {
        float ratio = pixelRatio();
        int w = Math.max(1, Math.round(guiWidth * ratio));
        int h = Math.max(1, Math.round(guiHeight * ratio));
        return scaled.computeIfAbsent(((long) w << 32) | (h & 0xFFFFFFFFL), key -> build(w, h));
    }

    private Entry build(int width, int height) {
        BufferedImage resized = resize(source, width, height);

        NativeImage image = new NativeImage(width, height, false);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setPixel(x, y, resized.getRGB(x, y));
            }
        }

        DynamicTexture texture = new DynamicTexture(() -> "renderlib_image_" + width + "x" + height, image);
        return new Entry(texture.getTextureView(),
                RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR), texture);
    }

    private static BufferedImage resize(BufferedImage src, int width, int height) {
        BufferedImage current = toPremultiplied(src);

        int w = current.getWidth();
        int h = current.getHeight();
        while (w / 2 >= width && h / 2 >= height && (w > width || h > height)) {
            w = Math.max(width, w / 2);
            h = Math.max(height, h / 2);
            current = draw(current, w, h);
        }
        if (w != width || h != height) {
            current = draw(current, width, height);
        }
        return current;
    }

    private static BufferedImage toPremultiplied(BufferedImage src) {
        BufferedImage out = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g = out.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return out;
    }

    private static BufferedImage draw(BufferedImage src, int width, int height) {
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(src, 0, 0, width, height, null);
        g.dispose();
        return out;
    }

    public record Entry(GpuTextureView view, GpuSampler sampler, DynamicTexture texture) {
    }

    public void close() {
        if (scaled.isEmpty()) {
            return;
        }
        RETIRED.addAll(scaled.values());
        scaled.clear();
    }

    public static void endFrame() {
        if (RETIRED.isEmpty()) {
            return;
        }
        for (Entry entry : RETIRED) {
            entry.texture().close();
        }
        RETIRED.clear();
    }

    private static final java.util.List<Entry> RETIRED = new java.util.ArrayList<>();
}
