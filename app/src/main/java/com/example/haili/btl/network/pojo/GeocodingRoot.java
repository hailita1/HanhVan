package com.example.haili.btl.network.pojo;

import java.util.List;


public class GeocodingRoot {
    private List<Result> results;
    private String status;

    public GeocodingRoot() {
    }

    public GeocodingRoot(List<Result> results, String status) {
        this.results = results;
        this.status = status;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

