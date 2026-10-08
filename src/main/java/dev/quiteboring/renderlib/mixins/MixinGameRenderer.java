package dev.quiteboring.renderlib.mixins;

import dev.quiteboring.renderlib.internal.projection.CameraState;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=26.3 {
@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void renderlib$captureCameraRenderState(CallbackInfo ci) {
        CameraState.set(((GameRenderer) (Object) this).gameRenderState().levelRenderState.cameraRenderState);
    }
}
//?} else {
/*@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @org.spongepowered.asm.mixin.Shadow
    @org.spongepowered.asm.mixin.Final
    private net.minecraft.client.renderer.state.GameRenderState gameRenderState;

    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void renderlib$captureCameraRenderState(net.minecraft.client.DeltaTracker deltaTracker, CallbackInfo ci) {
        CameraState.set(gameRenderState.levelRenderState.cameraRenderState);
    }
}
*///?}
