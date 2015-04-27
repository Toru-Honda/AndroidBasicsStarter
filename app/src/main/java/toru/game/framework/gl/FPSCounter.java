package toru.game.framework.gl;

import android.util.Log;

/**
 * Created by Toru on 2015/04/25.
 */
public class FPSCounter {
    static long startTime = System.nanoTime();
    static int frames = 0;

    static public void logFrame() {
        frames++;
        if(System.nanoTime() - startTime >= 1000000000) {
            Log.d("FPSCounter", "fps: " + frames);
            frames = 0;
            startTime = System.nanoTime();
        }
    }
}
