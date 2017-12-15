package com.rakaneth.wolfsden;

import squidpony.squidmath.Dice;

public final class StepDice {
  private static final String[] steps =
      new String[] { "0", "!4-2", "!4-1", "!4", "!6", "!8", "!10", "!6+!4", "!6+!6", "!6+!8", "!6+!10", "!8+!10" };
  private static Dice           dice  = new Dice(WolfGame.rng());

  public static final String getDiceRoll(int step) {
    if (step <= 0)
      return steps[0];
    else if (WolfUtils.between(step, 1, 11))
      return steps[step];
    else {
      String remainder = steps[(step % 6) + 6];
      int d10s = (step / 6) - 1;
      return String.valueOf(d10s) + "!10+" + remainder;
    }
  }

  public static final int roll(int step) {
    return dice.roll(getDiceRoll(step));
  }

  public static final int roll(int step, int diff) {
    int raw = roll(step);
    int result = raw - diff;
    return result >= 0 ? result / 5 + 1 : 0;
  }
}
