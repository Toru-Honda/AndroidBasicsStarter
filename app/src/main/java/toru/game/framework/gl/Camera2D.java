package toru.game.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import toru.game.framework.impl.GLGraphics;
import toru.game.framework.math.Vector2;

/**
 * Created by Toru on 2015/05/07.
 * 2DでViewport、zoomの管理を行うクラス。
 */
public class Camera2D {
    public final Vector2 position;
    public float zoom;
    public final float frustumWidth;
    public final float frustumHeight;
    final GLGraphics glGraphics;

    public Camera2D( GLGraphics glGraphics, float frustumWidth, float frustumHeight) {
        this.frustumWidth = frustumWidth;
        this.frustumHeight = frustumHeight;
        this.glGraphics = glGraphics;
        this.position = new Vector2(frustumWidth / 2, frustumHeight / 2);
        this.zoom = 1.0f;
    }

    public void setViewportAndMatrices() {
        GL10 gl = glGraphics.getGL();
        gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(position.x - frustumWidth / 2,
                position.x + frustumWidth / 2,
                position.y - frustumHeight / 2,
                position.y + frustumHeight / 2, -1, 1);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /**
     * タッチされた座標をGame世界の座標に変換する。
     * @param touch 変換されるVector2インスタンス
     */
    public void touchToWorld(Vector2 touch) {
        touch.x = (touch.x / (float)glGraphics.getWidth()) * frustumWidth * zoom;
        touch.y = (1 - touch.y / (float)glGraphics.getHeight()) * frustumHeight * zoom;
        touch.add(position).sub(frustumWidth * zoom / 2, frustumHeight * zoom / 2);
    }
}
