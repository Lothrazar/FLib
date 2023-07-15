package com.lothrazar.library.util;

import org.joml.Matrix4f;
import com.lothrazar.library.render.type.LineRenderType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RenderUtil {

  public static void drawString(GuiGraphics gg, String str, int x, int y) {
    Minecraft mc = Minecraft.getInstance();
    gg.drawString(mc.font, str, x, y, 0xFFFFFF);
  }

  public static void drawStack(GuiGraphics poseStack, ItemStack stack) {
    Minecraft mc = Minecraft.getInstance();
    int width = mc.getWindow().getGuiScaledWidth();
    int height = mc.getWindow().getGuiScaledHeight();
    //    mc.getItemRenderer().render(stack, null, false, null, null, width, height, null);
    //    var context = ItemDisplayContext.GUI;
    //    var pose = poseStack.pose();
    poseStack.renderItem(stack, width / 2, height / 2, 0, 10);
  }

  @OnlyIn(Dist.CLIENT)
  public static void createBox(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, float x, float y, float z, float offset) {
    long c = (System.currentTimeMillis() / 15L) % 360L;
    float[] color = RenderUtil.getHSBtoRGBF(c / 360f, 1f, 1f);
    Minecraft mc = Minecraft.getInstance();
    Vec3 cameraPosition = mc.gameRenderer.getMainCamera().getPosition();
    // get a closer pos if too far
    Vec3 vec = new Vec3(x, y, z).subtract(cameraPosition);
    if (vec.distanceTo(Vec3.ZERO) > 200d) { // could be 300
      vec = vec.normalize().scale(200d);
      x += vec.x;
      y += vec.y;
      z += vec.z;
    }
    RenderSystem.disableDepthTest();
    RenderType renderType = LineRenderType.tombLinesType();
    VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
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
    bufferSource.endBatch(renderType);
    RenderSystem.enableDepthTest();
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
