package toru.game.framework.impl;

import android.media.SoundPool;

import toru.game.framework.Sound;

/**
 * Created by Toru on 2015/04/06.
 * implementations of toru.game.framework.Sound
 */
public class AndroidSound implements Sound {
    protected final int soundId;
    protected final SoundPool soundPool;

    public AndroidSound(SoundPool soundPool, int soundId) {
        this.soundPool = soundPool;
        this.soundId = soundId;
    }

    /**
     * play sound
     * @param volume this is directly passed to SoundPool.play().
     */
    @Override
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    /**
     * dispose from SoundPool
     */
    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }
}
