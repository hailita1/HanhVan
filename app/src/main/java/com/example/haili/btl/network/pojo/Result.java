package com.example.haili.btl.network.pojo;

public class Result {
    private Geometry geometry;

    public Result() {
    }

    public Result(Geometry geometry) {
        this.geometry = geometry;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}