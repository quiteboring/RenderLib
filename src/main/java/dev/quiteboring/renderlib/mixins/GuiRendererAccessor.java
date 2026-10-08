package dev.quiteboring.renderlib.mixins;

//? if >=26.3 {
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
//?} else {
/*import com.mojang.blaze3d.buffers.GpuBufferSlice;
*///?}
//? if >=26.3 {
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
//?} else {
/*import com.mojang.blaze3d.pipeline.RenderPipeline;
*///?}
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.function.Supplier;

@Mixin(GuiRenderer.class)
public interface GuiRendererAccessor {
    @Accessor("draws")
    List<?> renderlib$draws();

    @Accessor("renderState")
    GuiRenderState renderlib$renderState();

    @Invoker("addElementToMesh")
    void renderlib$addElementToMesh(GuiElementRenderState element);

    @Accessor("previousScissorArea")
    void renderlib$setPreviousScissorArea(ScreenRectangle value);

    @Accessor("previousPipeline")
    void renderlib$setPreviousPipeline(RenderPipeline value);

    @Accessor("previousTextureSetup")
    void renderlib$setPreviousTextureSetup(TextureSetup value);

    @Accessor("previousDraw")
    void renderlib$setPreviousDraw(StagedVertexBuffer.Draw value);

    @Invoker("executeDrawRange")
    void renderlib$executeDrawRange(Supplier<String> name, RenderTarget target, GpuBufferSlice transform, int from, int to);
}
