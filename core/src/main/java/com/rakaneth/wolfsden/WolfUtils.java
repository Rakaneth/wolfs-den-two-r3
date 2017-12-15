package com.rakaneth.wolfsden;

import com.badlogic.gdx.ai.GdxAI;

public final class WolfUtils {
  private static boolean debug = true;

  public static final void setDebug(boolean _debug) {
    log("Debug", "Log messages have been set to %s", _debug);
    debug = _debug;
  }

  public static final void log(String tag, String template, Object... objects) {
    if (debug) {
      String fmtted = String.format(template, objects);
      GdxAI.getLogger()
           .info(tag, fmtted);
    }
  }

  public static final <T extends Comparable<? super T>> boolean between(T n, T lower, T upper) {
    if (n.compareTo(lower) < 0)
      return false;
    else if (n.compareTo(upper) > 0)
      return false;
    else
      return true;
  }

  public static final <T> T ifNull(T val, T dFault) {
    return (val == null) ? dFault : val;
  }
}
