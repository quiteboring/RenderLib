package dev.quiteboring.renderlib.mixins;

import dev.quiteboring.renderlib.internal.backend.BlurChains;
import dev.quiteboring.renderlib.internal.backend.Pipelines;
import net.minecraft.client.renderer.ShaderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderManager.class)
public abstract class MixinShaders {
    //? if >=26.3 {
    @Inject(method = "reload", at = @At("HEAD"))
    private void renderlib$invalidateBlurChains(CallbackInfo ci) {
        BlurChains.invalidate();
    }

    @Inject(method = "apply(Lcom/mojang/renderpearl/api/device/GpuDevice;Lnet/minecraft/client/renderer/ShaderManager$PendingResults;)V",
            at = @At("TAIL"))
    private void renderlib$markShadersReady(CallbackInfo ci) {
        Pipelines.markReady();
    }
    //?} else {
    /*@Inject(method = "apply(Lnet/minecraft/client/renderer/ShaderManager$Configs;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("HEAD"))
    private void renderlib$invalidateBlurChains(CallbackInfo ci) {
        BlurChains.invalidate();
    }

    @Inject(method = "apply(Lnet/minecraft/client/renderer/ShaderManager$Configs;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("TAIL"))
    private void renderlib$markShadersReady(CallbackInfo ci) {
        Pipelines.markReady();
    }
    *///?}
}
