package toru.game.framework.impl;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

import toru.game.framework.Music;

/**
 * Created by Toru on 2015/04/06.
 */
public class AndroidMusic implements Music {
    MediaPlayer mediaPlayer;
    boolean isPrepared = false;

    /**
     * Constructor.
     * @param assetDescriptor loaded music file descriptor.
     */
    public AndroidMusic(AssetFileDescriptor assetDescriptor) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(), assetDescriptor.getStartOffset(), assetDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.stop();
                    synchronized (this) {
                        isPrepared = false;
                    }
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Play music.
     */
    @Override
    public void play() {
        if(mediaPlayer.isPlaying()) { return; }

        try {
            synchronized (this) {
                if(!isPrepared) { mediaPlayer.prepare(); }
                mediaPlayer.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop playing music.
     */
    @Override
    public void stop() {
        mediaPlayer.stop();
        synchronized (this){
            isPrepared = false;
        }
    }

    /**
     * Pause playing music.
     */
    @Override
    public void pause() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    /**
     * Looping flag setter.
     * @param looping true: mediaPlayer plays repeatedly. false: mediaPlayer plays once.
     */
    @Override
    public void setLooping(boolean looping) {
        mediaPlayer.setLooping(looping);
    }

    /**
     * Volume setter.
     * @param volume raw scalars in range: 0.0 to 1.0.
     */
    @Override
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    /**
     * Checks whether media player is playing or not.
     * @return if playing, true. if not so false.
     */
    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * Checks whether media player is stopped or not.</br>
     * Due to implementations of Android media player, This method returns opposite value of isPlaying().
     * @return if not playing true. if not so false.
     */
    @Override
    public boolean isStopped() {
        return !mediaPlayer.isPlaying();
    }

    /**
     * Checks whether media player is looping or not.
     * @return if looping, true. if not so, false.
     */
    @Override
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    /**
     * Release resources of media player..
     */
    @Override
    public void dispose() {
        mediaPlayer.release();
    }

 }
