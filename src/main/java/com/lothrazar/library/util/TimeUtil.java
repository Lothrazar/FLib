package com.lothrazar.library.util;

import net.minecraft.world.level.Level;

public class TimeUtil {

  public static final String TIME_FORMAT = "HH:mm:ss";
  public static final String DATE_FORMAT = "yyyy/MM/dd";
  // https://minecraft.fandom.com/wiki/Daylight_cycle
  public static final int TICKS_PER_DAY = 24000;
  public static final int TICKS_PER_HOUR = 1000;
  //hour 18 is midnight in MC (zero is 6:00AM)
  public static final int MIDNIGHT = 18;

  /**
   * Passing in 2 will mean a true value if the hour of the night is within [16,20] hours. since 18 is midnight
   * 
   * @param level
   * @param hourRange
   * @return withinRange
   */
  public static boolean isWithinHoursOfMidnight(Level level, int hourRange) {
    final int hour = getHourOfDay(level);
    return MIDNIGHT - hourRange < hour && hour < MIDNIGHT + hourRange;
  }

  /**
   * from 0 to 24, where 0 is the start of the minecraft in-world day (6am)
   * 
   * @param level
   * @return hour
   */
  public static int getHourOfDay(Level level) {
    return ((int) level.getDayTime()) / TimeUtil.TICKS_PER_HOUR;
  }

  public static boolean isNight(Level level) {
    float angle = level.getSunAngle(0.0F);
    return angle >= 0.245F && angle <= 0.755F;
  }
}
