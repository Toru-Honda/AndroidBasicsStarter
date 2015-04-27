package toru.game.framework.impl;

import android.view.View;

import java.util.List;

import toru.game.framework.Input.TouchEvent;

/**
 * Created by Toru on 2015/04/06.
 */
public interface TouchHandler extends View.OnTouchListener {
    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
    public List<TouchEvent> getTouchEvents();
}
