package com.rakaneth.wolfsden.interfaces;

import com.badlogic.gdx.graphics.Colors;

import squidpony.squidgrid.gui.gdx.SColor;

public interface Drawable {
  String colorString();
  String name();
  default SColor color() {
    return (SColor)Colors.get(colorString());
  }
  char glyph();
  default float colorFloat() {
    return color().toFloatBits();
  }
  default String markupString() {
    return String.format("[%s]%s[]", colorString(), name());
  }
}
