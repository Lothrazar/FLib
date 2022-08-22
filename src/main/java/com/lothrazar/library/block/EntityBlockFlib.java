package com.lothrazar.library.block;
public abstract class EntityBlockFlib extends BlockFlib {

  public EntityBlockFlib(Properties prop, BlockFlib.Settings custom) {
    super(prop, custom);
  }

  public EntityBlockFlib(Properties prop) {
    this(prop, new BlockFlib.Settings());
  }
}
