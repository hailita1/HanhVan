package com.example.haili.btl.network.api;

import com.example.haili.btl.network.pojo.DirectionRoot;
import com.example.haili.btl.network.pojo.GeocodingRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapService {

    @GET("geocode/json?")
    Call<GeocodingRoot> getLocationResults(@Query("address") String address,
                                           @Query("key") String key);

    @GET("directions/json?")
    Call<DirectionRoot> getDirectionResults(@Query("origin") String originLatLng,
                                            @Query("destination") String destinationLatLng,
                                            @Query("key") String key);

    @GET("directions/json?")
    Call<DirectionRoot> getDirectionResults(@Query("origin") String originLatLng,
                                            @Query("destination") String destinationLatLng);
}
