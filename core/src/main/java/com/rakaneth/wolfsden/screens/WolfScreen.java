package com.rakaneth.wolfsden.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rakaneth.wolfsden.WolfUtils;

import squidpony.panel.IColoredString;
import squidpony.squidgrid.gui.gdx.GDXMarkup;
import squidpony.squidgrid.gui.gdx.SquidInput;
import squidpony.squidgrid.gui.gdx.TextCellFactory;

public abstract class WolfScreen {
  protected static final SpriteBatch batch       = new SpriteBatch();
  protected final int                cellWidth   = 12;
  protected final int                cellHeight  = 18;
  protected final float              tweakWidth  = cellWidth * 1.1f;
  protected final float              tweakHeight = cellHeight * 1.1f;
  protected String                   name;
  protected SquidInput               input;
  protected StretchViewport          vport;
  protected Stage                    stage;

  protected WolfScreen(String _name) {
    name = _name;
  }

  public void enter() {
    WolfUtils.log("Screen", "Entered %s screen", name);
    setInput();
  }

  public void exit() {
    WolfUtils.log("Screen", "Exited %s screen", name);
  }

  protected int pixelWidth(int gridDim) {
    return gridDim * cellWidth;
  }

  protected int pixelHeight(int gridDim) {
    return gridDim * cellHeight;
  }

  protected IColoredString<Color> toICString(String markupStringTemplate) {
    return GDXMarkup.instance.colorString(markupStringTemplate);
  }

  protected void initText(TextCellFactory tcf) {
    tcf.width(cellWidth)
       .height(cellHeight)
       .tweakWidth(tweakWidth)
       .tweakHeight(tweakHeight)
       .initBySize();
  }

  protected void setInput() {
    Gdx.input.setInputProcessor(new InputMultiplexer(stage, input));
  }
  
  abstract public void render();

  abstract public void resize(int width, int height);
}
