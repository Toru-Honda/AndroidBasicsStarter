package toru.game.framework.gl;

import android.util.FloatMath;

import javax.microedition.khronos.opengles.GL10;

import toru.game.framework.impl.GLGraphics;
import toru.game.framework.math.Vector2;

/**
 * Created by Toru on 2015/05/11.
 * 同じTextureをまとめて描画することで描画処理の負荷を軽減する。
 */
public class SpriteBatcher {
    final float[] verticesBuffer;
    int bufferIndex;
    final Vertices vertices;
    int numSprites;

    public SpriteBatcher(GLGraphics glGraphics, int maxSprites) {
        this.verticesBuffer = new float[maxSprites * 4 * 4]; //最大sprite数 * 頂点数 * (x, y), (u, v)座標分
        this.vertices = new Vertices(glGraphics, maxSprites * 4, maxSprites * 6, false, true);
        this.bufferIndex = 0;
        this.numSprites = 0;

        short[] indices = new short[maxSprites * 6];
        short j = 0;
        for(int i = 0; i < indices.length; i += 6, j += 4) {
            indices[i + 0] = (short)(j + 0);
            indices[i + 1] = (short)(j + 1);
            indices[i + 2] = (short)(j + 2);
            indices[i + 3] = (short)(j + 2);
            indices[i + 4] = (short)(j + 3);
            indices[i + 5] = (short)(j + 0);
        }
        vertices.setIndices(indices, 0, indices.length);
    }

    /**
     * Textureをbindし、Sprite batch処理を開始する。
     * @param texture
     */
    public void beginBatch(Texture texture) {
        texture.bind();
        numSprites = 0;
        bufferIndex = 0;
    }

    /**
     * Sprite batch処理を終了し、GLエンジンに描画させる。
     */
    public void endBatch() {
        vertices.setVertices(verticesBuffer, 0, bufferIndex);
        vertices.bind();
        vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
        vertices.unbind();
    }

    /**
     * 指定した座標にTextureを描画するクラス
     * @param x Texture x座標(中心)
     * @param y Texture y座標(中心)
     * @param width Textureの幅
     * @param height Textureの高さ
     * @param region TextureRegion
     */
    public void drawSprite(float x, float y, float width, float height, TextureRegion region) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;
        float x1 = x - halfWidth;
        float x2 = x + halfWidth;
        float y1 = y - halfHeight;
        float y2 = y + halfHeight;

        //Bottom left vertex
        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v2; //(u, v)座標は左上が原点であることに注意
        //Bottom right vertex
        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v2;
        //Upper right vertex
        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v1;
        //Upper left vertex
        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v1;

        numSprites++;
    }

    public void drawSprite(float x, float y, float width, float height, float angle, TextureRegion region) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;
        float rad = angle * Vector2.TO_RADIANS;
        float cos = FloatMath.cos(rad);
        float sin = FloatMath.sin(rad);

        //Bottom Left
        float x1 = -halfWidth * cos - (-halfHeight) * sin + x;
        float y1 = -halfWidth * sin + (-halfHeight) * cos + y;
        //Bottom Right
        float x2 = halfWidth * cos - (-halfHeight) * sin + x;
        float y2 = halfWidth * sin + (-halfWidth) * cos + y;
        //Upper Right
        float x3 = halfWidth * cos - halfHeight * sin + x;
        float y3 = halfWidth * sin + halfHeight * cos + y;
        //Upper Left
        float x4 = -halfWidth * cos - halfHeight * sin + x;
        float y4 = -halfWidth * sin + halfWidth * cos + y;

        //Bottom left vertex
        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v2; //(u, v)座標は左上が原点であることに注意
        //Bottom right vertex
        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v2;
        //Upper right vertex
        verticesBuffer[bufferIndex++] = x3;
        verticesBuffer[bufferIndex++] = y3;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v1;
        //Upper left vertex
        verticesBuffer[bufferIndex++] = x4;
        verticesBuffer[bufferIndex++] = y4;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v1;

        numSprites++;
    }
}
