package toru.game.framework.impl;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;

import toru.game.framework.Audio;
import toru.game.framework.Music;
import toru.game.framework.Sound;

/**
 * Created by Toru on 2015/04/06.
 * Implementations of toru.game.framework.Audio
 */
public class AndroidAudio implements Audio {
    protected AssetManager assets;
    protected SoundPool soundPool;
    protected final int numSoundPool = 20;

    public AndroidAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
        this.soundPool = new SoundPool(numSoundPool, AudioManager.STREAM_MUSIC, 0);
    }

    /**
     * Open file and return Music object. </br>
     * if file isn't exist, throw RuntimeException.
     * @param filename file path in assets.
     * @return Music object.
     */
    @Override
    public Music newMusic(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            return new AndroidMusic(assetDescriptor);
        } catch (IOException e) {
            throw new RuntimeException("'Couldn't load music'" + filename + "'");
        }
    }

    /**
     * Open file and return Sound object. </br>
     * if file isn't exist, throw RuntimeException.
     * @param filename file path in assets.
     * @return Sound object.
     */
    @Override
    public Sound newSound(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            int soundId = soundPool.load(assetDescriptor, 0);
            return new AndroidSound(soundPool, soundId);
        } catch (IOException e) {
            throw new RuntimeException("'Couldn't load sound'" + filename + "'");
        }
    }
}
