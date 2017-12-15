package com.rakaneth.wolfsden;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import squidpony.squidgrid.mapping.SectionDungeonGenerator;
import squidpony.squidgrid.mapping.SerpentMapGenerator;

public final class MapBuilder {
  private static String            fileName = "data/maps.js";
  private HashMap<String, MapBase> bases;
  public static final MapBuilder   instance = new MapBuilder();
  public HashMap<String, WolfMap>  maps;

  @SuppressWarnings("unchecked")
  private MapBuilder() {
    Json converter = new Json();
    bases = converter.fromJson(HashMap.class, MapBase.class, Gdx.files.internal(fileName));
    maps = new HashMap<String, WolfMap>();
  }

  public WolfMap buildMap(String id) {
    MapBase base = bases.get(id);
    SerpentMapGenerator smg = new SerpentMapGenerator(base.width, base.height, WolfGame.rng(), 0.2);
    SectionDungeonGenerator decorator = new SectionDungeonGenerator(base.width, base.height, WolfGame.rng());
    char[][] charMap, rawMap;
    smg.putBoxRoomCarvers(base.boxCarvers);
    smg.putRoundRoomCarvers(base.roundCarvers);
    smg.putCaveCarvers(base.caveCarvers);
    rawMap = smg.generate();
    decorator.addDoors(base.doors, base.doubleDoors);
    decorator.addLake(base.water);
    charMap = decorator.generate(rawMap, smg.getEnvironment());
    WolfMap raw = new WolfMap(charMap, id, base.dark, base.name);
    if (base.up != null)
      raw.makeUpStair(decorator.stairsUp);
    if (base.down != null)
      raw.makeDownStair(decorator.stairsDown);
    if (base.out != null)
      raw.makeOutStair(decorator.stairsUp);
    maps.put(id, raw);
    return raw;
  }

  public void buildAll() {

  }

  public WolfMap getMap(String mapID) {
    return maps.get(mapID);
  }

  public MapBase getBase(String mapID) {
    return bases.get(mapID);
  }

  public Set<String> allMapIDs() {
    return bases.keySet();
  }

  public static class MapBase {
    public int     width;
    public int     height;
    public boolean dark;
    public int     caveCarvers;
    public int     boxCarvers;
    public int     roundCarvers;
    public int     doors;
    public int     water;
    public boolean doubleDoors;
    public String  up;
    public String  down;
    public String  out;
    public String  name;
  }

  public List<WolfMap> allMaps() {
    return maps.values()
               .stream()
               .collect(Collectors.toList());
  }
}
