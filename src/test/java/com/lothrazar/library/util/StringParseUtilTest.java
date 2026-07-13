package com.lothrazar.library.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import net.minecraft.resources.Identifier;

class StringParseUtilTest {

  @Test
  void matchesByNamespace() {
    assertTrue(StringParseUtil.isInList(List.of("minecraft"), Identifier.parse("minecraft:stone")));
  }

  @Test
  void wildcardMatchesSuffixWithinNamespace() {
    assertTrue(StringParseUtil.isInList(List.of("hc:*_sapling"), Identifier.parse("hc:oak_sapling")));
  }

  @Test
  void wildcardDoesNotMatchAcrossNamespaces() {
    assertFalse(StringParseUtil.isInList(List.of("hc:*_sapling"), Identifier.parse("minecraft:oak_sapling")));
  }

  @Test
  void nullArgumentsReturnFalse() {
    assertFalse(StringParseUtil.isInList(null, Identifier.parse("a:b")));
    assertFalse(StringParseUtil.isInList(List.of("a"), null));
  }

  @Test
  void emptyAndNullEntriesAreSkipped() {
    assertTrue(StringParseUtil.isInList(Arrays.asList("", null, "minecraft"), Identifier.parse("minecraft:dirt")));
  }

  @Test
  void noWildcardModeMatchesNamespaceOnly() {
    assertTrue(StringParseUtil.isInList(List.of("minecraft"), Identifier.parse("minecraft:dirt"), false));
    assertFalse(StringParseUtil.isInList(List.of("minecraft:dirt"), Identifier.parse("minecraft:dirt"), false));
  }
}
