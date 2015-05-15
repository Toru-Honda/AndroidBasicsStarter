package toru.game.framework.gl;

/**
 * Created by Toru on 2015/05/11.
 * 矩形のTexture描画領域を表現する。
 */
public class TextureRegion {
    public final float u1, v1;
    public final float u2, v2;
    public final Texture texture;

    /**
     * TextureRegionクラスのコンストラクタ
     * @param texture Regionで描画されるTexture
     * @param x Atlas内のx座標
     * @param y Atlas内のy座標
     * @param width Textureの幅
     * @param height Textureの高さ
     */
    public TextureRegion(Texture texture, float x, float y, float width, float height){
        this.u1 = x / texture.width;
        this.v1 = y / texture.height;
        this.u2 = this.u1 + width / texture.width;
        this.v2 = this.v1 + height / texture.height;
        this.texture = texture;
    }
}
