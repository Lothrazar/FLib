package com.lothrazar.library.core;

import net.neoforged.neoforge.fluids.FluidStack;

public interface IHasFluid {

  public FluidStack getFluid();

  public void setFluid(FluidStack fluid);
}
