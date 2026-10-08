package dev.quiteboring.renderlib;

/** Background-blur algorithm used for a {@link BlurLayer}. */
public enum BlurStyle {
    GAUSSIAN,
    KAWASE,
    RISE;

    public static final BlurStyle[] VALUES = values();
}
