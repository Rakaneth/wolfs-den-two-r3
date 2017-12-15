package com.rakaneth.wolfsden.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rakaneth.wolfsden.StepDice;
import com.rakaneth.wolfsden.WolfUtils;

import squidpony.panel.IColoredString;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.GDXMarkup;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;
import squidpony.squidgrid.gui.gdx.SquidMessageBox;
import squidpony.squidgrid.gui.gdx.SquidPanel;
import squidpony.squidgrid.gui.gdx.TextCellFactory;

public class PlayScreen extends WolfScreen {
  private SparseLayers     display;
  private SquidMessageBox  messages;
  private SquidPanel       stats, info, equipment, skills;
  private final int        mapW     = 80;
  private final int        mapH     = 30;
  private final int        msgW     = 43;
  private final int        msgH     = 10;
  private final int        statW    = 37;
  private final int        statH    = 10;
  private final int        infoW    = 40;
  private final int        infoH    = 10;
  private final int        equipW   = 40;
  private final int        equipH   = 6;
  private final int        skW      = 40;
  private final int        skH      = 24;
  private final int        fullW    = 120;
  private final int        fullH    = 40;
  public static PlayScreen instance = new PlayScreen();
  

  private PlayScreen() {
    super("title");
    vport = new StretchViewport(pixelWidth(fullW), pixelHeight(fullH));
    stage = new Stage(vport, batch);
    TextCellFactory slab = DefaultResources.getSlabFamily();
    TextCellFactory stretch = DefaultResources.getStretchableSquareFont();
    initText(slab);
    initText(stretch);

    display = new SparseLayers(mapW, mapH, cellWidth, cellHeight, stretch.copy());
    display.setBounds(0, pixelHeight(msgH), pixelWidth(mapW), pixelHeight(mapH));

    stage.addActor(display);

    messages = new SquidMessageBox(msgW, msgH, slab.copy());
    messages.setBounds(0, 0, pixelWidth(msgW), pixelHeight(msgH));
    stage.addActor(messages);

    stats = new SquidPanel(statW, statH, slab.copy());
    stats.setBounds(pixelWidth(msgW), 0, pixelWidth(statW), pixelHeight(statH));
    stage.addActor(stats);

    info = new SquidPanel(infoW, infoH, slab.copy());
    info.setBounds(pixelWidth(msgW + statW), 0, pixelWidth(infoW), pixelHeight(infoH));
    stage.addActor(info);

    equipment = new SquidPanel(equipW, equipH, slab.copy());
    equipment.setBounds(pixelWidth(mapW), pixelHeight(skH + infoH), pixelWidth(equipW), pixelHeight(equipH));
    stage.addActor(equipment);

    skills = new SquidPanel(skW, skH, slab.copy());
    skills.setBounds(pixelWidth(mapW), pixelHeight(infoH), pixelWidth(skW), pixelHeight(skH));
    stage.addActor(skills);

    input = new SquidInput((char key, boolean alt, boolean ctrl, boolean shift) -> {
      
    });
    setInput();
  }

  public void drawHUDSkeleton() {
    float FW = SColor.FLOAT_WHITE;
    messages.erase();
    equipment.erase();
    info.erase();
    skills.erase();
    stats.erase();

    messages.putBorders(FW, "Messages");
    equipment.putBorders(FW, "Equipment");
    info.putBorders(FW, "Information");
    skills.putBorders(FW, "Skills");
    stats.putBorders(FW, "Stats");

    // TODO: rest of HUD
  }

  // Testing functions
  private void testRawRoll(int step) {
    WolfUtils.log("Dice", "Roll of step %d (%s): %d", step, StepDice.getDiceRoll(step), StepDice.roll(step));
  }


  private void testSuxRate(int step, int diff) {
    int sux = 0;
    for (int i = 0; i < 1000; i++) {
      if (StepDice.roll(step, diff) > 0)
        sux++;
    }
    WolfUtils.log("Dice", "1000 rolls of step %d (%s) vs. diff %d : %d%%", step, StepDice.getDiceRoll(step), diff,
                  sux / 10);
  }
  

  public void drawPanel(int x, int y, String whichPanel, IColoredString<Color> ics) {
    SquidPanel thePanel = null;
    switch (whichPanel) {
    case "equipment":
      thePanel = equipment;
      break;
    case "info":
      thePanel = info;
      break;
    case "stats":
      thePanel = stats;
      break;
    case "skills":
      thePanel = skills;
      break;
    default:
      WolfUtils.log("PlayScreen", "Attempt made to draw on nonexistent PlayScreen panel: %s", whichPanel);
      return;
    }

    thePanel.put(x, y, ics);
  }

  public void drawMap(int x, int y, char c, float fg, float bg) {
    display.put(x, y, c, fg, bg);
  }

  public void drawMap(int x, int y, char c, SColor color) {
    display.put(x, y, c, color);
  }

  public void clearMap() {
    display.clear();
  }

  public void clearPanel(String whichPanel) {
    SquidPanel thePanel = null;
    switch (whichPanel) {
    case "equipment":
      thePanel = equipment;
      break;
    case "info":
      thePanel = info;
      break;
    case "stats":
      thePanel = stats;
      break;
    case "skills":
      thePanel = skills;
      break;
    default:
      WolfUtils.log("PlayScreen", "Attempt made to erase nonexistent PlayScreen panel: %s", whichPanel);
      return;
    }

    thePanel.erase();
  }

  public void drawPanel(int x, int y, String whichPanel, String s) {
    drawPanel(x, y, whichPanel, toICString(s));
  }

  public void addMessage(String markupTemplate, Object... objects) {
    IColoredString<Color> raw = GDXMarkup.instance.colorString(String.format(markupTemplate, objects));
    messages.appendWrappingMessage(raw);
  }

  public int gridWidth() {
    return mapW;
  }

  public int gridHeight() {
    return mapH;
  }
  
  public void enter() {
    super.enter();
  }

  @Override
  public void render() {
    if (input.hasNext())
      input.next();
    stage.getViewport()
         .apply(false);
    stage.act();
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    // TODO Auto-generated method stub
  }
}
