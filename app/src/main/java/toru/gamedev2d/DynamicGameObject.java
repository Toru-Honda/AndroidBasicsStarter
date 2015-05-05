package toru.gamedev2d;

import toru.game.framework.math.Vector2;

/**
 * Created by Toru on 2015/05/04.
 * 2D game object which has velocity and acceleration.
 */
public class DynamicGameObject extends GameObject{
    public final Vector2 velocity;
    public final Vector2 accel;

    public DynamicGameObject(float x, float y, float width, float height) {
        super(x, y, width, height);
        velocity = new Vector2();
        accel = new Vector2();
    }
}
