package com.gymkhanachain.app.model.beans;

import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

public class TextPointBean extends PointBean {
    String longDescription;

    public TextPointBean(final Integer id, final String name, final String description,
                          final ImageView image, final LatLng position,
                          final String longDescription) {
        super(id, name, description, image, position);
        setLongDescription(longDescription);
    }

    public String getLongDescription() {
        return longDescription;
    }

    public TextPointBean setLongDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }
}
