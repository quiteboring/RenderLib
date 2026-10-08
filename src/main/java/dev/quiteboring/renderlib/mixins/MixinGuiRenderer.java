package dev.quiteboring.renderlib.mixins;

import dev.quiteboring.renderlib.RenderConfig;
import dev.quiteboring.renderlib.RenderFont;
import dev.quiteboring.renderlib.RenderImage;
import dev.quiteboring.renderlib.internal.backend.BloomElement;
import dev.quiteboring.renderlib.internal.backend.BloomFilter;
import dev.quiteboring.renderlib.internal.backend.Blur;
//? if >=26.3 {
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
//?} else {
/*import com.mojang.blaze3d.buffers.GpuBufferSlice;
*///?}
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(GuiRenderer.class)
public abstract class MixinGuiRenderer {
    @Unique
    private int renderlib$bloomStart = -1;
    @Unique
    private int renderlib$bloomEnd = -1;

    @Inject(method = "prepare()V", at = @At("TAIL"))
    private void renderlib$prepareBloomDraws(CallbackInfo ci) {
        renderlib$bloomStart = -1;
        renderlib$bloomEnd = -1;
        if (!RenderConfig.isBloom() || !Blur.shouldUpdateGlow()) {
            return;
        }

        GuiRendererAccessor self = (GuiRendererAccessor) this;

        self.renderlib$setPreviousScissorArea(null);
        self.renderlib$setPreviousPipeline(null);
        self.renderlib$setPreviousTextureSetup(null);
        self.renderlib$setPreviousDraw(null);

        int start = self.renderlib$draws().size();
        self.renderlib$renderState().forEachElement(element -> {
            if (element instanceof BloomElement && !BloomFilter.isSuppressed(element)) {
                self.renderlib$addElementToMesh(element);
            }
        }, GuiRenderState.TraverseRange.ALL);
        int end = self.renderlib$draws().size();

        if (end > start) {
            renderlib$bloomStart = start;
            renderlib$bloomEnd = end;
        }
    }

    @Redirect(method = "draw()V", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    private int renderlib$hideBloomDrawsFromMainPass(List<?> instance) {
        int size = instance.size();
        if (renderlib$bloomStart < 0 || instance != ((GuiRendererAccessor) this).renderlib$draws()) {
            return size;
        }
        return Math.min(size, renderlib$bloomStart);
    }

    @Inject(method = "draw()V", at = @At("HEAD"))
    private void renderlib$captureBlur(CallbackInfo ci) {
        Blur.captureNow();
    }

    @Inject(method = "draw()V", at = @At("RETURN"))
    private void renderlib$renderBloom(CallbackInfo ci) {
        try {
            if (renderlib$bloomStart < 0 || !RenderConfig.isBloom()) {
                return;
            }
            renderlib$doRenderBloom();
        } finally {
            Blur.endFrame();
            RenderFont.endFrame();
            RenderImage.endFrame();
        }
    }

    @Unique
    private void renderlib$doRenderBloom() {
        TextureTarget glow = Blur.glowTarget();
        if (glow == null) {
            return;
        }

        RenderSystem.getDevice().createCommandEncoder()
                .clearColorTexture(glow.getColorTexture(), new Vector4f(0.0f, 0.0f, 0.0f, 0.0f));

        GpuBufferSlice transform = RenderSystem.getDynamicUniforms()
                .writeTransform(new Matrix4f().setTranslation(0.0f, 0.0f, -11000.0f));

        ((GuiRendererAccessor) this).renderlib$executeDrawRange(
                (Supplier<String>) () -> "renderlib/bloom", (RenderTarget) glow, transform,
                renderlib$bloomStart, renderlib$bloomEnd);

        Blur.blurGlow();
    }
}
