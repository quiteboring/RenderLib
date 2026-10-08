package dev.quiteboring.renderlib.event;

/**
 * Fired once per frame at the start of level rendering, on the render thread.
 * Draw world-space shapes with {@code dev.quiteboring.renderlib.RenderWorld}
 * (backed by vanilla gizmos: boxes, lines, fills). Shapes are depth-tested by
 * default; chain {@code .setAlwaysOnTop()} on the returned properties for
 * through-wall ESP, or {@code .persistForMillis(ms)} for one-shot highlights.
 */
public record WorldRenderEvent() {
}
