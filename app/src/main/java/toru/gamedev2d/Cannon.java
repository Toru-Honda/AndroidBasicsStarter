package toru.gamedev2d;

/**
 * Created by Toru on 2015/05/04.
 * implementation of Cannon.
 */
public class Cannon extends GameObject {
    public float angle;

    public Cannon(float x, float y, float width, float height){
        super(x, y, width, height);
        angle = 0;
    }
}
