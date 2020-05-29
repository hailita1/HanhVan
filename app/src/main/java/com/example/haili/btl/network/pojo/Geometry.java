package com.example.haili.btl.network.pojo;

public class Geometry {
    public Geometry() {
    }

    private Location location;

    public Geometry(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
