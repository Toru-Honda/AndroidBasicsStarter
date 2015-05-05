package toru.androidbasicsstarter;

import android.util.FloatMath;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import toru.game.framework.Game;
import toru.game.framework.Input;
import toru.game.framework.Screen;
import toru.game.framework.gl.Vertices;
import toru.game.framework.impl.GLGame;
import toru.game.framework.impl.GLGraphics;
import toru.game.framework.math.Vector2;
import toru.gamedev2d.Cannon;
import toru.gamedev2d.DynamicGameObject;
import toru.gamedev2d.GameObject;
import toru.gamedev2d.SpatialHashGrid;

/**
 * Created by Toru on 2015/05/02.
 */
public class CannonScreen extends Screen {
    final int NUM_TARGET = 20;
    float WORLD_WIDTH = 9.6f;
    float WORLD_HEIGHT = 6.4f;
    GLGraphics glGraphics;

    Cannon cannon;
    DynamicGameObject ball;
    List<GameObject> targets;
    SpatialHashGrid grid;

    Vertices cannonVertices;
    Vertices ballVertices;
    Vertices targetVertices;

    Vector2 touchPos = new Vector2();
    Vector2 gravity = new Vector2(0, -3);

    public CannonScreen(Game game) {
        super(game);
        glGraphics = ((GLGame) game).getGlGraphics();

        //Game models initialization
        cannon = new Cannon(0, 0, 1, 1);
        ball = new DynamicGameObject(0, 0, 0.2f, 0.2f);
        targets = new ArrayList<>(NUM_TARGET);
        grid = new SpatialHashGrid(WORLD_WIDTH, WORLD_HEIGHT, 2.5f); //的（衝突判定を行うObject）の5倍のサイズにする。
        for(int i = 0; i < NUM_TARGET; i++) {
            GameObject target = new GameObject((float)Math.random() * WORLD_WIDTH, (float)Math.random() * WORLD_HEIGHT, 0.5f, 0.5f);
            grid.insertStaticObject(target);
            targets.add(target);
        }


        //Vertices initialization
        cannonVertices = new Vertices(glGraphics, 3, 0, false, false);
        cannonVertices.setVertices(new float[]{-0.5f, -0.5f, 0.5f, 0.0f, -0.5f, 0.5f}, 0, 6);

        ballVertices = new Vertices(glGraphics, 4, 6, false, false);
        ballVertices.setVertices(new float[]{-0.1f, 0.1f,
                -0.1f, -0.1f,
                0.1f, -0.1f,
                0.1f, 0.1f}, 0, 8);
        ballVertices.setIndices(new short[]{0, 1, 2, 2, 3, 0}, 0, 6);

        targetVertices = new Vertices(glGraphics, 4, 6, false, false);
        targetVertices.setVertices(new float[] {
                -0.25f, -0.25f,
                0.25f, -0.25f,
                0.25f, 0.25f,
                -0.25f, 0.25f }, 0, 8);
        targetVertices.setIndices(new short[] {0, 1, 2, 2, 3, 0}, 0, 6);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        List<Input.KeyEvent> keyEvents = game.getInput().getKeyEvents();

        for(int i = 0; i < touchEvents.size(); i++) {
            Input.TouchEvent event = touchEvents.get(i);
            touchPos.x = (event.x / (float)glGraphics.getWidth()) * WORLD_WIDTH;
            touchPos.y = (1 - event.y / (float)glGraphics.getHeight()) * WORLD_HEIGHT;
            cannon.angle = touchPos.sub(cannon.position).angle();

            if(event.type == Input.TouchEvent.TOUCH_UP) {
                float radians = cannon.angle * Vector2.TO_RADIANS;
                float ballSpeed = touchPos.len();
                ball.position.set(cannon.position);
                ball.velocity.x = FloatMath.cos(radians) * ballSpeed;
                ball.velocity.y = FloatMath.sin(radians) * ballSpeed;
                ball.bounds.lowerLeft.set(ball.position.x - 0.1f, ball.position.y - 0.1f);
                Log.d("CannonScreen", "Ball is shoot!!");
            }
        }
        ball.velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
        ball.position.add(ball.velocity.x * deltaTime, ball.velocity.y * deltaTime);
        ball.bounds.lowerLeft.set(ball.position.x - ball.bounds.width/2, ball.position.y - ball.bounds.height/2);
        Log.d("CannonScreen", "ball.positon = (" + ball.position.x + ", " + ball.position.y + "), ball.bounds = (" +ball.bounds.lowerLeft.x + ", " + ball.bounds.lowerLeft.y + ").");
        List<GameObject> colliders = grid.getPotentialColliders(ball);
        for(int i = 0; i < colliders.size(); i++) {
            GameObject collider = colliders.get(i);
            if(ball.bounds.overlapRectangle(collider.bounds)) {
                grid.removeObject(collider);
                targets.remove(collider);
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0, WORLD_WIDTH, 0, WORLD_HEIGHT, 1, -1);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        //Draw targets
        gl.glColor4f(0, 1, 0, 1);
        targetVertices.bind();
        for(int i = 0; i < targets.size(); i++) {
            GameObject target = targets.get(i);
            gl.glLoadIdentity();
            gl.glTranslatef(target.position.x, target.position.y, 0);
            targetVertices.draw(GL10.GL_TRIANGLES, 0, 6);
        }
        targetVertices.unbind();
        //Draw Ball
        gl.glLoadIdentity();
        gl.glTranslatef(ball.position.x, ball.position.y, 0);
        gl.glColor4f(1, 0, 0, 1);
        ballVertices.bind();
        ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
        ballVertices.unbind();
        //Draw Cannon
        gl.glLoadIdentity();
        gl.glTranslatef(cannon.position.x, cannon.position.y, 0);
        gl.glRotatef(cannon.angle, 0, 0, 1);
        gl.glColor4f(1, 1, 1, 1);
        cannonVertices.bind();
        cannonVertices.draw(GL10.GL_TRIANGLES, 0, 3);
        cannonVertices.unbind();

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
