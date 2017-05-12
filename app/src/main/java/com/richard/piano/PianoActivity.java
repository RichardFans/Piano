package com.richard.piano;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PianoActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return PianoFragment.newInstance();
    }
}
