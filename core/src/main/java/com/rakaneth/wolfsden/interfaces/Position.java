package com.rakaneth.wolfsden.interfaces;

import com.rakaneth.wolfsden.MapBuilder;
import com.rakaneth.wolfsden.WolfMap;

import squidpony.squidmath.Coord;

public interface Position {
  int X();

  int Y();

  String mapID();
  
  Coord getPos();

  default WolfMap map() {
    return MapBuilder.instance.getMap(mapID());
  }
}
