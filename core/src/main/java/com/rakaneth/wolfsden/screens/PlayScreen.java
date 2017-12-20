package com.rakaneth.wolfsden.screens;

import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rakaneth.wolfsden.GameInfo;
import com.rakaneth.wolfsden.MapBuilder;
import com.rakaneth.wolfsden.StepDice;
import com.rakaneth.wolfsden.WolfMap;
import com.rakaneth.wolfsden.WolfUtils;
import com.rakaneth.wolfsden.entities.Creature;
import com.rakaneth.wolfsden.entities.CreatureManager;
import com.rakaneth.wolfsden.interfaces.Drawable;

import squidpony.panel.IColoredString;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.GDXMarkup;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;
import squidpony.squidgrid.gui.gdx.SquidMessageBox;
import squidpony.squidgrid.gui.gdx.SquidPanel;
import squidpony.squidgrid.gui.gdx.TextCellFactory;
import squidpony.squidmath.Coord;

public class PlayScreen extends WolfScreen {
  private SparseLayers          display;
  private SquidMessageBox       messages;
  private SquidPanel            stats, info, equipment, skills;
  private final int             mapW     = 80;
  private final int             mapH     = 30;
  private final int             msgW     = 43;
  private final int             msgH     = 10;
  private final int             statW    = 37;
  private final int             statH    = 10;
  private final int             infoW    = 40;
  private final int             infoH    = 10;
  private final int             equipW   = 40;
  private final int             equipH   = 6;
  private final int             skW      = 40;
  private final int             skH      = 24;
  private final int             fullW    = 120;
  private final int             fullH    = 40;
  private final GameInfo        gi       = GameInfo.instance;
  private final CreatureManager cm       = CreatureManager.instance;
  private final MapBuilder      mb       = MapBuilder.instance;
  public static PlayScreen      instance = new PlayScreen();

  private PlayScreen() {
    super("title");
    vport = new StretchViewport(pixelWidth(fullW), pixelHeight(fullH));
    stage = new Stage(vport, batch);
    TextCellFactory slab = DefaultResources.getSlabFamily();
    initText(slab);

    display = new SparseLayers(mapW, mapH, cellWidth, cellHeight, slab.copy());
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

  public Coord cam() {
    Coord pos = gi.player()
                  .getPos();
    int left = MathUtils.clamp(pos.x - mapW / 2, 0, Math.max(0, gi.getMap()
                                                                  .getWidth()
                                                                - mapW));
    int top = MathUtils.clamp(pos.y - mapH / 2, 0, Math.max(0, gi.getMap()
                                                                 .getHeight()
                                                               - mapH));
    return Coord.get(left, top);
  }

  public void renderMap() {
    WolfMap map = gi.getMap();
    char[][] toDraw = map.displayMap;
    Coord cam = cam();
    int wx, wy;
    for (int x = 0; x < mapW; x++) {
      for (int y = 0; y < mapH; y++) {
        wx = cam.x + x;
        wy = cam.y + y;
        if (map.isOOB(wx, wy)) {
          drawMap(x, y, ' ', SColor.BLACK_DYE);
        } else {
          drawMap(x, y, toDraw[wx][wy], map.fgFloats[wx][wy], map.bgFloats[wx][wy]);
        }
      }
    }
  }

  public void renderThings() {
    Coord cam = cam();
    List<Drawable> things = CreatureManager.instance.allCreatures()
                                                    .stream()
                                                    .filter(f -> f.map() == gi.getMap())
                                                    .map(f -> (Drawable) f)
                                                    .collect(Collectors.toList());

    for (Drawable thing : things) {
      Coord tPos = thing.getPos();
      drawMap(tPos.x - cam.x, tPos.y - cam.y, thing.glyph(), thing.color());
    }
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

  @Override
  public void enter() {
    super.enter();
    gi.setMap(mb.getMap("wolfDen1"));
    cm.buildPlayer("fighter", gi.getMap());
  }

  @Override
  public void render() {
    drawHUDSkeleton();
    clearMap();
    renderMap();
    renderThings();
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
