package com.richard.piano;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PianoFragment extends Fragment implements View.OnTouchListener {
    private static final int TAG_STREAM_ID = 1;

    public static PianoFragment newInstance() {
        return new PianoFragment();
    }

    private MusicPlayer mPlayer;
    private ToneBox mToneBox;
    private List<Button> mButtons = new ArrayList<>(5);
    private List<Music> mMusicList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mToneBox = new ToneBox(getActivity());
        mPlayer = new MusicPlayer(getActivity());
        mPlayer.setToneBox(mToneBox);
        mMusicList = mPlayer.getMusics();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_piano, container, false);
        int baseBtnId = R.id.btn_1;
        for (int i = 0; i < 5; i++) {
            Button button = (Button) v.findViewById(baseBtnId + i);
            button.setOnTouchListener(this);
            mButtons.add(button);
        }
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        int pos = 0;
        for (Music music : mMusicList) {
            menu.add(Menu.NONE, pos, pos, music.getName());
            pos++;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int pos = item.getItemId();
        Music music = mMusicList.get(pos);
        mPlayer.play(music);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int index = view.getId() - R.id.btn_1;
        int soundId;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                String toneName = String.valueOf((char) ('c' + index)) + 4;
                Tone tone = mToneBox.getTone(toneName);
                soundId = mToneBox.play(tone);
                view.setTag(soundId);
                return true;
            case MotionEvent.ACTION_UP:
                soundId = (int) view.getTag();
                mToneBox.stop(soundId);
                return true;
        }
        return false;
    }
}
