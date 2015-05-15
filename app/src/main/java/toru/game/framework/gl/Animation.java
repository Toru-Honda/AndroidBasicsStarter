package toru.game.framework.gl;

/**
 * Created by Toru on 2015/05/13.
 * Spriteアニメーションを管理するクラス。
 */
public class Animation {
    public final int ANIMATION_LOOPING = 0;
    public final int ANIMATION_NONLOOPING = 1;

    final TextureRegion[] keyFrames;
    final float frameDuration;

    public Animation(float frameDuration, TextureRegion ... keyFrames) {
        this.frameDuration = frameDuration;
        this.keyFrames = keyFrames;
    }

    /**
     * startTimeで指定した時間に対応したTextureRegionを取得する。
     * @param stateTime アニメーション経過時間
     * @param mode Loop or NonLoop
     * @return
     */
    public TextureRegion getKeyFrame(float stateTime, int mode) {
        int frameNumber = (int)(stateTime / frameDuration);

        if(mode == ANIMATION_NONLOOPING) {
            frameNumber = Math.min(keyFrames.length - 1, frameNumber);
        } else {
            frameNumber %= keyFrames.length;
        }
        return keyFrames[frameNumber];
    }
}
