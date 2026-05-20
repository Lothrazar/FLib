package com.lothrazar.library.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import net.minecraft.resources.ResourceLocation;

class StringParseUtilTest {

  @Test
  void matchesByNamespace() {
    assertTrue(StringParseUtil.isInList(List.of("minecraft"), ResourceLocation.parse("minecraft:stone")));
  }

  @Test
  void wildcardMatchesSuffixWithinNamespace() {
    assertTrue(StringParseUtil.isInList(List.of("hc:*_sapling"), ResourceLocation.parse("hc:oak_sapling")));
  }

  @Test
  void wildcardDoesNotMatchAcrossNamespaces() {
    assertFalse(StringParseUtil.isInList(List.of("hc:*_sapling"), ResourceLocation.parse("minecraft:oak_sapling")));
  }

  @Test
  void nullArgumentsReturnFalse() {
    assertFalse(StringParseUtil.isInList(null, ResourceLocation.parse("a:b")));
    assertFalse(StringParseUtil.isInList(List.of("a"), null));
  }

  @Test
  void emptyAndNullEntriesAreSkipped() {
    assertTrue(StringParseUtil.isInList(Arrays.asList("", null, "minecraft"), ResourceLocation.parse("minecraft:dirt")));
  }

  @Test
  void noWildcardModeMatchesNamespaceOnly() {
    assertTrue(StringParseUtil.isInList(List.of("minecraft"), ResourceLocation.parse("minecraft:dirt"), false));
    assertFalse(StringParseUtil.isInList(List.of("minecraft:dirt"), ResourceLocation.parse("minecraft:dirt"), false));
  }
}
