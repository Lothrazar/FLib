package com.lothrazar.library.util;

import java.util.ArrayList;
import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FacadeUtil {

  public static void renderBlockState(Level level, BlockPos pos, MultiBufferSource ibuffer, PoseStack matrixStack, BlockState facadeState, int packedLight, int packedOverlay) {
    if (facadeState == null) {
      return;
    }
    Minecraft mc = Minecraft.getInstance();
    BlockStateModelSet modelSet = mc.getModelManager().getBlockStateModelSet();
    BlockStateModel model = modelSet.get(facadeState);
    BlockAndTintGetter tintGetter = level instanceof BlockAndTintGetter batg ? batg : BlockAndTintGetter.EMPTY;
    BlockColors blockColors = mc.getBlockColors();
    RandomSource random = level.getRandom();
    List<BlockStateModelPart> parts = new ArrayList<>();
    model.collectParts(tintGetter, pos, facadeState, random, parts);
    QuadInstance instance = new QuadInstance();
    instance.setLightCoords(packedLight);
    instance.setOverlayCoords(packedOverlay);
    PoseStack.Pose pose = matrixStack.last();
    for (BlockStateModelPart part : parts) {
      for (Direction direction : Direction.values()) {
        putQuads(tintGetter, pos, facadeState, blockColors, ibuffer, pose, instance, part.getQuads(direction));
      }
      putQuads(tintGetter, pos, facadeState, blockColors, ibuffer, pose, instance, part.getQuads(null));
    }
  }

  private static void putQuads(BlockAndTintGetter level, BlockPos pos, BlockState state, BlockColors blockColors, MultiBufferSource ibuffer, PoseStack.Pose pose,
      QuadInstance instance, List<BakedQuad> quads) {
    for (BakedQuad quad : quads) {
      int tintIndex = quad.materialInfo().tintIndex();
      int color = -1;
      if (tintIndex != -1) {
        BlockTintSource tintSource = blockColors.getTintSource(state, tintIndex);
        if (tintSource != null) {
          color = tintSource.colorInWorld(state, level, pos);
        }
      }
      instance.setColor(color);
      VertexConsumer vertexConsumer = ibuffer.getBuffer(renderTypeForLayer(quad.materialInfo().layer()));
      vertexConsumer.putBakedQuad(pose, quad, instance);
    }
  }

  private static RenderType renderTypeForLayer(ChunkSectionLayer layer) {
    return switch (layer) {
      case SOLID -> RenderTypes.solidMovingBlock();
      case CUTOUT -> RenderTypes.cutoutMovingBlock();
      case TRANSLUCENT -> RenderTypes.translucentMovingBlock();
    };
  }
}
