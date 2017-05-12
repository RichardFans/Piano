package com.richard.piano;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ToneBox {
    private static final String TAG = "ToneBox";
    private static final String SOUNDS_FOLDER = "tones";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;
    private List<List<Tone>> mTonesList = new ArrayList<>();
    private SoundPool mSoundPool;

    public ToneBox(Context context) {
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadTones();
    }

    public Tone getRandomTone(int btnIndex) {
        List<Tone> tones = mTonesList.get(btnIndex);
        return tones.get((int) (Math.random() * tones.size()));
    }

    public int play(Tone tone) {
        return play(tone, false);
    }

    public int play(Tone tone, boolean mute) {
        Integer soundId = tone.getSoundId();
        if (soundId == null) {
            return 0;
        }
        float vol = mute ? 0f : 1.0f;
        return mSoundPool.play(soundId, vol, vol, 1, 0, 1.0f);
    }

    public void stop(int streamId) {
        mSoundPool.stop(streamId);
    }

    private void loadTones() {
        String[] toneNames;
        try {
            toneNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + toneNames.length + " sounds");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }
        for (int i = 0; i < 5; i++) {
            mTonesList.add(new ArrayList<Tone>());
        }
        for (String filename : toneNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Tone tone = new Tone(assetPath);
                load(tone);
                mTonesList.get(getIndexByName(tone.getName()))
                        .add(tone);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }
        }
    }

    private int getIndexByName(String name) {
        int toneIdx = name.charAt(0);
        switch (toneIdx) {
            case 'a':
            case 'b':
                return 0;
            case 'c':
                return 1;
            case 'd':
                return 2;
            case 'e':
                return 3;
            default:
                return 4;
        }
    }

    private void load(Tone tone) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(tone.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        tone.setSoundId(soundId);
    }
}
