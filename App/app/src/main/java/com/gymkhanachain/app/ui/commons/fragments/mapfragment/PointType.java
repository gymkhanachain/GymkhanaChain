package com.gymkhanachain.app.ui.commons.fragments.mapfragment;

public enum PointType {
    GYMKHANA_POINTS("GymkhanaPoints"),
    GIS_POINTS("GisPoints");

    private String type;
    PointType(String type) { setType(type); }
    String getType() { return type; }
    void setType(String type) { this.type = type; }
}
