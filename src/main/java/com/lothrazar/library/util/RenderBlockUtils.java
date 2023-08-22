package com.lothrazar.library.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import com.lothrazar.library.data.Model3D;
import com.lothrazar.library.render.RenderResizableCuboid;
import com.lothrazar.library.render.type.FakeBlockRenderTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

/**
 * legacy ref https://www.minecraftforge.net/forum/topic/79556-1151-rendering-block-manually-clientside/?tab=comments#comment-379808
 */
public class RenderBlockUtils {

  // Replace various usages of this with the getter for calculating glow light, at least if we end up making it only
  // effect block light for the glow rather than having it actually become full light
  public static final int FULL_LIGHT = 0xF000F0;

  public static void renderCube(Matrix4f matrix, VertexConsumer builder, BlockPos pos, Color color, float alpha) {
    float red = color.getRed() / 255f, green = color.getGreen() / 255f, blue = color.getBlue() / 255f;
    float startX = 0, startY = 0, startZ = -1, endX = 1, endY = 1, endZ = 0;
    //down
    builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
    //up
    builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
    //east
    builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
    //west
    builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
    //south
    builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
    //north
    builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
  }

  /**
   * This block-rendering function from direwolf20 MIT open source project https://github.com/Direwolf20-MC/BuildingGadgets/blob/1.15/LICENSE.md
   */
  public static void renderModelBrightnessColorQuads(PoseStack.Pose matrixEntry, VertexConsumer builder, float red, float green, float blue, float alpha, List<BakedQuad> quads,
      int combinedLights, int combinedOverlay) {
    for (BakedQuad bakedquad : quads) {
      float r;
      float g;
      float b;
      if (bakedquad.isTinted()) {
        r = red * 1f;
        g = green * 1f;
        b = blue * 1f;
      }
      else {
        r = 1f;
        g = 1f;
        b = 1f;
      }
      //      addVertexData 
      boolean readExistingColor = false;
      builder.putBulkData(matrixEntry, bakedquad, r, g, b, alpha, combinedLights, combinedOverlay, readExistingColor);
    }
  }

  /**
   * Used for in-world fluid rendering Source reference from MIT open source https://github.com/mekanism/Mekanism/tree/1.15x
   * <p>
   * https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE
   * <p>
   * See MekanismRenderer.
   **/
  public static void renderObject(Model3D object, PoseStack matrix, VertexConsumer buffer, int argb, int light) {
    if (object != null) {
      RenderResizableCuboid.INSTANCE.renderCube(object, matrix, buffer, argb, light);
    }
  }

  /**
   * used for fluid in-world render lighting
   *
   * @param light
   * @param fluid
   * @return
   */
  public static int calculateGlowLight(int light, FluidStack fluid) {
    return fluid.isEmpty() ? light
        : calculateGlowLight(light,
            fluid.getFluid().getFluidType().getLightLevel());
  }

  public static int calculateGlowLight(int light, int glow) {
    if (glow >= 15) {
      return FULL_LIGHT;
    }
    int blockLight = LightTexture.block(light);
    int skyLight = LightTexture.sky(light);
    return LightTexture.pack(Math.max(blockLight, glow), Math.max(skyLight, glow));
  }

  @Deprecated
  public static int getColorARGB(FluidStack fluidStack, float fluidScale) {
    if (fluidStack.isEmpty()) {
      return -1;
    }
    return getColorARGB(fluidStack);
  }

  public static int getColorARGB(FluidStack fluidStack) {
    if (fluidStack.isEmpty()) {
      return -1;
    }
    IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluidStack.getFluid());
    return fluidAttributes.getTintColor(fluidStack);
  }

  public static float getRed(int color) {
    return (color >> 16 & 0xFF) / 255.0F;
  }

  public static float getGreen(int color) {
    return (color >> 8 & 0xFF) / 255.0F;
  }

  public static float getBlue(int color) {
    return (color & 0xFF) / 255.0F;
  }

  public static float getAlpha(int color) {
    return (color >> 24 & 0xFF) / 255.0F;
  }

  /**
   * Call from TESR perspective
   * 
   * @param level
   */
  public static void renderAsBlock(Level level, final BlockPos centerPos, final List<BlockPos> shape, PoseStack matrix, ItemStack stack, float alpha, float scale) {
    BlockState renderBlockState = Block.byItem(stack.getItem()).defaultBlockState();
    renderAsBlock(level, centerPos, shape, matrix, renderBlockState, alpha, scale);
  }

  /**
   * Render this BLOCK right here in the world, start with alpha and scale near 1. Call from TESR perspective
   * 
   * used by cyclic:light_camo
   * 
   */
  public static void renderAsBlock(Level world, final BlockPos centerPos, final List<BlockPos> shape, PoseStack matrix, BlockState renderBlockState, float alpha, float scale) {

    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

    Minecraft mc = Minecraft.getInstance();
    MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
    VertexConsumer builder = buffer.getBuffer(FakeBlockRenderTypes.FAKE_BLOCK);
    BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
    matrix.pushPose();
    matrix.translate(-centerPos.getX(), -centerPos.getY(), -centerPos.getZ());
    for (BlockPos coordinate : shape) {

      float x = coordinate.getX();
      float y = coordinate.getY();
      float z = coordinate.getZ();
      matrix.pushPose();
      matrix.translate(x, y, z);
      //
      //shrink it up
      matrix.translate(-0.0005f, -0.0005f, -0.0005f);
      matrix.scale(scale, scale, scale);
      BakedModel ibakedmodel = dispatcher.getBlockModel(renderBlockState);
      BlockColors blockColors = Minecraft.getInstance().getBlockColors();
      int color = blockColors.getColor(renderBlockState, world, coordinate, 0);
      float red = (color >> 16 & 255) / 255.0F;
      float green = (color >> 8 & 255) / 255.0F;
      float blue = (color & 255) / 255.0F;
      if (renderBlockState.getRenderShape() == RenderShape.MODEL) {
        int combinedLights = 15728640;
        int combinedOverlay = 655360;
        for (Direction direction : Direction.values()) {
          RenderBlockUtils.renderModelBrightnessColorQuads(matrix.last(), builder, red, green, blue, alpha,
              ibakedmodel.getQuads(renderBlockState, direction, world.random,
                  ibakedmodel.getModelData(world, centerPos, renderBlockState, null), FakeBlockRenderTypes.FAKE_BLOCK), // EmptyModelData.INSTANCE 
              combinedLights, combinedOverlay);
        }
      }
      matrix.popPose();
    }
    matrix.popPose();
  }

  public static void renderOutline(BlockPos view, BlockPos pos, PoseStack matrix, float scale, Color color) {
    List<BlockPos> coords = new ArrayList<>();
    coords.add(pos);
    renderOutline(view, coords, matrix, scale, color);
  }

  public static BlockHitResult getLookingAt(Player player, int range) {
    return (BlockHitResult) player.pick(range, 0F, false);
  }

  /**
   * Used by TESRs
   * 
   * View can be tile entity position, or player pos depending on context mc.gameRenderer.getMainCamera().getPosition();
   */
  public static void renderOutline(BlockPos view, List<BlockPos> coords, PoseStack matrix, float scale, Color color) {
    final Minecraft mc = Minecraft.getInstance();
    MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
    matrix.pushPose();
    matrix.translate(-view.getX(), -view.getY(), -view.getZ());
    VertexConsumer builder;
    builder = buffer.getBuffer(FakeBlockRenderTypes.SOLID_COLOUR);
    for (BlockPos e : coords) {
      if (e == null) {
        continue;
      }
      matrix.pushPose();
      float ctr = (1 - scale) / 2;
      matrix.translate(e.getX() + ctr, e.getY() + ctr, e.getZ() + ctr);
      matrix.translate(-0.005f, -0.005f, -0.005f);
      matrix.scale(scale, scale, scale);
      matrix.mulPose(Axis.YP.rotationDegrees(-90.0F));
      RenderBlockUtils.renderCube(matrix.last().pose(), builder, e, color, .125F);
      matrix.popPose();
    }
    matrix.popPose();
    RenderSystem.disableDepthTest();
    buffer.endBatch(FakeBlockRenderTypes.SOLID_COLOUR);
  }

  /**
   * Create your own PoseStack and view perspective and use the method that does not depend on forge events
   * 
   * @param evt
   * @param coords
   * @param alpha
   */
  @Deprecated
  public static void renderColourCubes(RenderLevelStageEvent evt, Map<BlockPos, Color> coords, float alpha) {
    PoseStack matrix = evt.getPoseStack();
    Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
    float scale = 1.01F;
    renderColourCubes(matrix, view, coords, scale, alpha);
  }

  /**
   * for ITEMS held by the PLAYER rendering cubes in world
   */
  public static void renderColourCubes(PoseStack matrix, Vec3 view, Map<BlockPos, Color> coords, float scale, float alpha) {
    LocalPlayer player = Minecraft.getInstance().player;
    if (player == null) {
      return;
    }
    final Minecraft mc = Minecraft.getInstance();
    MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
    matrix.pushPose();
    matrix.translate(-view.x(), -view.y(), -view.z());
    VertexConsumer builder = buffer.getBuffer(FakeBlockRenderTypes.TRANSPARENT_COLOUR);
    for (BlockPos posCurr : coords.keySet()) {
      matrix.pushPose();
      matrix.translate(posCurr.getX(), posCurr.getY(), posCurr.getZ());
      matrix.translate(-0.005f, -0.005f, -0.005f);
      matrix.scale(scale, scale, scale);
      matrix.mulPose(Axis.YP.rotationDegrees(-90.0F));
      RenderBlockUtils.renderCube(matrix.last().pose(), builder, posCurr, coords.get(posCurr), alpha);
      matrix.popPose();
    }
    matrix.popPose();
    RenderSystem.disableDepthTest();
    buffer.endBatch(FakeBlockRenderTypes.TRANSPARENT_COLOUR);
  }

  public static void createBox(PoseStack poseStack, BlockPos pos) {
    createBox(poseStack, pos, Minecraft.getInstance().gameRenderer.getMainCamera().getPosition());
  }
  public static void createBox(PoseStack poseStack, BlockPos pos, Vec3 cameraPosition) {
    poseStack.pushPose();
    Minecraft mc = Minecraft.getInstance();
    createBox(mc.renderBuffers().bufferSource(), cameraPosition, poseStack, pos.getX(), pos.getY(), pos.getZ(), 1.0F);
    poseStack.popPose();
  }


  public static void createBox(MultiBufferSource.BufferSource bufferSource, Vec3 cameraPosition, PoseStack poseStack, float x, float y, float z, float offset) {
    //rainbow magic
    float[] color = getRandomColour();
    // get a closer pos if too far
    Vec3 vec = new Vec3(x, y, z).subtract(cameraPosition);
    if (vec.distanceTo(Vec3.ZERO) > 200d) { // could be 300
      vec = vec.normalize().scale(200d);
      x += vec.x;
      y += vec.y;
      z += vec.z;
    }
    RenderSystem.disableDepthTest();
    VertexConsumer vertexConsumer = bufferSource.getBuffer(FakeBlockRenderTypes.TOMB_LINES);
    poseStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
    Matrix4f pose = poseStack.last().pose();
    vertexConsumer.vertex(pose, x, y, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y + offset, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y + offset, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y + offset, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y + offset, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y + offset, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y + offset, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y + offset, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y + offset, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y + offset, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y + offset, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y + offset, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y + offset, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    bufferSource.endBatch(FakeBlockRenderTypes.TOMB_LINES);
    RenderSystem.enableDepthTest();
  }

  public static float[] getRandomColour() {
    long c = (System.currentTimeMillis() / 15L) % 360L;
    float[] color = getHSBtoRGBF(c / 360f, 1f, 1f);
    return color;
  }

  /**
   * From https://github.com/Lothrazar/SimpleTomb/blob/704bad5a33731125285d700c489bfe2c3a9e387d/src/main/java/com/lothrazar/simpletomb/helper/WorldHelper.java#L163
   *
   * @param hue
   * @param saturation
   * @param brightness
   * @return
   */
  public static float[] getHSBtoRGBF(float hue, float saturation, float brightness) {
    int r = 0;
    int g = 0;
    int b = 0;
    if (saturation == 0.0F) {
      r = g = b = (int) (brightness * 255.0F + 0.5F);
    }
    else {
      float h = (hue - (float) Math.floor(hue)) * 6.0F;
      float f = h - (float) Math.floor(h);
      float p = brightness * (1.0F - saturation);
      float q = brightness * (1.0F - saturation * f);
      float t = brightness * (1.0F - saturation * (1.0F - f));
      switch ((int) h) {
        case 0:
          r = (int) (brightness * 255.0F + 0.5F);
          g = (int) (t * 255.0F + 0.5F);
          b = (int) (p * 255.0F + 0.5F);
        break;
        case 1:
          r = (int) (q * 255.0F + 0.5F);
          g = (int) (brightness * 255.0F + 0.5F);
          b = (int) (p * 255.0F + 0.5F);
        break;
        case 2:
          r = (int) (p * 255.0F + 0.5F);
          g = (int) (brightness * 255.0F + 0.5F);
          b = (int) (t * 255.0F + 0.5F);
        break;
        case 3:
          r = (int) (p * 255.0F + 0.5F);
          g = (int) (q * 255.0F + 0.5F);
          b = (int) (brightness * 255.0F + 0.5F);
        break;
        case 4:
          r = (int) (t * 255.0F + 0.5F);
          g = (int) (p * 255.0F + 0.5F);
          b = (int) (brightness * 255.0F + 0.5F);
        break;
        case 5:
          r = (int) (brightness * 255.0F + 0.5F);
          g = (int) (p * 255.0F + 0.5F);
          b = (int) (q * 255.0F + 0.5F);
      }
    }
    return new float[] {
        r / 255.0F,
        g / 255.0F,
        b / 255.0F,
    };
  }
}
