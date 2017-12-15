package com.rakaneth.wolfsden.screens;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rakaneth.wolfsden.MapBuilder;
import com.rakaneth.wolfsden.MapBuilder.MapBase;
import com.rakaneth.wolfsden.Swatch;
import com.rakaneth.wolfsden.WolfGame;
import com.rakaneth.wolfsden.WolfMap;

import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SquidInput;
import squidpony.squidgrid.gui.gdx.SquidPanel;

public class LoadScreen extends WolfScreen {

  private SquidPanel                     display;
  private Thread                         mapThread;
  private ConcurrentLinkedQueue<Integer> mapQueue;
  private int                            mapPct;
  private AssetManager                   am;
  private int                            tasks     = 0;
  private int                            doneTasks = 0;
  public static LoadScreen               instance  = new LoadScreen();

  private LoadScreen() {
    super("loading");
    vport = new StretchViewport(120 * cellWidth, 40 * cellHeight);
    stage = new Stage(vport, batch);
    display = new SquidPanel(100, 25, DefaultResources.getSlabFamily());
    initText(display.getTextCellFactory());
    display.setPosition(10 * cellWidth, 0);
    input = new SquidInput((key, alt, ctrl, shift) -> {
      if (key == SquidInput.ENTER) {
        WolfGame.setScreen(PlayScreen.instance);
      }
    });
    setInput();
    stage.addActor(display);
    mapQueue = new ConcurrentLinkedQueue<>();
    mapPct = 0;
    am = new AssetManager();
    loadMusic("059-Trollocs", "125-Judgment", "131-Boss Battle 1", "127-Polonaise, two pianos");
  };

  private void loadMusic(String... fileNames) {
    for (String fileName : fileNames) {
      am.load("music/" + fileName + ".mp3", Music.class);
    }
  }

  public Music getMusic(String fileName) {
    return am.get("music/" + fileName + ".mp3", Music.class);
  }

  private void drawBar() {
    display.erase();
    display.put(10, 14, String.format("Loading maps...%d%%", mapPct), SColor.WHITE);
    display.put(10, 11, String.format("Loading music...%.0f%%", am.getProgress() * 100f));
    Integer tryGet = mapQueue.poll();
    if (tryGet != null)
      mapPct = tryGet;
    for (int x = 0; x < 100; x++)
      display.put(x + 10, 15, Swatch.CHAR_WALL, SColor.DARK_BLUE);
    for (int y = 0; y < mapPct; y++)
      display.put(y + 10, 15, Swatch.CHAR_WALL, SColor.LIGHT_BLUE);
    for (int mx = 0; mx < 100; mx++)
      display.put(mx + 10, 12, Swatch.CHAR_WALL, SColor.DARK_BROWN);
    for (int my = 0; my < am.getProgress() * 100; my++)
      display.put(my + 10, 12, Swatch.CHAR_WALL, SColor.CW_LIGHT_BROWN);
    if (!mapThread.isAlive() && am.update()) {
      display.put(10, 16, "Press [Enter] to continue", SColor.WHITE);
      if (input.hasNext())
        input.next();
    }
  }

  @Override
  public void enter() {
    super.enter();
    mapThread = new Thread(() -> {
      MapBuilder mb = MapBuilder.instance;
      Set<String> mapIDs = mb.allMapIDs();
      int maxMaps = mapIDs.size();
      tasks += maxMaps;
      for (String id : mapIDs) {
        MapBase info = mb.getBase(id);
        WolfMap curMap, upMap, downMap, outMap;
        curMap = mb.getMap(id);
        if (curMap == null)
          curMap = mb.buildMap(id);

        if (info.up != null) {
          upMap = mb.getMap(info.up);
          if (upMap == null)
            upMap = mb.buildMap(info.up);
          curMap.connect(curMap.stairsUp, upMap.stairsDown, upMap);
        }

        if (info.down != null) {
          downMap = mb.getMap(info.down);
          if (downMap == null)
            downMap = mb.buildMap(info.down);
          curMap.connect(curMap.stairsDown, downMap.stairsUp, downMap);
        }

        if (info.out != null) {
          outMap = mb.getMap(info.out);
          if (outMap == null)
            outMap = mb.buildMap(info.out);
          curMap.connect(curMap.stairsOut, outMap.getEmpty(), outMap);
        }
        mapQueue.add(++doneTasks * 100 / tasks);
      }
    });
    mapThread.start();

  }

  public AssetManager am() {
    return am;
  }

  @Override
  public void render() {
    drawBar();
    stage.act();
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    // TODO Auto-generated method stub

  }

}
