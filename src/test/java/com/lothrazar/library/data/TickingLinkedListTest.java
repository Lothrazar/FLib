package com.lothrazar.library.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class TickingLinkedListTest {

  /** Concrete subclass: merging two entries at the same tick concatenates their strings. */
  private static class StringTickingList extends TickingLinkedList<String> {
    @Override
    protected String merge(String o1, String o2) {
      return o2 + o1;
    }
  }

  @Test
  void tickOnEmptyListReturnsNull() {
    StringTickingList list = new StringTickingList();
    assertNull(list.tick());
  }

  @Test
  void singleEntryExpiresAfterExactTickCount() {
    StringTickingList list = new StringTickingList();
    list.add(3, "a");
    assertNull(list.tick());
    assertNull(list.tick());
    assertEquals("a", list.tick());
    assertNull(list.tick());
  }

  @Test
  void entriesReturnedInChronologicalOrder() {
    StringTickingList list = new StringTickingList();
    list.add(5, "late");
    list.add(2, "early");
    assertNull(list.tick());
    assertEquals("early", list.tick());
    assertNull(list.tick());
    assertNull(list.tick());
    assertEquals("late", list.tick());
  }

  @Test
  void laterInsertSortsAfterExistingEntries() {
    StringTickingList list = new StringTickingList();
    list.add(2, "a");
    list.add(4, "b");
    assertNull(list.tick());
    assertEquals("a", list.tick());
    assertNull(list.tick());
    assertEquals("b", list.tick());
  }

  @Test
  void sameTickEntriesAreMerged() {
    StringTickingList list = new StringTickingList();
    list.add(3, "x");
    list.add(3, "y");
    assertNull(list.tick());
    assertNull(list.tick());
    assertEquals("xy", list.tick());
    assertNull(list.tick());
  }
}
