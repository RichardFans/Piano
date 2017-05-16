package com.richard.piano;

public class Music {
    private String mAssetPath;
    private String mName;

    public Music(String assetPath) {
        mAssetPath = assetPath;
        String[] components = assetPath.split("/");
        mName = components[components.length - 1];
    }

    public String getAssetPath() {
        return mAssetPath;
    }
    public String getName() {
        return mName;
    }
}
