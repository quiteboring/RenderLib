package dev.quiteboring.renderlib.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Subscribe with {@link #onWorldRender} to draw world-space shapes every frame. */
public interface WorldRenderCallback {
    List<WorldRenderCallback> LISTENERS = new CopyOnWriteArrayList<>();

    void onRender(WorldRenderEvent event);

    static void onWorldRender(WorldRenderCallback listener) {
        LISTENERS.add(listener);
    }

    static void dispatch(WorldRenderEvent event) {
        for (WorldRenderCallback listener : LISTENERS) {
            listener.onRender(event);
        }
    }
}
