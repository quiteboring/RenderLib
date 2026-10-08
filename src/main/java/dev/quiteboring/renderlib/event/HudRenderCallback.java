package dev.quiteboring.renderlib.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface HudRenderCallback {

    List<HudRenderCallback> LISTENERS = new CopyOnWriteArrayList<>();

    void onRender(HudRenderEvent event);

    static void onHudRender(HudRenderCallback listener) {
        LISTENERS.add(listener);
    }

    static void dispatch(HudRenderEvent event) {
        for (HudRenderCallback listener : LISTENERS) {
            listener.onRender(event);
        }
    }

}
