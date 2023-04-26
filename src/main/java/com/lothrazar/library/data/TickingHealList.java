package com.lothrazar.library.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author https://github.com/RedRelay/ForgeCreeperHeal https://github.com/Lothrazar/CreeperHeal
 *
 * 
 */
public class TickingHealList extends TickingLinkedList<Collection<BlockStatePosWrapper>> {

  @Override
  protected Collection<BlockStatePosWrapper> merge(Collection<BlockStatePosWrapper> o1, Collection<BlockStatePosWrapper> o2) {
    o2.addAll(o1);
    return o2;
  }

  public LinkedList<TickContainer<Collection<BlockStatePosWrapper>>> getLinkedList() {
    return list;
  }

  public void add(int tick, BlockStatePosWrapper data) {
    Collection<BlockStatePosWrapper> c = new ArrayList<BlockStatePosWrapper>(1);
    c.add(data);
    add(tick, c);
  }
}
