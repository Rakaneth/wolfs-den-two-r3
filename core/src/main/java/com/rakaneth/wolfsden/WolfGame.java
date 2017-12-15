package com.rakaneth.wolfsden;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rakaneth.wolfsden.screens.WolfScreen;

import squidpony.FakeLanguageGen;
import squidpony.NaturalLanguageCipher;
import squidpony.StringKit;
import squidpony.squidai.DijkstraMap;
import squidpony.squidgrid.Direction;
import squidpony.squidgrid.FOV;
import squidpony.squidgrid.Radius;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.MapUtility;
import squidpony.squidgrid.gui.gdx.PanelEffect;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;
import squidpony.squidgrid.gui.gdx.SquidMouse;
import squidpony.squidgrid.gui.gdx.TextCellFactory;
import squidpony.squidgrid.mapping.DungeonGenerator;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidmath.Coord;
import squidpony.squidmath.GreasedRegion;
import squidpony.squidmath.RNG;
import squidpony.squidmath.SeededNoise;
import squidpony.squidmath.StatefulRNG;

import java.util.ArrayList;
import java.util.List;

public class WolfGame extends ApplicationAdapter {

  private static final RNG rng = new StatefulRNG(0xDEADBEEF);
  private WolfScreen screen;
  
  @Override
  public void create() {
    
  }

  @Override
  public void render() {

  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);

  }
  
  public static RNG rng() {
    return rng;
  }
  
  public void setScreen(WolfScreen _screen) {
    if (screen != null)
      screen.exit();
    screen = _screen;
    screen.enter();
  }
}
