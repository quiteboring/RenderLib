package dev.quiteboring.renderlib.internal.projection;

import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.jetbrains.annotations.Nullable;

public final class CameraState {
    @Nullable
    private static CameraRenderState current;

    private CameraState() {
    }

    public static void set(CameraRenderState state) {
        current = state;
    }

    @Nullable
    public static CameraRenderState get() {
        return current;
    }
}
