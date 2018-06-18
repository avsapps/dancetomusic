package me.yaraju.dancetomusic;

/**
 * Created by avan on 06-02-2018.
 */

public interface SoundChangeListener {

    boolean isReady();
    void onSoundStarted();
    void onSoundStopped();

    int getListenerId();
}
