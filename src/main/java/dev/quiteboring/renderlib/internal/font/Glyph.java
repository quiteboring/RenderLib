package dev.quiteboring.renderlib.internal.font;

public final class Glyph {
    public final float u0, v0, u1, v1;

    public final int width, height;

    public final int bearingX, bearingY;

    public final float advance;

    public final boolean hasInk;

    public Glyph(float u0, float v0, float u1, float v1,
          int width, int height, int bearingX, int bearingY, float advance, boolean hasInk) {
        this.u0 = u0;
        this.v0 = v0;
        this.u1 = u1;
        this.v1 = v1;
        this.width = width;
        this.height = height;
        this.bearingX = bearingX;
        this.bearingY = bearingY;
        this.advance = advance;
        this.hasInk = hasInk;
    }
}
