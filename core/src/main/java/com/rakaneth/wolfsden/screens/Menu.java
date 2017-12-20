package com.rakaneth.wolfsden.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.rakaneth.wolfsden.WolfUtils;

import squidpony.panel.IColoredString;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SquidInput;
import squidpony.squidgrid.gui.gdx.SquidPanel;
import squidpony.squidgrid.gui.gdx.TextCellFactory;

public class Menu extends SquidPanel {

  private IColoredString<Color>       caption;
  private List<IColoredString<Color>> items;
  private int                         curIndex;
  private boolean                     loopIndex;
  private char                        downKey;
  private char                        upKey;
  private char                        exitKey;
  private char                        okKey;
  private SquidInput                  input;

  public Menu(
              TextCellFactory tcf, IColoredString<Color> _caption, char _downKey, char _upKey, char _exitKey,
              char _okKey) {
    super(10, 10, tcf);
    caption = _caption;
    curIndex = 1;
    downKey = _downKey;
    upKey = _upKey;
    exitKey = _exitKey;
    okKey = _okKey;
    input = new SquidInput((char key, boolean alt, boolean ctrl, boolean shift) -> {
      if (key == downKey)
        setIndex(curIndex + 1);
      else if (key == upKey)
        setIndex(curIndex - 1);
      else if (key == exitKey)
        this.remove();
      else if (key == okKey) {
        //TODO: internal functional interface to handle OK
        WolfUtils.log("Menu", "%s was selected", items.get(curIndex - 1));
      }
    });
  }

  private int getMaxLen() {
    if (items.isEmpty()) {
      return 0;
    } else {
      return items.stream()
                  .max((bigger, smaller) -> Integer.compare(bigger.length(), smaller.length()))
                  .get()
                  .length();
    }
  }

  public void add(IColoredString<Color> item) {
    items.add(item);
  }

  public void remove(int index) {
    items.remove(index);
  }

  public void clear() {
    items.clear();
  }

  public void setList(List<IColoredString<Color>> list) {
    items = new ArrayList<IColoredString<Color>>(list);
  }

  private void setIndex(int val) {
    int is = items.size();
    if (val < 0) {
      if (loopIndex) {
        curIndex = is;
      } else {
        curIndex = 1;
      }
    } else if (val > is) {
      if (loopIndex) {
        curIndex = 1;
      } else {
        curIndex = is;
      }
    } else {
      curIndex = val;
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    int is = items.size();
    setGridWidth(getMaxLen() + 4);
    setGridHeight(is + 2);
    putBordersCaptioned(SColor.FLOAT_WHITE, caption);
    for (int i = 0; i < is; i++) {
      put(3, i + 1, items.get(i));
    }
    put(1, curIndex, '>');
    super.draw(batch, parentAlpha);
  }

}
