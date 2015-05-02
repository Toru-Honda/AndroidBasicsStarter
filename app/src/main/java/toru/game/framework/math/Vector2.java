package toru.game.framework.math;

import android.util.FloatMath;

/**
 * Created by Toru on 2015/04/25.
 * Vector implementations for 2D Game.
 * All arithmetic method returns reference of itself to use as below. <br/>
 * vector2.set(3, 4).add(other_vector2);
 */
public class Vector2 {
    public static float TO_RADIANS = (1 / 180.0f) * (float) Math.PI; //For translate degrees to radian.
    public static float TO_DEGREES = (1 / (float) Math.PI) * 180;    //For translate radian to degrees.
    public float x, y;

    /**
     * Constructor.
     */
    public Vector2() {
    }

    /**
     * Constructor
     * @param x x axis value.
     * @param y y axis value.
     */
    public Vector2 (float x, float y) {
        this.x = x; this.y = y;
    }

    /**
     * Constructor.
     * @param other Creates instance has same x, y values.
     */
    public Vector2 (Vector2 other) {
        this.x = other.x; this.y = other.y;
    }

    /**
     * Copy Vector2 instance.
     * @return
     */
    public Vector2 copy() {
        return new Vector2(this);
    }

    /**
     * Setter of Vector2. Returns reference of itself so as to combine calc.
     * @param x
     * @param y
     * @return Reference of this instance.
     */
    public Vector2 set(float x, float y) {
        this.x = x; this.y = y;
        return this;
    }

    /**
     * Setter of Vector2. Returns reference of itself so as to combine calc.
     * @param other
     * @return Reference of this instance.
     */
    public Vector2 set(Vector2 other) {
        this.x = other.x; this.y = other.y;
        return this;
    }

    /**
     * Adds params. Returns reference of itself so as to combine calc.
     * @param x
     * @param y
     * @return
     */
    public Vector2 add(float x, float y) {
        this.x += x; this.y += y;
        return this;
    }

    /**
     * Adds vector2. Returns reference of itself so as to combine calc.
     * @param other
     * @return
     */
    public Vector2 add(Vector2 other) {
        this.x += other.x; this.y += other.y;
        return this;
    }

    /**
     * Subs x, y. Returns reference of itself so as to combine calc.
     * @param x
     * @param y
     * @return
     */
    public Vector2 sub(float x, float y) {
        this.x -= x; this.y -= y;
        return this;
    }

    /**
     * Subs vector2. Returns reference of itself so as to combine calc.
     * @param other
     * @return
     */
    public Vector2 sub(Vector2 other) {
        this.x -= other.x; this.y -= other.y;
        return this;
    }

    /**
     * Mul scalar.
     * @param scalar
     * @return
     */
    public Vector2 mul(float scalar) {
        this.x *= scalar; this.y *= scalar;
        return this;
    }

    /**
     * Get Vector2 length
     * @return
     */
    public float len() {
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * Normalizes Vector2
     * @return
     */
    public Vector2 normalize() {
        float len = len();
        if (len != 0) {
            this.x /= len;
            this.y /= len;
        }
        return this;
    }

    public float angle() {
        float angle = (float)Math.atan2(y, x) * TO_DEGREES;
        if(angle < 0) { angle += 360; }
        return angle;
    }
    /**
     * Rotates Vector2.
     * @param angle degrees.
     * @return
     */
    public Vector2 rotate(float angle) {
        float rad = angle * TO_RADIANS;
        float cos = FloatMath.cos(rad);
        float sin = FloatMath.sin(rad);

        float newX = this.x * cos - this.y * sin;
        float newY = this.x * sin + this.y * cos;

        this.x = newX; this.y = newY;
        return this;
    }

    /**
     * Gets distance between this and other.
     * @param other
     * @return
     */
    public float dist(Vector2 other) {
        float distX = this.x - other.x;
        float distY = this.y - other.y;
        return FloatMath.sqrt(distX * distX + distY * distY);
    }

    /**
     * Gets distance between this and (x, y)
     * @param x
     * @param y
     * @return
     */
    public float dist(float x, float y) {
        float distX = this.x - x;
        float distY = this.y - y;
        return FloatMath.sqrt(distX * distX + distY * distY);
    }

    /**
     * Gets squared distance between this and other.
     * @param other
     * @return
     */
    public float distSquared(Vector2 other) {
        float distX = this.x - other.x;
        float distY = this.y - other.y;
        return distX * distX + distY * distY;
    }

    /**
     * Gets squared distance between this and (x, y).
     * @param x
     * @param y
     * @return
     */
    public float distSquared(float x, float y) {
        float distX = this.x - x;
        float distY = this.y - y;
        return distX * distX + distY * distY;
    }
}
