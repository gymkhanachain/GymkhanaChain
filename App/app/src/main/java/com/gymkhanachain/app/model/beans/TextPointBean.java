package com.gymkhanachain.app.model.beans;

import com.google.android.gms.maps.model.LatLng;
import com.gymkhanachain.app.commons.WrapperBitmap;

public class TextPointBean extends PointBean {
    String longDescription;

    public TextPointBean(final Integer id, final String name, final String description,
                         final WrapperBitmap image, final LatLng position,
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
