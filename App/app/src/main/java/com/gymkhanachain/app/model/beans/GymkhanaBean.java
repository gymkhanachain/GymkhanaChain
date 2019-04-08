package com.gymkhanachain.app.model.beans;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class GymkhanaBean {
    private Integer id;
    private String name;
    private String description;
    private Bitmap image;
    private LatLng position;

    public GymkhanaBean(final Integer id, final String name, final String description,
                        final Bitmap image, final LatLng position) {
        setId(id);
        setName(name);
        setDescription(description);
        setImage(image);
        setPosition(position);

    }

    public Integer getId() {
        return new Integer(id.intValue());
    }

    public void setId(Integer id) {
        this.id = new Integer(id.intValue());
    }

    public String getName() {
        return new String(name);
    }

    public void setName(String name) {
        this.name = new String(name);
    }

    public String getDescription() {
        return new String(description);
    }

    public void setDescription(String description) {
        this.description = new String(description);
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}
