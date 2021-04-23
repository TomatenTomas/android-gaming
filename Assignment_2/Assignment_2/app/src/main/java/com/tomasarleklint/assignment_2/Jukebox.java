package com.tomasarleklint.assignment_2;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.IOException;

public class Jukebox {
    SoundPool _soundPool = null;
    public MediaPlayer mp = null;

    Jukebox(final Context context){
        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        _soundPool = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(Config.MAX_STREAMS)
                .build();

        loadSounds(context);
    }

    private void loadSounds(final Context context){
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;
            descriptor = assetManager.openFd("jump.wav");
            Config.SOUND_JUMP =_soundPool.load(descriptor, Config.PRIORITY);
            descriptor = assetManager.openFd("damage_taken.wav");
            Config.SOUND_DAMAGE = _soundPool.load(descriptor, Config.PRIORITY);
            descriptor = assetManager.openFd("game_over.wav");
            Config.SOUND_GAME_OVER = _soundPool.load(descriptor, Config.PRIORITY);
            descriptor = assetManager.openFd("coin.wav");
            Config.SOUND_COIN = _soundPool.load(descriptor, Config.PRIORITY);
            mp = MediaPlayer.create(context, R.raw.background_music);
            mp.setLooping(true);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void play(final int soundID){
        if (soundID > 0) {
            _soundPool.play(soundID, Config.LEFT_VOLUME, Config.RIGHT_VOLUME, Config.PRIORITY, Config.LOOP_ONCE, Config.RATE);
        }
    }

    void destroy(){
        _soundPool.release();
        _soundPool = null;
        mp.release();
    }

}
