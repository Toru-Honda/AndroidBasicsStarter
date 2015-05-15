package toru.androidbasicsstarter;

import android.util.FloatMath;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import toru.game.framework.Game;
import toru.game.framework.Input;
import toru.game.framework.Screen;
import toru.game.framework.gl.Camera2D;
import toru.game.framework.gl.SpriteBatcher;
import toru.game.framework.gl.Texture;
import toru.game.framework.gl.TextureRegion;
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

    Camera2D camera;

    Texture texture;
    TextureRegion cannonRegion;
    TextureRegion ballRegion;
    TextureRegion targetRegion;

    SpriteBatcher batcher;

    public CannonScreen(Game game) {
        super(game);
        glGraphics = ((GLGame) game).getGlGraphics();
        batcher = new SpriteBatcher(glGraphics, 100);
        //Game models initialization
        cannon = new Cannon(0, 0, 1, 0.5f);
        ball = new DynamicGameObject(0, 0, 0.2f, 0.2f);
        targets = new ArrayList<>(NUM_TARGET);
        grid = new SpatialHashGrid(WORLD_WIDTH, WORLD_HEIGHT, 2.5f); //的（衝突判定を行うObject）の5倍のサイズにする。
        for(int i = 0; i < NUM_TARGET; i++) {
            GameObject target = new GameObject((float)Math.random() * WORLD_WIDTH, (float)Math.random() * WORLD_HEIGHT, 0.5f, 0.5f);
            grid.insertStaticObject(target);
            targets.add(target);
        }

        //Vertices initialization. No need to initialize vertices.

        camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        List<Input.KeyEvent> keyEvents = game.getInput().getKeyEvents();

        for(int i = 0; i < touchEvents.size(); i++) {
            Input.TouchEvent event = touchEvents.get(i);
            touchPos.set(event.x, event.y);
            camera.touchToWorld(touchPos);
            cannon.angle = touchPos.sub(cannon.position).angle();

            if(event.type == Input.TouchEvent.TOUCH_UP) {
                float radians = cannon.angle * Vector2.TO_RADIANS;
                float ballSpeed = touchPos.len();
                ball.position.set(cannon.position);
                ball.velocity.x = FloatMath.cos(radians) * ballSpeed;
                ball.velocity.y = FloatMath.sin(radians) * ballSpeed;
                ball.bounds.lowerLeft.set(ball.position.x - 0.1f, ball.position.y - 0.1f);
            }
        }
        ball.velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
        ball.position.add(ball.velocity.x * deltaTime, ball.velocity.y * deltaTime);
        ball.bounds.lowerLeft.set(ball.position.x - ball.bounds.width / 2, ball.position.y - ball.bounds.height / 2);
        List<GameObject> colliders = grid.getPotentialColliders(ball);
        for(int i = 0; i < colliders.size(); i++) {
            GameObject collider = colliders.get(i);
            if(ball.bounds.overlapRectangle(collider.bounds)) {
                grid.removeObject(collider);
                targets.remove(collider);
            }
        }

        // ボールが飛んでいる間はボールにカメラを追従させる
        if(ball.position.y > 0) {
            camera.position.set(ball.position);
            camera.zoom = 1 + ball.position.y / WORLD_HEIGHT;
        } else {
            camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
            camera.zoom = 1;
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera.setViewportAndMatrices();

        //bind texture
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(texture);

        //Draw targets
        for(int i = 0; i < targets.size(); i++) {
            GameObject target = targets.get(i);
            batcher.drawSprite(target.position.x, target.position.y, 0.5f, 0.5f, targetRegion);
        }
        //Draw Ball
        batcher.drawSprite(ball.position.x, ball.position.y, 0.2f, 0.2f, ballRegion);
        //Draw Cannon
        batcher.drawSprite(cannon.position.x, cannon.position.y, 1, 0.5f, cannon.angle, cannonRegion);

        batcher.endBatch();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        texture = new Texture((GLGame)game, "pictures/atlas.png");
        cannonRegion = new TextureRegion(texture, 0, 0, 128, 64);
        ballRegion = new TextureRegion(texture, 0, 64, 32, 32);
        targetRegion = new TextureRegion(texture, 64, 64, 64, 64);
    }

    @Override
    public void dispose() {

    }
}
