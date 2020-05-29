package com.example.haili.btl.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.haili.btl.R;
import com.example.haili.btl.model.Place;
import com.example.haili.btl.network.api.ApiUtils;
import com.example.haili.btl.network.api.MapService;
import com.example.haili.btl.network.pojo.DirectionRoot;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.haili.btl.R.id.map;

public class MapsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private Place place;
    private GoogleApiClient apiClient;
    private LocationRequest mLocationRequest;
    private android.location.Location mLocation;
    private OnCompleteGetCurrentLoc onCompleteGetCurrentLoc;
    private LatLng latLngPlace;

    public void setOnCompleteGetCurrentLoc(OnCompleteGetCurrentLoc onCompleteGetCurrentLoc) {
        this.onCompleteGetCurrentLoc = onCompleteGetCurrentLoc;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);

        Intent intent = getIntent();
        if (intent.getAction().equals(PlaceDetailActivity.ACTION_DIRECT)) {
            String name = intent.getStringExtra("name");
            double lat = Double.parseDouble(intent.getStringExtra("lat"));
            double lng = Double.parseDouble(intent.getStringExtra("lng"));

            Log.e("Map", "lat " + String.valueOf(lat) + " lng" + String.valueOf(lng));

            place = new Place.Builder()
                    .setName(name)
                    .setLat(lat)
                    .setLng(lng)
                    .build();

            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;

                    latLngPlace = new LatLng(place.getLat(), place.getLng());
                    mMap.addMarker(new MarkerOptions().position(latLngPlace).title(place.getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngPlace, 15.0F));

                    if (android.support.v4.app.ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && android.support.v4.app.ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);

//                    onCompleteGetCurrentLoc.drawDirection(latLngPlace);

//                    if (mLocation != null) {
//                        Log.e("Current Location", mLocation.getLatitude() + " " + mLocation.getLongitude());
//                        LatLng latLngCurrent = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
//                        drawDirection(latLngCurrent, latLngPlace, getString(R.string.google_api_key));
//                    }
                }
            });

        } else if (intent.getAction().equals(PlacesActivity.ACTION_SHOW_ALL_PLACE)) {

            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                }
            });
        } else {

        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        apiClient.connect();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Connection Failed", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    private void getCurrentLocation() {
        if (android.support.v4.app.ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && android.support.v4.app.ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, mLocationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(final android.location.Location location) {
                        mLocation = location;

                        if (latLngPlace == null) return;
                        LatLng latLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
                        drawDirectionMap(latLngCurrent, latLngPlace, getString(R.string.google_api_key));
                        Log.e("Current Location", location.getLatitude() + " " + location.getLongitude());

                        if (apiClient != null) {
                            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
                        }
                    }
                }
        );
    }

    private void buildGoogleApiClient() {
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        apiClient.connect();
    }

    private void drawDirectionMap(LatLng origin, LatLng destination, String key) {
        MapService mapService = ApiUtils.getMapService();

        String orginLatLng = String.valueOf(origin.latitude) + "," + String.valueOf(origin.longitude);
        String destinationLatLng = String.valueOf(destination.latitude) + "," + String.valueOf(destination.longitude);

        Log.e("orginLatLng", String.valueOf(origin.latitude) + "," + String.valueOf(origin.longitude));
        Log.e("destinationLatLng", String.valueOf(destination.latitude) + "," + String.valueOf(destination.longitude));
        Log.e("key", key);

        Call<DirectionRoot> call = mapService.getDirectionResults(orginLatLng, destinationLatLng);
        call.enqueue(new Callback<DirectionRoot>() {
            @Override
            public void onResponse(Call<DirectionRoot> call, Response<DirectionRoot> response) {
                DirectionRoot directionRoot = response.body();
                Log.e("Status", directionRoot.getStatus());

                if (directionRoot.getStatus().equals("OK")) {
                    String points = directionRoot.getRoutes().get(0).getOverviewPolyline().getPoints();
                    List<LatLng> latLngList = PolyUtil.decode(points);

                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(latLngList));
                }
            }

            @Override
            public void onFailure(Call<DirectionRoot> call, Throwable t) {
                Log.e("Fail ", t.getMessage());
            }
        });
    }

    public interface OnCompleteGetCurrentLoc {
        void drawDirection(LatLng latLngPlace);
    }
}