package com.gymkhanachain.app.model.beans;

import com.google.android.gms.maps.model.LatLng;
import com.gymkhanachain.app.commons.WrapperBitmap;

public abstract class PointBean {
    Integer id;
    String name;
    String description;
    WrapperBitmap image;
    LatLng position;

    public PointBean(final Integer id, final String name, final String description,
                     final WrapperBitmap image, final LatLng position) {
        setId(id).setName(name).setDescription(description).setImage(image).setPosition(position);
    }

    public Integer getId() {
        return id;
    }

    public PointBean setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PointBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PointBean setDescription(String description) {
        this.description = description;
        return this;
    }

    public WrapperBitmap getImage() {
        return image;
    }

    public PointBean setImage(WrapperBitmap image) {
        this.image = image;
        return this;
    }

    public LatLng getPosition() {
        return position;
    }

    public PointBean setPosition(LatLng position) {
        this.position = position;
        return this;
    }
}
