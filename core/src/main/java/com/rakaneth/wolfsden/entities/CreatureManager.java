package com.rakaneth.wolfsden.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibraryManager;
import com.badlogic.gdx.utils.Json;
import com.rakaneth.wolfsden.GameInfo;
import com.rakaneth.wolfsden.MapBuilder;
import com.rakaneth.wolfsden.WolfMap;
import com.rakaneth.wolfsden.WolfUtils;

import squidpony.squidgrid.FOV;
import squidpony.squidmath.Coord;

public class CreatureManager {
  private static final String           fileName  = "data/creatures.js";
  private HashMap<String, CreatureBase> creatureBases;
  private HashMap<String, Creature>     creatures = new HashMap<>();
  private Creature                      foetus;
  private int                           counter   = 1;
  private MapBuilder                    mb        = MapBuilder.instance;
  private BehaviorTreeLibraryManager    btm       = BehaviorTreeLibraryManager.getInstance();
  public static final CreatureManager   instance  = new CreatureManager();

  @SuppressWarnings("unchecked")
  private CreatureManager() {
    Json converter = new Json();
    creatureBases = converter.fromJson(HashMap.class, CreatureBase.class, Gdx.files.internal(fileName));
  }

  public CreatureManager start() {
    foetus = new Creature();
    return this;
  }

  public CreatureManager setID(String id) {
    foetus.id = id + String.valueOf(counter);
    return this;
  }

  public CreatureManager setDesc(String desc) {
    foetus.desc = desc;
    return this;
  }

  public CreatureManager setName(String name) {
    foetus.name = name;
    return this;
  }

  public CreatureManager setPos(int x, int y) {
    foetus.x = x;
    foetus.y = y;
    return this;
  }

  public CreatureManager setMapID(String mapID) {
    foetus.mapID = mapID;
    return this;
  }

  public CreatureManager setGlyph(char glyph) {
    foetus.glyph = glyph;
    return this;
  }

  public CreatureManager setColor(String color) {
    foetus.color = color;
    return this;
  }

  public CreatureManager setStats(int str, int stam, int spd, int skl) {
    foetus.str = str;
    foetus.stam = stam;
    foetus.spd = spd;
    foetus.skl = skl;
    return this;
  }

  public CreatureManager setVision(double vision) {
    foetus.vision = vision;
    return this;
  }

  public CreatureManager setBTree(String btree) {
    foetus.bPath = "ai/" + btree + ".tree";
    return this;
  }

  public Creature build() {
    WolfMap map = foetus.map();
    foetus.visible = new double[map.getWidth()][map.getHeight()];
    FOV.reuseFOV(map.resistanceMap, foetus.visible, foetus.x, foetus.y, foetus.vision);
    foetus.maxEnd = foetus.stam * 15;
    foetus.maxVit = foetus.stam * 10;
    foetus.curXP = 0;
    foetus.totXP = 0;
    foetus.heal();
    foetus.refresh();
    //foetus.btree = btm.createBehaviorTree(foetus.bPath, foetus);
    counter++;
    return foetus;
  }

  public Creature build(String id, String mapID, Coord _start) {
    CreatureBase base = creatureBases.get(id);
    if (base == null) {
      WolfUtils.log("CreatureBuilder", "Attempt to build nonexistent creature %s", id);
      return null;
    }
    String name = WolfUtils.ifNull(base.name, "No Name");
    String desc = WolfUtils.ifNull(base.desc, "No Desc");
    String ai = WolfUtils.ifNull(base.ai, "hunt");
    Coord start = WolfUtils.ifNull(_start, mb.getMap(mapID)
                                             .getEmpty());
    Creature newCreature = this.start()
                               .setID(id)
                               .setName(name)
                               .setDesc(desc)
                               .setPos(start.x, start.y)
                               .setMapID(mapID)
                               .setColor(base.color)
                               .setGlyph(base.glyph)
                               .setVision(base.vision)
                               .setStats(base.str, base.stam, base.spd, base.skl)
                               .setBTree(ai)
                               .build();
    creatures.put(id, newCreature);
    return newCreature;
  }

  public Creature buildPlayer(String classID, String mapID) {
    Creature newPlayer = build(classID, mapID, null);
    newPlayer.btree = null;
    creatures.remove(newPlayer.id);
    newPlayer.id = "player";
    creatures.put("player", newPlayer);
    GameInfo.instance.setPlayer(newPlayer);
    return newPlayer;
  }
  
  public void buildPlayer(String classID, WolfMap map) {
    buildPlayer(classID, map.id);
  }
  
  public Creature getByID(String id) {
    return creatures.get(id);
  }
  
  public Collection<Creature> allCreatures() {
    return creatures.values();
  }

  private static class CreatureBase {
    public String            name;
    public int               str;
    public int               stam;
    public int               spd;
    public int               skl;
    public char              glyph;
    public String            color;
    public String            desc;
    public String            mh;
    public String            oh;
    public String            armor;
    public String            trinket;
    public ArrayList<String> factions;
    public String            ai;
    public double            vision;
  }


}
