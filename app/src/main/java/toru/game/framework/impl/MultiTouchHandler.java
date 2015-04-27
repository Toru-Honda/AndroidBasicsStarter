package toru.game.framework.impl;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import toru.game.framework.Input;
import toru.game.framework.Input.TouchEvent;

/**
 * Created by Toru on 2015/04/07.
 */
public class MultiTouchHandler implements TouchHandler {
    protected final int numTouches = 20; //仮の値。20個までのマルチタッチを想定。
    protected boolean[] isTouched = new boolean[numTouches];
    protected int[] touchX = new int[numTouches];
    protected int[] touchY = new int[numTouches];
    protected Pool<TouchEvent> touchEventPool;
    protected List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    protected List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    protected float scaleX;
    protected float scaleY;

    public MultiTouchHandler(View view, float scaleX, float scaleY) {
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };
        touchEventPool = new Pool<TouchEvent>(factory, 100);
        view.setOnTouchListener(this);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            if(pointer < 0 || pointer > 127) {
                Log.d("MultiTouchHandler", "invalid pointer.");
                return false;
            } else {
                return isTouched[pointer];
            }
        }
    }

    @Override
    public int getTouchX(int pointer) {
        synchronized (this) {
            if(pointer < 0 || pointer > 127) {
                Log.d("MultiTouchHandler", "invalid pointer.");
                return 0;
            } else {
                return touchX[pointer];
            }
        }
    }

    @Override
    public int getTouchY(int pointer) {
        synchronized (this) {
            if(pointer < 0 || pointer > 127) {
                Log.d("MultiTouchHandler", "invalid pointer.");
                return 0;
            } else {
                return touchY[pointer];
            }
        }
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                touchEventPool.free(touchEvents.get(i));
            }
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
            int action = event.getActionMasked();
            int pointerIndex = event.getActionIndex();
            int pointerId = event.getPointerId(pointerIndex);
            TouchEvent touchEvent;

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = TouchEvent.TOUCH_DOWN;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex) * scaleX);
                    touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex) * scaleY);
                    isTouched[pointerId] = true;
                    touchEventsBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = TouchEvent.TOUCH_UP;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex) * scaleX);
                    touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex) * scaleY);
                    isTouched[pointerId] = false;
                    touchEventsBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int pointerCount = event.getPointerCount();
                    for (int i = 0; i < pointerCount; i++) {
                        pointerIndex = i;
                        pointerId = event.getPointerId(pointerIndex);
                        touchEvent = touchEventPool.newObject();
                        touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                        touchEvent.pointer = pointerId;
                        touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex) * scaleX);
                        touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex) * scaleY);
                        touchEventsBuffer.add(touchEvent);
                    }
                    break;

            }
            return true;
        }
    }
}
