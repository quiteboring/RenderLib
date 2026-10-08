package dev.quiteboring.renderlib;

import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoProperties;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Static world-space drawing API. Call from a {@code WorldRenderCallback}
 * (see {@code dev.quiteboring.renderlib.event.WorldRenderCallback}).
 * Colors are packed {@code 0xAARRGGBB} ints (see {@link Colors}).
 * Every method returns properties for chaining, e.g.
 * {@code box(pos, red).setAlwaysOnTop()} for through-wall ESP.
 */
public final class RenderWorld {
    private RenderWorld() {
    }

    public static GizmoProperties box(AABB box, int color) {
        return Gizmos.cuboid(box, GizmoStyle.stroke(color));
    }

    public static GizmoProperties box(AABB box, int color, float width) {
        return Gizmos.cuboid(box, GizmoStyle.stroke(color, width));
    }

    public static GizmoProperties boxFilled(AABB box, int color) {
        return Gizmos.cuboid(box, GizmoStyle.fill(color));
    }

    public static GizmoProperties box(BlockPos pos, int color) {
        return Gizmos.cuboid(pos, GizmoStyle.stroke(color));
    }

    public static GizmoProperties box(BlockPos pos, float inflate, int color) {
        return Gizmos.cuboid(pos, inflate, GizmoStyle.stroke(color));
    }

    public static GizmoProperties boxFilled(BlockPos pos, int color) {
        return Gizmos.cuboid(pos, GizmoStyle.fill(color));
    }

    public static GizmoProperties line(Vec3 from, Vec3 to, int color) {
        return Gizmos.line(from, to, color);
    }

    public static GizmoProperties line(Vec3 from, Vec3 to, int color, float width) {
        return Gizmos.line(from, to, color, width);
    }
}
