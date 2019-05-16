package com.gymkhanachain.app.model.beans;

import com.google.android.gms.maps.model.LatLng;
import com.gymkhanachain.app.commons.ProxyBitmap;

import java.util.Date;
import java.util.List;

public class GymkhanaBean {
    Integer id;
    String name;
    String description;
    GymkhanaType type;
    Boolean accessibility;
    Boolean privGymk;
    String creator;
    String accessCode;
    Date createDate;
    Date openDate;
    Date closeDate;
    ProxyBitmap image;
    LatLng position;
    List<PointBean> points;

    public GymkhanaBean(final Integer id, final String name, final String description,
                        final GymkhanaType type, final Boolean accessibility,
                        final Boolean privGymk, final String accessCode, final String creator,
                        final Date createDate, final Date openDate, final Date closeDate,
                        final ProxyBitmap image, final LatLng position, final List<PointBean> points) {
        setId(id).setName(name).setDescription(description).setType(type)
                .setAccessibility(accessibility).setPrivGymk(privGymk).setAccessCode(accessCode)
                .setCreator(creator).setCreateDate(createDate).setOpenDate(openDate)
                .setCloseDate(closeDate).setImage(image).setPosition(position).setPoints(points);
    }

    public Integer getId() {
        return new Integer(id.intValue());
    }

    public GymkhanaBean setId(Integer id) {
        this.id = new Integer(id.intValue());
        return this;
    }

    public String getName() {
        return new String(name);
    }

    public GymkhanaBean setName(String name) {
        this.name = new String(name);
        return this;
    }

    public String getDescription() {
        return new String(description);
    }

    public GymkhanaBean setDescription(String description) {
        this.description = new String(description);
        return this;
    }

    public GymkhanaType getType() {
        return type;
    }

    public GymkhanaBean setType(GymkhanaType type) {
        this.type = type;
        return this;
    }

    public ProxyBitmap getImage() {
        return image;
    }

    public GymkhanaBean setImage(ProxyBitmap image) {
        this.image = image;
        return this;
    }

    public LatLng getPosition() {
        return position;
    }

    public GymkhanaBean setPosition(LatLng position) {
        this.position = position;
        return this;
    }

    public Boolean getAccessibility() {
        return accessibility;
    }

    public GymkhanaBean setAccessibility(Boolean accessibility) {
        this.accessibility = accessibility;
        return this;
    }

    public boolean getPrivGymk() {
        return privGymk;
    }

    public GymkhanaBean setPrivGymk(Boolean privGymk) {
        this.privGymk = privGymk;
        return this;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public GymkhanaBean setAccessCode(String accessCode) {
        this.accessCode = accessCode;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public GymkhanaBean setCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public GymkhanaBean setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public GymkhanaBean setOpenDate(Date openDate) {
        this.openDate = openDate;
        return this;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public GymkhanaBean setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
        return this;
    }

    public List<PointBean> getPoints() {
        return points;
    }

    public GymkhanaBean setPoints(List<PointBean> points) {
        this.points = points;
        return this;
    }
}
