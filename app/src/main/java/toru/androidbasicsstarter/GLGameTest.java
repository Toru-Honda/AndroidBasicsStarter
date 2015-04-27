package toru.androidbasicsstarter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import toru.game.framework.Game;
import toru.game.framework.Screen;
import toru.game.framework.impl.GLGame;
import toru.game.framework.impl.GLGraphics;


public class GLGameTest extends GLGame {
    @Override
    public Screen getStartScreen()
    {
        //return new TestScreen(this);
        //return new ColoredTriangleScreen(this);
        //return new TexturedTriangleScreen(this);
        //return new IndexedScreen(this);
        //return new BlendTest(this);
        return new YukkuriTest(this);
    }

    class TestScreen extends Screen {
        GLGraphics glGraphics;
        Random rand = new Random();

        public TestScreen (Game game) {
            super(game);
            glGraphics = ((GLGame) game).getGlGraphics();
        }

        @Override
        public void update(float deltaTime) {

        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = glGraphics.getGL();
            gl.glClearColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void dispose() {

        }
    }
}