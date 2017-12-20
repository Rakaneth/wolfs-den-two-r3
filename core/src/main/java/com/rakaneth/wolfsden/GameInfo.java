package com.rakaneth.wolfsden;

import com.rakaneth.wolfsden.entities.Creature;
import com.rakaneth.wolfsden.entities.CreatureManager;

public final class GameInfo {
  private WolfMap curMap;
  private Creature player;
  public static GameInfo instance = new GameInfo();
  
  private GameInfo() {}
  
  public void setMap(WolfMap map) {
    curMap = map;
  }
  
  public void setPlayer(Creature _player) {
    player = _player;
  }
  
  public WolfMap getMap() {
    return curMap;
  }
  
  public Creature player() {
    return player;
  }
}
