package com.lothrazar.library.gui;

import java.util.ArrayList;
import java.util.List;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.render.FluidRenderMap;
import com.lothrazar.library.render.FluidRenderMap.FluidFlow;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidBar {

  public static final Identifier FLUID_WIDGET = Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "textures/gui/fluid.png");
  public String emtpyTooltip = "0";
  private Font font;
  private int x;
  private int y;
  private int capacity;
  private int width = 18;
  private int height = 62;
  public int guiLeft;
  public int guiTop;

  public FluidBar(Font p, int cap) {
    this(p, 132, 8, cap);
  }

  public FluidBar(Font p, int x, int y, int cap) {
    font = p;
    this.x = x;
    this.y = y;
    this.capacity = cap;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void draw(GuiGraphicsExtractor gg, FluidStack fluid) {
    final int u = 0, v = 0, x = guiLeft + getX(), y = guiTop + getY();
    gg.blit(RenderPipelines.GUI_TEXTURED, FLUID_WIDGET,
        x, y, u, v,
        width, height,
        width, height);
    //NOW the fluid part
    if (fluid == null || this.getCapacity() == 0 || fluid.getAmount() == 0) {
      return;
    }
    float capacity = this.getCapacity();
    float amount = fluid.getAmount();
    float scale = amount / capacity;
    int fluidAmount = (int) (scale * height);
    TextureAtlasSprite sprite = FluidRenderMap.getFluidTexture(fluid, FluidFlow.STILL);
    //hack in the blue because water is grey and is filled in by the biome when in-world
    //RenderSystem.setShaderColor no longer exists; blitSprite now takes the tint directly as an ARGB int
    int tint = fluid.getFluid() == Fluids.WATER ? 0xFF0000FF : 0xFFFFFFFF;
    int xPosition = x + 1;
    int yPosition = y + 1;
    int maximum = height - 2;
    int desiredWidth = width - 2;
    int desiredHeight = fluidAmount - 2;
    gg.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, xPosition, yPosition + (maximum - desiredHeight), desiredWidth, desiredHeight, tint);
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x <= mouseX && mouseX <= guiLeft + x + width
        && guiTop + y <= mouseY && mouseY <= guiTop + y + height;
  }

  public void renderHoveredToolTip(GuiGraphicsExtractor ms, int mouseX, int mouseY, FluidStack current) {
    if (this.isMouseover(mouseX, mouseY)) {
      this.renderTooltip(ms, mouseX, mouseY, current);
    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getCapacity() {
    return capacity;
  }

  public void renderTooltip(GuiGraphicsExtractor gg, int mouseX, int mouseY, FluidStack current) {
    String tt = emtpyTooltip;
    if (current != null && !current.isEmpty()) {
      tt = current.getAmount() + "/" + getCapacity() + " " + current.getHoverName().getString(); // getDisplayName() -> getHoverName()
    }
    List<Component> list = new ArrayList<>();
    list.add(Component.translatable(tt));
    gg.setComponentTooltipForNextFrame(font, list, mouseX, mouseY);
  }
}
