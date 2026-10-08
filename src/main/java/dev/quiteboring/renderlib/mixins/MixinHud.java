package dev.quiteboring.renderlib.mixins;

import dev.quiteboring.renderlib.event.HudRenderCallback;
import dev.quiteboring.renderlib.event.HudRenderEvent;
import dev.quiteboring.renderlib.internal.backend.Blur;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class MixinHud {
    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void renderlib$compositeBloom(GuiGraphicsExtractor extractor, DeltaTracker deltaTracker, CallbackInfo ci) {
        Blur.compositeBloom(extractor);
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void renderlib$dispatchHudRender(GuiGraphicsExtractor extractor, DeltaTracker deltaTracker, CallbackInfo ci) {
        HudRenderCallback.dispatch(new HudRenderEvent(extractor, deltaTracker.getGameTimeDeltaPartialTick(false)));
    }
}
