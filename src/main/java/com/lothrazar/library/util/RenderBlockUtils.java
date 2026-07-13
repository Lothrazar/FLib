package com.lothrazar.library.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joml.Matrix4f;
import com.lothrazar.library.data.Model3D;
import com.lothrazar.library.render.RenderResizableCuboid;
import com.lothrazar.library.render.type.FakeBlockRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;

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
    //RenderTypes used here (FakeBlockRenderTypes.SOLID_COLOUR / TOMB_LINES) declare
    //POSITION_COLOR_TEX_LIGHTMAP, so each vertex must also supply UV0 + UV2 or BufferBuilder
    //rejects it with "Missing elements in vertex: UV0, UV2".
    //down
    addCubeVertex(builder, matrix, startX, startY, startZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, endX, startY, startZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, endX, startY, endZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, startX, startY, endZ, red, green, blue, alpha);
    //up
    addCubeVertex(builder, matrix, startX, endY, startZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, startX, endY, endZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, endX, endY, endZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, endX, endY, startZ, red, green, blue, alpha);
    //east
    addCubeVertex(builder, matrix, startX, startY, startZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, startX, endY, startZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, endX, endY, startZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, endX, startY, startZ, red, green, blue, alpha);
    //west
    addCubeVertex(builder, matrix, startX, startY, endZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, endX, startY, endZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, endX, endY, endZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, startX, endY, endZ, red, green, blue, alpha);
    //south
    addCubeVertex(builder, matrix, endX, startY, startZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, endX, endY, startZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, endX, endY, endZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, endX, startY, endZ, red, green, blue, alpha);
    //north
    addCubeVertex(builder, matrix, startX, startY, startZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, startX, startY, endZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, startX, endY, endZ, red, green, blue, alpha);
    addCubeVertex(builder, matrix, startX, endY, startZ, red, green, blue, alpha);
  }

  private static void addCubeVertex(VertexConsumer builder, Matrix4f matrix, float x, float y, float z, float r, float g, float b, float a) {
    //SOLID_COLOUR is QUADS with the lines shader; normal is required by the shader.
    //For face quads a +Y normal is fine (faces are colored fills, not widened lines).
    builder.addVertex(matrix, x, y, z).setColor(r, g, b, a).setNormal(0f, 1f, 0f);
  }

  /**
   * This block-rendering function from direwolf20 MIT open source project https://github.com/Direwolf20-MC/BuildingGadgets/blob/1.15/LICENSE.md
   */
  public static void renderModelBrightnessColorQuads(PoseStack.Pose matrixEntry, VertexConsumer builder, float red, float green, float blue, float alpha,
      List<BakedQuad> quads, int combinedLights, int combinedOverlay) {
    QuadInstance instance = new QuadInstance();
    instance.setLightCoords(combinedLights);
    instance.setOverlayCoords(combinedOverlay);
    for (BakedQuad quad : quads) {
      float r = quad.materialInfo().isTinted() ? red : 1f;
      float g = quad.materialInfo().isTinted() ? green : 1f;
      float b = quad.materialInfo().isTinted() ? blue : 1f;
      instance.setColor(ARGB.color((int) (alpha * 255f), (int) (r * 255f), (int) (g * 255f), (int) (b * 255f)));
      builder.putBakedQuad(matrixEntry, quad, instance);
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
    int blockLight = LightCoordsUtil.block(light);
    int skyLight = LightCoordsUtil.sky(light);
    return LightCoordsUtil.pack(Math.max(blockLight, glow), Math.max(skyLight, glow));
  }

  @Deprecated
  public static int getColorARGB(FluidStack fluidStack, float fluidScale) {
    if (fluidStack.isEmpty()) {
      return -1;
    }
    return getColorARGB(fluidStack);
  }

  // 26.1 port: IClientFluidTypeExtensions#getTintColor was removed. Fluid tint is now provided by
  // the fluid's baked FluidModel's FluidTintSource (same model FluidRenderMap resolves sprites from).
  public static int getColorARGB(FluidStack fluidStack) {
    if (fluidStack.isEmpty()) {
      return -1;
    }
    FluidState fluidState = fluidStack.getFluid().defaultFluidState();
    FluidModel model = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
    FluidTintSource tintSource = model.fluidTintSource();
    return tintSource == null ? -1 : tintSource.colorAsStack(fluidStack);
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
    if (renderBlockState.getRenderShape() != RenderShape.MODEL) {
      return;
    }
    Minecraft mc = Minecraft.getInstance();
    MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
    VertexConsumer builder = buffer.getBuffer(FakeBlockRenderTypes.FAKE_BLOCK);
    BlockStateModelSet modelSet = mc.getModelManager().getBlockStateModelSet();
    BlockStateModel model = modelSet.get(renderBlockState);
    BlockAndTintGetter tintGetter = world instanceof BlockAndTintGetter batg ? batg : BlockAndTintGetter.EMPTY;
    BlockTintSource tintSource = mc.getBlockColors().getTintSource(renderBlockState, 0);
    RandomSource random = world.getRandom();
    int combinedLights = 15728640;
    int combinedOverlay = OverlayTexture.NO_OVERLAY;
    List<BlockStateModelPart> parts = new ArrayList<>();
    matrix.pushPose();
    matrix.translate(-centerPos.getX(), -centerPos.getY(), -centerPos.getZ());
    for (BlockPos coordinate : shape) {
      if (coordinate == null) {
        continue;
      }
      matrix.pushPose();
      matrix.translate(coordinate.getX(), coordinate.getY(), coordinate.getZ());
      //shrink it up
      matrix.translate(-0.0005f, -0.0005f, -0.0005f);
      matrix.scale(scale, scale, scale);
      int tintColor = tintSource == null ? -1 : tintSource.colorInWorld(renderBlockState, tintGetter, coordinate);
      float red = ARGB.red(tintColor) / 255.0F;
      float green = ARGB.green(tintColor) / 255.0F;
      float blue = ARGB.blue(tintColor) / 255.0F;
      model.collectParts(tintGetter, coordinate, renderBlockState, random, parts);
      for (BlockStateModelPart part : parts) {
        for (Direction direction : Direction.values()) {
          List<BakedQuad> quads = part.getQuads(direction);
          if (!quads.isEmpty()) {
            RenderBlockUtils.renderModelBrightnessColorQuads(matrix.last(), builder, red, green, blue, alpha, quads, combinedLights, combinedOverlay);
          }
        }
        List<BakedQuad> unculledQuads = part.getQuads(null);
        if (!unculledQuads.isEmpty()) {
          RenderBlockUtils.renderModelBrightnessColorQuads(matrix.last(), builder, red, green, blue, alpha, unculledQuads, combinedLights, combinedOverlay);
        }
      }
      parts.clear();
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
    //This helper is for TESRs (BlockEntityRenderers). The TESR's PoseStack is translated by
    //vanilla so its origin = the tile's block position. The caller passes `view = tilePos`,
    //and we undo that translation here so the loop below can position cubes by absolute
    //world coordinate. Do NOT call this from RenderLevelStageEvent — that pose is at the
    //camera origin and subtracting the camera position double-shifts everything off-screen.
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
    Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().position();
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
    //RenderLevelStageEvent pose has the camera ROTATION but not the camera TRANSLATION applied
    //(in 1.21 same as 1.20). Subtract the camera so addVertex with world coords lands correctly.
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
    buffer.endBatch(FakeBlockRenderTypes.TRANSPARENT_COLOUR);
  }

  /**
   * 2-arg overload for use from {@link RenderLevelStageEvent} (e.g. OutlineRenderer). The pose
   * has the camera ROTATION but not the camera TRANSLATION applied, so pass the camera position
   * so the inner translate subtracts it and addVertex with absolute world coords lands at
   * (world - camera) in eye space. For TESR callers use the 3-arg form below and pass the
   * tile's world position so the TESR's vanilla translation gets undone instead.
   */
  public static void createBox(PoseStack poseStack, BlockPos pos) {
    createBox(poseStack, pos, Minecraft.getInstance().gameRenderer.getMainCamera().position());
  }
  public static void createBox(PoseStack poseStack, BlockPos pos, Vec3 poseOriginInWorld) {
    poseStack.pushPose();
    Minecraft mc = Minecraft.getInstance();
    createBox(mc.renderBuffers().bufferSource(), poseOriginInWorld, poseStack, pos.getX(), pos.getY(), pos.getZ(), 1.0F);
    poseStack.popPose();
  }


  /**
   * @param poseOriginInWorld where the PoseStack's local origin lives in world space.
   *   - For RenderLevelStageEvent in 1.21: PoseStack is camera-relative — pass Vec3.ZERO.
   *   - For TESRs (BlockEntityRenderer): PoseStack origin is at the tile — pass the tile's pos.
   *   We subtract this from the pose so subsequent draws can use absolute world coords.
   */
  public static void createBox(MultiBufferSource.BufferSource bufferSource, Vec3 poseOriginInWorld, PoseStack poseStack, float x, float y, float z, float offset) {
    //rainbow magic
    float[] color = getRandomColour();
    // distance-clamp far positions (use the real camera, not the pose-origin offset)
    Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().position();
    Vec3 vec = new Vec3(x, y, z).subtract(camera);
    if (vec.distanceTo(Vec3.ZERO) > 200d) { // could be 300
      vec = vec.normalize().scale(200d);
      x += vec.x;
      y += vec.y;
      z += vec.z;
    }
    VertexConsumer vertexConsumer = bufferSource.getBuffer(FakeBlockRenderTypes.TOMB_LINES);
    //Translate so the PoseStack's local origin maps to world origin. After this, addVertex
    //with absolute world coords (x, y, z) lands at the correct place.
    poseStack.translate(-poseOriginInWorld.x, -poseOriginInWorld.y, -poseOriginInWorld.z);
    PoseStack.Pose pose = poseStack.last();
    float r = color[0], g = color[1], b = color[2];
    //12 edges of the cube. The lines shader uses the vertex NORMAL as the line direction
    //(transformed through the pose's normal matrix) to compute screen-space widening —
    //without it the line collapses to ~0px and is invisible.
    //bottom 4 edges
    line(vertexConsumer, pose, x, y, z,                 x + offset, y, z,                 r, g, b, 1f, 0f, 0f);
    line(vertexConsumer, pose, x + offset, y, z,        x + offset, y, z + offset,        r, g, b, 0f, 0f, 1f);
    line(vertexConsumer, pose, x + offset, y, z + offset, x, y, z + offset,               r, g, b, -1f, 0f, 0f);
    line(vertexConsumer, pose, x, y, z + offset,        x, y, z,                          r, g, b, 0f, 0f, -1f);
    //top 4 edges
    line(vertexConsumer, pose, x, y + offset, z,        x + offset, y + offset, z,        r, g, b, 1f, 0f, 0f);
    line(vertexConsumer, pose, x + offset, y + offset, z, x + offset, y + offset, z + offset, r, g, b, 0f, 0f, 1f);
    line(vertexConsumer, pose, x + offset, y + offset, z + offset, x, y + offset, z + offset, r, g, b, -1f, 0f, 0f);
    line(vertexConsumer, pose, x, y + offset, z + offset, x, y + offset, z,                r, g, b, 0f, 0f, -1f);
    //4 vertical edges
    line(vertexConsumer, pose, x, y, z,                  x, y + offset, z,                 r, g, b, 0f, 1f, 0f);
    line(vertexConsumer, pose, x + offset, y, z,         x + offset, y + offset, z,        r, g, b, 0f, 1f, 0f);
    line(vertexConsumer, pose, x + offset, y, z + offset, x + offset, y + offset, z + offset, r, g, b, 0f, 1f, 0f);
    line(vertexConsumer, pose, x, y, z + offset,         x, y + offset, z + offset,         r, g, b, 0f, 1f, 0f);
    bufferSource.endBatch(FakeBlockRenderTypes.TOMB_LINES);
  }

  private static void line(VertexConsumer vc, PoseStack.Pose pose,
      float x1, float y1, float z1, float x2, float y2, float z2,
      float r, float g, float b, float nx, float ny, float nz) {
    //Use the PoseStack.Pose overloads of addVertex and setNormal so the position goes through
    //the pose matrix AND the normal goes through the normal matrix — matches vanilla's
    //LevelRenderer.renderLineBox pattern. Bare setNormal(float, float, float) writes the raw
    //model-space normal which the lines shader misinterprets, collapsing widening to ~0.
    vc.addVertex(pose, x1, y1, z1).setColor(r, g, b, 1.0F).setNormal(pose, nx, ny, nz);
    vc.addVertex(pose, x2, y2, z2).setColor(r, g, b, 1.0F).setNormal(pose, nx, ny, nz);
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
