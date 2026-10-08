package dev.quiteboring.renderlib;

/**
 * Which part of the UI a blur/bloom effect belongs to.
 * Each layer can use its own {@link BlurStyle} (see {@link RenderConfig}).
 */
public enum BlurLayer {
    HUD,
    MENU,
    WORLD,
    CHAT,
    WIDGET,
    OVERLAY;

    public static final BlurLayer[] VALUES = values();
}
