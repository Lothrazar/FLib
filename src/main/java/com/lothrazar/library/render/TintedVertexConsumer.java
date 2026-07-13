package com.lothrazar.library.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.ARGB;

public class TintedVertexConsumer implements VertexConsumer {

  private final VertexConsumer delegate;
  private final float r, g, b;

  public TintedVertexConsumer(VertexConsumer delegate, int rgb) {
    this.delegate = delegate;
    r = ((rgb >> 16) & 0xFF) / 255f;
    g = ((rgb >> 8) & 0xFF) / 255f;
    b = (rgb & 0xFF) / 255f;
  }

  @Override
  public VertexConsumer addVertex(float x, float y, float z) {
    delegate.addVertex(x, y, z);
    return this;
  }

  @Override
  public VertexConsumer setColor(int red, int green, int blue, int alpha) {
    delegate.setColor((int) (red * r), (int) (green * g), (int) (blue * b), alpha);
    return this;
  }

  @Override
  public VertexConsumer setColor(int color) {
    delegate.setColor((int) (ARGB.red(color) * r), (int) (ARGB.green(color) * g), (int) (ARGB.blue(color) * b), ARGB.alpha(color));
    return this;
  }

  @Override
  public VertexConsumer setUv(float u, float v) {
    delegate.setUv(u, v);
    return this;
  }

  @Override
  public VertexConsumer setUv1(int u, int v) {
    delegate.setUv1(u, v);
    return this;
  }

  @Override
  public VertexConsumer setUv2(int u, int v) {
    delegate.setUv2(u, v);
    return this;
  }

  @Override
  public VertexConsumer setNormal(float nx, float ny, float nz) {
    delegate.setNormal(nx, ny, nz);
    return this;
  }

  @Override
  public VertexConsumer setLineWidth(float width) {
    delegate.setLineWidth(width);
    return this;
  }
}
