package com.gymkhanachain.app.ui.commons.fragments.mapfragment;

public enum PointOrder {
    ROUTE_ORDER ("RouteOrder"),
    NONE_ORDER  ("NoneOrder");

    private String order;
    PointOrder(String order) { setOrder(order); }
    String getOrder() { return order; }
    void setOrder(String order) { this.order = order; }
}
