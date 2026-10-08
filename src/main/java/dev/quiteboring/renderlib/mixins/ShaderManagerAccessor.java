package dev.quiteboring.renderlib.mixins;

import net.minecraft.client.renderer.Projection;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.client.renderer.ShaderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShaderManager.class)
public interface ShaderManagerAccessor {

  @Accessor("postChainProjection")
  Projection renderlib$postChainProjection();

  @Accessor("postChainProjectionMatrixBuffer")
  ProjectionMatrixBuffer renderlib$postChainProjectionMatrixBuffer();

}
