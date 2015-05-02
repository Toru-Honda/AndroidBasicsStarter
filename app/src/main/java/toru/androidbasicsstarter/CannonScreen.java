package toru.androidbasicsstarter;

import android.util.FloatMath;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import toru.game.framework.Game;
import toru.game.framework.Input;
import toru.game.framework.Screen;
import toru.game.framework.gl.Vertices;
import toru.game.framework.impl.GLGame;
import toru.game.framework.impl.GLGraphics;
import toru.game.framework.math.Vector2;

/**
 * Created by Toru on 2015/05/02.
 */
public class CannonScreen extends Screen {
    float FRUSTUM_WIDTH = 9.6f;
    float FRUSTUM_HEIGHT = 6.4f;
    GLGraphics glGraphics;
    Vertices cannon;
    Vector2 cannonPos = new Vector2();
    Vertices ball;
    Vector2 ballPos = new Vector2();
    float cannonAngle = 0;
    Vector2 touchPos = new Vector2();
    Vector2 gravity = new Vector2(0, -5);
    Vector2 ballVelocity = new Vector2();

    public CannonScreen(Game game) {
        super(game);
        glGraphics = ((GLGame) game).getGlGraphics();
        cannon = new Vertices(glGraphics, 3, 0, false, false);
        cannon.setVertices(new float[] { -0.5f, -0.5f, 0.5f, 0.0f, -0.5f, 0.5f}, 0, 6);
        ball = new Vertices(glGraphics, 4, 6, false, false);
        ball.setVertices(new float[] { -0.1f, 0.1f,
        -0.1f, -0.1f,
        0.1f, -0.1f,
        0.1f, 0.1f}, 0, 8);
        ball.setIndices(new short[] { 0, 1, 2, 2, 3, 0}, 0, 6);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        List<Input.KeyEvent> keyEvents = game.getInput().getKeyEvents();

        for(int i = 0; i < touchEvents.size(); i++) {
            Input.TouchEvent event = touchEvents.get(i);
            touchPos.x = (event.x / (float)glGraphics.getWidth()) * FRUSTUM_WIDTH;
            touchPos.y = (1 - event.y / (float)glGraphics.getHeight()) * FRUSTUM_HEIGHT;
            cannonAngle = touchPos.sub(cannonPos).angle();

            if(event.type == Input.TouchEvent.TOUCH_UP) {
                float radians = cannonAngle * Vector2.TO_RADIANS;
                float ballSpeed = touchPos.len();
                ballPos.set(cannonPos);
                ballVelocity.x = FloatMath.cos(radians) * ballSpeed;
                ballVelocity.y = FloatMath.sin(radians) * ballSpeed;
            }
        }
        ballVelocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
        ballPos.add(ballVelocity.x * deltaTime, ballVelocity.y * deltaTime);
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0, FRUSTUM_WIDTH, 0, FRUSTUM_HEIGHT, 1, -1);
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        gl.glLoadIdentity();
        gl.glTranslatef(cannonPos.x, cannonPos.y, 0);
        gl.glRotatef(cannonAngle, 0, 0, 1);
        gl.glColor4f(1, 1, 1, 1);
        cannon.bind();
        cannon.draw(GL10.GL_TRIANGLES, 0, 3);
        cannon.unbind();

        gl.glLoadIdentity();
        gl.glTranslatef(ballPos.x, ballPos.y, 0);
        gl.glColor4f(1, 0, 0, 1);
        ball.bind();
        ball.draw(GL10.GL_TRIANGLES, 0, 6);
        ball.unbind();
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
