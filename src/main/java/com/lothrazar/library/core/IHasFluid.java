package com.lothrazar.library.core;

import net.minecraftforge.fluids.FluidStack;

public interface IHasFluid {

  public FluidStack getFluid();

  public void setFluid(FluidStack fluid);
}
