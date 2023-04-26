package com.lothrazar.library.data;
/**
 * @author https://github.com/RedRelay/ForgeCreeperHeal https://github.com/Lothrazar/CreeperHeal
 *
 * 
 */
public class TickContainer<T> extends TickComparable {

  private T data;

  public TickContainer(int tick, T data) {
    super(tick);
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
