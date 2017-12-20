package com.rakaneth.wolfsden.entities;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.math.MathUtils;
import com.rakaneth.wolfsden.interfaces.Drawable;
import com.rakaneth.wolfsden.interfaces.Position;

import squidpony.squidmath.Coord;

public class Creature implements Drawable, Position {
  // ID
  String                 id;
  String                 name;
  String                 desc;

  // Position
  int                    x;
  int                    y;
  String                 mapID;

  // Drawing
  char                   glyph;
  String                 color;

  // Vitals
  int                    curVit;
  int                    maxVit;
  int                    curEnd;
  int                    maxEnd;
  float                  curXP;
  float                  totXP;
  boolean                alive = true;

  // Primary stats
  int                    str;
  int                    stam;
  int                    spd;
  int                    skl;

  // AI
  String                 bPath;
  double[][]             visible;
  double                 vision;
  BehaviorTree<Creature> btree;

  public void heal() {
    curVit = maxVit;
  }

  public void refresh() {
    curEnd = maxEnd;
  }

  public void changeVit(int amt) {
    curVit = Math.min(curVit + amt, maxVit);
    if (curVit <= 0)
      alive = false;
  }

  public void changeEnd(int amt) {
    curEnd = MathUtils.clamp(curEnd + amt, 0, maxEnd);
  }

  public BehaviorTree<Creature> btree() {
    return btree;
  }

  public void updateAI() {
    btree.step();
  }

  @Override
  public String colorString() {
    return color;
  }

  @Override
  public char glyph() {
    return glyph;
  }

  @Override
  public int X() {
    return x;
  }

  @Override
  public int Y() {
    return y;
  }

  @Override
  public String mapID() {
    return mapID;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Coord getPos() {
    return Coord.get(x, y);
  }

}
