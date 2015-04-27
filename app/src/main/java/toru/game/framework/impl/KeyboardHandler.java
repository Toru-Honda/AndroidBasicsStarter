package toru.game.framework.impl;

import android.view.View;
import android.view.View.OnKeyListener;

import java.util.ArrayList;
import java.util.List;

import toru.game.framework.Input;
import toru.game.framework.Input.KeyEvent;

/**
 * Created by Toru on 2015/04/06.
 * Implementations of OnKeyListener. </br>
 * keyEventsBuffer is temporary buffer which holds key events from onKey().
 * keyEvents is used for game code.
 */
public class KeyboardHandler implements OnKeyListener {
    boolean[] pressedKeys = new boolean[128];
    Pool<KeyEvent> keyEventPool;
    List<KeyEvent> keyEventsBuffer = new ArrayList<KeyEvent>();
    List<KeyEvent> keyEvents = new ArrayList<>();

    /**
     * Constructor.</br>
     * @param view constructor set OnKeyListener to view.
     */
    public KeyboardHandler(View view) {
        PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {
            @Override
            public KeyEvent createObject() {
                return new KeyEvent();
            }
        };
        keyEventPool = new Pool<KeyEvent>(factory, 100);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    @Override
    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
        if(event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE){
            return false;
        }

        synchronized (this) {
            KeyEvent keyEvent = keyEventPool.newObject();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char)event.getUnicodeChar();

            if(event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                keyEvent.type = KeyEvent.KEY_DOWN;
                if(keyCode >= 0 && keyCode <= 127) {
                    pressedKeys[keyCode] = true;
                }
            }

            if(event.getAction() == android.view.KeyEvent.ACTION_UP) {
                keyEvent.type = KeyEvent.KEY_UP;
                if(keyCode >= 0 && keyCode <= 127) {
                    pressedKeys[keyCode] = false;
                }
            }
            keyEventsBuffer.add(keyEvent);
        }
        return false;
    }

    /**
     * Check whether key is pressed or not.
     * @param keyCode checked key.
     * @return if key is pressed, true. if not so, false.
     */
    public boolean isKeyPressed (int keyCode) {
        if(keyCode < 0 || keyCode > 127) { return false; }
        return pressedKeys[keyCode];
    }

    /**
     * Get key events.
     * @return key events list.
     */
    public List<KeyEvent> getKeyEvents() {
        synchronized (this) {
            int len = keyEvents.size();
            for(int i = 0; i < len; i++) { keyEventPool.free(keyEvents.get(i)); } // KeyEventを解放
            keyEvents.clear();                                                       // keyEventsを初期化
            keyEvents.addAll(keyEventsBuffer);                                    // onKeyでハンドルされ、keyEventsBufferに保持されたイベントをkeyEventsに移動
            keyEventsBuffer.clear();                                                // ↑は別スレッドでKeyEventを処理中にonKeyイベントを実行できるようにするため。
            return keyEvents;
        }
    }
}
