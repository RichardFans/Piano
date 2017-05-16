package com.richard.piano;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MusicPlayer {
    private static final String TAG = "MusicPlayer";
    private static final String MUSICS_FOLDER = "musics";
    private static final int TIME_FACTOR = 4;
    private static final long TIME_UNIT = 200;

    private AssetManager mAssets;
    private List<Music> mMusicList = new ArrayList<>();
    private List<Time> mTimeLine = new ArrayList<>();
    private PlayThread mPlayThread;

    private ToneBox mToneBox;

    public MusicPlayer(Context context) {
        mAssets = context.getAssets();
        loadMusics();
    }

    public ToneBox getToneBox() {
        return mToneBox;
    }

    public void setToneBox(ToneBox toneBox) {
        mToneBox = toneBox;
    }

    private void loadMusics() {
        String[] musicNames;
        try {
            musicNames = mAssets.list(MUSICS_FOLDER);
            Log.i(TAG, "Found " + musicNames.length + " sounds");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        for (String filename : musicNames) {
            String assetPath = MUSICS_FOLDER + "/" + filename;
            Music music = new Music(assetPath);
            mMusicList.add(music);
        }
    }

    public List<Music> getMusics() {
        return mMusicList;
    }

    private void load(Music music) throws IOException {
        mTimeLine.clear();
        InputStream in = mAssets.open(music.getAssetPath());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        String content = builder.toString();
        content = content.replaceAll(";", " ");
        Log.d(TAG, "content: " + content);
        String[] tones = content.split("\\s+");

        int timeLine = 0;
        for (String toneStr : tones) {
            Log.d(TAG, toneStr);
            String[] toneStrArray = toneStr.split(":");
            String toneName = toneStrArray[0];
            Tone tone = getToneFromName(toneName);
            int duration = TIME_FACTOR;
            if (toneStrArray.length > 1) {
                duration = (int) (Float.parseFloat(toneStrArray[1]) * TIME_FACTOR);
            }
            Time time = new Time(tone, timeLine, timeLine + duration);
            mTimeLine.add(time);
            timeLine += duration;
        }
    }

    private Tone getToneFromName(String name) {
        int note = name.charAt(0); // 音符
        if (note == '0') return null;
        int hl = 4;
        if (name.length() > 2) {
            int d = name.charAt(2) - '0';
            if (name.charAt(1) == 'L') d = -d;
            hl += d;
        }
        String toneName = getNoteName(note) + hl;
        return mToneBox.getTone(toneName);
    }

    private String getNoteName(int note) {
        if (note <= '5') {
            note = 'c' + note - '1';
        } else {
            note = 'a' + note - '6';
        }
        return String.valueOf((char) note);
    }

    public void stop() {
        if (mPlayThread != null) mPlayThread.stopRunning();
    }

    public void play(Music music) {
        Log.d(TAG, "play music: " + music.getName());
        try {
            load(music);
            stop();
            mPlayThread = new PlayThread();
            mPlayThread.start();
//            for (Time t :
//                    mTimeLine) {
//                Log.d(TAG, "play tone: " +
//                        (t.getTone() != null ? t.getTone().getName() : "休止符") +
//                        ",start: " + t.getStart() + ", end: " + t.getEnd()
//                );
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class PlayThread extends Thread {

        private boolean mRunning;

        public void stopRunning() {
            mRunning = false;
        }

        @Override
        public void run() {
            int curTime = 0;
            mRunning = true;
            while (mRunning && mTimeLine.size() > 0) {
                Iterator<Time> i = mTimeLine.iterator();
                while (i.hasNext()) {
                    Time time = i.next();
                    if (time.getStart() > curTime) break;

                    if (time.getEnd() == curTime) {
                        Tone tone = time.getTone();
                        if (tone != null) mToneBox.stop(time.getSoundId());
                        i.remove();
                    } else if (time.getStart() == curTime) {
                        Tone tone = time.getTone();
                        if (tone != null) {
                            int soundId = mToneBox.play(tone);
                            time.setSoundId(soundId);
                        }
                        break;
                    }
                }
                try {
                    Thread.sleep(TIME_UNIT);
                    curTime += 1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
