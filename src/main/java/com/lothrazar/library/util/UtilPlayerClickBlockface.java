package com.lothrazar.library.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

/**
 * Copyright (c) 2015 Vorquel (modified by Lothrazar 2016-2023)
 * 
 * This software is provided 'as-is', without any express or implied warranty. In no event will the authors be held liable for any damages arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose, including commercial applications, and to alter it and redistribute it freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not claim that you wrote the original software. If you use this software in a product, an acknowledgment in the product
 * documentation would be appreciated but is not required.
 * 
 * 2. Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.
 * 
 * 3. This notice may not be removed or altered from any source distribution.
 *
 * 
 */
public class UtilPlayerClickBlockface {

  private static final int[] sidesXY = new int[] { 4, 5, 0, 1 };
  private static final int[] sidesYZ = new int[] { 0, 1, 2, 3 };
  private static final int[] sidesZX = new int[] { 2, 3, 4, 5 };
  public final static float LO = 0.25F;
  public final static float HI = 1 - LO;

  /**
   * User clicks on a block, on the 1x1 face. where is it in the face. is it in the center? then face away to opposide side
   * 
   * is near the upper edge of the 1x1, or east or west etc. relatively . return the appropriate direction
   * 
   * @param clickedFace
   * @param clickLocation
   * @param pos
   * @return
   */
  public static Direction getClickLocationDirection(Direction clickedFace, Vec3 clickLocation, BlockPos pos) {
    int side = clickedFace.ordinal();
    double xIn = clickLocation.x - pos.getX(),
        yIn = clickLocation.y - pos.getY(),
        zIn = clickLocation.z - pos.getZ();
    //if the middle was clicked, place on the opposite side
    int centeredSides = 0;
    if (side != 0 && side != 1) {
      centeredSides += yIn > LO && yIn < HI ? 1 : 0;
    }
    if (side != 2 && side != 3) {
      centeredSides += zIn > LO && zIn < HI ? 1 : 0;
    }
    if (side != 4 && side != 5) {
      centeredSides += xIn > LO && xIn < HI ? 1 : 0;
    }
    if (centeredSides == 2) {
      return Direction.values()[side].getOpposite();
    }
    //otherwise, place on the nearest side
    double left, right;
    int[] sides;
    switch (clickedFace) {
      case DOWN:
      case UP:
        left = zIn;
        right = xIn;
        sides = sidesZX;
      break;
      case NORTH:
      case SOUTH:
        left = xIn;
        right = yIn;
        sides = sidesXY;
      break;
      case WEST:
      case EAST:
        left = yIn;
        right = zIn;
        sides = sidesYZ;
      break;
      default:
        return Direction.UP;
    }
    //    SimilsaxTranstructors.log.info("{} :: {}, are the left/rights ", left, right);
    double cutoff = LO;
    boolean leftCorner = left < cutoff || left > 1 - cutoff;
    boolean rightCorner = right < cutoff || right > 1 - cutoff;
    if (leftCorner && rightCorner) {
      return null;
    }
    boolean b0 = left > right;
    boolean b1 = left > 1 - right;
    int result = 0;
    if (b0 && b1) {
      result = sides[0];
    }
    else if (!b0 && !b1) {
      result = sides[1];
    }
    else if (b1) {
      result = sides[2];
    }
    else {
      result = sides[3];
    }
    return Direction.values()[result].getOpposite();
  }
}
