package com.gymkhanachain.app.model.beans;

import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

public abstract class PointBean {
    Integer id;
    String name;
    String description;
    ImageView image;
    LatLng position;

    public PointBean(final Integer id, final String name, final String description,
                     final ImageView image, final LatLng position) {
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

    public ImageView getImage() {
        return image;
    }

    public PointBean setImage(ImageView image) {
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
