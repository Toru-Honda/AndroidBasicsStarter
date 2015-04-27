package toru.androidbasicsstarter;

import javax.microedition.khronos.opengles.GL10;

import toru.game.framework.Game;
import toru.game.framework.Screen;
import toru.game.framework.gl.Texture;
import toru.game.framework.gl.Vertices;
import toru.game.framework.impl.GLGame;
import toru.game.framework.impl.GLGraphics;

/**
 * Created by Toru on 2015/04/25.
 */
public class BlendTest extends Screen {
    GLGraphics glGraphics;
    Vertices vertices;
    Texture textureRgb;
    Texture textureRgba;

    public BlendTest(Game game) {
        super(game);
        glGraphics = ((GLGame) game).getGlGraphics();

        textureRgb = new Texture((GLGame)game, "pictures/yukkuri.png");
        textureRgba = new Texture((GLGame)game, "pictures/yukkuri.png");

        vertices = new Vertices(glGraphics, 8, 12, true, true);
        float[] rects = new float[]{
                100, 100, 1, 1, 1, 0.5f, 0, 1,
                228, 100, 1, 1, 1, 0.5f, 1, 1,
                228, 228, 1, 1, 1, 0.5f, 1, 0,
                100, 228, 1, 1, 1, 0.5f, 0, 0,
                100, 300, 1, 1, 1, 1, 0, 1,
                228, 300, 1, 1, 1, 1, 1, 1,
                228, 428, 1, 1, 1, 1, 1, 0,
                100, 428, 1, 1, 1, 1, 0, 0
        };
        vertices.setVertices(rects, 0, rects.length);
        vertices.setIndices(new short[] { 0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4}, 0, 12);
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getKeyEvents();
        game.getInput().getTouchEvents();
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0, 320, 0, 480, 1, -1);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gl.glEnable(GL10.GL_TEXTURE_2D);
        textureRgb.bind();
        vertices.draw(GL10.GL_TRIANGLES, 0, 6);

        textureRgba.bind();
        vertices.draw(GL10.GL_TRIANGLES, 6, 6);
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
