package toru.game.framework.math;

/**
 * Created by Toru on 2015/05/02.
 * 衝突判定用外接円クラス
 */
public class Circle {
    public final Vector2 center = new Vector2();
    public float radius;

    public Circle(float x, float y, float radius){
        center.set(x, y);
        this.radius = radius;
    }

    public boolean overlapCircle(Circle c) {
        float distance = this.center.distSquared(c.center);
        float squaredRadius = this.radius * this.radius + c.radius * c.radius;
        return distance <= squaredRadius;
    }

    public boolean overRectangle(Rectangle r) {
        float closestX = this.center.x;
        float closestY = this.center.y;

        if(this.center.x < r.lowerLeft.x) {
            closestX = r.lowerLeft.x;
        } else if(r.lowerLeft.x + r.width < this.center.x) {
            closestX = r.lowerLeft.x + r.width;
        }
        if(this.center.y < r.lowerLeft.y) {
            closestY = r.lowerLeft.y;
        } else if(r.lowerLeft.y + r.height < this.center.y) {
            closestY = r.lowerLeft.y + r.height;
        }
        return  this.center.distSquared(closestX, closestY) < this.radius * this.radius;
    }
}
