package com.lothrazar.library.render.type;

import java.util.OptionalDouble;
import com.lothrazar.library.FutureLibMod;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

/**
 * Render Types with help from direwolf20 MIT open source project https://github.com/Direwolf20-MC/BuildingGadgets/blob/1.15/LICENSE.md
 */
public class FakeBlockRenderTypes extends RenderType {

  private static final boolean MIPMAP = false;
  private static final boolean BLUR = false;
  private static final boolean SORT = false;
  private static final boolean CRUMBLING = false;
  private static final int BUFFERSIZE = 256;

  public FakeBlockRenderTypes(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
    super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
  }

  // ADDITIVE_TRANSPARENCY was removed from RenderStateShard in 1.21.1; define it locally
  private static final TransparencyStateShard ADDITIVE_TRANSPARENCY_SHARD = new TransparencyStateShard(
      "additive_transparency",
      () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
      },
      RenderSystem::disableBlend);

  public final static ResourceLocation BEAM = ResourceLocation.fromNamespaceAndPath(FutureLibMod.MODID, "textures/effect/beam.png");

  public static final RenderType LASER_MAIN_BEAM = create(FutureLibMod.MODID + ":mininglasermainbeam",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, BUFFERSIZE, CRUMBLING, SORT,
      RenderType.CompositeState.builder()
          .setTextureState(new TextureStateShard(BEAM, BLUR, MIPMAP))
          .setShaderState(RENDERTYPE_TEXT_SHADER) // was POSITION_COLOR_TEX_SHADER
          .setLayeringState(VIEW_OFFSET_Z_LAYERING)
          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .setDepthTestState(NO_DEPTH_TEST)
          .setCullState(NO_CULL)
          .setLightmapState(NO_LIGHTMAP)
          .setWriteMaskState(COLOR_WRITE)
          .createCompositeState(false));
  /**
   * used by TESR that render blocks with textures Shape builder, ghostsoundmuffler, render light camo.
   *
   */
  public static final RenderType FAKE_BLOCK = create(FutureLibMod.MODID + ":fakeblock",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, BUFFERSIZE, CRUMBLING, SORT,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_SOLID_SHADER) //1.17 was -   BLOCK_SHADER
          .setLayeringState(POLYGON_OFFSET_LAYERING) // VIEW_OFFSET_Z_LAYERING)
          .setLightmapState(NO_LIGHTMAP)
          .setTextureState(BLOCK_SHEET_MIPPED)
          .setTransparencyState(ADDITIVE_TRANSPARENCY_SHARD)
          .setDepthTestState(NO_DEPTH_TEST)
          .setCullState(CULL)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .createCompositeState(false));
  /**
   * used by EventRender -> RenderWorldLastEvent by most held items that pick locations, such as cyclic:location_data
   */
  public static final RenderType TRANSPARENT_COLOUR = create(FutureLibMod.MODID + ":transparentcolour",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, BUFFERSIZE, CRUMBLING, SORT,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_LINES_SHADER)
          .setLayeringState(VIEW_OFFSET_Z_LAYERING)
          .setOutputState(ITEM_ENTITY_TARGET)
          .setTransparencyState(ADDITIVE_TRANSPARENCY_SHARD)
          .setTextureState(NO_TEXTURE)
          .setDepthTestState(NO_DEPTH_TEST)
          .setCullState(CULL)
          .setLightmapState(NO_LIGHTMAP)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .createCompositeState(false));
  /**
   * used by most blocks that select blocks such as cyclic:forester, cyclic:harvester, cyclic:miner in TESRs
   *
   *
   */
  public static final RenderType SOLID_COLOUR = create(FutureLibMod.MODID + ":solidcolour",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, BUFFERSIZE, CRUMBLING, SORT,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_LINES_SHADER)
          .setLayeringState(VIEW_OFFSET_Z_LAYERING)
          .setOutputState(ITEM_ENTITY_TARGET)
          .setTransparencyState(ADDITIVE_TRANSPARENCY_SHARD)
          .setTextureState(NO_TEXTURE)
          .setDepthTestState(NO_DEPTH_TEST)
          .setCullState(CULL)
          .setLightmapState(NO_LIGHTMAP)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .createCompositeState(false));
  /**
   * Used by cyclic:prospector
   */
  public static final RenderType TOMB_LINES = create(FutureLibMod.MODID + ":tomb_lines",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.LINES, BUFFERSIZE, CRUMBLING, SORT,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_LINES_SHADER)
          .setLineState(new LineStateShard(OptionalDouble.of(2.5D)))
          .setLayeringState(VIEW_OFFSET_Z_LAYERING)
          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .setOutputState(ITEM_ENTITY_TARGET)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .setCullState(NO_CULL)
          .setDepthTestState(NO_DEPTH_TEST)
          .createCompositeState(false));
}
