package toru.androidbasicsstarter;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import toru.game.framework.Game;
import toru.game.framework.Screen;
import toru.game.framework.gl.FPSCounter;
import toru.game.framework.gl.Texture;
import toru.game.framework.gl.Vertices;
import toru.game.framework.impl.GLGame;
import toru.game.framework.impl.GLGraphics;

/**
 * Created by Toru on 2015/04/25.
 */
public class YukkuriTest extends Screen {
    public class Yukkuri {
        public float x, y;
        public float vx, vy;
        public float scaleX, scaleY;
        public float vScaleX, vScaleY;
        public float angle;
        public float vAngle;
        int width, height;

        public Yukkuri(int width, int height) {
            this.width = width;
            this.height = height;
            this.scaleX = 1.0f;
            this.scaleY = 1.0f;
            Random random = new Random();
            x = random.nextInt(width);
            y = random.nextInt(height);
            vx = random.nextInt() % 50 - 25;
            vy = random.nextInt() % 50 - 25;
            vScaleX = random.nextFloat() - 0.5f;
            vScaleY = random.nextFloat() - 0.5f;
            angle = random.nextInt(360) - 180;
            vAngle = random.nextInt(360) - 180;
        }

        public void move(float deltaTime) {
            x += vx * deltaTime;
            y += vy * deltaTime;

            if(x < 0) { vx = -vx; x = 0; }
            if(x > width) { vx = -vx; x = width; }
            if(y < 0) { vy = -vy; y = 0; }
            if(y > height) { vy = -vy;  y = height; }

            scaleX += vScaleX * deltaTime;
            scaleY += vScaleY * deltaTime;

            if(scaleX < 0.1f) { vScaleX = -vScaleX; scaleX = 0.1f; }
            if(scaleX > 2.0f) { vScaleX = -vScaleX; scaleX = 2.0f; }
            if(scaleY < 0.1f) { vScaleY = -vScaleY; scaleY = 0.1f; }
            if(scaleY > 2.0f) { vScaleY = -vScaleY; scaleY = 2.0f; }

            angle = (angle + vAngle * deltaTime) % 360;
        }
    }

    static final int NUM_YUKKURI = 300;
    GLGraphics glGraphics;
    Texture yukkuriTexture;
    Vertices yukkuriModel;
    Yukkuri[] yukkuris;

    public YukkuriTest(Game game) {
        super(game);
        glGraphics = ((GLGame)game).getGlGraphics();
        yukkuriTexture = new Texture((GLGame)game, "pictures/yukkuri.png");
        yukkuriModel = new Vertices(glGraphics, 4, 12, false, true);
        yukkuriModel.setVertices(new float[] {
                -16, -16, 0, 1,
                16, -16, 1, 1,
                16, 16, 1, 0,
                -16, 16, 0, 0
        }, 0, 16);
        yukkuriModel.setIndices(new short[] { 0, 1, 2, 2, 3, 0}, 0, 6);

        yukkuris = new Yukkuri[NUM_YUKKURI];
        for(int i = 0; i < NUM_YUKKURI; i++) { yukkuris[i] = new Yukkuri(320, 480); }
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        for(int i = 0; i < NUM_YUKKURI; i++) { yukkuris[i].move(deltaTime); }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        yukkuriModel.bind();
        for(int i = 0; i < NUM_YUKKURI; i++) {
            gl.glLoadIdentity();
            gl.glTranslatef(yukkuris[i].x, yukkuris[i].y, 0);
            gl.glRotatef(yukkuris[i].angle, 0, 0, 1);
            gl.glScalef(yukkuris[i].scaleX, yukkuris[i].scaleY, 1);
            yukkuriModel.draw(GL10.GL_TRIANGLES, 0, 6);
        }
        yukkuriModel.unbind();

        FPSCounter.logFrame();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        GL10 gl = glGraphics.getGL();
        gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
        gl.glClearColor(0, 0, 0.5f, 1);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0, 320, 0, 480, 1, -1);
        yukkuriTexture.reload();
        gl.glEnable(GL10.GL_TEXTURE_2D);
        yukkuriTexture.bind();
    }

    @Override
    public void dispose() {

    }
}
