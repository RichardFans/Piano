package com.richard.piano;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PianoFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private static final int TAG_STREAM_ID = 1;

    public static PianoFragment newInstance() {
        return new PianoFragment();
    }

    private ToneBox mToneBox;
    private List<Button> mButtons = new ArrayList<>(5);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToneBox = new ToneBox(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_piano, container, false);
        int baseBtnId = R.id.btn_1;
        for (int i = 0; i < 5; i++) {
            Button button = (Button) v.findViewById(baseBtnId + i);
            button.setOnClickListener(this);
            button.setOnTouchListener(this);
            mButtons.add(button);
        }
        return v;
    }

    @Override
    public void onClick(View view) {
//        int index = view.getId() - R.id.btn_1;
//        Tone tone = mToneBox.getRandomTone(index);
//        mToneBox.play(tone);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int index = view.getId() - R.id.btn_1;
        int soundId;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Tone tone = mToneBox.getRandomTone(index);
                soundId = mToneBox.play(tone);
                view.setTag(soundId);
                return true;
            case MotionEvent.ACTION_UP:
                soundId = (int)view.getTag();
                mToneBox.stop(soundId);
                return true;
        }
        return false;
    }
}
