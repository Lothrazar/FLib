package com.lothrazar.library.render.type;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import com.lothrazar.library.FutureLibMod;

public class FakeBlockRenderTypes extends RenderType {
  private static final boolean MIPMAP = false;
  private static final boolean BLUR = false;
  private static final boolean SORT = false;
  private static final boolean CRUMBLING = false;
  private static final int BUFFERSIZE = 256;

  public FakeBlockRenderTypes(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
    super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
  }

  public final static ResourceLocation BEAM = ResourceLocation.fromNamespaceAndPath(FutureLibMod.MODID, "textures/effect/beam.png");
  public static final RenderType LASER_MAIN_BEAM = create(FutureLibMod.MODID + ":mininglasermainbeam",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, BUFFERSIZE, CRUMBLING, SORT,
      RenderType.CompositeState.builder()
          .setTextureState(new TextureStateShard(BEAM, BLUR, MIPMAP)).setShaderState(RENDERTYPE_TEXT_SHADER)
          .setLayeringState(VIEW_OFFSET_Z_LAYERING)
          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .setDepthTestState(NO_DEPTH_TEST)
          .setCullState(NO_CULL)
          .setLightmapState(NO_LIGHTMAP)
          .setWriteMaskState(COLOR_WRITE)
          .createCompositeState(false));

  public static final RenderType FAKE_BLOCK = create(FutureLibMod.MODID + ":fakeblock",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, BUFFERSIZE, CRUMBLING, SORT,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_SOLID_SHADER) 
          .setLayeringState(POLYGON_OFFSET_LAYERING) 
          .setLightmapState(NO_LIGHTMAP)
          .setTextureState(BLOCK_SHEET_MIPPED)
          .setTransparencyState(ADDITIVE_TRANSPARENCY)
          .setDepthTestState(NO_DEPTH_TEST)
          .setCullState(CULL)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .createCompositeState(false));

  public static final RenderType TRANSPARENT_COLOUR = create(FutureLibMod.MODID + ":transparentcolour",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, BUFFERSIZE, CRUMBLING, SORT,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_LINES_SHADER)
          .setLayeringState(VIEW_OFFSET_Z_LAYERING)
          .setOutputState(ITEM_ENTITY_TARGET)
          .setTransparencyState(ADDITIVE_TRANSPARENCY)
          .setTextureState(NO_TEXTURE)
          .setDepthTestState(NO_DEPTH_TEST)
          .setCullState(CULL)
          .setLightmapState(NO_LIGHTMAP)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .createCompositeState(false));

  public static final RenderType SOLID_COLOUR = create(FutureLibMod.MODID + ":solidcolour",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, BUFFERSIZE, CRUMBLING, SORT,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_LINES_SHADER)
          .setLayeringState(VIEW_OFFSET_Z_LAYERING)
          .setOutputState(ITEM_ENTITY_TARGET)
          .setTransparencyState(ADDITIVE_TRANSPARENCY)
          .setTextureState(NO_TEXTURE)
          .setDepthTestState(NO_DEPTH_TEST)
          .setCullState(CULL)
          .setLightmapState(NO_LIGHTMAP)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .createCompositeState(false));

  public static final RenderType TOMB_LINES = create(FutureLibMod.MODID + ":tomb_lines",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.LINES, BUFFERSIZE, CRUMBLING, SORT,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_LINES_SHADER)
          .setLineState(new LineStateShard(java.util.OptionalDouble.of(2.5D)))
          .setLayeringState(VIEW_OFFSET_Z_LAYERING)
          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .setOutputState(ITEM_ENTITY_TARGET)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .setCullState(NO_CULL)
          .setDepthTestState(NO_DEPTH_TEST)
          .createCompositeState(false));
}
