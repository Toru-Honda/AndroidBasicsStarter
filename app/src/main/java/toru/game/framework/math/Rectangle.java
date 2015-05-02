package toru.game.framework.math;

/**
 * Created by Toru on 2015/05/02.
 * 衝突判定用外接矩形クラス
 */
public class Rectangle {
    public final Vector2 lowerLeft;
    public float width, height;

    public Rectangle(float x, float y, float width, float height){
        this.lowerLeft = new Vector2(x, y);
        this.width = width;
        this.height = height;
    }

    /**
     * 矩形との衝突判定
     * @param r
     * @return true : overlap, false : not overlap
     */
    public boolean overlapRectangle(Rectangle r) {
        if (this.lowerLeft.x < r.lowerLeft.x + r.width
                && r.lowerLeft.x < this.lowerLeft.x + this.width
                && this.lowerLeft.y < r.lowerLeft.y + r.height
                && r.lowerLeft.y < this.lowerLeft.y + this.height) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 外接円との衝突判定
     * @param c
     * @return true : overlap, false : not overlap
     */
    public boolean overCircle(Circle c) {
        float closestX = c.center.x;
        float closestY = c.center.y;

        if(c.center.x < this.lowerLeft.x) {
            closestX = this.lowerLeft.x;
        } else if(this.lowerLeft.x + this.width < c.center.x) {
            closestX = this.lowerLeft.x + this.width;
        }
        if(c.center.y < this.lowerLeft.y) {
            closestY = this.lowerLeft.y;
        } else if(this.lowerLeft.y + this.height < c.center.y) {
            closestY = this.lowerLeft.y + this.height;
        }
        return  c.center.distSquared(closestX, closestY) < c.radius * c.radius;
    }
}
