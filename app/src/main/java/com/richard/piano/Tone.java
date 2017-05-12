package com.richard.piano;

public class Tone {
    private String mAssetPath;
    private String mName;
    private Integer mSoundId;

    public Tone(String assetPath) {
        mAssetPath = assetPath;
        String[] components = assetPath.split("/");
        String filename = components[components.length - 1];
        mName = filename.replace(".ogg", "");
    }

    public String getAssetPath() {
        return mAssetPath;
    }
    public String getName() {
        return mName;
    }

    public Integer getSoundId() {
        return mSoundId;
    }
    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }
}
