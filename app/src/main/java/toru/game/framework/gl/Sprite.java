package toru.game.framework.gl;

/**
 * Created by Toru on 2015/05/11.
 */
public class Sprite {
    public float x, y;
    public float width, height;
    TextureRegion region;

    public Sprite(float x, float y, float width, float height, TextureRegion region) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.region = region;
    }
}
