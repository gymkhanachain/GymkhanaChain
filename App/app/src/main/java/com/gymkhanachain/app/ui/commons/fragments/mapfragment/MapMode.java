package com.gymkhanachain.app.ui.commons.fragments.mapfragment;

public enum MapMode {
    EDIT_MODE    ("EditMode"),
    NORMAL_MODE  ("NormalMode"),
    PLAY_MODE    ("PlayMode");

    private String mode;
    MapMode(String mode) { setMode(mode); }
    String getMode() { return mode; }
    void setMode(String mode) { this.mode = mode; }
}
