package com.example.haili.btl.network.api;


public class ApiUtils {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    public static MapService getMapService(){
        return RetrofitClient.getClient(BASE_URL).create(MapService.class);
    }
}
