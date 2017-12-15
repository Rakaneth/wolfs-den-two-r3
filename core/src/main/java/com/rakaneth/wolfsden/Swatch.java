package com.rakaneth.wolfsden;

import squidpony.squidgrid.gui.gdx.SColor;

public final class Swatch {
  // char constants
  public static final char   CHAR_WALL     = 0x2588;
  public static final char   CHAR_FLOOR    = 0x00A0;
  public static final char   CHAR_UP       = '<';                        // 0x25E2;
  public static final char   CHAR_DOWN     = '>';                        // 0x25E3;
  public static final char   CHAR_OUT      = 0x25B2;
  public static final char   CHAR_CLOSED   = '+';
  public static final char   CHAR_OPEN     = '\\';
  public static final char   BOX_TOP_LEFT  = 0x250C;
  public static final char   BOX_TOP_RIGHT = 0x2510;
  public static final char   BOX_BOT_LEFT  = 0x2514;
  public static final char   BOX_BOT_RIGHT = 0x2518;
  public static final char   BOX_HORZ      = 0x2500;
  public static final char   BOX_VERT      = 0x2502;
  public static final char   BRIDGE        = 0x2550;

  // color constants
  public static final String INFO          = SColor.LIGHT_BLUE.getName();
  public static final String WARNING       = SColor.AMBER.getName();
  public static final String VIT           = SColor.CRIMSON.getName();
  public static final String ARM           = SColor.SILVER.getName();
  public static final String XP            = SColor.GREEN.getName();

}
