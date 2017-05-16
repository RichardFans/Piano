package com.richard.piano;

/**
 * 节拍
 */
public class Time {


    private Tone mTone;
    private int mSoundId;
    private int mStart;
    private int mEnd;

    public Time(Tone tone, int start, int end) {
        mTone = tone;
        mStart = start;
        mEnd = end;
    }

    public Tone getTone() {
        return mTone;
    }

    public int getStart() {
        return mStart;
    }

    public int getEnd() {
        return mEnd;
    }

    public int getSoundId() {
        return mSoundId;
    }

    public void setSoundId(int soundId) {
        mSoundId = soundId;
    }
}
