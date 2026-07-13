package com.lothrazar.library.util;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.lothrazar.library.render.type.LineRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class RenderUtil {

  private static final float TOMB_LINE_WIDTH = 2.5F;

  public static void drawString(GuiGraphicsExtractor gg, String str, int x, int y) {
    Minecraft mc = Minecraft.getInstance();
    gg.text(mc.font, str, x, y, 0xFFFFFF);
  }

  public static void drawStack(GuiGraphicsExtractor poseStack, ItemStack stack) {
    Minecraft mc = Minecraft.getInstance();
    int width = mc.getWindow().getGuiScaledWidth();
    int height = mc.getWindow().getGuiScaledHeight();
    // old renderItem(stack, x, y, seed, guiOffset) had a separate z-offset param that no longer
    // exists on item(...) - z-layering is now handled via the pose stack instead.
    poseStack.item(stack, width / 2, height / 2, 0);
  }

  @OnlyIn(Dist.CLIENT)
  public static void createBox(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, float x, float y, float z, float offset) {
    long c = (System.currentTimeMillis() / 15L) % 360L;
    float[] color = RenderUtil.getHSBtoRGBF(c / 360f, 1f, 1f);
    int packedColor = 0xFF000000 | (int) (color[0] * 255) << 16 | (int) (color[1] * 255) << 8 | (int) (color[2] * 255);
    Minecraft mc = Minecraft.getInstance();
    Vec3 cameraPosition = mc.gameRenderer.getMainCamera().position();
    // get a closer pos if too far
    Vec3 vec = new Vec3(x, y, z).subtract(cameraPosition);
    if (vec.distanceTo(Vec3.ZERO) > 200d) { // could be 300
      vec = vec.normalize().scale(200d);
      x += vec.x;
      y += vec.y;
      z += vec.z;
    }
    RenderType renderType = LineRenderType.tombLinesType();
    VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
    poseStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
    PoseStack.Pose pose = poseStack.last();
    Matrix4f mat = pose.pose();
    // no depth test is already baked into the tomb-lines RenderType's pipeline, no RenderSystem toggling needed here.
    addEdge(vertexConsumer, pose, mat, packedColor, x, y, z, x + offset, y, z);
    addEdge(vertexConsumer, pose, mat, packedColor, x, y, z, x, y + offset, z);
    addEdge(vertexConsumer, pose, mat, packedColor, x, y, z, x, y, z + offset);
    addEdge(vertexConsumer, pose, mat, packedColor, x + offset, y + offset, z + offset, x, y + offset, z + offset);
    addEdge(vertexConsumer, pose, mat, packedColor, x + offset, y + offset, z + offset, x + offset, y, z + offset);
    addEdge(vertexConsumer, pose, mat, packedColor, x + offset, y + offset, z + offset, x + offset, y + offset, z);
    addEdge(vertexConsumer, pose, mat, packedColor, x, y + offset, z, x, y + offset, z + offset);
    addEdge(vertexConsumer, pose, mat, packedColor, x, y + offset, z, x + offset, y + offset, z);
    addEdge(vertexConsumer, pose, mat, packedColor, x + offset, y, z, x + offset, y, z + offset);
    addEdge(vertexConsumer, pose, mat, packedColor, x + offset, y, z, x + offset, y + offset, z);
    addEdge(vertexConsumer, pose, mat, packedColor, x, y, z + offset, x + offset, y, z + offset);
    addEdge(vertexConsumer, pose, mat, packedColor, x, y, z + offset, x, y + offset, z + offset);
    bufferSource.endBatch(renderType);
  }

  private static void addEdge(VertexConsumer vertexConsumer, PoseStack.Pose pose, Matrix4f mat, int color, float x1, float y1, float z1, float x2, float y2, float z2) {
    Vector3f normal = new Vector3f(x2 - x1, y2 - y1, z2 - z1).normalize();
    vertexConsumer.addVertex(mat, x1, y1, z1).setColor(color).setNormal(pose, normal).setLineWidth(TOMB_LINE_WIDTH);
    vertexConsumer.addVertex(mat, x2, y2, z2).setColor(color).setNormal(pose, normal).setLineWidth(TOMB_LINE_WIDTH);
  }

  public static float[] getRGBColor3F(int color) {
    return new float[] {
        (color >> 16 & 255) / 255.0F,
        (color >> 8 & 255) / 255.0F,
        (color & 255) / 255.0F,
    };
  }

  public static int intColor(int r, int g, int b) {
    return (r * 65536 + g * 256 + b);
  }

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
