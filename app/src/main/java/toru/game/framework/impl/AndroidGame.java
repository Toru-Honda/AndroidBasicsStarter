package toru.game.framework.impl;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import toru.game.framework.Audio;
import toru.game.framework.FileIO;
import toru.game.framework.Game;
import toru.game.framework.Graphics;
import toru.game.framework.Input;
import toru.game.framework.Screen;

/**
 * Created by Toru on 2015/04/12.
 * ウインドウ管理、起動ロック、Graphics、Audio、FileIO、Inputインターフェイスのインスタンス生成。Screenの管理。
 */
public abstract class AndroidGame extends Activity implements Game {
    protected AndroidFastRenderView renderView;
    protected Graphics graphics;
    protected Audio audio;
    protected Input input;
    protected FileIO fileIO;
    protected Screen screen;
    protected WakeLock wakeLock;

    protected final static int LANDSCAPE_WIDTH = 480;
    protected final static int LANDSCAPE_HEIGHT = 320;
    protected final static int PORTRATE_WIDTH = 320;
    protected final static int PORTRATE_HEIGHT = 480;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? LANDSCAPE_WIDTH : PORTRATE_WIDTH;
        int frameBufferHeight = isLandscape ? LANDSCAPE_HEIGHT : PORTRATE_HEIGHT;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Bitmap.Config.RGB_565);

        float scaleX;
        float scaleY;
        if(Build.VERSION.SDK_INT > 13) {
            Point outSize = new Point();
            getWindowManager().getDefaultDisplay().getSize(outSize);
            scaleX = (float) frameBufferWidth / outSize.x;
            scaleY = (float) frameBufferHeight / outSize.y;
        } else {
            scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
            scaleY = (float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();
        }
        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getStartScreen();
        setContentView(renderView);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();
        if(isFinishing()) {
            screen.dispose();
        }
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if(screen == null) {
            throw new IllegalArgumentException("Screen must not be null");
        }
        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }
}
