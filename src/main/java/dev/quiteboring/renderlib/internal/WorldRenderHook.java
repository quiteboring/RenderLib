package dev.quiteboring.renderlib.internal;

import dev.quiteboring.renderlib.event.WorldRenderCallback;
import dev.quiteboring.renderlib.event.WorldRenderEvent;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.Gizmos.TemporaryCollection;
import net.minecraft.gizmos.SimpleGizmoCollector;
import net.minecraft.gizmos.SimpleGizmoCollector.GizmoInstance;

/**
 * Opens a gizmo collection window, fires world-render listeners, and hands
 * the drained shapes to the level renderer for this frame. Called from the
 * per-version {@code MixinLevelRenderer} at the head of level render.
 */
public final class WorldRenderHook {
    private WorldRenderHook() {
    }

    public static void dispatch() {
        if (WorldRenderCallback.LISTENERS.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.levelRenderer == null) {
            return;
        }
        SimpleGizmoCollector collector = new SimpleGizmoCollector();
        try (TemporaryCollection ignored = Gizmos.withCollector(collector)) {
            WorldRenderCallback.dispatch(new WorldRenderEvent());
        }
        List<GizmoInstance> gizmos = collector.drainGizmos();
        if (!gizmos.isEmpty()) {
            mc.levelRenderer.addMainThreadGizmos(gizmos);
        }
    }
}
