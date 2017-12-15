package com.rakaneth.wolfsden;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.rakaneth.wolfsden.screens.LoadScreen;
import com.rakaneth.wolfsden.screens.WolfScreen;

import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidmath.RNG;
import squidpony.squidmath.StatefulRNG;

public class WolfGame extends ApplicationAdapter {

  private static final RNG rng = new StatefulRNG(0xDEADBEEF);
  private static WolfScreen screen;
  private final SColor bgColor = SColor.DARK_SLATE_GRAY;
  
  @Override
  public void create() {
    setScreen(LoadScreen.instance);
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(bgColor .r / 255.0f, bgColor.g / 255.0f, bgColor.b / 255.0f, 1.0f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    screen.render();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);

  }
  
  public static RNG rng() {
    return rng;
  }
  
  public static void setScreen(WolfScreen _screen) {
    if (screen != null)
      screen.exit();
    screen = _screen;
    screen.enter();
  }
}
