package toru.gamedev2d;

import toru.game.framework.math.Rectangle;
import toru.game.framework.math.Vector2;

/**
 * Created by Toru on 2015/05/04.
 * Base class of 2D game object
 */
public class GameObject {
    public final Vector2 position;
    public final Rectangle bounds;

    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle(x - width / 2, y - height / 2, width, height);
    }
}
