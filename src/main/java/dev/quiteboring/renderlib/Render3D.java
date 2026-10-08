package dev.quiteboring.renderlib;

import dev.quiteboring.renderlib.internal.projection.CameraState;
import dev.quiteboring.renderlib.internal.projection.Projector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

/**
 * Static world-to-screen projection API ("3D ESP" style).
 * The camera frame is captured automatically every frame; if no frame is
 * available yet (or the point is behind the camera) these return null.
 */
public final class Render3D {
    private Render3D() {
    }

    public record ScreenPoint(float x, float y) {
    }

    public record ScreenBox(float x, float y, float width, float height) {
    }

    @Nullable
    public static ScreenPoint project(Vec3 worldPos, int screenWidth, int screenHeight) {
        CameraRenderState camera = CameraState.get();
        if (camera == null) {
            return null;
        }
        Vector4f out = Projector.project(worldPos, camera, screenWidth, screenHeight);
        return out == null ? null : new ScreenPoint(out.x, out.y);
    }

    @Nullable
    public static ScreenBox project(AABB box, int screenWidth, int screenHeight) {
        CameraRenderState camera = CameraState.get();
        if (camera == null) {
            return null;
        }
        Projector.ScreenBox out = Projector.project(box, camera, screenWidth, screenHeight);
        return out == null ? null : new ScreenBox(out.x(), out.y(), out.width(), out.height());
    }

    @Nullable
    public static ScreenBox project(LivingEntity entity, float partialTick, int screenWidth, int screenHeight) {
        return project(interpolatedBox(entity, partialTick), screenWidth, screenHeight);
    }

    /** Bounding box interpolated for the current render frame. */
    public static AABB interpolatedBox(LivingEntity entity, float partialTick) {
        Vec3 pos = entity.getPosition(partialTick);
        AABB current = entity.getBoundingBox();
        double halfWidth = (current.maxX - current.minX) / 2.0;
        double height = current.maxY - current.minY;
        return new AABB(pos.x - halfWidth, pos.y, pos.z - halfWidth,
                pos.x + halfWidth, pos.y + height, pos.z + halfWidth);
    }
}
